package cn.neocross.libs.neosocket.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import cn.neocross.libs.neosocket.NeoSocketServer;

/**
 * 阻塞式监听线程
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class ListeningThread extends Thread {

    private NeoSocketServer neoSocketServer;
    private ServerSocket serverSocket;
    /**
     * 自增长的对象数组,保存已连接线程
     */
    private Vector<ConnectThread> connectThreads;
    private boolean isRunning;

    public ListeningThread(NeoSocketServer neoSocketServer, ServerSocket serverSocket) {
        super.setName("ListeningThread");
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
                // 当前阻塞,说明设备已经连接
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
