package cn.neocross.libs.neosocket.callback;

/**
 * 客户端回调
 * Created by shenhua on 2017-11-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public interface NeoSocketClientCallback {

    /**
     * 状态改变时回调
     */
    void onClientStatusChange();

    /**
     * 接收到服务端消息时回调
     *
     * @param msg json
     */
    void onClientMessageReceived(String msg);
}
