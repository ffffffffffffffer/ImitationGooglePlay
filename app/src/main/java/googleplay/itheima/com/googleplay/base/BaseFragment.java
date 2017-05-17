package googleplay.itheima.com.googleplay.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import googleplay.itheima.com.googleplay.utils.ResourceUtils;

/**
 * @author TanJJ
 * @time 2017/5/15 9:14
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.fragment
 * @des 基类Fragment
 * @SVN_Version: $Rev$
 * @UpdateAuthor: $Author$
 * @UpdateTime: $Date$
 * @UpdateDes: TODO
 */

public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        TextView textView = new TextView(ResourceUtils.getContext());
        textView.setText("首页");
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        return textView;
    }
}
