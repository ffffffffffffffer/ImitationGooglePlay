package googleplay.itheima.com.googleplay.fragment;

import googleplay.itheima.com.googleplay.base.BaseFragment;

/**
 * @author TanJJ
 * @time 2017/5/15 9:36
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des Fragment的工厂
 */

public class FragmentFactory {
    public static BaseFragment get(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new HomeFragment();
                break;
            case 3:
                fragment = new HomeFragment();
                break;
            case 4:
                fragment = new HomeFragment();
                break;
            case 5:
                fragment = new HomeFragment();
                break;
            case 6:
                fragment = new HomeFragment();
                break;
            case 7:
                fragment = new HomeFragment();
                break;
        }
        return fragment;
    }
}
