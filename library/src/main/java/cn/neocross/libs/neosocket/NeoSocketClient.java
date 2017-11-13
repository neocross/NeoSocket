package cn.neocross.libs.neosocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 */
public class NeoSocketClient {

    private Socket socket;

    public NeoSocketClient(InetAddress inetAddress, int port) {
        try {
            socket = new Socket(inetAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String msg) {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            printWriter.write(msg);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket.close();
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
