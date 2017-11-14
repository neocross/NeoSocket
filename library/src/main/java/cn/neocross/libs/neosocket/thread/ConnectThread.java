package cn.neocross.libs.neosocket.thread;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import cn.neocross.libs.neosocket.bean.Connection;
import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.NeoSocketServer;
import cn.neocross.libs.neosocket.callback.StatusType;

/**
 * 通讯监听线程,长连接
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
class ConnectThread extends Thread {

    private Socket socket;
    private NeoSocketServer neoSocketServer;
    private Connection connection;
    private boolean isRunning;
    private boolean isConnect;

    ConnectThread(Socket socket, NeoSocketServer neoSocketServer) {
        super.setName("ConnectThread");
        this.socket = socket;
        this.neoSocketServer = neoSocketServer;
        connection = new Connection(socket);
        isRunning = true;
    }

    void stopRunning() {
        isRunning = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        neoSocketServer.getInstantMessageHandler().obtainMessage(1, StatusType.TYPE_CONNECTING).sendToTarget();
        while (isRunning) {
            if (socket.isClosed()) {
                isRunning = false;
                neoSocketServer.getInstantMessageHandler().obtainMessage(1, StatusType.TYPE_DISCONNECT).sendToTarget();
                break;
            }
            try {
                if (!isConnect) {
                    System.out.println("已连接客户端ip:" + connection.getIp());
                    neoSocketServer.getInstantMessageHandler().obtainMessage(1, StatusType.TYPE_CONNECTED).sendToTarget();
                }
                InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(reader);
                String msg = bufferedReader.readLine();
                if ("disconnect".equals(msg)) {
                    isConnect = false;
                    neoSocketServer.getInstantMessageHandler().obtainMessage(1, StatusType.TYPE_DISCONNECT).sendToTarget();
                    //该方法可以是服务端断开
                    //neoSocketServer.close();
                    break;
                }
                if (!TextUtils.isEmpty(msg)) {
                    System.out.println("接收到消息: " + msg);
                    InstantMessage message = new InstantMessage(connection, msg);
                    neoSocketServer.getInstantMessageHandler().obtainMessage(2, message).sendToTarget();
                    connection.callback("200");
                }
                isConnect = true;
            } catch (IOException e) {
                e.printStackTrace();
                isRunning = false;
                neoSocketServer.getInstantMessageHandler().obtainMessage(1, StatusType.TYPE_DISCONNECT).sendToTarget();
                break;
            }
        }
    }
}
