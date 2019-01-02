package com.vittach.jumpjack.android;

import android.os.Bundle;
import com.vittach.jumpjack.JJEngine;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savingInstanceState) {
        super.onCreate(savingInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        initialize(new JJEngine(1), config);
    }
}
