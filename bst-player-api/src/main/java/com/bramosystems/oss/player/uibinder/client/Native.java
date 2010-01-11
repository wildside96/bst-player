package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.google.gwt.uibinder.client.UiConstructor;
import java.util.ArrayList;

public class Native extends PlayerWrapper<NativePlayer> {

    @UiConstructor
    public Native(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @Override
    protected NativePlayer initPlayerEngine(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        if (mediaURL.contains(",")) {
            String[] murls = mediaURL.split(",");
            ArrayList<String> _urls = new ArrayList<String>();
            for (String url : murls) {
                _urls.add(url);
            }
            return new NativePlayer(_urls, autoplay, height, width);
        } else {
            return new NativePlayer(mediaURL, autoplay, height, width);
        }
    }
}
