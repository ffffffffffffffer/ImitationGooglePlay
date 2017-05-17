package googleplay.itheima.com.googleplay.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.base.BaseFragment;
import googleplay.itheima.com.googleplay.bean.HomeBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.utils.ToastUtils;

/**
 * @author TanJJ
 * @time 2017/5/15 9:14
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 首页Fragment
 */

public class HomeFragment extends BaseFragment {
    //HomeView
    private View mInflate;
    private String serverAddress = Constants.BASE_SERVER + Constants.HOME_INTERFACE;
    private final int accessTime = 5000;
    private LoadingUI.LoadingEnum mSuccess = LoadingUI.LoadingEnum.LOADING;

    @Override
    public LoadingUI.LoadingEnum initData() {
//        LoadingUI.LoadingEnum[] enums = {LoadingUI.LoadingEnum.ERROR, LoadingUI.LoadingEnum.EMPTY
//                , LoadingUI.LoadingEnum.SUCCESS};
//
//        Random random = new Random();
//        return enums[random.nextInt(3)];

        //网络访问
        RequestParams entity = new RequestParams(serverAddress + 0);
        entity.setConnectTimeout(accessTime);
        entity.setReadTimeout(accessTime);
        x.http().get(entity, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mSuccess = LoadingUI.LoadingEnum.SUCCESS;
                gsonDecode(result);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            loadDate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
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

    private void gsonDecode(String result) {
        Gson gson = new Gson();
        HomeBean homeBean = gson.fromJson(result, HomeBean.class);
    }

    @Override
    protected View successView() {
        mInflate = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout.home_fragment, null, false);
//        TextView textView= (TextView) mInflate.findViewById(R.id.tv);
//        textView.setText("我会报错吗?");
//        textView.setTextColor(Color.BLACK);
        RecyclerView recyclerView = (RecyclerView) mInflate.findViewById(R.id.recycler_view);

        //模拟假数据
        final String[] strings = {"proguard-rules", "CMakeLists.txt", "app.iml", "AndriodManifest.xml", "gradlew" +
                ".bat", "settings.gradle"};

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        GridLayoutManager layoutManager = new GridLayoutManager(ResourceUtils.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflate = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout
                        .home_item_recyclerview, parent, false);

                return new TextHolder(inflate);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (holder instanceof TextHolder) {
                    ((TextHolder) holder).mTextView.setText(strings[position % strings.length]);
                    ((TextHolder) holder).mTextView.setTextColor(Color.BLACK);
                    ((TextHolder) holder).mTextView.setTextSize(25);
                }
            }

            @Override
            public int getItemCount() {
                return strings.length * 5;
            }


            class TextHolder extends RecyclerView.ViewHolder {

                private TextView mTextView;

                public TextHolder(View itemView) {
                    super(itemView);
                    mTextView = (TextView) itemView.findViewById(R.id.tv);
                }
            }
        });


        return mInflate;
    }
}
