package cn.neocross.libs.neosocket;

import java.io.IOException;
import java.net.ServerSocket;

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
        messageCallback.onServerStatusChanged(StatusType.TYPE_SERVER_STARTING);
        try {
            serverSocket = new ServerSocket(port);
            listeningThread = new ListeningThread(this, serverSocket);
            listeningThread.start();
            messageCallback.onServerStatusChanged(StatusType.TYPE_SERVER_STARTED);
        } catch (IOException e) {
            e.printStackTrace();
            messageCallback.onServerStatusChanged(StatusType.TYPE_SERVER_ERROR);
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
