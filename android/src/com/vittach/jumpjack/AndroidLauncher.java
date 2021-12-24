package com.vittach.jumpjack;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.vittach.jumpjack.engine.MainEngine;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savingInstanceState) {
        super.onCreate(savingInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        initialize(com.vittach.jumpjack.engine.MainEngine.getInstance(MainEngine.Device.ANDROID), config);
    }
}
