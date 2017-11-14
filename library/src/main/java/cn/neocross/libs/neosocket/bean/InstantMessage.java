package cn.neocross.libs.neosocket.bean;

/**
 * Created by shenhua on 2017-11-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class InstantMessage {

    private Connection connection;
    private String message;

    public InstantMessage(Connection connection, String message) {
        this.connection = connection;
        this.message = message;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
