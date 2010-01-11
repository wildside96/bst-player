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
package com.bramosystems.oss.player.uibinder.client.youtube;

import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.uibinder.client.PlayerWrapper;
import com.bramosystems.oss.player.youtube.client.ChromelessPlayer;
import com.google.gwt.uibinder.client.UiConstructor;

/**
 * Widget to embed the chromeless YouTube video player.  The player is particularly useful
 * when embedding YouTube with custom controls.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new Chromeless("http://www.youtube.com/v/VIDEO_ID&fs=1", "100%", "350px");
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
public class Chromeless extends PlayerWrapper<ChromelessPlayer> {
    private String videoURL;

    /**
     * Constructs <code>Chromeless</code> with the specified {@code height} and
     * {@code width} to playback video located at {@code videoURL}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoURL the URL of the video
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required Flash player plugin is not found
     * @throws PluginVersionException if Flash player version 8 and above is not found
     * @throws NullPointerException if either {@code videoURL}, {@code height} or {@code width} is null
     */
    @UiConstructor
    public Chromeless(String videoURL, String width, String height) {
        super(videoURL, false, height, width);
    }

    @Override
    protected ChromelessPlayer initPlayerEngine(String mediaURL, boolean autoplay, String height, String width)
            throws PluginVersionException, PluginNotFoundException {
        return new ChromelessPlayer(mediaURL, width, height);
    }

}
