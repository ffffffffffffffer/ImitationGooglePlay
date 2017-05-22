package googleplay.itheima.com.googleplay.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shitou.googleplay.lib.randomlayout.StellarMap;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import googleplay.itheima.com.googleplay.base.BaseFragment;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ToastUtils;

/**
 * @author TanJJ
 * @time 2017/5/22 17:46
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des TODO
 */

public class RecommendFragment extends BaseFragment {
    //HomeView
    private String serverAddress = Constants.BASE_SERVER + Constants.RECOMMEND_INTERFACE;
    private final int accessTime = 5000;
    //加载更多时间
    private LoadingUI.LoadingEnum mSuccess = LoadingUI.LoadingEnum.LOADING;
    //主页要显示的内容集合
    private List<String> mList;
    private int index = 0;

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
        StellarMap mStellarMap = new StellarMap(getActivity());
        //1.设置内部的TextView距离四周的内边距
        int padding = 15;//设置控件到外边距的值
        mStellarMap.setInnerPadding(padding, padding, padding, padding);
        mStellarMap.setAdapter(new StellarMapAdapter());
        // 设置默认显示第几组的数据
        mStellarMap.setGroup(0, true);// 这里默认显示第0组
        // 设置x和y方向上的显示的密度
        mStellarMap.setRegularity(11, 11);// 如果值设置的过大，有可能造成子View摆放比较稀疏
        return mStellarMap;
    }

    class StellarMapAdapter implements StellarMap.Adapter {
        /**
         * 返回有几组数据，就是几页数据
         */
        @Override
        public int getGroupCount() {
            return mList.size() / getCount(0);
        }

        /**
         * 返回每组有多少个数据,每组都有11个
         */
        @Override
        public int getCount(int group) {
            return 11;
        }

        /**
         * 返回需要随机摆放的View
         * group: 表示当前是第几组
         * position： 表示当前组中的位置
         */
        @Override
        public View getView(int group, int position, View convertView) {
            final TextView textView = new TextView(getActivity());
            //1.设置文本数据
            int listPosition = group * getCount(group) + position;
            textView.setText(mList.get(listPosition));
            //2.设置随机的文字大小
            Random random = new Random();
            textView.setTextSize(random.nextInt(10) + 14);//14-23
            //3.设置随机的字体颜色
            int red = random.nextInt(150);//0-190
            int green = random.nextInt(150);//0-190
            int blue = random.nextInt(150);//0-190
            int color = Color.rgb(red, green, blue);//使用rgb混合生成一种新的颜色
            textView.setTextColor(color);
            //4.设置点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(getActivity(), textView.getText().toString());
                }
            });
            return textView;
        }

        /**
         * 当执行完平移动画后下一组加载哪一组的数据，但是在源码中没有任何地方用到改方法，
         * 所以此方法并没有什么用
         */
        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        /**
         * 当执行完缩放动画后下一组加载哪一组的数据
         * group： 表示当前是第几组
         */
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            //0->1->2->0
            return (group + 1) % getGroupCount();
        }

    }
}
