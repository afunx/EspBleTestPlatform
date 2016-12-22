package com.esp.espbletestplatform.recycleview;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        void onItemClick(View view, BluetoothDevice bleAddr);
    }

    private OnRecyclerViewItemClickListener mItemClickListener;

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, (BluetoothDevice) v.getTag());
        }
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * OnRecyclerViewItemClickListener end
     */

    private final Context mAppContext;
    private final List<BluetoothDevice> mBluetoothDevices;
    private final List<Integer> mRssis;

    public BleDeviceAdapter(Context context, List<BluetoothDevice> bluetoothDevices,List<Integer> rssis) {
        mAppContext = context.getApplicationContext();
        mBluetoothDevices = bluetoothDevices;
        mRssis = rssis;
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
        final BluetoothDevice bluetoothDevice = mBluetoothDevices.get(position);
        String name = bluetoothDevice.getName();
        if(TextUtils.isEmpty(name)) {
            name = "Unnamed";
        }
        final int rssi = mRssis.get(position);
        holder.tv_addr.setText(bluetoothDevice.getAddress());
        holder.tv_name.setText(name);
        holder.tv_sig.setText(Integer.toString(rssi));
        holder.itemView.setTag(bluetoothDevice);
    }

    @Override
    public int getItemCount() {
        return mBluetoothDevices.size();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tv_addr;
        TextView tv_name;
        TextView tv_sig;

        public VH(View view) {
            super(view);
            tv_addr = (TextView) view.findViewById(R.id.tv_dev_addr);
            tv_name = (TextView) view.findViewById(R.id.tv_dev_name);
            tv_sig = (TextView) view.findViewById(R.id.tv_dev_sig);
        }
    }
}