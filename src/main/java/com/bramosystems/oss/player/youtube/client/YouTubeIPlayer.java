/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.youtube.client;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.core.client.spi.PlayerWidget;
import com.bramosystems.oss.player.youtube.client.impl.YouTubeIPlayerImpl;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider;

/**
 * Widget to embed YouTube video player with the IFrame API
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new YouTubeIPlayer("VIDEO_ID", "100%", "350px");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user to download plugin first.
 *      // An option is to use the utility method in PlayerUtil class.
 *      player = PlayerUtil.getMissingPluginNotice(e.getPlugin());
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 2.0
 */
@Player(name = "IYouTube", minPluginVersion = "5.0.0", providerFactory = YouTubePlayerProvider.class)
public class YouTubeIPlayer extends YouTubeBasePlayer {

    /**
     * Constructs
     * <code>YouTubeIPlayer</code> with the specified {@code height} and
     * {@code width} to playback video with {@code videoID}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoID the ID of the video
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required player plugin is not found
     * @throws PluginVersionException if the required plugin version is not found
     * @throws NullPointerException if either {@code videoID}, {@code height} or
     * {@code width} is null
     */
    public YouTubeIPlayer(String videoID, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        super(videoID, width, height, true);
        PluginVersion det = getProvider().getDetectedPluginVersion("IYouTube");
        PluginVersion req = PluginVersion.get(5, 0, 0);
        if (det.compareTo(req) < 0) {
            throw new PluginVersionException(req.toString(), det.toString());
        }
        PlayerWidget pw = new PlayerWidget(YouTubePlayerProvider.PROVIDER_NAME, "IYouTube",
                playerId, "", false);
        pw.setSize("100%", _height);
        initWidget(pw);
        setSize(_width, _height);
        getProvider().initHandler(playerId, true, new DefaultEventHandler() {
            @Override
            public void onInit() {
            }
        });
    }

    @Override
    protected void onLoad() {
        if (getProvider().isIFrameAPIReady()) {
            fireDebug("YouTube Player");
            impl = YouTubeIPlayerImpl.getIPlayerImpl(playerId, _vid, pParams, pParams.getAutoHide().ordinal());
        } else {
            fireError("FATAL ERROR: YouTube IFrame API not ready or not loaded yet!");
            throw new IllegalStateException("YouTube IFrame API not ready or not loaded yet!");
        }

    }

    @Override
    protected void onUnload() {
        if (impl != null) {
            impl.stop();
            impl.clear();
        }
        getProvider().close(playerId);
    }
}
