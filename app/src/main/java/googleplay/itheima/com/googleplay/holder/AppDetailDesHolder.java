package googleplay.itheima.com.googleplay.holder;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.activity.AppDetailActivity;
import googleplay.itheima.com.googleplay.bean.AppDetailInfoBean;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;

/**
 * @author TanJJ
 * @time 2017/5/26 16:13
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.holder
 * @des 简介holder
 */

public class AppDetailDesHolder extends BaseHolder<AppDetailInfoBean> {

    private final AppDetailActivity activity;
    private TextView mTextView_des;
    private TextView mTextView_author;
    private TextView mTextView_test_line_height;
    private static final int LINE_HEIGHT = 7;
    private int mMeasured_desHeight;
    private LinearLayout mLinearLayout_root;
    private static boolean flag = true;
    private int mLineHeight;

    public AppDetailDesHolder(Activity activity) {
        this.activity = (AppDetailActivity) activity;
    }

    @Override
    public View initView() {
        View view = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout.detail_app_des, null, false);
        mTextView_des = (TextView) view.findViewById(R.id.detail_app_des_textView);
        mTextView_author = (TextView) view.findViewById(R.id.detail_app_des_author);
        mLinearLayout_root = (LinearLayout) view.findViewById(R.id.detail_app_des_linearLayout);
        mTextView_test_line_height = new TextView(ResourceUtils.getContext());
        mTextView_test_line_height.setText(R.string.load_error);
        return view;
    }

    @Override
    public void initData(AppDetailInfoBean appDetailInfoBean) {
        mTextView_author.setText(ResourceUtils.getResource().getString(R.string.detail_app_des_author,
                appDetailInfoBean.getAuthor()));
        mTextView_des.setText(appDetailInfoBean.getDes());
        mLineHeight = getTestHeight();
        setAnimationForDes(mLineHeight);
        linearLayoutOnClick();
    }

    private void linearLayoutOnClick() {
        mLinearLayout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMeasured_desHeight - (mLineHeight * LINE_HEIGHT) > 50) {
                    toggle();
                }
            }
        });
    }

    private void toggle() {
        if (flag) {
            animationDes(mLineHeight * LINE_HEIGHT, mMeasured_desHeight);
            ResourceUtils.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.scrollView2Up();
                }
            });
        } else {
            animationDes(mMeasured_desHeight, mLineHeight * LINE_HEIGHT);
        }
        flag = !flag;
    }

    private void animationDes(int measured_desHeight, int lineHeight) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(measured_desHeight, lineHeight);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mTextView_des.getLayoutParams();
                layoutParams.height = value;
                mTextView_des.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    public int getTestHeight() {
        mTextView_test_line_height.measure(0, 0);
        LogUtil.e("测试TestView的高度:  " + mTextView_test_line_height.getMeasuredHeight());
        return mTextView_test_line_height.getMeasuredHeight();
    }

    public void setAnimationForDes(int animationForDes) {
        mTextView_des.measure(0, 0);
        mMeasured_desHeight = mTextView_des.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = mTextView_des.getLayoutParams();
        layoutParams.height = animationForDes * LINE_HEIGHT;
        mTextView_des.setLayoutParams(layoutParams);
    }
}
