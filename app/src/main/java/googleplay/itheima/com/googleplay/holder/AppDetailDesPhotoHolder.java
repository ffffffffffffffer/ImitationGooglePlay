package googleplay.itheima.com.googleplay.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.xutils.x;

import java.util.List;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.bean.AppDetailInfoBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.utils.ToastUtils;

/**
 * @author TanJJ
 * @time 2017/5/26 15:21
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.holder
 * @des 简介图片holder
 */

public class AppDetailDesPhotoHolder extends BaseHolder<AppDetailInfoBean> {

    private final Activity mActivity;
    private LinearLayout mLinearLayout;


    public AppDetailDesPhotoHolder(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public View initView() {
        View view = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout
                .detail_app_des_photo, null, false);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.detail_app_des_photo_linearLayout);
        return view;
    }

    @Override
    public void initData(AppDetailInfoBean appDetailInfoBean) {
        List<String> screen = appDetailInfoBean.getScreen();
        for (int i = 0; i < screen.size(); i++) {
            String photoUrl = screen.get(i);
            ImageView imageView = new ImageView(ResourceUtils.getContext());
            x.image().bind(imageView, Constants.BASE_SERVER + Constants.IMAGE_INTERFACE + photoUrl);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 5;
            layoutParams.bottomMargin = 5;
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 4;
            layoutParams.width = ResourceUtils.dp2px(150);
            layoutParams.height = ResourceUtils.dp2px(250);
            mLinearLayout.addView(imageView, layoutParams);
            ImageView childAt = (ImageView) mLinearLayout.getChildAt(i);
            final int finalI = i;
            childAt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(mActivity, "点击了第" + finalI + "个图片");
                }
            });
        }

    }
}
