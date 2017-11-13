package cn.neocross.libs.neosocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 */
public class ListeningThread extends Thread {

    private NeoSocketServer neoSocketServer;
    private ServerSocket serverSocket;
    private Vector<ConnectThread> connectThreads;
    private boolean isRunning;

    ListeningThread(NeoSocketServer neoSocketServer, ServerSocket serverSocket) {
        this.neoSocketServer = neoSocketServer;
        this.serverSocket = serverSocket;
        connectThreads = new Vector<>();
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            if (serverSocket.isClosed()) {
                isRunning = false;
                break;
            }
            try {
                Socket socket = serverSocket.accept();
                ConnectThread connectThread = new ConnectThread(socket, neoSocketServer);
                connectThreads.addElement(connectThread);
                connectThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRunning() {
        for (int i = 0; i < connectThreads.size(); i++) {
            connectThreads.elementAt(i).stopRunning();
        }
        isRunning = false;
    }
}
