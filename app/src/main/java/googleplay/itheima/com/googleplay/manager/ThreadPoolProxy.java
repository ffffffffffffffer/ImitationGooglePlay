package googleplay.itheima.com.googleplay.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author TanJJ
 * @time 2017/5/18 7:35
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.manager
 * @des 线程池代理
 */

public class ThreadPoolProxy {
    private final int corePoolSize;
    private final int maximumPoolSize;
    private final long keepAliveTime;
    private ThreadPoolExecutor mExecutor;

    /**
     * @param corePoolSize    线程池大小
     * @param maximumPoolSize 线程池最大大小
     * @param keepAliveTime   存活时间(秒)
     */
    ThreadPoolProxy(int corePoolSize,
                    int maximumPoolSize,
                    long keepAliveTime) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
    }

    /**
     * 检测有没有线程池或者关闭的,没就创建新的
     */
    private void checkPool() {
        if (mExecutor == null || mExecutor.isShutdown()) {
            //创建队列方式:不固定大小
            BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
            //使用默认的工厂模式
            ThreadFactory factory = Executors.defaultThreadFactory();

            //错误捕获器
            RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

            mExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                    blockingQueue, factory, handler);
        }
    }

    /**
     * 执行线程任务
     *
     * @param task
     */
    public void submit(Runnable task) {
        if (task == null) {
            return;
        }
        checkPool();
        mExecutor.submit(task);
    }

    /**
     * 清除线程
     *
     * @param task
     */
    public void remove(Runnable task) {
        if (task == null) {
            return;
        }
        checkPool();
        mExecutor.getQueue().remove(task);
    }

}
