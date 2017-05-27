package googleplay.itheima.com.googleplay.holder;

import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.xutils.x;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.bean.AppDetailInfoBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;

/**
 * @author TanJJ
 * @time 2017/5/24 13:04
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.holder
 * @des 应用的详情holder
 */

public class AppDetailInfoHolder extends BaseHolder<AppDetailInfoBean> {
    ImageView mImageView_photo;
    TextView mTextView_name;
    TextView mTextView_downloads;
    TextView mTextView_data;
    TextView mTextView_version;
    TextView mTextView_size;
    RatingBar mRatingBar_stars;
    private ScrollView mScrollView_root;

    @Override
    public View initView() {
        View view = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout.detail_app_infos, null, false);
        mImageView_photo = (ImageView) view.findViewById(R.id.detail_info_photo);
        mTextView_name = (TextView) view.findViewById(R.id.detail_info_name);
        mTextView_downloads = (TextView) view.findViewById(R.id.detail_info_downloads);
        mTextView_data = (TextView) view.findViewById(R.id.detail_info_data);
        mTextView_version = (TextView) view.findViewById(R.id.detail_info_version);
        mTextView_size = (TextView) view.findViewById(R.id.detail_info_size);
        mRatingBar_stars = (RatingBar) view.findViewById(R.id.detail_info_stars);
        return view;
    }

    @Override
    public void initData(AppDetailInfoBean appDetailInfoBean) {
        //设置数据
        mTextView_name.setText(appDetailInfoBean.getName());
        mRatingBar_stars.setRating(appDetailInfoBean.getStars());
        mTextView_downloads.setText(ResourceUtils.getResource().getString(R.string.detail_info_download,
                appDetailInfoBean.getDownloadNum()));
        mTextView_data.setText(ResourceUtils.getResource().getString(R.string.detail_info_time, appDetailInfoBean
                .getDate()));
        mTextView_version.setText(ResourceUtils.getResource().getString(R.string.detail_info_version, appDetailInfoBean
                .getVersion()));
        mTextView_size.setText(ResourceUtils.getResource().getString(R.string.detail_info_size, Formatter
                .formatFileSize(ResourceUtils.getContext(), appDetailInfoBean
                        .getSize())));
        x.image().bind(mImageView_photo, Constants.BASE_SERVER + Constants.IMAGE_INTERFACE + appDetailInfoBean
                .getIconUrl());
    }
}
