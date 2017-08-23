package com.example.ahmedwahdan.flicker_photo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ahmedwahdan on 8/22/17.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }

}
