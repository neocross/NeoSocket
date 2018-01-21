package cn.neocorss.os.neosocket.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
public class MainActivity extends AppCompatActivity implements NeoSocketClientCallback, NeoSocketServerCallback {

    private NeoSocketClient mClient;
    private int mDefaultPort = 5556;
    private String mIp = "192.168.16.16";
    private TextView mTextView;
    private EditText mEditText;
    private static SimpleDateFormat sSdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tvText);
        mEditText = (EditText) findViewById(R.id.etText);
    }

    public void connect(View view) {
        try {
            mClient = new NeoSocketClient().connect(mIp, mDefaultPort).get(5000, TimeUnit.MILLISECONDS);
            mClient.addClientListener(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("错误: InterruptedException");
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.out.println("错误: ExecutionException");
        } catch (TimeoutException e) {
            e.printStackTrace();
            System.out.println("错误: TimeoutException");
        }
    }

    public void disconnect(View view) {
        mClient.close();
    }

    public void send(View view) {
        mClient.send(new InstantMessage(StatusType.TYPE_MSG, mEditText.getText().toString()));
    }

    private void setText(final String msg) {
        if (msg == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.append("\n");
                mTextView.append(getTime());
                mTextView.append("  -> ");
                mTextView.append(msg);
            }
        });
    }

    private synchronized String getTime() {
        return sSdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        mClient.close();
        super.onDestroy();
    }

    @Override
    public void onClientStatusChange() {
        setText("本机客户端断开");
    }

    @Override
    public void onClientMessageReceived(Object msg) {
        setText(msg.toString());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new NeoSocketServer(5557, this);
    }

    @Override
    public void onServerStatusChanged(MsgEngine msgEngine) {
        switch (msgEngine.getType()) {
            case TYPE_SERVER_STARTED:
                setText("启动成功,当前端口:5557");
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
                break;
            case TYPE_DISCONNECT:
                setText("客户端断开连接");
                break;
            default:
                break;
        }
    }

    @Override
    public void onServerMsgReceived(Object message) {
        setText(message.toString());
    }
}
