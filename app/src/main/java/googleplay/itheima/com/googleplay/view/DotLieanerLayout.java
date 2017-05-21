package googleplay.itheima.com.googleplay.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import googleplay.itheima.com.googleplay.R;
import googleplay.itheima.com.googleplay.utils.ResourceUtils;

/**
 * @author TanJJ
 * @time 2017/5/21 13:04
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.view
 * @des 指示器(Indicator)点
 */

public class DotLieanerLayout extends LinearLayout {
    public DotLieanerLayout(Context context) {
        super(context);
    }

    public DotLieanerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    public void initImageView(int size) {
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(ResourceUtils.getContext());
            if (i == 0) {
                imageView.setImageResource(R.drawable.indicator_selected);
            } else {
                imageView.setImageResource(R.drawable.indicator_normal);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            addView(imageView, params);
        }

    }

    public void changeImageView(int position) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            if (position == i) {
                imageView.setImageResource(R.drawable.indicator_selected);
            } else {
                imageView.setImageResource(R.drawable.indicator_normal);
            }
        }
    }

    public DotLieanerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
