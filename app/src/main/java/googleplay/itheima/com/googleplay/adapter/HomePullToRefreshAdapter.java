package googleplay.itheima.com.googleplay.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.x;

import java.util.List;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.bean.HomeBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;

/**
 * @author TanJJ
 * @time 2017/5/19 11:16
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.adapter
 * @des 主页的适配器
 */

public class HomePullToRefreshAdapter extends BaseQuickAdapter<HomeBean.ListBean, BaseViewHolder> {
    public HomePullToRefreshAdapter(@LayoutRes int layoutResId, @Nullable List<HomeBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder textHolder, HomeBean.ListBean listBean) {
        //获取View
        TextView textView_name = textHolder.getView(R.id.tv_name);
        TextView textView_download = textHolder.getView(R.id.textview_download);
        TextView textView_des = textHolder.getView(R.id.textview_des);
        TextView textView_size = textHolder.getView(R.id.textview_size);
        ImageView imageview_download = textHolder.getView(R.id.iv_download_image);
        ImageView imageview_logo = textHolder.getView(R.id.image_logo);
        RatingBar ratingBar_stars = textHolder.getView(R.id.ratingbar_star);

        //设置标题
        textView_name.setText(listBean.getName());
        textView_name.setTextColor(Color.BLACK);
        textView_name.setTextSize(16);
        //设置大小
        textView_size.setText(android.text.format.Formatter.formatFileSize(ResourceUtils
                .getContext(), listBean.getSize()));
        textView_size.setTextColor(Color.GRAY);
        //设置介绍
        textView_des.setText(listBean.getDes());
        textView_des.setTextColor(Color.GRAY);
        //靠,设置了cardView背景色后全部文本都要设置字体颜色
        textView_download.setTextColor(Color.GRAY);

        //设置星星 //
        ratingBar_stars.setProgress((int) listBean.getStars());
        ratingBar_stars.setRating(listBean.getStars());
        //设置图片 //
        x.image().bind(imageview_logo, Constants.BASE_SERVER + Constants.IMAGE_INTERFACE +
                listBean.getIconUrl());
    }
}
