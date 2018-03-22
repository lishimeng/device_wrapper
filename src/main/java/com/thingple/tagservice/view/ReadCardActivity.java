package com.thingple.tagservice.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thingple.tag.operator.DeviceApp;
import com.thingple.tag.wrapper.R;
import com.thingple.tagservice.ReadCardListener;
import com.thingple.tagservice.device.DeviceCategory;
import com.thingple.tagservice.device.context.DeviceContext;

import java.util.concurrent.atomic.AtomicBoolean;

public class ReadCardActivity extends BaseActivity {

    private String defaultCategory = DeviceCategory.UHF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);

        DeviceApp app = this.getApp();
        doReadCard(app, getPower());
    }

    public void cancelInventory(View view) {
        Intent intent = new Intent();
        setResult(0, intent);
        finish();
    }

    private void stopInventory() {

        DeviceContext deviceContext = getDeviceContext(getApp());
        if (deviceContext != null) {
            deviceContext.inventoryStop();
        }
    }

    private ReadCardListener createListener() {

        return new ReadCardListener() {
            private AtomicBoolean flag = new AtomicBoolean(false);

            @Override
            public void onReadCard(String tid, String epc) {

                if (!flag.getAndSet(true)) {// 只返回一次
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("TID", tid);
                    bundle.putString("EPC", epc);
                    intent.putExtras(bundle);
                    setResult(9, intent);
                    Log.d(getClass().getName() + "#onReadCard", "返回数据并关闭界面");
                    finish();
                }
            }
        };
    }

    private void doReadCard(final DeviceApp app, final int power) {
        Bundle bundle = getIntent().getExtras();
        String filter = null;
        String category = null;
        int bank = -1;// <0无效
        int begin = -1;// <0无效
        int words = -1;// <0无效
        String password = null;
        if (bundle != null) {
            filter = bundle.getString("filter");
            filter = filter == null || filter.trim().equals("") ? null : filter;
            category = bundle.getString("category");

            // -读区域参数----------------------------------------
            bank = bundle.getInt("bank", -1);
            begin = bundle.getInt("begin", -1);
            words = bundle.getInt("words", -1);
            password = bundle.getString("password");
            // --------------------------------------------------
        }
        if (bank > -1) {
            if (begin < 0 || words <= 0 || filter == null) {// 条件不满足
                return;
            }
        }
        final String filterExp = filter;
        final int bankIndex = bank;
        final int beginIndex = begin;
        final int len = words;
        final String passwd = password;

        final Handler handler = new Handler();
        final String deviceCategory = category != null ? category : defaultCategory;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceContext deviceContext = getDeviceContext(app);
                if (deviceContext != null) {
                    TextView lableStatus = (TextView) findViewById(R.id.label_read_status);
                    lableStatus.setText(R.string.status_reading);
                    ReadCardListener listener = createListener();
                    if (bankIndex > -1) {// 读区域
                        deviceContext.readArea(listener, filterExp, bankIndex, beginIndex, len, passwd, power, deviceCategory.toLowerCase());
                    } else {// 查找卡
                        deviceContext.inventoryOnce(listener, filterExp, power, deviceCategory.toLowerCase());
                    }
                } else {
                    TextView lableStatus = (TextView) findViewById(R.id.label_read_status);
                    lableStatus.setText(R.string.status_initial);
                    handler.postDelayed(this, 500);
                }
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopInventory();
    }
}
