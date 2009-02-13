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
package com.bramosystems.gwt.player.client.ui;

import com.bramosystems.gwt.player.client.AbstractMediaPlayer;
import com.bramosystems.gwt.player.client.LoadException;
import com.bramosystems.gwt.player.client.MediaStateListener;
import com.bramosystems.gwt.player.client.PlayException;
import com.bramosystems.gwt.player.client.PlayerUtil;
import com.bramosystems.gwt.player.client.PluginNotFoundException;
import com.bramosystems.gwt.player.client.PluginVersion;
import com.bramosystems.gwt.player.client.PluginVersionException;
import com.bramosystems.gwt.player.client.impl.QuickTimePlayerImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Widget to embed QuickTime plugin.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new QuickTimePlayer("www.example.com/mediafile.mov");
 * } catch(LoadException e) {
 *      // catch loading exception and alert user
 *      Window.alert("An error occured while loading");
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
public class QuickTimePlayer extends AbstractMediaPlayer {

    private static QuickTimePlayerImpl impl;
    private String playerId, playerDivId, mediaUrl, height, width;
    private boolean autoplay;
    private SimplePanel playerDiv;

    QuickTimePlayer() {
        if (impl == null) {
            impl = GWT.create(QuickTimePlayerImpl.class);
        }
    }

    /**
     * Constructs <code>QuickTimePlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units. A value of {@code null}
     * for {@code height} or {@code width} renders the player invisible on the page.  This is
     * desired especially when used with custom controls.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * QuickTime plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the QuickTime plugin is not
     * installed on the client.
     */
    public QuickTimePlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginVersionException, PluginNotFoundException {
        this();

        PluginVersion v = PlayerUtil.getQuickTimePluginVersion();
        if (v.compareTo(7, 2, 1) < 0) {
            throw new PluginVersionException("7, 2, 1", v.toString());
        }

        playerId = DOM.createUniqueId().replace("-", "");
        mediaUrl = mediaURL;
        this.width = width;
        this.height = height;
        this.autoplay = autoplay;
        impl.init(playerId, new MediaStateListener() {

            public void onPlayFinished() {
                firePlayFinished();
            }

            public void onLoadingComplete() {
                fireLoadingComplete();
            }

            public void onError(String description) {
                fireError(description);
            }

            public void onDebug(String message) {
                fireDebug(message);
            }

            public void onLoadingProgress(double progress) {
                fireLoadingProgress(progress);
            }

            public void onPlayStarted() {
                firePlayStarted();
            }

            public void onPlayerReady() {
                firePlayerReady();
            }
        });

        playerDivId = playerId + "_div";
        playerDiv = new SimplePanel();
        playerDiv.getElement().setId(playerDivId);

        initWidget(playerDiv);
    }

    /**
     * Constructs <code>QuickTimePlayer</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 25px and width of 300px.
     *
     * <p> This is the same as calling {@code QuickTimePlayer(mediaURL, true, "25px", "300px")}
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * QuickTime plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the QuickTime plugin is not
     * installed on the client.
     *
     */
    public QuickTimePlayer(String mediaURL) throws LoadException, PluginVersionException,
            PluginNotFoundException {
        this(mediaURL, true, "25px", "300px");
    }

    /**
     * Constructs <code>QuickTimePlayer</code> to playback media located at {@code mediaURL}
     * using the default height of 25px and width of 300px. Media playback begins
     * automatically if {@code autoplay} is {@code true}.
     *
     * <p> This is the same as calling {@code QuickTimePlayer(mediaURL, autoplay, "25px", "300px")}
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * QuickTime plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the QuickTime plugin is not
     * installed on the client.
     */
    public QuickTimePlayer(String mediaURL, boolean autoplay) throws LoadException,
            PluginVersionException, PluginNotFoundException {
        this(mediaURL, autoplay, "25px", "300px");
    }

    /**
     * Overridden to register player for plugin DOM events
     *
     */
    @Override
    protected final void onLoad() {
       impl.injectScript(playerDivId, mediaUrl, playerId, autoplay, height, width);
    }

    /**
     * Subclasses that override this method should call <code>super.onUnload()</code>
     * to ensure the player is properly removed from the browser's DOM.
     *
    @Override
    protected void onUnload() {
//        playerDiv.setText("");
    }
     */

    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.loadSound(playerId, mediaURL);
    }

    public void playMedia() throws PlayException {
        checkAvailable();
        impl.playMedia(playerId);
    }

    public void stopMedia() {
        checkAvailable();
        impl.stop(playerId);
    }

    public void pauseMedia() {
        checkAvailable();
        impl.pause(playerId);
    }

    public void ejectMedia() {
        checkAvailable();
    }

    public void close() {
    }

    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getDuration(playerId);
    }

    public double getPlayPosition() {
        checkAvailable();
        return impl.getTime(playerId);
    }

    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setTime(playerId, position);
    }

    public double getVolume() {
        checkAvailable();
        return impl.getVolume(playerId) / 255.0;
    }

    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(playerId, (int) (volume * 255));
    }

    private void checkAvailable() {
        if(!impl.isPlayerAvailable(playerId))
            throw new IllegalStateException("Player closed already, create" +
                    " another instance.");
    }
}
