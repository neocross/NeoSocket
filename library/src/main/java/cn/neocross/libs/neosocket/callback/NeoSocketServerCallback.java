package cn.neocross.libs.neosocket.callback;

import cn.neocross.libs.neosocket.bean.InstantMessage;

/**
 * 服务端回调
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public interface NeoSocketServerCallback {

    /**
     * 连接成功
     *
     * @param type 状态回调
     * @see StatusType
     */
    void onServerStatusChanged(StatusType type);

    /**
     * 服务端接收到消息时回调
     *
     * @param instantMessage 及时消息体
     */
    void onServerMsgReceived(InstantMessage instantMessage);
}
