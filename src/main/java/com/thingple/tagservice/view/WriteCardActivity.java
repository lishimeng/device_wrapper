package com.thingple.tagservice.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thingple.tag.operator.DeviceApp;
import com.thingple.tag.wrapper.R;
import com.thingple.tagservice.WriteCardListener;
import com.thingple.tagservice.device.DeviceCategory;
import com.thingple.tagservice.device.context.DeviceContext;

import java.util.concurrent.atomic.AtomicBoolean;


public class WriteCardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_card);

        DeviceApp app = this.getApp();
        doWriteCard(app, getPower());
    }

    public void cancelOperate(View view) {
        resultToParent(9, "canceled", "");
    }

    private void resultToParent(int code, String message, String tid) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("code", "" + code);
        bundle.putString("message", message);
        bundle.putString("tid", tid);
        intent.putExtras(bundle);
        setResult(0, intent);
        finish();
    }

    private WriteCardListener createListener() {

        return new WriteCardListener() {
            private AtomicBoolean flag = new AtomicBoolean(false);

            @Override
            public void afterWriteCard(int code, String message, String tid) {

                if (!flag.getAndSet(true)) {// 只返回一次
                    Log.d("#WriteCard", "返回数据并关闭界面");
                    resultToParent(code, message, tid);
                }
            }
        };
    }

    private void doWriteCard(final DeviceApp app, final int power) {
        Bundle bundle = getIntent().getExtras();
        String epcForSelect = null;
        String epcForWrite = null;
        String password = null;
        if (bundle != null) {
            epcForSelect = bundle.getString("filter");
            epcForWrite = bundle.getString("epc");
            password = bundle.getString("password");
        }

        if (epcForSelect == null
                || epcForSelect.trim().equals("")
                || epcForWrite == null
                || epcForWrite.trim().equals("")) {
            resultToParent(-1, "缺少参数", "");
        }
        final String filter = epcForSelect;
        final String data = epcForWrite;
        final String passwd = password;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceContext deviceContext = getDeviceContext(app);
                if (deviceContext != null) {
                    TextView lableStatus = (TextView) findViewById(R.id.label_write_status);
                    lableStatus.setText(R.string.status_writing);
                    WriteCardListener listener = createListener();
                    deviceContext.writeCard(listener, filter, data, passwd, power, DeviceCategory.UHF);
                } else {
                    TextView lableStatus = (TextView) findViewById(R.id.label_write_status);
                    lableStatus.setText(R.string.status_initial);
                    handler.postDelayed(this, 500);
                }
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
