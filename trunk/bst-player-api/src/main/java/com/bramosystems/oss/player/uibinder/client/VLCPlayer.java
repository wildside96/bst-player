package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.google.gwt.uibinder.client.UiConstructor;
import java.util.ArrayList;

/**
 * 
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class VLCPlayer extends PlayerWrapper<com.bramosystems.oss.player.core.client.ui.VLCPlayer> {

    @UiConstructor
    public VLCPlayer(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @Override
    protected com.bramosystems.oss.player.core.client.ui.VLCPlayer initPlayerEngine(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        ArrayList<String> _urls = new ArrayList<String>();
        if (mediaURL.contains(",")) {
            String[] murls = mediaURL.split(",");
            for (String url : murls) {
                _urls.add(url);
            }
        } else {
            _urls.add(mediaURL);
        }

        com.bramosystems.oss.player.core.client.ui.VLCPlayer mp =
                new com.bramosystems.oss.player.core.client.ui.VLCPlayer(_urls.get(0),
                autoplay, height, width);
        for (int i = 1; i < _urls.size(); i++) {
            mp.addToPlaylist(_urls.get(i));
        }
        return mp;
    }
}
