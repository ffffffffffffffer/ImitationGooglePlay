package googleplay.itheima.com.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.base.BaseFragment;
import googleplay.itheima.com.googleplay.bean.CategoryBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.utils.ToastUtils;

/**
 * @author TanJJ
 * @time 2017/5/22 14:28
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des TODO
 */

public class CategoryFragment extends BaseFragment {
    //HomeView
    private String serverAddress = Constants.BASE_SERVER + Constants.CATOGERY_INTERFACE;
    private final int accessTime = 5000;
    //加载更多时间
    private LoadingUI.LoadingEnum mSuccess = LoadingUI.LoadingEnum.LOADING;
    //主页要显示的内容集合
    private List<CategoryBean> mList;
    private int index = 0;
    private View mView;
    private LinearLayout mInflate;
    private ImageView mImageView;
    private TextView mTextView;
    private LinearLayout.LayoutParams mLayoutParams;
    private LinearLayout mLinearLayout;

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
        Type type = new TypeToken<List<CategoryBean>>() {
        }.getType();
        mList = gson.fromJson(result, type);
    }


    @Override
    protected View successView() {
        mView = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout.category_fragment, null,
                false);
        mLinearLayout = (LinearLayout) mView.findViewById(R.id.category_linearLayout);
        //根据获取的集合来动态新建控件
        for (int i = 0; i < mList.size(); i++) {
            addView(i);
        }
        return mView;
    }

    /**
     * 给根布局添加View
     *
     * @param position 添加的是第position个View
     */
    private void addView(int position) {
        CategoryBean categoryBean = mList.get(position);
        //定义TextView控件
        TextView textView = new TextView(ResourceUtils.getContext());
        textView.setText(categoryBean.getTitle());
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(20);
        textView.setBackgroundResource(R.drawable.btn_normal);
        textView.setPadding(ResourceUtils.dp2px(10), 0, 0, 0);
        mLinearLayout.addView(textView);
        //对类别里的数据进行遍历
        List<CategoryBean.InfosBean> infos = categoryBean.getInfos();
        //初始化类型Item
        for (int i = 0; i < infos.size(); i++) {
            CategoryBean.InfosBean infosBean = infos.get(i);
            //定义容器装控件
            LinearLayout linearLayout = new LinearLayout(ResourceUtils.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            int itemSize = getItemNum(infosBean);
            mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                    .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mLayoutParams.weight = 1;
            for (int j = 0; j < itemSize; j++) {
                initItem();
                //设置类型数据
                String imageUrl = infosBean.getUrl(j);
                final String textViewName = infosBean.getName(j);
                mTextView.setText(textViewName);
                mTextView.setTextColor(Color.BLACK);
                //加载图片
                x.image().bind(mImageView, Constants.BASE_SERVER + Constants.IMAGE_INTERFACE + imageUrl);
                linearLayout.addView(mInflate, mLayoutParams);
                //设置点击事件
                mInflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show(getActivity(), "点击了  " + textViewName);
                    }
                });
            }
            if (itemSize == 2) {
                initItem();
                mLayoutParams.weight = 2;
                mInflate.setBackgroundColor(Color.TRANSPARENT);
                linearLayout.addView(mInflate, mLayoutParams);
            }
            mLinearLayout.addView(linearLayout);
            mInflate = null;
        }

    }

    /**
     * 获取一行有多少个Item
     *
     * @param infosBean 根据传入的对象中储存的变量值判断
     *
     * @return 返回当前行应该显示多少个
     */
    private int getItemNum(CategoryBean.InfosBean infosBean) {
        if (TextUtils.isEmpty(infosBean.getName1())) {
            return 0;
        } else if (TextUtils.isEmpty(infosBean.getName2())) {
            return 1;
        } else if (TextUtils.isEmpty(infosBean.getName3())) {
            return 2;
        }
        return 3;
    }

    /**
     * 初始化Item
     */
    private void initItem() {
        mInflate = new LinearLayout(ResourceUtils.getContext());
        //动态设置背景状态改变
        //注意: 要先添加按下,最后才添加正常的进去,不然没效果
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, ResourceUtils.getContext().getResources()
                .getDrawable(R.drawable
                        .btn_pressed));
        stateListDrawable.addState(new int[]{}, ResourceUtils.getContext().getResources().getDrawable(R.drawable
                .grid_item_bg_normal));
        mInflate.setBackgroundDrawable(stateListDrawable);
        mInflate.setOrientation(LinearLayout.VERTICAL);
        mImageView = new ImageView(ResourceUtils.getContext());
        mImageView.setPadding(ResourceUtils.dp2px(10), ResourceUtils.dp2px(10), ResourceUtils.dp2px(10),
                ResourceUtils.dp2px(10));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ResourceUtils.dp2px(83), ResourceUtils
                .dp2px(83));
        layoutParams.gravity = Gravity.CENTER;
        mTextView = new TextView(ResourceUtils.getContext());
        mInflate.addView(mImageView, layoutParams);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mTextView.setPadding(0, ResourceUtils.dp2px(1), 0, ResourceUtils.dp2px(3));
        mInflate.addView(mTextView, layoutParams);
    }
}
