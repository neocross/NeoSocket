package cn.neocross.libs.neosocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class NeoSocketClient {

    private Socket socket;

    public NeoSocketClient(InetAddress inetAddress, int port) {
        try {
            socket = new Socket(inetAddress, port);
            socket.setKeepAlive(true);
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String msg) {
        write(msg, null);
    }

    public void write(String msg, NeoSocketClientCallback callback) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            msg += "\n";
            outputStream.write(msg.getBytes("utf-8"));
            if (callback != null) {
                String result = read();
                callback.onMessageReceived(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onStatusChange();
            }
        }
    }

    private String read() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
