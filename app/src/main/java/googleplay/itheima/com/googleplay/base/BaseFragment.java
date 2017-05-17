package googleplay.itheima.com.googleplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import googleplay.itheima.com.googleplay.fragment.LoadingUI;

/**
 * @author TanJJ
 * @time 2017/5/15 9:14
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 基类Fragment
 * @SVN_Version: $Rev$
 * @UpdateAuthor: $Author$
 * @UpdateTime: $Date$
 * @UpdateDes: TODO
 */

public class BaseFragment extends Fragment {

    @Nullable
    @Override
    /**
     * 分析:
     *      对所有的Fragment进行抽取出基类
     * 共性:
     *      1.加载中
     *          1.内容为空
     *          2.加载失败
     *          3.加载成功
     * 由于有多种加载结果,而onCreateView又只能返回一种,那么就不能按简单的返回方式解决,要想出可以控制返回结果的方法,
     * 可以用容器实现,而能胜任的唯有继承了ViewGroup的类才行,而当前的是Fragment显示内容,就用FrameLayout布局存放,
     * 把全部状态放进容器中,可以根据访问后的结果来确定到底显示那个内容
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
//        TextView textView = new TextView(ResourceUtils.getContext());
//        textView.setText("首页");
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(25);
//        return textView;
        //创建容器
        LoadingUI loadingUI = new LoadingUI(getContext());
        return loadingUI.getRootView();
    }
}
