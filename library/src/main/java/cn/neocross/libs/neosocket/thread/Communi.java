package cn.neocross.libs.neosocket.thread;

import com.google.gson.Gson;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.neocross.libs.neosocket.bean.InstantMessage;
import cn.neocross.libs.neosocket.callback.StatusType;

/**
 * Created by shenhua on 2017-12-29-0029.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class Communi {

    public ThreadPoolExecutor create() {
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
        return new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2), threadFactory);
    }

    public static boolean isClose(Gson gson, String msg) {
        InstantMessage instantMessage = gson.fromJson(msg, InstantMessage.class);
        return instantMessage.getType() == StatusType.TYPE_DISCONNECT;
    }

}
