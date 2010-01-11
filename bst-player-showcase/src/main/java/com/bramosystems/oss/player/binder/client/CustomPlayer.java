/*
 *  Copyright 2010 Sikiru Braheem <sbraheem at bramosystems . com>.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.bramosystems.oss.player.binder.client;

import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.uibinder.client.PlayerWrapper;
import com.google.gwt.uibinder.client.UiConstructor;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class CustomPlayer extends PlayerWrapper {

    @UiConstructor
    public CustomPlayer(String name, String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, name + "_" + height, width);
    }

    @Override
    protected AbstractMediaPlayer initPlayerEngine(String mediaURL, boolean autoplay, String height,
            String width) throws LoadException, PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer mp = null;
        String[] w = height.split("_");
        height = w[1];

        switch (Links.valueOf(w[0])) {
            case dynAuto:
                mp = new Capsule(Plugin.Auto, mediaURL, autoplay);
                mp.showLogger(true);
                break;
            case dynNtv:
                mp = new Capsule(Plugin.Native, mediaURL, autoplay);
                break;
            case dynQt:
                mp = new Capsule(Plugin.QuickTimePlayer, mediaURL, autoplay);
                break;
            case dynSwf:
                mp = new Capsule(Plugin.FlashPlayer, mediaURL, autoplay);
                break;
            case dynVlc:
                mp = new Capsule(Plugin.VLCPlayer, mediaURL, autoplay);
                break;
            case dynWmp:
                mp = new Capsule(Plugin.WinMediaPlayer, mediaURL, autoplay);
                break;
            case dynvdAuto:
                mp = new FlatVideoPlayer(Plugin.Auto, mediaURL, autoplay, height, width);
                break;
            case dynvdNtv:
                mp = new FlatVideoPlayer(Plugin.Native, mediaURL, autoplay, height, width);
                break;
            case dynvdQt:
                mp = new FlatVideoPlayer(Plugin.QuickTimePlayer, mediaURL, autoplay, height, width);
                break;
            case dynvdSwf:
                mp = new FlatVideoPlayer(Plugin.FlashPlayer, mediaURL, autoplay, height, width);
                break;
            case dynvdVlc:
                mp = new FlatVideoPlayer(Plugin.VLCPlayer, mediaURL, autoplay, height, width);
                break;
            case dynvdWmp:
                mp = new FlatVideoPlayer(Plugin.WinMediaPlayer, mediaURL, autoplay, height, width);
                break;
        }
        return mp;
    }
}
