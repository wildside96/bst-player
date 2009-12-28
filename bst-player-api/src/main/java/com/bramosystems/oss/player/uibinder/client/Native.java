package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.google.gwt.uibinder.client.UiConstructor;


public class Native extends BinderPlayer<NativePlayer> {

    @UiConstructor
    public Native(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @Override
    protected Plugin getPlugin() {
        return Plugin.Native;
    }
}
