/*
 * Copyright 2012 sbraheem.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bramosystems.oss.player.provider.samples.client.impl;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.spi.ConfigurationContext;
import com.bramosystems.oss.player.core.client.spi.PlayerElement;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory;
import com.bramosystems.oss.player.provider.samples.client.FlatVideoPlayer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sbraheem
 */
@PlayerProvider("bst.sample.flat")
public class FlatPlayerProvider implements PlayerProviderFactory {

    @Override
    public PlayerElement getPlayerElement(String playerName, String playerId, String mediaURL, boolean autoplay, HashMap<String, String> params) {
        //       if(playerName.equals("Capsule"))
        return null;
    }

    @Override
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException {
        if (playerName.equals("FlatVideoPlayer")) {
            return PluginVersion.get(1, 0, 0);
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay, String height, String width)
            throws PluginNotFoundException, PluginVersionException {
        if (playerName.equals("FlatVideoPlayer")) {
            try {
                return new FlatVideoPlayer(mediaURL, autoplay, height, width);
            } catch (LoadException ex) {
                throw new IllegalStateException(ex);
            }
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay) throws PluginNotFoundException, PluginVersionException {
        if (playerName.equals("FlatVideoPlayer")) {
            try {
                return new FlatVideoPlayer(mediaURL, autoplay, "300px", "100%");
            } catch (LoadException ex) {
                throw new IllegalStateException(ex);
            }
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }

    @Override
    public void init(ConfigurationContext context) {
    }

    @Override
    public PluginInfo getDetectedPluginInfo(String playerName) throws PluginNotFoundException {
        PluginInfo pi = new PluginInfo(Plugin.Auto, null, PluginInfo.PlayerPluginWrapperType.Native);
        return pi;
    }

    @Override
    public Set<String> getPermittedMimeTypes(String playerName, PluginVersion version) {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getPermittedMediaProtocols(String playerName, PluginVersion version) {
        return new HashSet<String>();
    }
}
