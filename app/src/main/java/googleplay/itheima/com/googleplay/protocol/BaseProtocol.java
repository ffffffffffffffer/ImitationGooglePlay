package googleplay.itheima.com.googleplay.protocol;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.fragment.LoadingUI;
import googleplay.itheima.com.googleplay.utils.Constants;

/**
 * @author TanJJ
 * @time 2017/5/21 15:06
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.protocol
 * @des 网络访问协议基类
 */

public abstract class BaseProtocol<T> {
    //访问时机
    protected int state = LOADING_DATA;
    protected static final int LOADING_DATA = 0;
    protected static final int LOAD_MORE = 1;
    protected LoadingUI.LoadingEnum mSuccess = LoadingUI.LoadingEnum.LOADING;
    private int accessTime = 5000;
    private Callback.CacheCallback<R> callback;

    public void setCacheCallback(Callback.CacheCallback<R> callback) {
        this.callback = callback;
    }


    public void loadData(int index) {
        //        LoadingUI.LoadingEnum[] enums = {LoadingUI.LoadingEnum.ERROR, LoadingUI.LoadingEnum.EMPTY
//                , LoadingUI.LoadingEnum.SUCCESS};
//
//        Random random = new Random();
//        return enums[random.nextInt(3)];

        //网络访问
        RequestParams entity = new RequestParams(Constants.BASE_SERVER + getINTERFACEAddress() + index);
        entity.setConnectTimeout(accessTime);
        entity.setReadTimeout(accessTime);
        x.http().get(entity, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseProtocol.this.onSuccess(result, state);
//
//                if (state == LOADING_DATA) {
//                    mSuccess = LoadingUI.LoadingEnum.SUCCESS;
//                    gsonDecode(result, state);
//                    //使用线程池
////                mTask = new TaskRunnable();
////                ThreadPoolManager.getLongThread().submit(mTask);
//
//                    //没必再开线程了
//                    loadDate();
//                } else if (state == LOAD_MORE) {
//                    gsonDecode(result, state);
//                }
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                BaseProtocol.this.onError(ex, isOnCallback);

//                ToastUtils.show(getActivity(), "访问失败!" + ex.getMessage());
//                LogUtil.d("访问失败!" + ex.getMessage());
//                mSuccess = LoadingUI.LoadingEnum.ERROR;
////                mTask = new TaskRunnable();
////                ThreadPoolManager.getLongThread().submit(mTask);
//                loadDate();
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
//    final List<HomeBean.ListBean> list = mHomeBean.getList();
//        if (state == LOADING_DATA) {
//        mList = list;
//    } else if (state == LOAD_MORE) {
//        if (mAdapter != null) {
//            //让加载时间久一点
//            ResourceUtils.getHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (mCurrentCounter >= TOTAL_COUNTER) {
//                        //数据全部加载完毕
//                        mAdapter.loadMoreEnd();
//                    } else {
//                        if (list.size() > 0) {
//                            //if(isErr){
//                            //设置数据进集合中
//                            mList.addAll(list);
//                            //#### 对于该BaseRecyclerViewAdapterHelper开源项目来讲,下面两个方法非常重要,能不能加载跟多就看它们了 #####
//                            //把要新增的数据添加到Adapter内部维护的List集合中
//                            mAdapter.addData(list);
//                            //成功获取更多数据
//                            mCurrentCounter = mAdapter.getData().size();
//                            //加载完成,刷新
//                            mAdapter.loadMoreComplete();
//                            //################################################
//                        } else {
//                            //获取更多数据失败
//                            isErr = true;
//                            ToastUtils.show(getActivity(), ResourceUtils.getContext().getString(R.string
//                                    .load_error));
//                            mAdapter.loadMoreFail();
//
//                        }
//                    }
//                }
//            }, DELAYED);
//        }
//    }

    protected abstract void onSuccess(String result, int state);

    protected abstract void onError(Throwable ex, boolean isOnCallback);

    //
    protected String getINTERFACEAddress() {
        return "";
    }

    /**
     * 解析gson格式数据
     *
     * @param result gson数据
     * @param state  调用解析方法的时间
     */
    protected T gsonDecode(String result, int state) {
        Gson gson = new Gson();
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type type1 = type.getActualTypeArguments()[0];
        return gson.fromJson(result, type1);

    }
}
