package com.example.wyz.xiyoug.Viewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.wyz.xiyoug.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Wyz on 2016/7/17.
 */
public class SlideMenu extends HorizontalScrollView {

    //屏幕宽度
    private int mScreenWidth;
    //屏幕高度
    private int mScreenHeight;
    //侧边栏宽度
    private static int mMenuWidth;
    //距离右边距离
    private  int rightPadding;
    //左边可以滑动距离
    private  int leftSlide;
    //侧边菜单栏
    private View mMenu;
    //内容区域
    private View mContent;

    public SlideMenu(Context context) {
        this(context,null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    //获取菜单栏
    public View getMenuView() {

        return mMenu;
    }
    //获取内容区域
    public View getContentView() {
        return mContent;
    }

    //是否隐藏
    private boolean mHasInit = false;
    //是否打开
    private boolean isOpen=false;
    private LinearLayout mWrapper;





    private void init(AttributeSet attrs,int defStyleAttr) {
        //解析自定义参数的值
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SlideMenu,defStyleAttr,0);
        int n= typedArray.getIndexCount();
        int menuLayoutId=0;
        int contentLayoutId=0;

        //通过查询自定义属性设置rightPadding
        for(int i=0;i<n;i++)
        {
            int attr=typedArray.getIndex(i);

            switch (attr)
            {
                case  R.styleable.SlideMenu_menu_layout :
                    menuLayoutId=typedArray.getResourceId(i,-1);
                    break;
                case R.styleable.SlideMenu_content_layout:
                    contentLayoutId=typedArray.getResourceId(i,-1);
                    break;
                case R.styleable.SlideMenu_right_padding:
                    rightPadding=typedArray.getDimensionPixelSize(attr,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getContext().getResources().getDisplayMetrics()));
                case R.styleable.SlideMenu_left_slide:
                    leftSlide=typedArray.getDimensionPixelSize(attr,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getContext().getResources().getDisplayMetrics()));

            }
        }
        Log.d("G_typeArray",String.valueOf(n));

       /* int menuLayoutId = typedArray.getResourceId(0, -1);
        int contentLayoutId = typedArray.getResourceId(1, -1);
        int rightPadding=typedArray.getResourceId(2,0);*/
        //获取屏幕宽高
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;

        mMenuWidth=mScreenWidth-rightPadding;

        //加载View
        mMenu =View.inflate(getContext(), menuLayoutId, null);

        mContent = View.inflate(getContext(), contentLayoutId, null);


        //设置为不能滑出边界(滑出边界的效果比较难看)
        setOverScrollMode(OVER_SCROLL_NEVER);
        //设置无滚动条
        setHorizontalScrollBarEnabled(false);
        Log.d("G_init","success");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置测量的宽高为屏幕的宽高
       // setMeasuredDimension(mScreenWidth, mScreenHeight);
        if(!mHasInit){

            //由于会多次测量，所以使用mHasInit保证布局只加载一次
            mHasInit = true;
            //创建子View，并加到ScrollView中

            mWrapper = new LinearLayout(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mMenuWidth+mScreenWidth,getMeasuredHeight());
            mWrapper.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(mMenuWidth,getMeasuredHeight());
            mWrapper.addView(mMenu, menuParams);
            LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(mScreenWidth, getMeasuredHeight());
            mWrapper.addView(mContent, contentParams);
            mWrapper.setLayoutParams(layoutParams);
            addView(mWrapper);

        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if(changed){
            //将ScrollView滚动到mMenuWidth的位置，也就是滚动到跟内容View的左侧对其，让菜单栏刚好完全隐藏在屏幕左侧
            //注意滚动的时机，当view有新的布局和尺寸时调用,也就是在初始化的时候去滚动。这个时候滚动才不会在打开页面时有滚动的效果
            scrollTo(mMenuWidth, 0);
        }
        super.onLayout(changed, l, t, r, b);
    }

    private long mDownTime;
    private float mDownX;
    private float mDownY;

