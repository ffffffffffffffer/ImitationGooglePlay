package googleplay.itheima.com.googleplay.fragment;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import googleplay.itheima.com.googleplay.R;

/**
 * @author TanJJ
 * @time 2017/5/17 8:58
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 加载容器
 * @SVN_Version: $Rev$
 * @UpdateAuthor: $Author$
 * @UpdateTime: $Date$
 * @UpdateDes: TODO
 */

public class LoadingUI extends FrameLayout {
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
    private View mSuccessView;

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
            mLoadingView = LayoutInflater.from(context).inflate(R.layout.loading_view, null);
            addView(mLoadingView);
        }
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(context).inflate(R.layout.empty_view, null);
            addView(mEmptyView);
        }
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(context).inflate(R.layout.error_view, null);
            addView(mErrorView);
        }
//        if (mSuccessView==null){
//            mSuccessView= LayoutInflater.from(context).inflate(R.layout.loading_view,null);
//        }
    }

    /**
     * 返回特定View
     *
     * @return 返回状态的View
     */
    public View getRootView() {
        return this;
    }
}
