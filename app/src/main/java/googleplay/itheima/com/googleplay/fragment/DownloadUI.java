package googleplay.itheima.com.googleplay.fragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import googleplay.itheima.com.googleplay.activity.AppDetailActivity;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ToastUtils;

/**
 * @author TanJJ
 * @time 2017/5/28 9:15
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.base
 * @des 下载UI更新
 */

public class DownloadUI {
    private static String SERVER = Constants.BASE_SERVER + Constants.DOWNLOAD_INTERFACE;
    private static int CURRENT_STATE;
    private static int NONE_STATE = 0;
    private static int GO_DOWNLOAD = 1;
    private static int DOWNLOADING = 2;
    private static int PAUSE_DOWNLOAD = 3;
    private static int NO_NETWORK = 4;
    private static int DOWNLOADED = 5;
    private static int INSTALLED = 6;
    private String apkUrl;
    private StateChangeListener stateChangeListener;
    private Callback.Cancelable mCancelable;

    /**
     * 改变下载状态
     *
     * @param activity
     *
     * @return
     */
    public void changeState(final AppDetailActivity activity) {
        if (CURRENT_STATE == NONE_STATE || CURRENT_STATE == PAUSE_DOWNLOAD || CURRENT_STATE == NO_NETWORK) {
            //网络访问,下载apk
            RequestParams entity = new RequestParams(SERVER + apkUrl);
            entity.setAutoRename(true);
            mCancelable = x.http().post(entity, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    CURRENT_STATE = DOWNLOADED;
                    stateChangeListener.setChangeState(getStateString(CURRENT_STATE));
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    CURRENT_STATE = NO_NETWORK;
                    stateChangeListener.setChangeState(getStateString(CURRENT_STATE));
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    ToastUtils.show(activity, "下载取消");

                }

                @Override
                public void onFinished() {
                }

                @Override
                public void onWaiting() {
                    ToastUtils.show(activity, "等待下载");

                }

                @Override
                public void onStarted() {
                    CURRENT_STATE = GO_DOWNLOAD;
                    ToastUtils.show(activity, "开始  Started");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    CURRENT_STATE = DOWNLOADING;
                    stateChangeListener.downloadingInfo(total, current, isDownloading);
                }
            });
        } else if (CURRENT_STATE == GO_DOWNLOAD || CURRENT_STATE == DOWNLOADING) {
            //暂停下载 //
            ToastUtils.show(activity, "暂停下载");
            mCancelable.cancel();
            CURRENT_STATE = PAUSE_DOWNLOAD;
            stateChangeListener.setChangeState(getStateString(CURRENT_STATE));
        } else if (CURRENT_STATE == DOWNLOADED) {
            //安装 // TODO: 2017/5/28
            ToastUtils.show(activity, "安装中");
            CURRENT_STATE = INSTALLED;
            stateChangeListener.setChangeState(getStateString(CURRENT_STATE));
        } else if (CURRENT_STATE == INSTALLED) {
            //打开 // TODO: 2017/5/28
            ToastUtils.show(activity, "跳转应用中");
        }
    }

    private String getStateString(int currentState) {
        if (currentState == GO_DOWNLOAD) {
            return "去下载";
        } else if (currentState == DOWNLOADING) {
            return "下载中";
        } else if (currentState == PAUSE_DOWNLOAD) {
            return "暂停下载";
        } else if (currentState == NO_NETWORK) {
            return "重试";
        } else if (currentState == DOWNLOADED) {
            return "安装";
        } else if (currentState == INSTALLED) {
            return "打开";
        }
        return "";
    }

    public void setChangeStateListener(StateChangeListener stateListener) {
        stateChangeListener = stateListener;
    }

    public interface StateChangeListener {
        void setChangeState(String stateString);

        void downloadingInfo(long total, long current, boolean isDownloading);
    }

    public void setUrlApk(String urlApk) {
        apkUrl = urlApk;
    }
}
