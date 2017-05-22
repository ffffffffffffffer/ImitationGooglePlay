package googleplay.itheima.com.googleplay.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.x;

import java.util.List;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.bean.SubjectBean;
import googleplay.itheima.com.googleplay.utils.Constants;

/**
 * @author TanJJ
 * @time 2017/5/19 11:16
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.adapter
 * @des 主页的适配器
 */

public class SubjectPullToRefreshAdapter extends BaseQuickAdapter<SubjectBean, BaseViewHolder> {
    public SubjectPullToRefreshAdapter(@LayoutRes int layoutResId, @Nullable List<SubjectBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder textHolder, SubjectBean listBean) {
        final TextView textView = textHolder.getView(R.id.subject_tv);
        textView.setText(listBean.getDes());
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        //获取View
        ImageView imageview_subject = textHolder.getView(R.id.subject_iv);
//        imageview_subject.measure(0,160);
        //设置图片
        x.image().bind(imageview_subject, Constants.BASE_SERVER + Constants.IMAGE_INTERFACE +
                listBean.getUrl());
    }
}
