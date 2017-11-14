package cn.neocorss.os.neosocket.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.neocross.libs.neosocket.NeoSocketClient;
import cn.neocross.libs.neosocket.callback.NeoSocketClientCallback;

/**
 * Created by shenhua on 2017/11/13.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class MainActivity extends AppCompatActivity {

    private NeoSocketClient mClient;
    private int mDefaultPort = 5556;
    private String mIp = "192.168.16.18";
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

        // RejectedExecutionHandler 拒绝策略
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        /*
         * 参数1: 设置核心池大小
         * 参数2: 设置线程池最大能接受多少线程
         * 参数3: 当前线程数大于核心池大小、小于最大能接受线程时，超出核心池大小的线程数的生命周期
         * 参数4: 设置生命周期时间单位，秒
         * 参数5: 设置线程池缓存队列的排队策略为FIFO，并且指定缓存队列大小为2
         * 参数6: 线程工厂
         */
        mExecutor = new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2), threadFactory);
    }

    public void connect(View view) {
        mExecutor.submit(new ConnectRunnable());
    }

    public void disconnect(View view) {
        close();
    }

    private void close() {
        mExecutor.submit(new MessageRunnable("disconnect"));
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClient.close();
            }
        }, 1000);
    }

    public void send(View view) {
        mExecutor.submit(new MessageRunnable(mEditText.getText().toString()));
    }

    private class MessageRunnable implements Runnable {

        String msg;

        MessageRunnable(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            mClient.write(msg, new NeoSocketClientCallback() {
                @Override
                public void onStatusChange() {

                }

                @Override
                public void onMessageReceived(String msg) {
                    setText(msg);
                }
            });
        }
    }

    private class ConnectRunnable implements Runnable {
        @Override
        public void run() {
            try {
                mClient = new NeoSocketClient(InetAddress.getByName(mIp), mDefaultPort);
                setText("已连接");
            } catch (UnknownHostException e) {
                e.printStackTrace();
                setText("连接失败");
            }
        }
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
        super.onDestroy();
        close();
    }
}
