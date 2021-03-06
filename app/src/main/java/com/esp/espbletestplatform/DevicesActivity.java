package com.esp.espbletestplatform;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.afunx.ble.blelitelib.scanner.BleScanner;
import com.esp.espbletestplatform.recycleview.BleDeviceAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DevicesActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AtomicBoolean mIsDataClear;
    private RecyclerView mRecyclerView;
    private List<BluetoothDevice> mBluetoothDevices;
    private List<Integer> mRssis;
    private BleDeviceAdapter mAdapter;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private Scheduler mSingleScheduler;

    private void init() {
        mIsDataClear = new AtomicBoolean(false);
        mSingleScheduler = Schedulers.newThread();
        mBluetoothDevices = new ArrayList<>();
        mRssis = new ArrayList<>();
        mAdapter = new BleDeviceAdapter(this, mBluetoothDevices, mRssis);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setItemClickListener(new BleDeviceAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, BluetoothDevice bluetoothDevice) {
                Toast.makeText(DevicesActivity.this, bluetoothDevice.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                // clear all
                if (mIsDataClear.get()) {
                    Observable.create(new Observable.OnSubscribe<Void>() {
                        @Override
                        public void call(Subscriber<? super Void> subscriber) {
                            mBluetoothDevices.clear();
                            mRssis.clear();
                            mAdapter.notifyDataSetChanged();
                            mIsDataClear.set(false);
                        }
                    }).observeOn(AndroidSchedulers.mainThread()).subscribe();
                    return;
                }
                Observable.from(mBluetoothDevices).subscribeOn(mSingleScheduler)
                        .exists(new Func1<BluetoothDevice, Boolean>() {
                            @Override
                            public Boolean call(BluetoothDevice bluetoothDevice) {
                                return bluetoothDevice.getAddress().equals(device.getAddress());
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(Boolean exist) {
                                if (!exist) {
                                    mBluetoothDevices.add(device);
                                    mRssis.add(rssi);
                                    mAdapter.notifyItemInserted(mBluetoothDevices.size() - 1);
                                } else {
                                    int position = mBluetoothDevices.indexOf(device);
                                    mRssis.set(position, rssi);
                                    mAdapter.notifyItemChanged(position);
                                }
                            }
                        });
            }
        };
    }

    private void doRefresh() {
        mIsDataClear.set(true);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BleScanner.startLeScan(mLeScanCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BleScanner.stopLeScan(mLeScanCallback);
    }
}