    private boolean mAcceptClick;
    private boolean mAcceptMove;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!isOpen)
        {
            if (getScrollX() == 0) {
                return true;
            }
            if (getScrollX() == mMenuWidth) {
                mDownX = ev.getX();
                if (mDownX + getScrollX() > mMenuWidth + leftSlide) {
                    //isOpen=true;
                    clearTemp();
                    return false;
                }
            }
        }
        else if(isOpen)
        {
            mDownX=ev.getX();
            if(mDownX>mMenuWidth)
            {
                smoothScrollTo(mMenuWidth, 0);
                isOpen = false;
            }
        }


        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    mDownTime = System.currentTimeMillis();
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    if (getScrollX() == 0 || getScrollX() != 0 && mDownX + getScrollX() < mMenuWidth + leftSlide) {
                        mAcceptMove = true;
                    }
                    if (getScrollX() == 0 && mDownX + getScrollX() > mMenuWidth) {
                        mAcceptClick = true;
                    }
                    break;


                case MotionEvent.ACTION_MOVE:

                    if (!mAcceptMove) {
                        if (getScrollX() + ev.getX() < mMenuWidth + leftSlide) {
                            mAcceptMove = true;
                        }
                        ev.setAction(MotionEvent.ACTION_DOWN);
                    }
                    break;


                case MotionEvent.ACTION_UP:
                    float upX = ev.getX();
                    float upY = ev.getY();
                    if (isClick(upX, upY)) {
                        smoothScrollTo(mMenuWidth, 0);
                        clearTemp();
                        return true;
                    }
                    //关闭菜单
                    if (getScrollX() > mMenuWidth / 2) {
                        smoothScrollTo(mMenuWidth, 0);
                        isOpen = false;
                    }
                    //打开菜单
                    else {
                        smoothScrollTo(0, 0);
                        isOpen = true;
                    }
                    clearTemp();
                    return true;
            }



        return super.onTouchEvent(ev);
    }

    /*
@Override
public boolean onTouchEvent(MotionEvent ev) {
    int action=ev.getAction();

    switch (action)
    {
        case MotionEvent.ACTION_DOWN:
            mDownX=ev.getX();
            mDownY=ev.getY();
            if (getScrollX()==0&& mDownX + getScrollX() > mMenuWidth) {
                mAcceptClick = true;
            }
            break;
        case MotionEvent.ACTION_UP:
            float upX = ev.getX();
            float upY = ev.getY();

            if (isClick(upX, upY)) {
                smoothScrollTo(mMenuWidth, 0);
                clearTemp();
                return true;
            }
            //隐藏在左边的宽度
            int scrollX=getScrollX();
            //关闭菜单
            if (scrollX > mMenuWidth /2) {
                this.smoothScrollTo(mMenuWidth, 0);
                isOpen=false;
            }
            //打开菜单
            else {
                this.smoothScrollTo(0,0);
                isOpen=true;
            }
            clearTemp();
            return true;
    }
    return super.onTouchEvent(ev);
}
*/
    private void clearTemp() {
        mDownX = 0;
        mDownY = 0;
        mAcceptClick = false;
        mAcceptMove = false;
    }

    private boolean isClick(float upX, float upY) {
       // return mAcceptClick && System.currentTimeMillis() - mDownTime < 1000 && Math.pow((mDownX - upX), 2) + Math.pow((mDownY - upY), 2) < 200;
        return  mAcceptClick;
    }
    /**
     * 打开菜单
     */
    public  void openMenu()
    {
        if(isOpen)
            return;
        else
        {
            this.smoothScrollTo(0,0);
            Log.d("menu_open","open");
            isOpen=true;
        }
    }
    /**
     * 关闭菜单
     */
    public  void closeMenu()
    {
        if(!isOpen)

            return;
        else
        {

            this.smoothScrollTo(mMenuWidth,0);
            Log.d("menu_close",String.valueOf(mMenuWidth));
            isOpen=false;
        }
    }

    /**
     * 切换菜单
     */
    /**
     * 弹出或隐藏
     */

    public  void toggle(){
        if (isOpen){
            closeMenu();
        }else{
            openMenu();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //显示了1/3，L就是2/3，所以刚开始L为mMenuWith，scale为1
        float scale=l*1.0f/mMenuWidth;   //1-0
        /**
         * 区别一：内容区域1.0-0.7，缩放的效果
         *
         * 区别二：菜单有显示有缩放以及透明度变化 0.7-1.0  0.6-1.0
         */
        float rightScale=0.7f+0.3f*scale;
        float leftScale=1.0f-0.3f*scale;
        float leftAlpha=1.0f-0.4f*scale;
        //调用属性动画，TranslationX
        ViewHelper.setTranslationX(mMenu,mMenuWidth*scale);

        ViewHelper.setScaleX(mMenu,leftScale);
        ViewHelper.setScaleY(mMenu,leftScale);
        ViewHelper.setAlpha(mMenu,leftAlpha);

        //设置Content缩放的中心点
        ViewHelper.setPivotX(mContent,0);
        ViewHelper.setScaleY(mContent,mContent.getHeight()/2);
        ViewHelper.setScaleY(mContent,rightScale);
        ViewHelper.setScaleX(mContent,rightScale);
    }}


