package cn.neocross.libs.neosocket.bean;

import cn.neocross.libs.neosocket.callback.StatusType;

/**
 * Created by shenhua on 2018-01-02-0002.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class MsgEngine {

    private StatusType type;
    private Object msg;

    public MsgEngine(StatusType type) {
        this.type = type;
    }

    public MsgEngine(StatusType type, Object msg) {
        this.type = type;
        this.msg = msg;
    }

    public StatusType getType() {
        return type;
    }

    public void setType(StatusType type) {
        this.type = type;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
