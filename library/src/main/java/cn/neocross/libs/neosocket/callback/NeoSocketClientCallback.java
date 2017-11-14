package cn.neocross.libs.neosocket.callback;

/**
 * 客户端回调
 * Created by shenhua on 2017-11-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public interface NeoSocketClientCallback {

    void onStatusChange();

    void onMessageReceived(String msg);
}
