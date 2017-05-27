package googleplay.itheima.com.googleplay.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.base.BaseActivity;
import googleplay.itheima.com.googleplay.bean.AppDetailInfoBean;
import googleplay.itheima.com.googleplay.holder.AppDetailDesHolder;
import googleplay.itheima.com.googleplay.holder.AppDetailDesPhotoHolder;
import googleplay.itheima.com.googleplay.holder.AppDetailInfoHolder;
import googleplay.itheima.com.googleplay.holder.AppDetailSafeHolder;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;

/**
 * @author TanJJ
 * @time 2017/5/22 22:06
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.activity
 * @des 详情界面的Activity
 */

public class AppDetailActivity extends BaseActivity {
    @ViewInject(R.id.detail_image)
    ImageView mImageView;
    @ViewInject(R.id.detail_navigation)
    Button mImageView_navigation;
    @ViewInject(R.id.detail_textView)
    TextView mTextView;
    @ViewInject(R.id.detail_info)
    FrameLayout mFrameLayout_info;
    @ViewInject(R.id.detail_safe)
    FrameLayout mFrameLayout_safe;
    @ViewInject(R.id.detail_photo)
    FrameLayout mFrameLayout_photo;
    @ViewInject(R.id.detail_des)
    FrameLayout mFrameLayout_des;
    @ViewInject(R.id.detail_app_scrollView)
    ScrollView mScrollView;
    private Intent mIntent;

    private static final String appLocalPhotoUrl = Constants.BASE_SERVER + Constants.IMAGE_INTERFACE;
    private static final String appDetailUrl = Constants.BASE_SERVER + Constants.DETAIL_INTERFACE;
    private String mIconUrl;
    private String mPackageName;
    //超时时间
    private static final int accessTime = 5000;
    private AppDetailInfoHolder mAppDetailInfosHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        initData();
        loadData();
    }

    private void initView(AppDetailInfoBean mAppDetailInfoBean) {
//        //获取Detail的数据
//        AppDetailProtocol appDetailProtocol = new AppDetailProtocol(mPackageName);
//        mAppDetailInfoBean = appDetailProtocol.loadData();
        //添加应用详情
        mAppDetailInfosHolder = new AppDetailInfoHolder();
        mFrameLayout_info.addView(mAppDetailInfosHolder.getRootView());
        mAppDetailInfosHolder.setData(mAppDetailInfoBean);
        //添加应用安全
        AppDetailSafeHolder appDetailSafeHolder = new AppDetailSafeHolder();
        mFrameLayout_safe.addView(appDetailSafeHolder.getRootView());
        appDetailSafeHolder.setData(mAppDetailInfoBean);
        //添加应用简介图片
        AppDetailDesPhotoHolder appDetailDesPhotoHolder = new AppDetailDesPhotoHolder(this);
        mFrameLayout_photo.addView(appDetailDesPhotoHolder.getRootView());
        appDetailDesPhotoHolder.setData(mAppDetailInfoBean);
        //添加应用简介
        AppDetailDesHolder appDetailDesHolder = new AppDetailDesHolder(this);
        mFrameLayout_des.addView(appDetailDesHolder.getRootView());
        appDetailDesHolder.setData(mAppDetailInfoBean);

    }

    /**
     * 把ScrollView滚动到底部
     *
     * @return
     */
    public void scrollView2Up() {
        ResourceUtils.post(new Runnable() {
            @Override
            public void run() {
                if (mScrollView != null) {
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            }
        });
    }

    private void initData() {
        x.view().inject(this);
        //获取传递过来的数据
        mIntent = getIntent();
        String name = getResources().getString(R.string.app_name);
        if (mIntent != null) {
            mPackageName = mIntent.getStringExtra("packageName");
            name = mIntent.getStringExtra("name");
            mIconUrl = mIntent.getStringExtra("iconUrl");
        }
        //设置标题
        mTextView.setText(name);
        //加载上一级保存在本地的图片
        x.image().loadFile(appLocalPhotoUrl + mIconUrl, ImageOptions.DEFAULT, new
                Callback.CacheCallback<File>() {
                    @Override
                    public boolean onCache(File result) {
                        return false;
                    }

                    @Override
                    public void onSuccess(File result) {
                        Drawable fromPath = Drawable.createFromPath(result.getPath());
                        mImageView.setImageDrawable(fromPath);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mImageView.setImageDrawable(ResourceUtils.getDrawbleResource(R.drawable.ic_launcher));
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
        //设置点击navigation返回上一级
        mImageView_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void loadData() {
        //网络请求
        RequestParams entity = new RequestParams(appDetailUrl + mPackageName);
        entity.setConnectTimeout(accessTime);
        entity.setReadTimeout(accessTime);
        x.http().get(entity, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                gsonParse(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                ToastUtils.show(ResourceUtils.getContext().getap.getContext(), "访问失败!" + ex.getMessage());
                LogUtil.d("访问失败!" + ex.getMessage());
//                mTask = new TaskRunnable();
//                ThreadPoolManager.getLongThread().submit(mTask);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    private void gsonParse(String result) {
        Gson gson = new Gson();
        //获取T的类型
        //##############在不知道具体由什么Bean去解析时,使用以下方式获取##############
//        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
//        Type type1 = type.getActualTypeArguments()[0];
//        mAppDetailInfoBean = gson.fromJson(result, type1);

        initView(gson.fromJson(result, AppDetailInfoBean.class));
        //######################################################################
    }

}
