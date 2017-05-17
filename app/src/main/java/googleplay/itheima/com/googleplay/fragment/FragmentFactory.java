package googleplay.itheima.com.googleplay.fragment;

import android.support.v4.app.Fragment;

/**
 * @author TanJJ
 * @time 2017/5/15 9:36
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des Fragment的工厂
 * @SVN_Version: $Rev$
 * @UpdateAuthor: $Author$
 * @UpdateTime: $Date$
 * @UpdateDes: TODO
 */

public class FragmentFactory {
    public static Fragment get(int position) {
        Fragment fragment = null;
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
