package cn.neocorss.os.neosocket.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.neocross.libs.neosocket.NeoSocketClient;
import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class MainActivity extends AppCompatActivity implements NeoSocketClientCallback {

    private NeoSocketClient mClient;
    private int mDefaultPort = 5556;
    private String mIp = "192.168.16.17";
    private ThreadPoolExecutor mExecutor;
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
            mClient = new NeoSocketClient().connect(mIp, mDefaultPort).get(5000, TimeUnit.SECONDS);
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
        mClient.send(new InstantMessage(0, mEditText.getText().toString()));
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
    public void onStatusChange() {
        setText("状态改变");
    }

    @Override
    public void onMessageReceived(String msg) {
        setText("回调消息:" + msg);
    }
}
