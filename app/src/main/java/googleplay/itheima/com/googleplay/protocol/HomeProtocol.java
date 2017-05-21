package googleplay.itheima.com.googleplay.protocol;

import googleplay.itheima.com.googleplay.bean.HomeBean;

/**
 * @author TanJJ
 * @time 2017/5/21 15:28
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.protocol
 * @des TODO
 */

public class HomeProtocol extends BaseProtocol<HomeBean> {
    @Override
    protected void onSuccess(String result, int state) {

    }

    @Override
    protected void onError(Throwable ex, boolean isOnCallback) {

    }
//    private final HomeFragment homeFragment;
//
//    public HomeProtocol(HomeFragment homeFragment) {
//        this.homeFragment = homeFragment;
//    }
//
//    @Override
//    protected void onSuccess(String result, int state) {
//        if (state == BaseProtocol.LOADING_DATA) {
//            mSuccess = LoadingUI.LoadingEnum.SUCCESS;
//            HomeBean homeBean = gsonDecode(result, state);
//            //使用线程池
////                mTask = new TaskRunnable();
////                ThreadPoolManager.getLongThread().submit(mTask);
//            onLoadData(homeBean);
//            //没必再开线程了
//            homeFragment.loadDate();
//        } else if (state == LOAD_MORE) {
//            gsonDecode(result, state);
//        }
//    }
//
//    private void onLoadData(HomeBean mHomeBean) {
//        final List<HomeBean.ListBean> list = mHomeBean.getList();
//        if (state == LOADING_DATA) {
//            mList = list;
//        } else if (state == LOAD_MORE) {
//            if (homeFragmentmAdapter != null) {
//                //让加载时间久一点
//                ResourceUtils.getHandler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mCurrentCounter >= TOTAL_COUNTER) {
//                            //数据全部加载完毕
//                            mAdapter.loadMoreEnd();
//                        } else {
//                            if (list.size() > 0) {
//                                //if(isErr){
//                                //设置数据进集合中
//                                mList.addAll(list);
//                                //#### 对于该BaseRecyclerViewAdapterHelper开源项目来讲,下面两个方法非常重要,能不能加载跟多就看它们了 #####
//                                //把要新增的数据添加到Adapter内部维护的List集合中
//                                mAdapter.addData(list);
//                                //成功获取更多数据
//                                mCurrentCounter = mAdapter.getData().size();
//                                //加载完成,刷新
//                                mAdapter.loadMoreComplete();
//                                //################################################
//                            } else {
//                                //获取更多数据失败
//                                isErr = true;
//                                ToastUtils.show(getActivity(), ResourceUtils.getContext().getString(R.string
//                                        .load_error));
//                                mAdapter.loadMoreFail();
//
//                            }
//                        }
//                    }
//                }, DELAYED);
//            }
//        }
//    }
//
//    @Override
//    protected void onError(Throwable ex, boolean isOnCallback) {
//        ToastUtils.show(homeFragment.getActivity(), "访问失败!" + ex.getMessage());
//        LogUtil.d("访问失败!" + ex.getMessage());
//        mSuccess = LoadingUI.LoadingEnum.ERROR;
////                mTask = new TaskRunnable();
////                ThreadPoolManager.getLongThread().submit(mTask);
//        homeFragment.loadDate();
//    }
}
