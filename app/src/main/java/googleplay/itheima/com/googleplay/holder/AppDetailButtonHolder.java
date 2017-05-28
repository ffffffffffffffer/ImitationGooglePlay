package googleplay.itheima.com.googleplay.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.activity.AppDetailActivity;
import googleplay.itheima.com.googleplay.bean.AppDetailInfoBean;
import googleplay.itheima.com.googleplay.fragment.DownloadUI;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.view.CustomProgressRectView;

/**
 * @author TanJJ
 * @time 2017/5/27 16:19
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.holder
 * @des 详情页面的按钮
 */

public class AppDetailButtonHolder extends BaseHolder<AppDetailInfoBean> {

    private final AppDetailActivity activity;
    private CustomProgressRectView mDownload_button;
    private TextView mDownload_progress;

    public AppDetailButtonHolder(AppDetailActivity activity) {
        this.activity = activity;
    }

    @Override
    public View initView() {
        View view = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout.detail_app_button, null, false);
        mDownload_button = (CustomProgressRectView) view.findViewById(R.id.detail_app_button_download);
        mDownload_progress = (TextView) view.findViewById(R.id.app_detail_download_progress);
        return view;
    }

    @Override
    public void initData(final AppDetailInfoBean appDetailInfoBean) {
        final DownloadUI downloadUI = new DownloadUI();
        mDownload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置下载的apk
                downloadUI.setUrlApk(appDetailInfoBean.getDownloadUrl());
                //改变显示状态
                downloadUI.changeState(activity);
                //监听状态改变
                downloadUI.setChangeStateListener(new DownloadUI.StateChangeListener() {
                    @Override
                    public void setChangeState(String stateString) {
                        mDownload_progress.setText(stateString);
                        mDownload_button.setProgress(0);
                    }

                    @Override
                    public void downloadingInfo(long total, long current, boolean isDownloading) {
                        //下载进度
                        int currentProgress = (int) (current * 100 / total + 0.5f);
                        mDownload_button.setProgress(currentProgress);
                        mDownload_progress.setText(currentProgress + " %");
                    }
                });
            }
        });
    }
}
