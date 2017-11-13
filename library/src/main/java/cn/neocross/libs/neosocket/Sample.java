package cn.neocross.libs.neosocket;

import java.net.InetAddress;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 */
public class Sample {

    public static void main(String[] args) throws Exception {
        NeoSocketServer server = new NeoSocketServer(5556, new MessageCallback() {
            @Override
            public void onReceive(Connection connection, String msg) {
                System.out.println(msg);
            }
        });
        System.out.println("Server starts.");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NeoSocketClient client = new NeoSocketClient(InetAddress.getLocalHost(), 5556);
        client.write("client say Hello!");
        System.out.println(client.read());
        server.close();
    }
}
