package com.thingple.tagservice.settings;

import android.content.Context;
import android.util.Log;

/**
 * 系统配置
 * Created by lism on 2017/8/4.
 */

public class PreferencesUtil extends PropertiesHandler {

    private static PreferencesUtil ins = null;

    /**
     * 初始化,在App#onCreate中调用
     * @param context app
     */
    public static void init(Context context) {
        ins = new PreferencesUtil(context);
    }

    public static PreferencesUtil shareInstance() {
        return ins;
    }

    private PreferencesUtil(Context context) {
        super(context);
    }

    public int getHightPower() {
        String value = preferences.getString("reader_hight_power", null);
        int power = 29;
        try {
            int valueInt = Integer.parseInt(value);
            power = valueInt;
            Log.d(getClass().getName() + "#getHightPower", valueInt + "");
        } catch (Exception e) {
            Log.d(getClass().getName() + "#getHightPower", "use default value");
        }
        return power;
    }

    public int getLowPower() {
        String value = preferences.getString("reader_low_power", null);
        int power = 5;
        try {
            int valueInt = Integer.parseInt(value);
            power = valueInt;
            Log.d(getClass().getName() + "#getLowPower", valueInt + "");
        } catch (Exception e) {
            Log.d(getClass().getName() + "#getLowPower", "use default value");
        }
        return power;
    }

}
