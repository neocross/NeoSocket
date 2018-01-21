package cn.neocross.libs.neosocket.bean;

import com.google.gson.Gson;

import cn.neocross.libs.neosocket.callback.StatusType;

/**
 * Created by shenhua on 2017-11-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class InstantMessage {

    private Connection connection;
    private StatusType type;
    private Object message;

    public InstantMessage(Connection connection, StatusType type, Object message) {
        this.connection = connection;
        this.type = type;
        this.message = message;
    }

    public InstantMessage(StatusType type, Object message) {
        this.type = type;
        this.message = message;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public StatusType getType() {
        return type;
    }

    public void setType(StatusType type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
