package com.ustb.shellbox.shelllife.apater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by 37266 on 2017/3/13.
 */
public class PtrrvBaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_HISVIDEO = 1;
    public static final int TYPE_MESSAGE = 2;
    protected Context mContext = null;
    protected int mCount = 0;
    protected LayoutInflater mInflater;

    public PtrrvBaseAdapter(Context mContext, LayoutInflater mInflater) {
        this.mContext = mContext;
        this.mInflater = mInflater;
    }
    public Object getItem(int paramInt)
    {
        return null;
    }
    public void setCount(int paramInt)
    {
        this.mCount = paramInt;
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
    @Override
    public void onBindViewHolder(VH holder, int position) {

    }
    @Override
    public int getItemCount() {
        return this.mCount;
    }

}
