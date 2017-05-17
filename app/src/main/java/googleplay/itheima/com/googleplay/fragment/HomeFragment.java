package googleplay.itheima.com.googleplay.fragment;

import java.util.Random;

import googleplay.itheima.com.googleplay.base.BaseFragment;

/**
 * @author TanJJ
 * @time 2017/5/15 9:14
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 首页Fragment
 */

public class HomeFragment extends BaseFragment {
    @Override
    public LoadingUI.LoadingEnum initData() {
        LoadingUI.LoadingEnum[] enums = {LoadingUI.LoadingEnum.ERROR, LoadingUI.LoadingEnum.EMPTY
                , LoadingUI.LoadingEnum.SUCCESS};
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        return enums[random.nextInt(3)];
    }
}
