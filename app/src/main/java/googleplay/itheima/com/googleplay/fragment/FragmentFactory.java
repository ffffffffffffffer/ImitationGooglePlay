package googleplay.itheima.com.googleplay.fragment;

import android.util.SparseArray;

import googleplay.itheima.com.googleplay.base.BaseFragment;

/**
 * @author TanJJ
 * @time 2017/5/15 9:36
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des Fragment的工厂
 */

public class FragmentFactory {
    //使用稀疏算法来保存Fragment
    private static SparseArray<BaseFragment> mSparseArray = new SparseArray<>();


    public static BaseFragment get(int position) {
        if (mSparseArray.get(position) == null) {
            switch (position) {
                case 0:
                    mSparseArray.append(position, new HomeFragment());
                    break;
                case 1:
                    mSparseArray.append(position, new AppFragment());
                    break;
                case 2:
                    mSparseArray.append(position, new GameFragment());
                    break;
                case 3:
                    mSparseArray.append(position, new SubjectFragment());
                    break;
                case 4:
                    mSparseArray.append(position, new HomeFragment());
                    break;
                case 5:
                    mSparseArray.append(position, new HomeFragment());
                    break;
                case 6:
                    mSparseArray.append(position, new HotFragment());
                    break;
            }
        }
        return mSparseArray.get(position);
    }
}
