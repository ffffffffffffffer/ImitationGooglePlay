package googleplay.itheima.com.googleplay.fragment;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.manager.ThreadPoolManager;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;

import static googleplay.itheima.com.googleplay.fragment.LoadingUI.LoadingEnum.EMPTY;
import static googleplay.itheima.com.googleplay.fragment.LoadingUI.LoadingEnum.ERROR;
import static googleplay.itheima.com.googleplay.fragment.LoadingUI.LoadingEnum.LOADING;
import static googleplay.itheima.com.googleplay.fragment.LoadingUI.LoadingEnum.SUCCESS;

/**
 * @author TanJJ
 * @time 2017/5/17 8:58
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 加载容器
 */

public abstract class LoadingUI extends FrameLayout {
    //把要显示的View定义出来
    /*
     *      1.加载中
     *          1.内容为空
     *          2.加载失败
     *          3.加载成功
     */
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    //暂时不知道怎么写
    private View mSuccessView;

    //使用标记来确定显示那个View
    private int flag = LOADING.getState();
    private static final int LOADING_STATE = 0;
    private static final int EMPTY_STATE = 1;
    private static final int ERROR_STATE = 2;
    private static final int SUCCESS_STATE = 3;
    private TaskRunnable mTask;

    public LoadingUI(@NonNull Context context) {
        super(context);
        initView(context);
    }


    public LoadingUI(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingUI(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(context).inflate(R.layout.loading_view, null, false);
            addView(mLoadingView);
        }
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(context).inflate(R.layout.empty_view, null, false);
            addView(mEmptyView);
        }
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(context).inflate(R.layout.error_view, null, false);
            addView(mErrorView);
        }

        updateUI();
    }

    private void updateUI() {
        //View的显示
        mLoadingView.setVisibility(flag == LOADING.getState() ? View.VISIBLE : View.INVISIBLE);
        mEmptyView.setVisibility(flag == EMPTY.getState() ? View.VISIBLE : View.INVISIBLE);
        mErrorView.setVisibility(flag == ERROR.getState() ? View.VISIBLE : View.INVISIBLE);
        if (flag == SUCCESS.getState()) {
            if (mSuccessView == null) {
                //加载布局
                mSuccessView = onSuccessView();
            }
            mSuccessView.setVisibility(View.VISIBLE);
            //加载成功的view
            ViewParent parent = mSuccessView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mSuccessView);
            }
            addView(mSuccessView);
        } else {
            if (mSuccessView != null) {
                mSuccessView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 访问成功显示View,要给具体的类实现
     *
     * @return 返回显示的View
     */
    protected abstract View onSuccessView();

    /**
     * 只有调用者才知道当前要干什么
     *
     * @return LoadingEnum 用来控制显示的View
     */
    public abstract LoadingEnum onInitData();

    //安全地更新ui
    public void safeUpdataUI() {
        ResourceUtils.post(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }

    public void loadData() {
        if (flag == SUCCESS.getState()) {
            return;
        }
        //重新加载
        flag = LOADING.getState();
        safeUpdataUI();

        //使用线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        //开启子线程去加载数据
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                LoadingEnum loadingEnum = onInitData();
//                flag = loadingEnum.getState();
//                safeUpdataUI();
//
//            }
//        });

        //使用线程管理来创建线程池
        mTask = new TaskRunnable();
        ThreadPoolManager.getLongThread().submit(mTask);
    }

    public void removeThreadPoolTask() {
        ThreadPoolManager.getLongThread().remove(mTask);
    }

    private class TaskRunnable implements Runnable {

        @Override
        public void run() {
            LoadingEnum loadingEnum = onInitData();
            flag = loadingEnum.getState();
            safeUpdataUI();
        }
    }


    //只使用int值来表示flag可能存在漏洞,但用enum来限定的话,就不会出现enum以外的值
    public enum LoadingEnum {
        LOADING(LOADING_STATE), EMPTY(EMPTY_STATE), ERROR(ERROR_STATE), SUCCESS(SUCCESS_STATE);
        private int state;

        LoadingEnum(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

//    /**
//     * 返回特定View
//     *
//     * @return 返回状态的View
//     */
//    public View getRootView() {
//        View view = null;
//        if (flag == LOADING.getState() && mLoadingEnum == null) {
//            view = mLoadingView;
//            return view;
//        }
//        if (mLoadingEnum.getState() == EMPTY.getState()) {
//            view = mEmptyView;
//        } else if (mLoadingEnum.getState() == ERROR.getState()) {
//            view = mErrorView;
//        } else if (mLoadingEnum.getState() == SUCCESS.getState()) {
//            view = mSuccessView;
//        }
//        return view;
//    }


}
