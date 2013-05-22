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

import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.core.client.spi.PlayerWidget;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerImpl;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider;
import com.google.gwt.user.client.ui.SimplePanel;
import java.util.Iterator;

/**
 * Widget to embed YouTubes' Flash video player
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new YouTubePlayer("VIDEO_ID", "100%", "350px");
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
@Player(name = "YouTube", minPluginVersion = "10.1.0", providerFactory = YouTubePlayerProvider.class)
public class YouTubePlayer extends YouTubeBasePlayer {

    /**
     * Constructs <code>YouTubePlayer</code> with the specified {@code height} and
     * {@code width} to playback video with {@code videoID}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoID the ID of the video
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required Flash player plugin is not found
     * @throws PluginVersionException if the required Flash plugin version is not found
     * @throws NullPointerException if either {@code videoID}, {@code height} or
     * {@code width} is null
     */
    public YouTubePlayer(String videoID, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        super(videoID, width, height, false);
        PluginVersion det = getProvider().getDetectedPluginVersion("YouTube");
        PluginVersion req = PluginVersion.get(10, 1, 0);
        if (det.compareTo(req) < 0) {
            throw new PluginVersionException(req.toString(), det.toString());
        }
        initWidget(new SimplePanel());

        // register for DOM events ...
        getProvider().initHandler(playerId, false, new DefaultEventHandler() {
            @Override
            public void onInit() {
                fireDebug("YouTube Player");
                impl = YouTubePlayerImpl.getPlayerImpl(playerId);
                impl.registerHandlers(playerId, getProvider().getHandlerPrefix());
//                ypm.commitPlaylist();
                onYTReady();
            }
        });
    }

    @Override
    protected void onLoad() {
        PlayerWidget pwidget = new PlayerWidget(YouTubePlayerProvider.PROVIDER_NAME, getPlayerName(), playerId,
                getNormalizedVideoAppURL(_vid, pParams), false);
        pwidget.addParam("allowScriptAccess", "always");
        pwidget.addParam("bgcolor", "#000000");
        pwidget.addParam("allowFullScreen", "true");
        
        Iterator<String> pm = configParam.keySet().iterator();
        while (pm.hasNext()) {
            String p = pm.next();
            pwidget.addParam(p, configParam.get(p));
        }
        ((SimplePanel) getWidget()).setWidget(pwidget);
        pwidget.setSize("100%", _height);
        setWidth(_width);
    }

    @Override
    protected void onUnload() {
        if (impl != null) {
            impl.stop();
            impl.clear();
        }
        getProvider().close(playerId);
    }

    /**
     * Returns the normalized URL of the video.
     *
     * <p>This method is called by the player Constructors. It adjusts the
     * parameters that may be present in the
     * <code>playerParameters</code> (possibly overriding some) to match the
     * requirements of this players' internals.
     *
     * @param videoID the ID of the YouTube&trade; video
     * @param playerParameters the parameters of the video
     * @return the normalized URL of the video
     */
    protected String getNormalizedVideoAppURL(String videoID, PlayerParameters playerParameters) {
        playerParameters.setJSApiEnabled(true);
        playerParameters.setPlayerAPIId(playerId);
        StringBuilder vurl = new StringBuilder("http://www.youtube.com/v/").append(videoID);
        vurl.append("?version=3&").append(paramsToString(playerParameters));
        return vurl.toString();
    }

    protected String getPlayerName() {
        return "YouTube";
    }
}
