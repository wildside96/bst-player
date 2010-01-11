package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.google.gwt.uibinder.client.UiConstructor;

/**
 * 
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class QuickTimePlayer extends PlayerWrapper<com.bramosystems.oss.player.core.client.ui.QuickTimePlayer> {

    @UiConstructor
    public QuickTimePlayer(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @Override
    protected com.bramosystems.oss.player.core.client.ui.QuickTimePlayer
            initPlayerEngine(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        return new com.bramosystems.oss.player.core.client.ui.QuickTimePlayer(mediaURL, autoplay, height, width);
    }

 }
