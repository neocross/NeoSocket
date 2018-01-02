package cn.neocross.os.neosocket.server;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.neocross.libs.neosocket.NeoSocketClient;
import cn.neocross.libs.neosocket.NeoSocketServer;
import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.bean.MsgEngine;
import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;
import cn.neocross.libs.neosocket.callback.NeoSocketServerCallback;
import cn.neocross.libs.neosocket.callback.StatusType;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class MainActivity extends AppCompatActivity implements NeoSocketServerCallback, NeoSocketClientCallback {

    private TextView mTextView;
    private NeoSocketServer mServer;
    private NeoSocketClient mClient;
    private int mDefaultPort = 5556;
    private static SimpleDateFormat sSdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.sample_text);

        mServer = new NeoSocketServer(mDefaultPort, this);
        setText("当前ip:" + mServer.getSelfIp(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mServer.isClose()) {
            mServer = new NeoSocketServer(mDefaultPort, this);
        }
    }

    @Override
    protected void onPause() {
        mServer.close();
        super.onPause();
    }

    @Override
    public void onServerStatusChanged(MsgEngine msgEngine) {
        switch (msgEngine.getType()) {
            case TYPE_SERVER_STARTED:
                setText("启动成功,当前端口:" + mDefaultPort);
                break;
            case TYPE_SERVER_STARTING:
                setText("启动中......");
                break;
            case TYPE_SERVER_STOP:
                setText("客户端主动断开");
                break;
            case TYPE_SERVER_ERROR:
                setText("启动错误");
                break;
            case TYPE_CONNECTING:
                setText("发现客户端连接");
                break;
            case TYPE_CONNECTED:
                setText("客户端已连接 : " + msgEngine.getMsg());
                mClient = mServer.createNeoSocketClient(msgEngine.getMsg(), 5557, this);
                break;
            case TYPE_DISCONNECT:
                setText("客户端断开连接");
                break;
            default:
                break;
        }
    }

    @Override
    public void onServerMsgReceived(String message) {
        setText(message);
    }

    private void setText(String msg) {
        mTextView.append("\n");
        mTextView.append(getTime());
        mTextView.append("  > ");
        mTextView.append(msg);
    }

    private synchronized String getTime() {
        return sSdf.format(new Date());
    }

    public void send(View view) {
        mClient.send(new InstantMessage(StatusType.TYPE_MSG, "来自服务端的虚拟客户端"));
    }

    @Override
    public void onClientStatusChange() {
        setText("本机客户端断开");
    }

    @Override
    public void onClientMessageReceived(final String msg) {
        setText(msg);
    }
}
