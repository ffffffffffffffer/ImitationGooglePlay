package googleplay.itheima.com.googleplay.manager;

/**
 * @author TanJJ
 * @time 2017/5/18 7:34
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.manager
 * @des 线程池管理
 */

public class ThreadPoolManager {

    private static ThreadPoolProxy mThreadPoolProxy;
    //防止并发,上锁
    public static Object objects = new Object();

    public static ThreadPoolProxy getLongThread() {
        //双重判断
        if (mThreadPoolProxy == null) {
            synchronized (objects) {
                if (mThreadPoolProxy == null) {
                    mThreadPoolProxy = new ThreadPoolProxy(3, 3, 2);
                }
            }
        }

        return mThreadPoolProxy;
    }
}
