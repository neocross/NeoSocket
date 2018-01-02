package cn.neocross.libs.neosocket.callback;

/**
 * Created by shenhua on 2017-11-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public enum StatusType {

    /**
     * 启动服务端时发生错误
     */
    TYPE_SERVER_ERROR(-2),
    /**
     * 服务端启动中
     */
    TYPE_SERVER_STARTING(0),
    /**
     * 服务端已开启
     */
    TYPE_SERVER_STARTED(1),
    /**
     * 服务端关闭
     */
    TYPE_SERVER_STOP(-1),
    /**
     * 客户端连接中
     */
    TYPE_CONNECTING(2),
    /**
     * 已和客户端建立连接
     */
    TYPE_CONNECTED(3),
    /**
     * 与客服端断开连接
     */
    TYPE_DISCONNECT(4),
    /**
     * 通讯中
     */
    TYPE_MSG(5);

    StatusType(int type) {
    }
}
