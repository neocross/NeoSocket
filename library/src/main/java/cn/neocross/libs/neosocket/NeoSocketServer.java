package cn.neocross.libs.neosocket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.neocross.libs.neosocket.bean.MsgEngine;
import cn.neocross.libs.neosocket.callback.HandlerType;
import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;
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
    private NeoSocketServerCallback serverCallback;
    private InstantMessageHandler instantMessageHandler;

    public NeoSocketServer(int port, NeoSocketServerCallback serverCallback) {
        this.serverCallback = serverCallback;
        serverCallback.onServerStatusChanged(new MsgEngine(StatusType.TYPE_SERVER_STARTING));
        try {
            serverSocket = new ServerSocket(port);
            listeningThread = new ListeningThread(this, serverSocket);
            listeningThread.start();
            serverCallback.onServerStatusChanged(new MsgEngine(StatusType.TYPE_SERVER_STARTED));
        } catch (IOException e) {
            e.printStackTrace();
            serverCallback.onServerStatusChanged(new MsgEngine(StatusType.TYPE_SERVER_ERROR));
            return;
        }
        instantMessageHandler = new InstantMessageHandler(this.serverCallback);
    }

    public void setServerCallback(NeoSocketServerCallback messageCallback) {
        this.serverCallback = messageCallback;
    }

    public boolean isClose() {
        return serverSocket == null || serverSocket.isClosed();
    }

    public NeoSocketServerCallback getServerCallback() {
        return serverCallback;
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
                getInstantMessageHandler().obtainMessage(HandlerType.TYPE_SERVER_MSG, StatusType.TYPE_SERVER_STOP).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public NeoSocketClient createNeoSocketClient(String ip, int port, NeoSocketClientCallback clientCallback) {
        try {
            NeoSocketClient client = new NeoSocketClient().connect(ip, port).get(5000, TimeUnit.MILLISECONDS);
            client.addClientListener(clientCallback);
            return client;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}
