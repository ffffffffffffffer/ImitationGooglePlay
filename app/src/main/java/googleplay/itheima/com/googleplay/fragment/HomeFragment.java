package googleplay.itheima.com.googleplay.fragment;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import googleplay.itheima.com.googleplay.DepthPageTransformer;
import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.activity.AppDetailActivity;
import googleplay.itheima.com.googleplay.adapter.HomePullToRefreshAdapter;
import googleplay.itheima.com.googleplay.base.BaseFragment;
import googleplay.itheima.com.googleplay.bean.HomeBean;
import googleplay.itheima.com.googleplay.utils.Constants;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;
import googleplay.itheima.com.googleplay.utils.ToastUtils;
import googleplay.itheima.com.googleplay.view.DotLieanerLayout;

/**
 * @author TanJJ
 * @time 2017/5/15 9:14
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 首页Fragment
 */

public class HomeFragment extends BaseFragment {
    //总共要加载的条目
    private static final int TOTAL_COUNTER = 60;
    //HomeView
    private View mInflate;
    private String serverAddress = Constants.BASE_SERVER + Constants.HOME_INTERFACE;
    private final int accessTime = 5000;
    //加载更多时间
    private final int DELAYED = 3000;
    private LoadingUI.LoadingEnum mSuccess = LoadingUI.LoadingEnum.LOADING;
    //主页要显示的内容集合
    private List<HomeBean.ListBean> mList;
    //网络访问条目条件
    private int index = 0;
    //访问时机
    private int state = LOADING_DATA;
    private static final int LOADING_DATA = 0;
    private static final int LOAD_MORE = 1;
    private HomePullToRefreshAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mCurrentCounter;
    private boolean isErr;
    private SparseArray<ImageView> mViews;
    private HomeBean mHomeBean;
    private int mViewPagerPostion;
    private ViewPager mViewPager;
    private AutoViewPagerDelayed mViewPagerDelayed;

//    private TaskRunnable mTask;

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
        RequestParams entity = new RequestParams(serverAddress + index);
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

//    class TaskRunnable implements Runnable {
//
//        @Override
//        public void run() {
//                loadDate();
//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ThreadPoolManager.getLongThread().remove(mTask);
    }

    /**
     * 解析gson格式数据
     *
     * @param result gson数据
     * @param state  调用解析方法的时间
     */
    private void gsonDecode(String result, int state) {
        Gson gson = new Gson();
        mHomeBean = gson.fromJson(result, HomeBean.class);
        final List<HomeBean.ListBean> list = mHomeBean.getList();
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
                                isErr = true;
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

        //常规RecylerView用法
//        recyclerView.setAdapter(new RecyclerView.Adapter() {
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View inflate = LayoutInflater.from(ResourceUtils.getContext()).inflate(R.layout
//                        .home_item_recyclerview, parent, false);
//
//                return new TextHolder(inflate);
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//                if (holder instanceof TextHolder) {
//                    TextHolder textHolder = ((TextHolder) holder);
//                    //设置数据
//                    HomeBean.ListBean listBean = mList.get(position);
//                    //设置标题
//                    textHolder.mTextView_name.setText(listBean.getName());
//                    textHolder.mTextView_name.setTextColor(Color.BLACK);
//                    textHolder.mTextView_name.setTextSize(16);
//                    //设置大小
//                    textHolder.mTextView_size.setText(android.text.format.Formatter.formatFileSize(ResourceUtils
//                            .getContext(), listBean.getSize()));
//                    textHolder.mTextView_size.setTextColor(Color.GRAY);
//                    //设置介绍
//                    textHolder.mTextView_des.setText(listBean.getDes());
//                    textHolder.mTextView_des.setTextColor(Color.GRAY);
//                    //靠,设置了cardView背景色后全部文本都要设置字体颜色
//                    textHolder.mTextView_download.setTextColor(Color.GRAY);
//
//                    //设置星星 //
//                    textHolder.mRatingBar.setProgress((int) listBean.getStars());
//                    textHolder.mRatingBar.setRating(listBean.getStars());
//                    //设置图片 //
//                    x.image().bind(textHolder.mImageView_logo, Constants.BASE_SERVER + Constants.IMAGE_INTERFACE +
//                            listBean.getIconUrl());
//                }
//            }
//
//            @Override
//            public int getItemCount() {
//                return mList.size();
//            }
//
//
//            class TextHolder extends RecyclerView.ViewHolder {
//
//                private TextView mTextView_name;
//                private final ImageView mImageView_download;
//                private final ImageView mImageView_logo;
//                private final RatingBar mRatingBar;
//                private final TextView mTextView_download;
//                private final TextView mTextView_des;
//                private final TextView mTextView_size;
//
//                public TextHolder(View itemView) {
//                    super(itemView);
//                    mTextView_name = (TextView) itemView.findViewById(R.id.tv_name);
//                    mImageView_download = (ImageView) itemView.findViewById(R.id.iv_download_image);
//                    mImageView_logo = (ImageView) itemView.findViewById(R.id.image_logo);
//                    mRatingBar = (RatingBar) itemView.findViewById(R.id.ratingbar_star);
//                    mTextView_download = (TextView) itemView.findViewById(R.id.textview_download);
//                    mTextView_des = (TextView) itemView.findViewById(R.id.textview_des);
//                    mTextView_size = (TextView) itemView.findViewById(R.id.textview_size);
//                }
//            }
//        });
    }

    private void initAdapter() {
        mAdapter = new HomePullToRefreshAdapter(R.layout.home_item_recyclerview, mList);
        initViewPager();
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
                if (view.getId() == R.id.home_item_linearLayout) {
                    //跳转到详细界面
                    Intent intent = new Intent(getActivity(), AppDetailActivity.class);
                    intent.putExtra("name", mList.get(position).getName());
                    intent.putExtra("iconUrl", mList.get(position).getIconUrl());
                    getActivity().startActivity(intent);
                    LogUtil.e("点击了 LinearLayout 1 " + position);
                } else {
                    LogUtil.e("点击了 LinearLayout 2" + position);

                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    //初始化Viewpager

    /**
     * 靠,妈的,搞这个Viewpager搞了快一天了,加载后没反应,什么都看不到,但添加除了ViewPager和ListView外的东西居然没问题
     * 找了N久也找不到问题所在,妈的,直到次日上午坚持最后一次尝试,不行就用普通的方式使用RecyclerView来加载ViewPager,
     * 复制了BaseRecyclerViewAdapterHelper源码的头布局,靠,居然可以,用对比工具看看和自己哪里设置不同,妈的,没区别,
     * 不断尝试才知道要为ViewPager设置height才有反应,草了,这个还是我自己设置好再放到BaseRecyclerViewAdapterHelper
     * 的头布局中的,还以为是它那个布局和自己设置不同,问题还是自己解决的......引以为戒..........在一个方案上不要耗时
     * 太久了,坚决换方案(有的话)........
     */
    private void initViewPager() {

        View view = getActivity().getLayoutInflater().inflate(R.layout.home_item_head, (ViewGroup) mRecyclerView
                .getParent(), false);

        final DotLieanerLayout linearLayout = (DotLieanerLayout) view.findViewById(R.id.dll);
        linearLayout.initImageView(mHomeBean.getPicture().size());

        mViewPager = (ViewPager) view.findViewById(R.id.vp);
        mViewPager.setAdapter(new ViewpagerAdapter());

        //加入google提供的切换动画
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPagerPostion = position;
                linearLayout.changeImageView(position % mHomeBean.getPicture().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //自动轮播
        autoPlayViewPager();

        mAdapter.setHeaderView(view);
    }

    private void autoPlayViewPager() {
        //设置viewpager触摸事件
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopViewPagerAuto();
                        break;
                    case MotionEvent.ACTION_UP:
                        startViewPagerAuto();
                        break;
                }


                return false;
            }
        });

        mViewPagerDelayed = new AutoViewPagerDelayed();
        ResourceUtils.postDelayed(mViewPagerDelayed, 1500);
    }

    private class AutoViewPagerDelayed implements Runnable {
        @Override
        public void run() {
            mViewPager.setCurrentItem(++mViewPagerPostion);

            //递归
            ResourceUtils.postDelayed(this, 1500);
        }
    }

    private void stopViewPagerAuto() {
        ResourceUtils.getHandler().removeCallbacks(mViewPagerDelayed);
    }

    private void startViewPagerAuto() {
        autoPlayViewPager();
    }

    private class ViewpagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mHomeBean.getPicture().size();
            ImageView imageView = new ImageView(ResourceUtils.getContext());
            container.addView(imageView);
            //加载图片
            x.image().bind(imageView, Constants.BASE_SERVER + Constants.IMAGE_INTERFACE + mHomeBean.getPicture().get
                    (position));
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
