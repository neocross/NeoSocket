package cn.neocross.libs.neosocket.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 连接的设备
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class Connection {

    private transient Socket socket;
    /**
     * 已连接设备的IP
     */
    private String ip;

    public Connection(Socket socket) {
        this.socket = socket;
        this.ip = socket.getInetAddress().toString();
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    /**
     * 服务端给客户端的回调消息
     *
     * @param message 消息
     */
    public void callback(String message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            message += "\n";
            outputStream.write(message.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
