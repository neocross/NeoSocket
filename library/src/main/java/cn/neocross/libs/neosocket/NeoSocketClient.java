package cn.neocross.libs.neosocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;
import cn.neocross.libs.neosocket.thread.Communi;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class NeoSocketClient {

    private Socket socket;
    private ThreadPoolExecutor mExecutor;
    private NeoSocketClientCallback clientCallback;

    public NeoSocketClient() {
        mExecutor = new Communi().create();
    }

    public NeoSocketClient(InetAddress inetAddress, int port) {
        try {
            socket = new Socket(inetAddress, port);
            socket.setKeepAlive(true);
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClientListener(NeoSocketClientCallback clientCallback) {
        this.clientCallback = clientCallback;
    }

    /**
     * 客户端主动发起连接
     *
     * @param ip   ip地址
     * @param port 端口
     * @return NeoSocketClient
     */
    public Future<NeoSocketClient> connect(String ip, int port) {
        return mExecutor.submit(new ConnectRunnable(ip, port));
    }

    private class ConnectRunnable implements Callable<NeoSocketClient> {

        String ip;
        int port;

        ConnectRunnable(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public NeoSocketClient call() throws Exception {
            try {
                socket = new Socket(InetAddress.getByName(ip), port);
                socket.setKeepAlive(true);
                socket.setSoTimeout(5000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return NeoSocketClient.this;
        }
    }

    /**
     * 发送即时消息到服务端
     *
     * @param instantMessage InstantMessage
     */
    public void send(InstantMessage instantMessage) {
        mExecutor.submit(new MessageRunnable(instantMessage));
    }

    public void close() {
        mExecutor.submit(new MessageRunnable(new InstantMessage(-1, "disconnect")));
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MessageRunnable implements Runnable {

        InstantMessage instantMessage;

        MessageRunnable(InstantMessage instantMessage) {
            this.instantMessage = instantMessage;
        }

        @Override
        public void run() {
            try {
                String msg = instantMessage.toString();
                OutputStream outputStream = socket.getOutputStream();
                msg += "\n";
                outputStream.write(msg.getBytes("utf-8"));
                if (clientCallback != null) {
                    String result = read();
                    clientCallback.onMessageReceived(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (clientCallback != null) {
                    clientCallback.onStatusChange();
                }
            }
        }
    }

    private String read() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}