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
package com.bramosystems.oss.player.provider.sample.client;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.bramosystems.oss.player.core.client.skin.CustomVideoPlayer;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.core.client.ui.Logger;
import com.bramosystems.oss.player.provider.sample.client.impl.SamplePlayersProvider;


/**
 * Custom video player implementation using CustomPlayerControl
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new FlatVideoPlayer("www.example.com/videofile.mp4", false, "150px", "100%");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user, possibly providing a link
 *      // to the plugin download page.
 *      player = new HTML(".. some nice message telling the user to download plugin first ..");
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @author Sikirulai Braheem
 */
@Player(name="FlatVideoPlayer", providerFactory=SamplePlayersProvider.class, minPluginVersion="1.0.0")
public class FlatVideoPlayer extends CustomVideoPlayer {

    private Logger logger;

    /**
     * Constructs <code>FlatVideoPlayer</code> player with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL} using the specified
     * media plugin. Media playback begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units. A minimum value of 20px should
     * be specified for {@code height} to enable a proper display of the controls.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws PluginVersionException if the required player plugin version is not installed on the client.
     * @throws PluginNotFoundException if the player plugin is not installed on the client.
     * @throws NullPointerException if {@code height} or {@code width} is {@code null}
     */
    public FlatVideoPlayer(Plugin plugin, String mediaURL, boolean autoplay, String height, String width)
            throws PluginNotFoundException, PluginVersionException, LoadException { // TODO: remove LoadEx on API 2.0.3 release ...
        super(plugin, mediaURL, autoplay, height, "100%");

        logger = new Logger();
        logger.setVisible(false);

        setPlayerControlWidget(new CustomPlayerControl(this));
        setWidth(width);
    }

    /**
     * Constructs <code>FlatVideoPlayer</code> player with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL} using a dynamically
     * determined plugin. Media playback begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> This is the same as calling {@code FlatVideoPlayer(Plugin.Auto, mediaURL,
     * autoplay, height, width)}
     *
     * <p> {@code height} and {@code width} are specified as CSS units. A minimum value of 20px should
     * be specified for {@code height} to enable a proper display of the controls. 
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws PluginVersionException if the required player plugin version is not installed on the client.
     * @throws PluginNotFoundException if the player plugin is not installed on the client.
     * @throws NullPointerException if {@code height} or {@code width} is {@code null}
     *
     */
    public FlatVideoPlayer(String mediaURL, boolean autoplay, String height, String width)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        this(Plugin.Auto, mediaURL, autoplay, height, width);
    }

    @Override
    protected void onVideoDimensionChanged(int width, int height) {
        setSize(width + "px", (height + 20) + "px");
    }
}
