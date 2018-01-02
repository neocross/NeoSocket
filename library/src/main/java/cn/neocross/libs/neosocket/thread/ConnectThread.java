package cn.neocross.libs.neosocket.thread;

import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import cn.neocross.libs.neosocket.NeoSocketServer;
import cn.neocross.libs.neosocket.bean.Connection;
import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.bean.MsgEngine;
import cn.neocross.libs.neosocket.callback.HandlerType;
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
    private Gson gson;

    ConnectThread(Socket socket, NeoSocketServer neoSocketServer) {
        super.setName("ConnectThread");
        this.socket = socket;
        this.neoSocketServer = neoSocketServer;
        connection = new Connection(socket);
        gson = new Gson();
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

    private Message getMessage(int what, Object obj) {
        return neoSocketServer.getInstantMessageHandler().obtainMessage(what, obj);
    }

    @Override
    public void run() {
        getMessage(HandlerType.TYPE_SERVER_STATUS, new MsgEngine(StatusType.TYPE_CONNECTING)).sendToTarget();
        while (isRunning) {
            if (socket.isClosed()) {
                isRunning = false;
                getMessage(HandlerType.TYPE_SERVER_STATUS, new MsgEngine(StatusType.TYPE_DISCONNECT)).sendToTarget();
                break;
            }
            try {
                if (!isConnect) {
                    System.out.println("ConnectThread-- 已连接客户端ip:" + connection.getIp());
                    getMessage(HandlerType.TYPE_SERVER_STATUS, new MsgEngine(StatusType.TYPE_CONNECTED, connection.getIp())).sendToTarget();
                }
                InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(reader);
                String msg = bufferedReader.readLine();
                if (!TextUtils.isEmpty(msg)) {
                    System.out.println("ConnectThread-- 接收到消息: " + msg);
                    if (Communi.isClose(gson, msg)) {
                        isConnect = false;
                        getMessage(HandlerType.TYPE_SERVER_STATUS, new MsgEngine(StatusType.TYPE_DISCONNECT)).sendToTarget();
                        createCallbackMsg("success");
                        // 该方法可以是服务端断开
                        // neoSocketServer.close();
                        break;
                    }
                    getMessage(HandlerType.TYPE_SERVER_MSG, new MsgEngine(StatusType.TYPE_MSG, msg)).sendToTarget();
                    createCallbackMsg("200");
                }
                isConnect = true;
            } catch (IOException e) {
                e.printStackTrace();
                isRunning = false;
                getMessage(HandlerType.TYPE_SERVER_STATUS, new MsgEngine(StatusType.TYPE_DISCONNECT)).sendToTarget();
                break;
            }
        }
    }

    private void createCallbackMsg(String msg) {
        connection.setIp(socket.getLocalAddress().toString());
        InstantMessage message = new InstantMessage(connection, StatusType.TYPE_MSG, msg);
        connection.callback(message.toString());
    }

}
