package googleplay.itheima.com.googleplay.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import googleplay.itheima.com.googleplay.activity.AppDetailActivity;
import googleplay.itheima.com.googleplay.manager.ThreadPoolManager;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.FileUtils;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.utils.SharedPreferencesUtils;
import googleplay.itheima.com.googleplay.utils.ToastUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private String packageName;
    private String apkUrl;
    private StateChangeListener stateChangeListener;
    private String mPath;
    private final SharedPreferencesUtils mSharedPreferencesUtils;
    private static final String FINAL_CURRENT = "finalCurrent";

    public DownloadUI(String packageName) {
        this.packageName = packageName;
        mPath = ResourceUtils.getContext().getExternalCacheDir().getPath() + "/apk/" + packageName;
        mSharedPreferencesUtils = new SharedPreferencesUtils(ResourceUtils.getContext(), packageName);
        int currentState = mSharedPreferencesUtils.getInt(FINAL_CURRENT, 0);
        if (currentState != 0) {
            CURRENT_STATE = currentState;
            changeStates();
        }
        checkApp(ResourceUtils.getContext());
    }

    /**
     * 检测当前包是否已经安装过
     *
     * @param context
     */
    private void checkApp(Context context) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(PackageManager
                .GET_ACTIVITIES);
        for (PackageInfo installedPackage : installedPackages) {
            if (packageName.equals(installedPackage.packageName)) {
                CURRENT_STATE = INSTALLED;
                //安全更新ui
                changeStates();
            }
        }
    }

    private Call mExecute;

    /**
     * 改变下载状态
     *
     * @param activity
     *
     * @return
     */
    public void changeState(final AppDetailActivity activity) {
        if (CURRENT_STATE == NONE_STATE || CURRENT_STATE == PAUSE_DOWNLOAD || CURRENT_STATE == NO_NETWORK) {
            File file = new File(mPath);
            long breakPointLong = 0;
            if (!FileUtils.createFile(mPath)) {
                breakPointLong = file.length();
            }
            //如果有安装文件,还是完整的话只需要安装不用再次下载
//            long total = mSharedPreferencesUtils.getLong("total", 0);
//            if (total != 0) {
//                if (total == breakPointLong) {
//                    CURRENT_STATE = DOWNLOADED;
//                    //安全更新ui
//                    changeStates();
//                    return;
//                }
//            }

            final long[] finalBreakPoint = {breakPointLong};
            ThreadPoolManager.getLongThread().submit(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    Request build = new Request.Builder().url(SERVER + apkUrl + "&range=" +
                            finalBreakPoint[0])
                            .build();
                    mExecute = client.newCall(build);
                    if (CURRENT_STATE == PAUSE_DOWNLOAD) {
                        CURRENT_STATE = NONE_STATE;
                    }
                    mExecute.enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtil.e("okhttp   下载失败");
                            CURRENT_STATE = NO_NETWORK;
                            //安全更新ui
                            changeStates();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final long total = response.body().contentLength();
//                            mSharedPreferencesUtils.putLong("total", total);
                            LogUtil.e("total  " + total);
                            InputStream inputStream = response.body().byteStream();
                            FileOutputStream fos = new FileOutputStream(mPath, true);
                            int len = 0;
                            byte[] bys = new byte[2048];
                            while ((len = inputStream.read(bys)) != -1) {
                                if (CURRENT_STATE == PAUSE_DOWNLOAD) {
                                    // 暂停下载
                                    //安全更新ui
                                    changeStates();
                                    break;
                                }
                                finalBreakPoint[0] += len;
                                fos.write(bys, 0, len);
                                CURRENT_STATE = DOWNLOADING;
                                ResourceUtils.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        stateChangeListener.downloadingInfo(total, finalBreakPoint[0], true);
                                        if (total == finalBreakPoint[0]) {
                                            CURRENT_STATE = DOWNLOADED;
                                            //安全更新ui
                                            changeStates();
                                        }
                                    }
                                });
                            }
                            LogUtil.d("total--" + total + "  current  " + finalBreakPoint[0]);
                            fos.flush();
                            fos.close();
                        }
                    });
                }
            });

            //Xutils3 网络访问,下载apk ,实现不了断点下载
//            RequestParams entity = new RequestParams(SERVER);
//            mPath = ResourceUtils.getContext().getExternalCacheDir().getPath() + "/apk/" + packageName + 100;
//            File file = new File(mPath);
//            entity.setSaveFilePath(mPath);
//            File file = new File(mPath);
//            long breakPointLong = 0;
//            boolean file1 = FileUtils.createFile(mPath);
//            if (!file1) {
//                breakPointLong = file.length();
//            }
//            entity.addQueryStringParameter("name", apkUrl);
//            entity.addQueryStringParameter("rang", breakPointLong + "");
//            entity.setAutoResume(true);
//            mCancelable = x.http().get(entity, new Callback.ProgressCallback<File>() {
//                @Override
//                public void onSuccess(File result) {
//                    LogUtil.e("path  " + result.getPath());
//                    CURRENT_STATE = DOWNLOADED;
//                    stateChangeListener.setChangeState(getStateString(CURRENT_STATE));
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    CURRENT_STATE = NO_NETWORK;
//                    stateChangeListener.setChangeState(getStateString(CURRENT_STATE));
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//                    ToastUtils.show(activity, "下载取消");
//
//                }
//
//                @Override
//                public void onFinished() {
//                }
//
//                @Override
//                public void onWaiting() {
//                    ToastUtils.show(activity, "等待下载");
//
//                }
//
//                @Override
//                public void onStarted() {
//                    CURRENT_STATE = GO_DOWNLOAD;
//                    ToastUtils.show(activity, "开始  Started");
//                }
//
//                @Override
//                public void onLoading(long total, long current, boolean isDownloading) {
//                    CURRENT_STATE = DOWNLOADING;
//                    stateChangeListener.downloadingInfo(total, current, isDownloading);
////                    LogUtil.e("current  "+current);
////                    //断点
////                    SharedPreferences.Editor edit = mSharedPreferences.edit();
////                    edit.putLong(apkUrl, current);
////                    edit.commit();
//                }
//            });
        } else if (CURRENT_STATE == GO_DOWNLOAD || CURRENT_STATE == DOWNLOADING) {
            //暂停下载 //
            ToastUtils.show(activity, "暂停下载");
//            mCancelable.cancel();
//            mExecute.cancel();
            CURRENT_STATE = PAUSE_DOWNLOAD;
            //安全更新ui
            changeStates();
        } else if (CURRENT_STATE == DOWNLOADED) {
            //安装 //
            ToastUtils.show(activity, "安装中");
            //安全更新ui
//            CURRENT_STATE = INSTALLED;
//            changeStates();
            //apk下载完成后，调用系统的安装方法
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(mPath)), "application/vnd.android.package-archive");
            activity.startActivity(intent);
        } else if (CURRENT_STATE == INSTALLED) {
            //打开 //
            Intent intent = ResourceUtils.getContext().getPackageManager().getLaunchIntentForPackage
                    (packageName);
            ResourceUtils.getContext().startActivity(intent);
            ToastUtils.show(activity, "跳转应用中");
        }
    }

    /**
     * 在主线程更新UI
     */
    private void changeStates() {
        ResourceUtils.post(new Runnable() {
            @Override
            public void run() {
                stateChangeListener.setChangeState(getStateString(CURRENT_STATE));
            }
        });
    }

    private String getStateString(int currentState) {
        //保存状态,下次打开就可以直接操作
        mSharedPreferencesUtils.putInt(FINAL_CURRENT, currentState);
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
