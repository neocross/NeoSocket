package cn.neocross.libs.neosocket.thread;

import android.os.Handler;
import android.os.Message;

import cn.neocross.libs.neosocket.bean.MsgEngine;
import cn.neocross.libs.neosocket.callback.HandlerType;
import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;
import cn.neocross.libs.neosocket.callback.NeoSocketServerCallback;

/**
 * Created by shenhua on 2017-11-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class InstantMessageHandler extends Handler {

    private NeoSocketServerCallback serverCallback;
    private NeoSocketClientCallback clientCallback;

    public InstantMessageHandler(NeoSocketServerCallback callback) {
        this.serverCallback = callback;
    }

    public InstantMessageHandler(NeoSocketClientCallback callback) {
        this.clientCallback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == HandlerType.TYPE_SERVER_STATUS) {
            MsgEngine engine = (MsgEngine) msg.obj;
            serverCallback.onServerStatusChanged(engine);
        } else if (msg.what == HandlerType.TYPE_SERVER_MSG) {
            MsgEngine engine = (MsgEngine) msg.obj;
            serverCallback.onServerMsgReceived(engine.getMsg());
        } else if (msg.what == HandlerType.TYPE_CLIENT_STATUS) {
            clientCallback.onClientStatusChange();
        } else if (msg.what == HandlerType.TYPE_CLIENT_MSG) {
            MsgEngine engine = (MsgEngine) msg.obj;
            clientCallback.onClientMessageReceived(engine.getMsg());
        }
    }
}
