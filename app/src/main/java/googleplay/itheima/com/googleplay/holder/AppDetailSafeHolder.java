package googleplay.itheima.com.googleplay.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.List;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.bean.AppDetailInfoBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;

/**
 * @author TanJJ
 * @time 2017/5/25 20:56
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.holder
 * @des 详情的安全Item
 */

public class AppDetailSafeHolder extends BaseHolder<AppDetailInfoBean> {

    private static final int duration = 200;

    private LinearLayout mLinearLayout;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private ImageView mImageView_arrow;
    private ImageView mImageView_des1;
    private ImageView mImageView_des2;
    private ImageView mImageView_des3;
    private TextView mTextView_des1;
    private TextView mTextView_des2;
    private TextView mTextView_des3;
    private boolean flag;
    private int mLinearLayout_height;

    @Override
    public View initView() {
        View view = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout.detail_app_safe, null, false);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.detail_safe_linearLayout);
        mImageView1 = (ImageView) view.findViewById(R.id.detail_safe_image1);
        mImageView2 = (ImageView) view.findViewById(R.id.detail_safe_image2);
        mImageView3 = (ImageView) view.findViewById(R.id.detail_safe_image3);
        mImageView_arrow = (ImageView) view.findViewById(R.id.detail_safe_arrow);
        mImageView_des1 = (ImageView) view.findViewById(R.id.detail_safe_des_image1);
        mImageView_des2 = (ImageView) view.findViewById(R.id.detail_safe_des_image2);
        mImageView_des3 = (ImageView) view.findViewById(R.id.detail_safe_des_image3);
        mTextView_des1 = (TextView) view.findViewById(R.id.detail_safe_des_des1);
        mTextView_des2 = (TextView) view.findViewById(R.id.detail_safe_des_des2);
        mTextView_des3 = (TextView) view.findViewById(R.id.detail_safe_des_des3);
        return view;
    }

    @Override
    public void initData(AppDetailInfoBean appDetailInfoBean) {
        List<AppDetailInfoBean.SafeBean> safe = appDetailInfoBean.getSafe();
        for (int i = 0; i < safe.size(); i++) {
            AppDetailInfoBean.SafeBean safeBean = safe.get(i);
            //加载数据
            switch (i) {
                case 0:
                    mTextView_des1.setText(safeBean.getSafeDes());
                    //加载图片
                    loadPhoto_safe(mImageView1, safeBean.getSafeUrl());
                    loadPhoto_safe(mImageView_des1, safeBean.getSafeDesUrl());
                    break;
                case 1:
                    mTextView_des2.setText(safeBean.getSafeDes());
                    //加载图片
                    loadPhoto_safe(mImageView2, safeBean.getSafeUrl());
                    loadPhoto_safe(mImageView_des2, safeBean.getSafeDesUrl());
                    break;
                case 2:
                    mTextView_des3.setText(safeBean.getSafeDes());
                    //加载图片
                    loadPhoto_safe(mImageView3, safeBean.getSafeUrl());
                    loadPhoto_safe(mImageView_des3, safeBean.getSafeDesUrl());
                    break;
            }

        }

        realizeViewEvent();
    }

    private void animation2Arrow(int x, int y, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImageView_arrow, "rotation", x, y);
        animator.setDuration(duration);
        animator.start();
    }

    private void realizeViewEvent() {
        mLinearLayout.measure(0, 0);
        mLinearLayout_height = mLinearLayout.getMeasuredHeight();
        initImageViewHeight();
        //设置View的可见范围
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    public void toggle() {
        if (flag) {
            changeImageViewHeight(mLinearLayout_height, mImageView_height, duration);
            animation2Arrow(-180, 0, duration);
        } else {
            changeImageViewHeight(mImageView_height, mLinearLayout_height, duration);
            animation2Arrow(0, 180, duration);
        }
        flag = !flag;
    }

    private void loadPhoto_safe(ImageView imageView, String safeUrl) {
        x.image().bind(imageView, Constants.BASE_SERVER + Constants.IMAGE_INTERFACE + safeUrl);
    }

    /**
     * 初始Imageview的高度
     */
    public void initImageViewHeight() {
        mLinearLayout.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                mLinearLayout.getViewTreeObserver().removeOnDrawListener(this);
                mImageView1.measure(0, 0);
                int paddingTop = mLinearLayout.getPaddingTop();
                int paddingBottom = mLinearLayout.getPaddingBottom();
                mImageView_height = mImageView1.getHeight() + paddingTop + paddingBottom;
                LogUtil.e("mImageView_height  " + mImageView_height);
                //初始化位置
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.height = mImageView_height;
                mLinearLayout.setLayoutParams(params);
            }
        });
    }

    private int mImageView_height;

    /**
     * 初始Imageview的高度
     */
    public void changeImageViewHeight(int imageView_height, int linearLayout_height, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(imageView_height, linearLayout_height);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.height = animatedValue;
                mLinearLayout.setLayoutParams(params);

            }
        });
        valueAnimator.start();
    }
}
