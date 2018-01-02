package cn.neocross.libs.neosocket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.ServerSocket;

import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.bean.MsgEngine;
import cn.neocross.libs.neosocket.callback.NeoSocketServerCallback;
import cn.neocross.libs.neosocket.callback.StatusType;
import cn.neocross.libs.neosocket.thread.InstantMessageHandler;
import cn.neocross.libs.neosocket.thread.ListeningThread;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class NeoSocketServer {

    private ServerSocket serverSocket;
    private ListeningThread listeningThread;
    private NeoSocketServerCallback messageCallback;
    private InstantMessageHandler instantMessageHandler;

    public NeoSocketServer(int port, NeoSocketServerCallback messageCallback) {
        this.messageCallback = messageCallback;
        messageCallback.onServerStatusChanged(new MsgEngine(StatusType.TYPE_SERVER_STARTING));
        try {
            serverSocket = new ServerSocket(port);
            listeningThread = new ListeningThread(this, serverSocket);
            listeningThread.start();
            messageCallback.onServerStatusChanged(new MsgEngine(StatusType.TYPE_SERVER_STARTED));
        } catch (IOException e) {
            e.printStackTrace();
            messageCallback.onServerStatusChanged(new MsgEngine(StatusType.TYPE_SERVER_ERROR));
            return;
        }
        instantMessageHandler = new InstantMessageHandler(this.messageCallback);
    }

    public void setMessageCallback(NeoSocketServerCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public boolean isClose() {
        return serverSocket == null || serverSocket.isClosed();
    }

    public NeoSocketServerCallback getMessageCallback() {
        return messageCallback;
    }

    public InstantMessageHandler getInstantMessageHandler() {
        return instantMessageHandler;
    }

    public String getSelfIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return "Error";
        }
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    public void push(InstantMessage message) {

    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 服务端断开
     */
    public void close() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            listeningThread.stopRunning();
            listeningThread.interrupt();
            try {
                serverSocket.close();
                getInstantMessageHandler().obtainMessage(1, StatusType.TYPE_SERVER_STOP).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
