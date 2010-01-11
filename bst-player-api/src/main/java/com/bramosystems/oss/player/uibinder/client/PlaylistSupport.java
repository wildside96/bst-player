package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.google.gwt.uibinder.client.UiConstructor;
import java.util.ArrayList;

/**
 * 
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class PlaylistSupport extends PlayerWrapper {

    @UiConstructor
    public PlaylistSupport(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @Override
    protected AbstractMediaPlayer initPlayerEngine(String mediaURL, boolean autoplay,
            String height, String width) throws LoadException, PluginNotFoundException, PluginVersionException {
        ArrayList<String> _urls = new ArrayList<String>();
        if (mediaURL.contains(",")) {
            String[] murls = mediaURL.split(",");
            for (String url : murls) {
                _urls.add(url);
            }
        } else {
            _urls.add(mediaURL);
        }

        AbstractMediaPlayer mp = PlayerUtil.getPlayer(Plugin.PlaylistSupport, _urls.get(0),
                autoplay, height, width);
        for (int i = 1; i < _urls.size(); i++) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) mp).addToPlaylist(_urls.get(i));
        }
        return mp;
    }
}
