package com.ustb.shellbox.shelllife.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by 37266 on 2016/12/31.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Drawable mDivider;
    private int mOrientation;
    public static final int HORIZTIONTAL_LIST= LinearLayout.HORIZONTAL;
    public static final int VERTICAL_LIST=LinearLayout.VERTICAL;
    //我们通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
    private static final int[] ATTRS=new int[]{android.R.attr.listDivider};

    public DividerItemDecoration(Context mContext, int orientation) {
        this.mContext = mContext;
        final TypedArray ta=mContext.obtainStyledAttributes(ATTRS);
        this.mDivider=ta.getDrawable(0);
        ta.recycle();
        setOrientation(orientation);

    }
    public void setOrientation(int orientation){
        if(orientation!=HORIZTIONTAL_LIST&&orientation!=VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation=orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(mOrientation==VERTICAL_LIST){
            drawVertical(c,parent);
        }else {
            drawHorizontal(c,parent);
        }
    }
    public void drawVertical(Canvas canvas,RecyclerView parent){
        final int left=parent.getPaddingLeft();
        final int right=parent.getWidth()-parent.getPaddingRight();

        final int childCount=parent.getChildCount();
        for(int i=0;i<childCount;i++){
            final View child=parent.getChildAt(i);

            RecyclerView recyclerView=new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)child.getLayoutParams();
            final int top  =child.getBottom()+params.bottomMargin;
            final int bottom=top+mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(canvas);
        }
    }
    public void drawHorizontal(Canvas canvas,RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(mOrientation==VERTICAL_LIST){
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
        }else {
            outRect.set(0,0,mDivider.getIntrinsicWidth(),0);
        }
    }
}
