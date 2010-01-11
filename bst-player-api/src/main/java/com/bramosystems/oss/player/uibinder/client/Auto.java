package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.google.gwt.uibinder.client.UiConstructor;

/**
 * 
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class Auto extends PlayerWrapper {

    @UiConstructor
    public Auto(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @Override
    protected AbstractMediaPlayer initPlayerEngine(String mediaURL, boolean autoplay, String height, String width) throws LoadException, PluginNotFoundException, PluginVersionException {
        return PlayerUtil.getPlayer(mediaURL, autoplay, height, width);
    }
}
