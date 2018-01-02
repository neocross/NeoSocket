package cn.neocross.libs.neosocket;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import cn.neocross.libs.neosocket.bean.Connection;
import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.bean.MsgEngine;
import cn.neocross.libs.neosocket.callback.HandlerType;
import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;
import cn.neocross.libs.neosocket.callback.StatusType;
import cn.neocross.libs.neosocket.thread.Communi;
import cn.neocross.libs.neosocket.thread.InstantMessageHandler;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class NeoSocketClient {

    private Socket socket;
    private Connection connection;
    private ThreadPoolExecutor mExecutor;
    private NeoSocketClientCallback clientCallback;
    private InstantMessageHandler instantMessageHandler;
    private Gson gson;

    public NeoSocketClient() {
        mExecutor = new Communi().create();
        gson = new Gson();
    }

    public NeoSocketClient(InetAddress inetAddress, int port) {
        gson = new Gson();
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
        instantMessageHandler = new InstantMessageHandler(clientCallback);
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
            if (ip.startsWith("/")) {
                ip = ip.substring(1, ip.length());
            }
            this.ip = ip;
            this.port = port;
        }

        @Override
        public NeoSocketClient call() throws Exception {
            socket = new Socket(InetAddress.getByName(ip), port);
            socket.setKeepAlive(true);
            socket.setSoTimeout(5000);
            connection = new Connection(socket);
            return NeoSocketClient.this;
        }
    }

    /**
     * 发送即时消息到服务端
     *
     * @param instantMessage InstantMessage
     */
    public void send(InstantMessage instantMessage) {
        connection.setIp(socket.getLocalAddress().toString());
        instantMessage.setConnection(connection);
        mExecutor.submit(new MessageRunnable(instantMessage));
    }

    public void close() {
        send(new InstantMessage(StatusType.TYPE_DISCONNECT, "disconnect"));
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
                instantMessageHandler.obtainMessage(HandlerType.TYPE_CLIENT_MSG, new MsgEngine(StatusType.TYPE_MSG, read())).sendToTarget();
                if (Communi.isClose(gson, msg)) {
                    if (socket != null && !socket.isClosed()) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                instantMessageHandler.obtainMessage(HandlerType.TYPE_CLIENT_STATUS).sendToTarget();
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