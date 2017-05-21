package googleplay.itheima.com.googleplay.fragment;

import googleplay.itheima.com.googleplay.utils.Constants;

/**
 * @author TanJJ
 * @time 2017/5/21 17:09
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 游戏界面
 */

public class GameFragment extends AppFragment {
    private final String ADDRESSSERVER = Constants.BASE_SERVER + Constants.GAME_INTERFACE;

    @Override
    protected void setAddressServer(String address) {
        super.setAddressServer(ADDRESSSERVER);
    }
}
