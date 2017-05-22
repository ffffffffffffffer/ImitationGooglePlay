package googleplay.itheima.com.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.base.BaseFragment;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.utils.ToastUtils;
import googleplay.itheima.com.googleplay.view.FlowLayout;

/**
 * @author TanJJ
 * @time 2017/5/22 9:48
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 排行界面
 */

public class HotFragment extends BaseFragment {
    //总共要加载的条目
    private static final int TOTAL_COUNTER = 60;
    //HomeView
    private String serverAddress = Constants.BASE_SERVER + Constants.HOT_INTERFACE;
    private final int accessTime = 5000;
    //加载更多时间
    private LoadingUI.LoadingEnum mSuccess = LoadingUI.LoadingEnum.LOADING;
    //主页要显示的内容集合
    private List<String> mList;
    private int index = 0;
    private ScrollView mView;

    @Override
    public LoadingUI.LoadingEnum initData() {
        return requestData();
    }

    private LoadingUI.LoadingEnum requestData() {
        //网络访问
        RequestParams entity = new RequestParams(serverAddress + index);
        entity.setConnectTimeout(accessTime);
        entity.setReadTimeout(accessTime);
        x.http().get(entity, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mSuccess = LoadingUI.LoadingEnum.SUCCESS;
                gsonDecode(result);
                //没必再开线程了
                loadDate();
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
     */
    private void gsonDecode(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        mList = gson.fromJson(result, type);
    }

    @Override
    protected View successView() {
        mView = (ScrollView) LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout.hot_fragment, null,
                false);
        FlowLayout mInflate = (FlowLayout) mView.findViewById(R.id.flowlayout);
        Random random = new Random();
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = new TextView(ResourceUtils.getContext());
            final String appName = mList.get(i);
            textView.setText(appName);
            textView.setPadding(5, 5, 5, 5);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            //随机argb设置
            int r = (random.nextInt(220) + 20);
            int b = (random.nextInt(220) + 20);
            int g = (random.nextInt(220) + 20);
            int argb = Color.rgb(r, g, b);
            //普通样式
            GradientDrawable gradientDrawable_normal = new GradientDrawable();
            gradientDrawable_normal.setColor(argb);
            gradientDrawable_normal.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable_normal.setCornerRadius(15);
            //按下样式
            GradientDrawable gradientDrawable_press = new GradientDrawable();
            gradientDrawable_press.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable_press.setCornerRadius(15);
            gradientDrawable_press.setColor(Color.GRAY);

            //组合一起
            StateListDrawable listDrawable = new StateListDrawable();
            listDrawable.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable_press);
            listDrawable.addState(new int[]{}, gradientDrawable_normal);


            textView.setBackgroundDrawable(listDrawable);
//            textView.setBackgroundColor(argb);
            //设置点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(getActivity(), "点击了:  " + appName);
                }
            });
            mInflate.addView(textView);
        }
        return mInflate;
    }
}
