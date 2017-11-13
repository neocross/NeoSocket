package cn.neocross.libs.neosocket;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 */
public class NeoSocketServer {

    private ServerSocket serverSocket;
    private ListeningThread listeningThread;
    private MessageCallback messageCallback;


    public NeoSocketServer(int port, MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
        try {
            serverSocket = new ServerSocket(port);
            listeningThread = new ListeningThread(this, serverSocket);
            listeningThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMessageCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public MessageCallback getMessageCallback() {
        return messageCallback;
    }

    public void close() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            listeningThread.stopRunning();
            listeningThread.interrupt();
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
