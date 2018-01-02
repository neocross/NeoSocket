package cn.neocross.libs.neosocket;

import java.net.InetAddress;

import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.bean.MsgEngine;
import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;
import cn.neocross.libs.neosocket.callback.NeoSocketServerCallback;
import cn.neocross.libs.neosocket.callback.StatusType;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class Sample {

    public static void main(String[] args) throws Exception {
        NeoSocketServer server = new NeoSocketServer(5556, new NeoSocketServerCallback() {

            @Override
            public void onServerStatusChanged(MsgEngine msgEngine) {

            }

            @Override
            public void onServerMsgReceived(String message) {
                System.out.println("Server receive the client message: " + message);
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NeoSocketClient client = new NeoSocketClient(InetAddress.getLocalHost(), 5556);
        client.addClientListener(new NeoSocketClientCallback() {
            @Override
            public void onStatusChange() {

            }

            @Override
            public void onMessageReceived(String msg) {
                System.out.println("Server return: " + msg);
            }
        });
        client.send(new InstantMessage(StatusType.TYPE_MSG, "client say Hello!"));
        server.close();
    }
}
