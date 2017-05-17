package googleplay.itheima.com.googleplay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.base.BaseActivity;
import googleplay.itheima.com.googleplay.fragment.FragmentFactory;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;
    @ViewInject(R.id.dl_drawer)
    DrawerLayout mDrawerLayout;
    @ViewInject(R.id.viewpager)
    ViewPager mViewPager;
    @ViewInject(R.id.pagersliding)
    PagerSlidingTabStrip mPagerSlidingTabStrip;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    private void initData() {
        //初始化view
        x.view().inject(this);
        //设置标题
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setLogo(R.drawable.ic_launcher);
        //定义抽屉开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        toggle.syncState();
        //设置了这个,连Navigation都不用设置了,还自带动画
        mDrawerLayout.addDrawerListener(toggle);
        //初始化paper
        initPaper();

    }

    private void initPaper() {
        //获取tab数据
        final String[] tabArray = getResources().getStringArray(R.array.paper_tab);


        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabArray[position];
            }

            @Override
            public Fragment getItem(int position) {
                return FragmentFactory.get(position);
            }

            @Override
            public int getCount() {
                return tabArray.length;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                FragmentFactory.get(position).loadDate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
