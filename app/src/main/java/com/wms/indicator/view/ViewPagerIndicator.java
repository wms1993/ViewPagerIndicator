



























package com.wms.indicator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wms.indicator.R;

/**
 * Created by wms1993 on 2016/4/7 0007.
 */
public class ViewPagerIndicator extends LinearLayout {

    /**
     * 三角形宽度是每个Tab宽度的1/8
     */
    private static final float RADIO_TRIANGLE = 1 / 8F;
    /**
     * 默认的tab的个数
     */
    private static final int DEFAULT_TAB_VISIBLE_COUNT = 3;
    /**
     * 默认的未选中的字体颜色
     */
    private static final int DEFAULT_UNSELECTED_COLOR = Color.parseColor("#CCFFFFFF");
    /**
     * 默认的选中的字体颜色
     */
    private static final int DEFAULT_SELECTED_COLOR = Color.parseColor("#FFFFFF");
    /**
     * 三角形的宽度
     */
    private int mTriangleWidth;
    /**
     * 三角形的高度
     */
    private int mTriangleHeight;
    /**
     * 三角形路径
     */
    private Path mTriangelePath;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 初始状态的三角形偏移量
     */
    private int mInitTranslationX;
    /**
     * 三角形偏移量
     */
    private int mTranslationX;
    /**
     * 联动的ViewPagr
     */
    private ViewPager mViewPager;
    /**
     * Tab标题
     */
    private String[] mTitles;
    /**
     * 自定义属性中设置的可见的tab数量
     */
    private int mVisibleTabCount;
    /**
     * 默认的三角形的宽度
     */
    private int mDefaultTriangleWidth;
    /**
     * 正常的字体颜色
     */
    private int mUnselectedColor;
    /**
     * 选中的字体颜色
     */
    private int mSelectedColor;
    /**
     * 正常状态字体大小 （单位是sp）
     */
    private float mUnselectedTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
    /**
     * 选中状态字体大小 （单位是sp）,默认情况下和正常状态下字体大小一致
     */
    private float mSelectedTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mVisibleTabCount = array.getInt(R.styleable.ViewPagerIndicator_tab_visible_count, DEFAULT_TAB_VISIBLE_COUNT);
        mUnselectedColor = array.getColor(R.styleable.ViewPagerIndicator_tab_unselected_textcolor, DEFAULT_UNSELECTED_COLOR);
        mSelectedColor = array.getColor(R.styleable.ViewPagerIndicator_tab_selected_textcolor, DEFAULT_SELECTED_COLOR);
        mUnselectedTextSize = array.getDimension(R.styleable.ViewPagerIndicator_tab_unselected_textsize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        mSelectedTextSize = array.getDimension(R.styleable.ViewPagerIndicator_tab_selected_textsize, mUnselectedTextSize);
        array.recycle();

        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        mDefaultTriangleWidth = (int) (getScreenWidth() / DEFAULT_TAB_VISIBLE_COUNT * RADIO_TRIANGLE);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth = (int) (w / mVisibleTabCount * RADIO_TRIANGLE);
        mTriangleWidth = Math.min(mTriangleWidth, mDefaultTriangleWidth);
        mTriangleHeight = mTriangleWidth / 2;

        mInitTranslationX = w / mVisibleTabCount / 2 - mTriangleWidth / 2;

        initTriangle();
    }

    /**
     * 动态生成Tab
     *
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView textView = new TextView(getContext());
        textView.setText(title);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnselectedTextSize);
        textView.setTextColor(mUnselectedColor);
        textView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getScreenWidth() / mVisibleTabCount, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);

        return textView;
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outmetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outmetrics);
        return outmetrics.widthPixels;
    }

    /**
     * 创建三角形路径
     */
    private void initTriangle() {
        mTriangelePath = new Path();
        mTriangelePath.moveTo(0, 0);
        mTriangelePath.lineTo(mTriangleWidth, 0);
        mTriangelePath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mTriangelePath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        canvas.save();

        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 5);//+5目的是为了让三角形显示的不是那么突兀
        canvas.drawPath(mTriangelePath, mPaint);

        canvas.restore();
    }

    /**
     * 设置viewpager
     *
     * @param pager
     */
    public void setViewPager(ViewPager pager, int position) {
        this.mViewPager = pager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                setSelectedTextColorAndSize(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(position);
        setSelectedTextColorAndSize(position);
    }

    /**
     * 设置选中字体颜色
     *
     * @param position
     */
    private void setSelectedTextColorAndSize(int position) {
        resetTextColor();

        View view = getChildAt(position);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(mSelectedColor);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectedTextSize);
        }
    }

    /**
     * 重置字体颜色
     */
    private void resetTextColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(mUnselectedColor);
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnselectedTextSize);
            }
        }
    }

    /**
     * 设置标题
     *
     * @param titles
     */
    public void setTitles(String[] titles) {
        if (titles != null && titles.length != 0) {
            mTitles = titles;
        }

        for (String title : mTitles) {
            addView(generateTextView(title));
        }

        setItemClickEvent();
    }

    /**
     * 设置滚动
     *
     * @param position
     * @param offset
     */
    private void scroll(int position, float offset) {
        int tabWidth = getWidth() / mVisibleTabCount;
        mTranslationX = (int) (position * tabWidth + tabWidth * offset);

        if (position <= (getChildCount() - 3) && position <= (getChildCount() - 2) && position >= (mVisibleTabCount - 2) && offset > 0 && getChildCount() > mVisibleTabCount) {
            if (mVisibleTabCount != 1) {
                this.scrollTo((int) ((position - (mVisibleTabCount - 2)) * tabWidth + tabWidth * offset), 0);
            } else {
                this.scrollTo((int) (position * tabWidth + tabWidth * offset), 0);
            }
        }

        invalidate();
    }

    public void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int finalI = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(finalI);
                }
            });
        }
    }

}