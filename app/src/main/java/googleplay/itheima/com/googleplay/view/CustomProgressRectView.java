package googleplay.itheima.com.googleplay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;

import googleplay.itheima.com.googleplay.R;

/**
 * @author TanJJ
 * @time 2017/5/28 14:35
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.view
 * @des 自定义矩形进度
 */

public class CustomProgressRectView extends Button {
    private float mProgress;
    private int mColor;
    private int mMax;

    public CustomProgressRectView(Context context) {
        super(context);
    }

    public CustomProgressRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressRectView,
                0, 0);
        mColor = typedArray.getColor(R.styleable.CustomProgressRectView_backColor, Color.BLACK);
    }

    public CustomProgressRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置进度最大值
        mMax = 100;
        RectF rect = new RectF();
        rect.set(0, 0, getMeasuredWidth() * getProgress() * 1f / mMax + 0.5f, getBottom());
        Paint paint = new Paint();
        paint.setColor(mColor);
        canvas.drawRect(rect, paint);
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }
}
