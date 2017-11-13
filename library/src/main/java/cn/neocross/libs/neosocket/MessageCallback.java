package cn.neocross.libs.neosocket;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 */
public interface MessageCallback {
    void onReceive(Connection connection, String msg);
}
