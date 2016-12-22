package com.esp.espbletestplatform.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.esp.espbletestplatform.R;

import java.util.List;

/**
 * Created by afunx on 22/12/2016.
 */

public class BleDeviceAdapter extends RecyclerView.Adapter<BleDeviceAdapter.VH> implements View.OnClickListener {

    /**
     * OnRecyclerViewItemClickListener start
     */

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String bleAddr);
    }

    private OnRecyclerViewItemClickListener mItemClickListener;

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * OnRecyclerViewItemClickListener end
     */

    private final Context mAppContext;
    private final List<String> mDatas;

    public BleDeviceAdapter(Context context, List<String> datas) {
        mAppContext = context.getApplicationContext();
        mDatas = datas;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAppContext).inflate(R.layout.item_ble_device, parent, false);
        VH holder = new VH(view);
        // bind listener
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.tv.setText(mDatas.get(position));
        holder.itemView.setTag(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tv;

        public VH(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_item_ble_device);
        }
    }
}
