package cn.neocross.libs.neosocket.callback;

import cn.neocross.libs.neosocket.bean.MsgEngine;

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
     * @param msgEngine 消息引擎
     * @see MsgEngine
     */
    void onServerStatusChanged(MsgEngine msgEngine);

    /**
     * 服务端接收到消息时回调
     *
     * @param message 及时消息体
     */
    void onServerMsgReceived(Object message);
}
