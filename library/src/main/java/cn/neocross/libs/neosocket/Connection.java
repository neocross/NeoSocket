package cn.neocross.libs.neosocket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 */
public class Connection {

    private Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;
    }

    public void println(String message) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()), true);
            writer.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
