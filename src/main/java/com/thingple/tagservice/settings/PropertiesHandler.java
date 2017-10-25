package com.thingple.tagservice.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置
 * Created by lism on 2017/8/4.
 */

class PropertiesHandler {

    SharedPreferences preferences = null;

    PropertiesHandler(Context context) {

        preferences = context.getSharedPreferences("thingple.com", Context.MODE_PRIVATE);
    }

}
