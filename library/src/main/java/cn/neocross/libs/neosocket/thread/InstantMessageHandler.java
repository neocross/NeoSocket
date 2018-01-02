package cn.neocross.libs.neosocket.thread;

import android.os.Handler;
import android.os.Message;

import cn.neocross.libs.neosocket.bean.MsgEngine;
import cn.neocross.libs.neosocket.callback.NeoSocketServerCallback;

/**
 * Created by shenhua on 2017-11-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class InstantMessageHandler extends Handler {

    private NeoSocketServerCallback callback;

    public InstantMessageHandler(NeoSocketServerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {
            MsgEngine engine = (MsgEngine) msg.obj;
            callback.onServerStatusChanged(engine);
        } else if (msg.what == 1) {
            MsgEngine engine = (MsgEngine) msg.obj;
            callback.onServerMsgReceived(engine.getMsg());
        }


//        if (msg.what == 1) {
//            StatusType type = (StatusType) msg.obj;
//            callback.onServerStatusChanged(type);
//        } else if (msg.what == 2) {
//            callback.onServerMsgReceived((InstantMessage) msg.obj);
//        } else {
//
//        }
    }
}
