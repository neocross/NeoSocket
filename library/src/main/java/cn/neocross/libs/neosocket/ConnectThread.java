package cn.neocross.libs.neosocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 */
class ConnectThread extends Thread {

    private Socket socket;
    private NeoSocketServer neoSocketServer;
    private Connection connection;
    private boolean isRunning;

    public ConnectThread(Socket socket, NeoSocketServer neoSocketServer) {
        this.socket = socket;
        this.neoSocketServer = neoSocketServer;
        connection = new Connection(socket);
        isRunning = true;
    }

    public void stopRunning() {
        isRunning = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            if (socket.isClosed()) {
                isRunning = false;
                break;
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = bufferedReader.readLine();
                if (msg != null) {
                    neoSocketServer.getMessageCallback().onReceive(connection, msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
