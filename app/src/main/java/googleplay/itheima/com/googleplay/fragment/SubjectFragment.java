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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.adapter.SubjectPullToRefreshAdapter;
import googleplay.itheima.com.googleplay.base.BaseFragment;
import googleplay.itheima.com.googleplay.bean.SubjectBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.utils.ToastUtils;

/**
 * @author TanJJ
 * @time 2017/5/21 20:38
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des TODO
 */

public class SubjectFragment extends BaseFragment {
    //总共要加载的条目
    private static final int TOTAL_COUNTER = 60;
    //HomeView
    private View mInflate;
    private String serverAddress = Constants.BASE_SERVER + Constants.SUBJECT_INTERFACE;
    private final int accessTime = 5000;
    //加载更多时间
    private final int DELAYED = 3000;
    private LoadingUI.LoadingEnum mSuccess = LoadingUI.LoadingEnum.LOADING;
    //主页要显示的内容集合
    private List<SubjectBean> mList;
    //网络访问条目条件
    private int index = 0;
    //访问时机
    private int state = LOADING_DATA;
    private static final int LOADING_DATA = 0;
    private static final int LOAD_MORE = 1;
    private SubjectPullToRefreshAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mCurrentCounter;
    private List<SubjectBean> mSubjectBean;


    @Override
    public LoadingUI.LoadingEnum initData() {
        return requestData(index, state);
    }

    private LoadingUI.LoadingEnum requestData(int index, final int state) {
        //网络访问
        RequestParams entity = new RequestParams(serverAddress + index);
        entity.setConnectTimeout(accessTime);
        entity.setReadTimeout(accessTime);
        x.http().get(entity, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (state == LOADING_DATA) {
                    mSuccess = LoadingUI.LoadingEnum.SUCCESS;
                    gsonDecode(result, state);
                    //没必再开线程了
                    loadDate();
                } else if (state == LOAD_MORE) {
                    gsonDecode(result, state);
                }
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(getActivity(), "访问失败!" + ex.getMessage());
                LogUtil.d("访问失败!" + ex.getMessage());
                mSuccess = LoadingUI.LoadingEnum.ERROR;
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
     * 解析gson格式数据
     *
     * @param result gson数据
     * @param state  调用解析方法的时间
     */
    private void gsonDecode(String result, int state) {
        Gson gson = new Gson();
        Type type = (ParameterizedType) new TypeToken<List<SubjectBean>>() {
        }.getType();
        mSubjectBean = gson.fromJson(result, type);
        if (state == LOADING_DATA) {
            mList = mSubjectBean;
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
                            if (mSubjectBean.size() > 0) {
                                //if(isErr){
                                //设置数据进集合中
                                mList.addAll(mSubjectBean);
                                //#### 对于该BaseRecyclerViewAdapterHelper开源项目来讲,下面两个方法非常重要,能不能加载跟多就看它们了 #####
                                //把要新增的数据添加到Adapter内部维护的List集合中
                                mAdapter.addData(mSubjectBean);
                                //成功获取更多数据
                                mCurrentCounter = mAdapter.getData().size();
                                //加载完成,刷新
                                mAdapter.loadMoreComplete();
                                //################################################
                            } else {
                                //获取更多数据失败
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
        mRecyclerView = (RecyclerView) mInflate.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        //初始化Adapter
        initAdapter();


        return mInflate;

    }

    private void initAdapter() {
        mAdapter = new SubjectPullToRefreshAdapter(R.layout.subject_item_content, mList);
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
        mRecyclerView.setAdapter(mAdapter);
    }

}
