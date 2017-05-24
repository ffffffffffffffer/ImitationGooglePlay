package googleplay.itheima.com.googleplay.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.adapter.AppPullToRefreshAdapter;
import googleplay.itheima.com.googleplay.base.BaseFragment;
import googleplay.itheima.com.googleplay.bean.AppBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.utils.ToastUtils;

/**
 * @author TanJJ
 * @time 2017/5/21 16:08
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 应用界面
 */

public class AppFragment extends BaseFragment {
    //网络访问条目条件
    private int index = 0;
    //访问时机
    private int state = LOADING_DATA;
    private static final int LOADING_DATA = 0;
    private static final int LOAD_MORE = 1;
    private String serverAddress = Constants.BASE_SERVER + Constants.APP_INTERFACE;
    private String serverAddress2;
    private final int accessTime = 5000;
    //加载更多时间
    private final int DELAYED = 3000;
    private LoadingUI.LoadingEnum mSuccess = LoadingUI.LoadingEnum.LOADING;
    //主页要显示的内容集合
    private List<AppBean> mList;
    private AppPullToRefreshAdapter mAdapter;
    //总共要加载的条目
    private static final int TOTAL_COUNTER = 60;
    private int mCurrentCounter;
    private RecyclerView mRecyclerView;
    private View mInflate;


    @Override
    public LoadingUI.LoadingEnum initData() {
        return requestData(index, state);

    }

    private LoadingUI.LoadingEnum requestData(int index, final int state) {
        //        LoadingUI.LoadingEnum[] enums = {LoadingUI.LoadingEnum.ERROR, LoadingUI.LoadingEnum.EMPTY
//                , LoadingUI.LoadingEnum.SUCCESS};
//
//        Random random = new Random();
//        return enums[random.nextInt(3)];

        //网络访问
//        LogUtil.d("index  "+serverAddress + index);
        setAddressServer(serverAddress);
        RequestParams entity = new RequestParams(serverAddress2 + index);
        entity.setConnectTimeout(accessTime);
        entity.setReadTimeout(accessTime);
        x.http().get(entity, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (state == LOADING_DATA) {
                    mSuccess = LoadingUI.LoadingEnum.SUCCESS;
                    gsonDecode(result, state);
                    //使用线程池
//                mTask = new TaskRunnable();
//                ThreadPoolManager.getLongThread().submit(mTask);

                    //没必再开线程了
                    loadDate();
                } else if (state == LOAD_MORE) {
                    gsonDecode(result, state);
                }
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(getActivity(), "访问失败!" + ex.getMessage());
//                LogUtil.d("访问失败!" + ex.getMessage());
                mSuccess = LoadingUI.LoadingEnum.ERROR;
//                mTask = new TaskRunnable();
//                ThreadPoolManager.getLongThread().submit(mTask);
                loadDate();
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
        return mSuccess;
    }

    /**
     * 设置网络访问地址,方便子类调用
     *
     * @param address
     */
    protected void setAddressServer(String address) {
        this.serverAddress2 = address;
    }

    /**
     * 解析gson格式数据
     *
     * @param result gson数据
     * @param state  调用解析方法的时间
     */
    private void gsonDecode(String result, int state) {
        Gson gson = new Gson();
        //在解析json数据是数组时必须要用TypeToken方法来获取bean的type来解析,否则报错
        // json数据为数组意思是最外面的是[]中括号的,而不是{}这个就要这么做,还有小心命名,下面就是命名与上面的mList一样
        //一直报错,所有要把命名改好点
        Type collectionType = new TypeToken<List<AppBean>>() {
        }.getType();
        final List<AppBean> list = gson.fromJson(result, collectionType);
        if (state == LOADING_DATA) {
            mList = list;
        } else if (state == LOAD_MORE) {
            if (mAdapter != null) {
                //让加载时间久一点
                ResourceUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentCounter >= TOTAL_COUNTER) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            if (list.size() > 0) {
                                //if(isErr){
                                //设置数据进集合中
                                mList.addAll(list);
                                //#### 对于该BaseRecyclerViewAdapterHelper开源项目来讲,下面两个方法非常重要,能不能加载跟多就看它们了 #####
                                //把要新增的数据添加到Adapter内部维护的List集合中
                                mAdapter.addData(list);
                                //成功获取更多数据
                                mCurrentCounter = mAdapter.getData().size();
                                //加载完成,刷新
                                mAdapter.loadMoreComplete();
                                //################################################
                            } else {
                                //获取更多数据失败
//                                isErr = true;
                                ToastUtils.show(getActivity(), ResourceUtils.getContext().getString(R.string
                                        .load_error));
                                mAdapter.loadMoreFail();

                            }
                        }
                    }
                }, DELAYED);
            }
        }
    }


    @Override
    protected View successView() {
        mInflate = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout.home_fragment, null, false);
//        TextView textView= (TextView)Inflate.findViewById(R.id.tv);
//        textView.setText("我会报错吗?");
//        textView.setTextColor(Color.BLACK);
        mRecyclerView = (RecyclerView) mInflate.findViewById(R.id.recycler_view);

        //模拟假数据
//        final String[] strings = {"proguard-rules", "CMakeLists.txt", "app.iml", "AndriodManifest.xml", "gradlew" +
//                ".bat", "settings.gradle"};

//        GridLayoutManager layoutManager = new GridLayoutManager(ResourceUtils.getContext(), 2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        //初始化Adapter
        initAdapter();


        return mInflate;
    }

    private void initAdapter() {
        mAdapter = new AppPullToRefreshAdapter(R.layout.home_item_recyclerview, mList);
        //通过调用下面方法设置加载更多的布局,可以定义一个类去继承LoadMoreView并且实现抽象方法就可以自定义布局了
//        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        //设置加载更多的逻辑
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //网络访问
                requestData(mList.size(), LOAD_MORE);
            }
        }, mRecyclerView);
        //设置adapter条目的动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtil.e("点击了 " + position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
