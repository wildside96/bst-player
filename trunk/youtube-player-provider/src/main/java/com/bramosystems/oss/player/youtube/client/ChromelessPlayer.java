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

import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider;
import com.google.gwt.core.client.GWT;

/**
 * Widget to embed the Chromeless YouTube video player. The player is
 * particularly useful when embedding YouTube with custom controls.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new ChromelessPlayer("VIDEO_ID", "100%", "350px");
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
 * @since 1.1
 */
@Player(name = "Chromeless", minPluginVersion = "10.1.0", providerFactory = YouTubePlayerProvider.class)
public class ChromelessPlayer extends YouTubePlayer {

    /**
     * Constructs <code>ChromelessPlayer</code> with the specified {@code height} and
     * {@code width} to playback video {@code videoID}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoID the ID of the video
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required Flash player plugin is not found
     * @throws PluginVersionException if the required Flash player version not found
     * @throws NullPointerException if either {@code vId}, {@code height} or {@code width} is null
     */
    public ChromelessPlayer(String videoID, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        super(videoID, width, height);
    }

    @Override
    protected String getNormalizedVideoAppURL(String videoId, PlayerParameters playerParameters) {
        playerParameters.setPlayerAPIId(playerId);
        playerParameters.setJSApiEnabled(true);
        playerParameters.setOrigin(GWT.getHostPageBaseURL());
        
        StringBuilder vurl = new StringBuilder("http://www.youtube.com/apiplayer?version=3");
        vurl.append("&video_id=").append(videoId).append("&");
        vurl.append(paramsToString(playerParameters));
        return vurl.toString();
    }

    @Override
    protected String getPlayerName() {
        return "Chromeless";
    }

    /**
     * Checks whether the player controls are visible. This implementation
     * <b>always</b> return false.
     */
    @Override
    public boolean isControllerVisible() {
        return false;
    }
}
