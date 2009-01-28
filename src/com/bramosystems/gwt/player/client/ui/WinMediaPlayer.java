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

import com.bramosystems.gwt.player.client.*;
import com.bramosystems.gwt.player.client.impl.WinMediaPlayerImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;

/**
 * Widget to embed Windows Media Player.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new WinMediaPlayer("www.example.com/mediafile.wma");
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
public class WinMediaPlayer extends AbstractMediaPlayer {

    private static WinMediaPlayerImpl impl = GWT.create(WinMediaPlayerImpl.class);
    private String playerId;
    private HTML playerDiv;

    /**
     * Constructs <code>WinMediaPlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Windows Media Player plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Windows Media
     * Player plugin is not installed on the client.
     */
    public WinMediaPlayer(String mediaURL, boolean autoplay, String height, String width) 
            throws LoadException, PluginNotFoundException, PluginVersionException {
        PluginVersion v = PlayerUtil.getFlashPlayerVersion();
        if (v.compareTo(1, 1, 1) < 0) {
            throw new PluginVersionException("1, 1, 1", v.toString());
        }

        playerId = DOM.createUniqueId();
        impl.init(playerId);
        playerDiv = new HTML(impl.getPlayerScript(mediaURL, playerId, autoplay, height, width));
        playerDiv.setStyleName("");
        playerDiv.setSize("100%", "100%");
        playerDiv.setHorizontalAlignment(HTML.ALIGN_CENTER);
        initWidget(playerDiv);
    }

    /**
     * Constructs <code>WinMediaPlayer</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 50px and width of 100%.
     *
     * <p> This is the same as calling {@code WinMediaPlayer(mediaURL, true, "50", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Windows Media Player plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Windows Media
     * Player plugin is not installed on the client.
     */
    public WinMediaPlayer(String mediaURL) throws LoadException,
            PluginNotFoundException, PluginVersionException {
        this(mediaURL, true, "50", "100%");
    }

    /**
     * Constructs <code>WinMediaPlayer</code> to playback media located at {@code mediaURL} using
     * the default height of 50px and width of 100%. Media playback begins automatically if
     * {@code autoplay} is {@code true}.
     *
     * <p> This is the same as calling {@code WinMediaPlayer(mediaURL, autoplay, "50", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * Windows Media Player plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the Windows Media
     * Player plugin is not installed on the client.
     */
    public WinMediaPlayer(String mediaURL, boolean autoplay) throws LoadException,
            PluginNotFoundException, PluginVersionException {
        this(mediaURL, autoplay, "50", "100%");
    }

    @Override
    protected void onLoad() {
        impl.setMediaStateListener(playerId, new MediaStateListener() {

            public void onPlayStarted() {
                firePlayStarted();
            }

            public void onPlayFinished() {
                firePlayFinished();
            }

            public void onLoadingComplete() {
                fireLoadingComplete();
            }

            public void onLoadingProgress(double progress) {
                fireLoadingProgress(progress);
            }

            public void onIOError() {
                fireIOError();
            }

            public void onDebug(String message) {
                fireDebug(message);
            }
        });
    }


    /**
     * Subclasses that override this method should call <code>super.onUnload()</code>
     * to ensure the player is properly removed from the browser's DOM.
     */
    @Override
    protected void onUnload() {
        impl.close(playerId);
        playerDiv.setText("");
    }

    public void loadMedia(String mediaURL) throws LoadException {
        impl.loadSound(playerId, mediaURL);
    }

    public void playMedia() throws PlayException {
        impl.play(playerId);
    }

    public void stopMedia() {
        impl.stop(playerId);
    }

    public void pauseMedia() {
        impl.pause(playerId);
    }

    public void ejectMedia() {
    }

    public void close() {
        impl.close(playerId);
    }

    public long getMediaDuration() {
        return (long)impl.getDuration(playerId);
    }

    public double getPlayPosition() {
        return impl.getCurrentPosition(playerId);
    }

    public void setPlayPosition(double position) {
        impl.setCurrentPosition(playerId, position);
    }

    public double getVolume() {
        return impl.getVolume(playerId) / (double)100;
    }

    public void setVolume(double volume) {
        impl.setVolume(playerId, (int)(volume * 100));
    }

}
