package cn.neocross.libs.neosocket.bean;

/**
 * Created by shenhua on 2017-11-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class InstantMessage {

    private Connection connection;
    private int type;
    private String message;

    public InstantMessage(Connection connection, int type, String message) {
        this.connection = connection;
        this.type = type;
        this.message = message;
    }

    public InstantMessage(Connection connection, int type) {
        this.connection = connection;
        this.type = type;
    }

    public InstantMessage(Connection connection, String message) {
        this.connection = connection;
        this.message = message;
    }

    public InstantMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        if (connection == null) {
            return "{\"ip\":null," +
                    "\"type\":" + getType() + "," +
                    "\"message\":\"" + getMessage() +
                    "\"}";
        } else {
            return "{\"ip\":" + connection.getIp() + "," +
                    "\"type\":" + getType() + "," +
                    "\"message\":\"" + getMessage() +
                    "\"}";
        }
    }
}
