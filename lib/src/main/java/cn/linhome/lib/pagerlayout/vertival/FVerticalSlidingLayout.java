package cn.linhome.lib.pagerlayout.vertival;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class FVerticalSlidingLayout extends RelativeLayout
{
    public FVerticalSlidingLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public FVerticalSlidingLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public FVerticalSlidingLayout(Context context)
    {
        super(context);
        init();
    }

    private static final long DURATION_HORIZONTAL = 500;

    private static final long DURATION_VERTICAL = 300;

    private EnumFinishState mFinishState;

    //vertical
    private View mTopView;
    private View mBottomView;
    private View mVerticalView;
    private int mVerticalHeight;
    private SDViewSizeListener mVerticalViewSizeListener;
    private int mVerticalIndex = 1;
    private float mDownY;
    private float mUpY;
    private boolean mEnableVerticalScroll = false;
    private boolean mNeedDealVerticalOnUp = false;

    //horizontal
    private View mLeftView;
    private View mRightView;
    private View mHorizontalView;
    private int mHorizontalWidth;
    private SDViewSizeListener mHorizontalViewSizeListener;
    private int mHorizontalIndex = 1;
    private float mDownX;
    private float mUpX;
    private boolean mEnableHorizontalScroll = true;
    private boolean mNeedDealHorizontalOnUp = false;

    private boolean mVerticalMode = true;

    private ScrollListener mListenerScroll;

    private SDGestureHandler mGestureHandler;
    private OnClickListener mOnClickListener;

    private TouchEvent mTouchEvent;

    @Override
    public void setOnClickListener(OnClickListener l)
    {
        this.mOnClickListener = l;
    }

    public void setListenerScroll(ScrollListener listenerScroll)
    {
        this.mListenerScroll = listenerScroll;
    }

    public void setTopView(View topView)
    {
        this.mTopView = topView;
        resetTopView();
    }

    public void setVerticalView(View verticalView)
    {
        this.mVerticalView = verticalView;
        mVerticalViewSizeListener.setView(verticalView).setCallback(new SDSizeChangedCallback<View>()
        {

            @Override
            public void onWidthChanged(int newWidth, int oldWidth, View target)
            {
            }

            @Override
            public void onHeightChanged(int newHeight, int oldHeight, View target)
            {
                initVertical();
            }
        });
        resetVerticalView();
    }

    public void setBottomView(View bottomView)
    {
        this.mBottomView = bottomView;
        resetBottomView();
    }

    public void setLeftView(View leftView)
    {
        this.mLeftView = leftView;
        resetLeftView();
    }

    public void setHorizontalView(View horizontalView)
    {
        this.mHorizontalView = horizontalView;
        mHorizontalViewSizeListener.setView(horizontalView).setCallback(new SDSizeChangedCallback<View>()
        {

            @Override
            public void onWidthChanged(int newWidth, int oldWidth, View target)
            {
                initHorizontal();
            }

            @Override
            public void onHeightChanged(int newHeight, int oldHeight, View target)
            {
            }
        });
        resetHorizontalView();
    }

    public void setRightView(View rightView)
    {
        this.mRightView = rightView;
        resetRightView();
    }

    private void resetTopView()
    {
        if (mTopView != null)
        {
            mTopView.scrollTo(0, mVerticalHeight);
        }
    }

    private void resetVerticalView()
    {
        if (mVerticalView != null)
        {
            mVerticalView.scrollTo(0, 0);
        }
    }

    private void resetBottomView()
    {
        if (mBottomView != null)
        {
            mBottomView.scrollTo(0, -mVerticalHeight);
        }
    }

    private void resetLeftView()
    {
        if (mLeftView != null)
        {
            mLeftView.scrollTo(0, mHorizontalWidth);
        }
    }

    private void resetHorizontalView()
    {
        if (mHorizontalView != null)
        {
            mHorizontalView.scrollTo(0, 0);
        }
    }

    private void resetRightView()
    {
        if (mRightView != null)
        {
            mRightView.scrollTo(0, -mHorizontalWidth);
        }
    }

    public void resetVerticalViews()
    {
        resetTopView();
        resetVerticalView();
        resetBottomView();
        mVerticalIndex = 1;
    }

    public void resetHorizontalViews()
    {
        resetLeftView();
        resetHorizontalView();
        resetRightView();
        mHorizontalIndex = 1;
    }

    private void init()
    {
        mHorizontalViewSizeListener = new SDViewSizeListener();
        mVerticalViewSizeListener = new SDViewSizeListener();

        mGestureHandler = new SDGestureHandler(getContext());
        mGestureHandler.setGestureListener(new SDGestureHandler.SDGestureListener()
        {
            @Override
            public boolean onScrollVertical(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                if (canScrollVertical(distanceY))
                {
                    mNeedDealVerticalOnUp = true;
                    if (mTopView != null)
                    {
                        mTopView.scrollBy(0, (int) distanceY);
                    }
                    if (mBottomView != null)
                    {
                        mBottomView.scrollBy(0, (int) distanceY);
                    }
                    mVerticalView.scrollBy(0, (int) distanceY);

                    if (mListenerScroll != null)
                    {
                        mListenerScroll.onVerticalScroll(e1, e2, distanceX, distanceY);
                    }
                    return true;
                } else
                {
                    mDownY = e2.getRawY();
                    return false;
                }
            }

            @Override
            public boolean onScrollHorizontal(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                if (canScrollHorizontal(distanceX))
                {
                    mNeedDealHorizontalOnUp = true;
                    if (mLeftView != null)
                    {
                        mLeftView.scrollBy((int) distanceX, 0);
                    }
                    if (mRightView != null)
                    {
                        mRightView.scrollBy((int) distanceX, 0);
                    }
                    mHorizontalView.scrollBy((int) distanceX, 0);

                    if (mListenerScroll != null)
                    {
                        mListenerScroll.onHorizontalScroll(e1, e2, distanceX, distanceY);
                    }
                    return true;
                } else
                {
                    mDownX = e2.getRawX();
                    return false;
                }
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                if (mOnClickListener != null)
                {
                    mOnClickListener.onClick(FVerticalSlidingLayout.this);
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDown(MotionEvent e)
            {
                mDownX = e.getRawX();
                mDownY = e.getRawY();
                return true;
            }

            @Override
            public void onActionUp(MotionEvent event, float velocityX, float velocityY)
            {
                mUpX = event.getRawX();
                mUpY = event.getRawY();
                if (mGestureHandler.isScrollVertical())
                {
                    if (mNeedDealVerticalOnUp)
                    {
                        mVerticalMode = true;
                        if (Math.abs(velocityY) > mGestureHandler.getScaledMinimumFlingVelocityCommon())
                        {
                            if (velocityY > 0)
                            {
                                if (mTopView != null)
                                {
                                    scrollToBottom();
                                } else
                                {
                                    scrollToCenterVertical();
                                }
                            } else
                            {
                                if (mBottomView != null)
                                {
                                    scrollToTop();
                                } else
                                {
                                    scrollToCenterVertical();
                                }
                            }
                        } else
                        {
                            dealActionUpVertical();
                        }
                    }
                } else if (mGestureHandler.isScrollHorizontal())
                {
                    if (mNeedDealHorizontalOnUp)
                    {
                        mVerticalMode = false;
                        if (Math.abs(velocityX) > mGestureHandler.getScaledMinimumFlingVelocityCommon())
                        {
                            if (velocityX > 0)
                            {
                                if (mLeftView != null)
                                {
                                    scrollToRight();
                                } else
                                {
                                    scrollToCenterHorizontal();
                                }
                            } else
                            {
                                if (mRightView != null)
                                {
                                    scrollToLeft();
                                } else
                                {
                                    scrollToCenterHorizontal();
                                }
                            }
                        } else
                        {
                            dealActionUpHorizontal();
                        }
                    }
                }
                mNeedDealVerticalOnUp = false;
                mNeedDealHorizontalOnUp = false;
            }
        });
    }

    /**
     * 是否可以竖直方向滚动
     *
     * @param distanceY
     * @return
     */
    private boolean canScrollVertical(float distanceY)
    {
        if (!mEnableVerticalScroll)
        {
            return false;
        }

        if (mVerticalView == null || (mTopView == null && mBottomView == null))
        {
            return false;
        }

        int scrollY = mVerticalView.getScrollY();

        int bottomValue = 0;
        if (mBottomView == null)
        {
            bottomValue = 0;
        } else
        {
            bottomValue = mVerticalHeight;
        }

        int topValue = 0;
        if (mTopView == null)
        {
            topValue = 0;
        } else
        {
            topValue = -mVerticalHeight;
        }

        if (scrollY >= bottomValue)
        {
            //在最底部页面
            if (distanceY > 0)
            {
                //上滑动
                return false;
            }
        } else if (scrollY <= topValue)
        {
            //在最顶部页面
            if (distanceY < 0)
            {
                //下滑动
                return false;
            }
        }
        return true;
    }

    /**
     * 是否可以横向滚动
     *
     * @param distanceX
     * @return
     */
    private boolean canScrollHorizontal(float distanceX)
    {
        if (!mEnableHorizontalScroll)
        {
            return false;
        }

        if (mHorizontalView == null || (mLeftView == null && mRightView == null))
        {
            return false;
        }

        int scrollX = mHorizontalView.getScrollX();

        int rightValue = 0;
        if (mRightView == null)
        {
            rightValue = 0;
        } else
        {
            rightValue = mHorizontalWidth;
        }

        int leftValue = 0;
        if (mLeftView == null)
        {
            leftValue = 0;
        } else
        {
            leftValue = -mHorizontalWidth;
        }

        if (scrollX >= rightValue)
        {
            //在最右边页面
            if (distanceX > 0)
            {
                //左滑动
                return false;
            }
        } else if (scrollX <= leftValue)
        {
            //在最左边页面
            if (distanceX < 0)
            {
                //右滑动
                return false;
            }
        }
        return true;
    }

    private void initVertical()
    {
        if (mVerticalView != null)
        {
            int newHeight = mVerticalView.getHeight();
            if (newHeight != mVerticalHeight)
            {
                mVerticalHeight = newHeight;
                resetVerticalViews();
            }
        }
    }

    private void initHorizontal()
    {
        if (mHorizontalView != null)
        {
            int newWidth = mHorizontalView.getWidth();
            if (newWidth != mHorizontalWidth)
            {
                mHorizontalWidth = newWidth;
                resetHorizontalViews();
            }
        }
    }

    @Override
    public void computeScroll()
    {
        if (mGestureHandler.getScroller().computeScrollOffset())
        {
            if (mGestureHandler.getScroller().isFinished())
            {
                switch (mFinishState)
                {
                    case top:
                        mVerticalIndex = 0;
                        if (mListenerScroll != null)
                        {
                            mListenerScroll.onFinishTop();
                        }
                        break;
                    case centerVertical:
                        mVerticalIndex = 1;
                        if (mListenerScroll != null)
                        {
                            mListenerScroll.onFinishCenter();
                        }
                        break;
                    case bottom:
                        mVerticalIndex = 2;
                        if (mListenerScroll != null)
                        {
                            mListenerScroll.onFinishBottom();
                        }
                        break;
                    case left:
                        mHorizontalIndex = 0;
                        break;
                    case centerHorizontal:
                        mHorizontalIndex = 1;
                        break;
                    case right:
                        mHorizontalIndex = 2;
                        break;

                    default:
                        break;
                }
            } else
            {
                if (mVerticalMode)
                {
                    int currX = mGestureHandler.getScroller().getCurrX();
                    int currY = mGestureHandler.getScroller().getCurrY();

                    if (mTopView != null)
                    {
                        mTopView.scrollTo(currX, currY + mVerticalHeight);
                    }
                    mVerticalView.scrollTo(currX, currY);
                    if (mBottomView != null)
                    {
                        mBottomView.scrollTo(currX, currY - mVerticalHeight);
                    }
                } else
                {
                    int currX = mGestureHandler.getScroller().getCurrX();
                    int currY = mGestureHandler.getScroller().getCurrY();

                    if (mLeftView != null)
                    {
                        mLeftView.scrollTo(currX + mHorizontalWidth, currY);
                    }
                    mHorizontalView.scrollTo(currX, currY);
                    if (mRightView != null)
                    {
                        mRightView.scrollTo(currX - mHorizontalWidth, currY);
                    }
                }
            }
        }
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(mTouchEvent != null)
        {
            mTouchEvent.onTouchEvent(event);
        }
        return mGestureHandler.onTouchEvent(event);
    }

    public interface TouchEvent
    {
        void onTouchEvent(MotionEvent event);
    }

    public void setOnTouchEvent(TouchEvent touchEvent)
    {
        this.mTouchEvent = touchEvent;
    }

    private float getDistanceX()
    {
        return Math.abs(mDownX - mUpX);
    }

    private float getDistanceY()
    {
        return Math.abs(mDownY - mUpY);
    }

    /**
     * 手指向左滑动
     *
     * @return
     */
    private boolean isSlidLeftOnUp()
    {
        return mDownX - mUpX > 0;
    }

    /**
     * 手指向上滑动
     *
     * @return
     */
    private boolean isSlideTopOnUp()
    {
        return mDownY - mUpY > 0;
    }

    private void dealActionUpHorizontal()
    {
        if (mHorizontalView != null)
        {
            if (getDistanceX() > mHorizontalWidth / 2)
            {
                switch (mHorizontalIndex)
                {
                    case 0:
                        if (isSlidLeftOnUp())
                        {
                            scrollToCenterHorizontal();
                        }
                        break;
                    case 1:
                        if (isSlidLeftOnUp())
                        {
                            scrollToLeft();
                        } else
                        {
                            scrollToRight();
                        }
                        break;
                    case 2:
                        if (isSlidLeftOnUp())
                        {

                        } else
                        {
                            scrollToCenterHorizontal();
                        }
                        break;
                    default:
                        break;
                }
            } else
            {
                switch (mHorizontalIndex)
                {
                    case 0:
                        scrollToRight();
                        break;
                    case 1:
                        scrollToCenterHorizontal();
                        break;
                    case 2:
                        scrollToLeft();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 界面向左滑动
     */
    private void scrollToLeft()
    {
        int dur = SDGestureHandler.getDurationPercent(mHorizontalView.getScrollX() - mHorizontalWidth, mHorizontalWidth, DURATION_HORIZONTAL);
        mGestureHandler.getScroller().startScrollToX(mHorizontalView.getScrollX(), mHorizontalWidth, dur);
        mFinishState = EnumFinishState.right;
        postInvalidate();
    }

    /**
     * 界面向右
     */
    private void scrollToRight()
    {
        int dur = SDGestureHandler.getDurationPercent(mHorizontalView.getScrollX() - (-mHorizontalWidth), mHorizontalWidth, DURATION_HORIZONTAL);
        mGestureHandler.getScroller().startScrollToX(mHorizontalView.getScrollX(), -mHorizontalWidth, dur);
        mFinishState = EnumFinishState.left;
        postInvalidate();
    }

    private void scrollToCenterHorizontal()
    {
        int dur = SDGestureHandler.getDurationPercent(mHorizontalView.getScrollX(), mHorizontalWidth, DURATION_HORIZONTAL);
        mGestureHandler.getScroller().startScrollToX(mHorizontalView.getScrollX(), 0, dur);
        mFinishState = EnumFinishState.centerHorizontal;
        postInvalidate();
    }

    private void dealActionUpVertical()
    {
        if (mVerticalView != null)
        {
            if (getDistanceY() > mVerticalHeight / 2)
            {
                switch (mVerticalIndex)
                {
                    case 0:
                        if (isSlideTopOnUp())
                        {
                            scrollToCenterVertical();
                        }
                        break;
                    case 1:
                        if (isSlideTopOnUp())
                        {
                            scrollToTop();
                        } else
                        {
                            scrollToBottom();
                        }
                        break;
                    case 2:
                        if (isSlideTopOnUp())
                        {

                        } else
                        {
                            scrollToCenterVertical();
                        }
                        break;
                    default:
                        break;
                }
            } else
            {
                switch (mHorizontalIndex)
                {
                    case 0:
                        scrollToBottom();
                        break;
                    case 1:
                        scrollToCenterVertical();
                        break;
                    case 2:
                        scrollToTop();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 界面向上滚动
     */
    private void scrollToTop()
    {
        int dur = SDGestureHandler.getDurationPercent(mVerticalView.getScrollY() - mVerticalHeight, mVerticalHeight, DURATION_VERTICAL);
        mGestureHandler.getScroller().startScrollToY(mVerticalView.getScrollY(), mVerticalHeight, dur);
        mFinishState = EnumFinishState.bottom;
        postInvalidate();
    }

    /**
     * 界面向下滚动
     */
    private void scrollToBottom()
    {
        int dur = SDGestureHandler.getDurationPercent(mVerticalView.getScrollY() - (-mVerticalHeight), mVerticalHeight, DURATION_VERTICAL);
        mGestureHandler.getScroller().startScrollToY(mVerticalView.getScrollY(), -mVerticalHeight, dur);
        mFinishState = EnumFinishState.top;
        postInvalidate();
    }

    private void scrollToCenterVertical()
    {
        int dur = SDGestureHandler.getDurationPercent(mVerticalView.getScrollY(), mVerticalHeight, DURATION_VERTICAL);
        mGestureHandler.getScroller().startScrollToY(mVerticalView.getScrollY(), 0, dur);
        mFinishState = EnumFinishState.centerVertical;
        postInvalidate();
    }

    public void setEnableVerticalScroll(boolean enableVerticalScroll)
    {
        this.mEnableVerticalScroll = enableVerticalScroll;
    }

    public void setEnableHorizontalScroll(boolean enableHorizontalScroll)
    {
        this.mEnableHorizontalScroll = enableHorizontalScroll;
    }

    public enum EnumFinishState
    {
        top, centerVertical, bottom, left, centerHorizontal, right;
    }

    public interface ScrollListener
    {
        void onFinishTop();

        void onFinishCenter();

        void onFinishBottom();

        void onVerticalScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        void onHorizontalScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    }
}
