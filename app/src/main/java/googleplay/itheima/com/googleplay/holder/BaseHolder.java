package googleplay.itheima.com.googleplay.holder;

import android.view.View;

/**
 * @author TanJJ
 * @time 2017/5/24 12:57
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.holder
 * @des 基类holder, 用于提供View/数据设置方法
 */

public abstract class BaseHolder<T> {
    /**
     * 获取要加载的View
     *
     * @return
     */
    public View getRootView() {
        return initView();
    }

    /**
     * 设置数据
     *
     * @param t
     */
    public void setData(T t) {
        initData(t);
    }

    public abstract View initView();

    public abstract void initData(T t);
}
