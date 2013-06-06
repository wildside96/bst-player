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
package com.bramosystems.oss.player.provider.vimeo.client;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.core.client.spi.PlayerWidget;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerIFImpl;
import com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider;
import com.google.gwt.user.client.DOM;

/**
 * Widget to embed Vimeo Universal Player.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new VimeoUniversalPlayer("video-id", false, "100%", "350px");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user to download plugin first.
 *      // An option is to use the utility method in PlayerUtil class.
 *      player = PlayerUtil.getMissingPluginNotice(e.getPlugin(), "Missing Plugin",
 *              ".. some nice message telling the user to click and download plugin first ..",
 *              false);
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @since 2.0
 * @author Sikiru Braheem
 */
@Player(name = VimeoPlayerProvider.UNIVERSAL_PLAYER, providerFactory = VimeoPlayerProvider.class, minPluginVersion = "5.0.0")
public class VimeoUniversalPlayer extends AbstractMediaPlayer {

    private PlayerWidget upf;
    private String playerId;
    private VimeoPlayerIFImpl impl;
    private VimeoPlayerProvider provider;
    private RepeatMode repeatMode;
    private double _duration, _playTime, _vol;
    private int _vidWidth, _vidHeight;

    private VimeoUniversalPlayer() throws PluginNotFoundException {
        if (!PlayerUtil.isHTML5CompliantClient()) {
            throw new PluginNotFoundException(Plugin.Native, "HTML5 Compliant browser not found !");
        }

        playerId = DOM.createUniqueId().replace("-", "");
        repeatMode = RepeatMode.REPEAT_OFF;

        provider = ((VimeoPlayerProvider) getWidgetFactory(VimeoPlayerProvider.PROVIDER_NAME));
        provider.initHandlers(playerId, new VimeoPlayerProvider.EventHandler() {
            @Override
            public void onInit() {
                fireDebug("Vimeo Universal Player");
                impl = VimeoPlayerIFImpl.getPlayerImpl(playerId);
                impl.registerHandlers(provider.getEvtFunctionBaseName(playerId));
                firePlayerStateEvent(PlayerStateEvent.State.Ready);
                impl.getVideoHeight();
                impl.getVideoWidth();
                impl.getVolume();
            }

            @Override
            public void onLoadingProgress(double progress, double duration) {
                _duration = duration * 1000;
                fireLoadingProgress(progress);
            }

            @Override
            public void onPlayingProgress(double seconds) {
                _playTime = seconds * 1000;
            }

            @Override
            public void onPlay() {
                firePlayStateEvent(State.Started, 1);
                fireDebug("Playback Started");
            }

            @Override
            public void onFinish() {
                firePlayStateEvent(State.Finished, 1);
                fireDebug("Playback Finished");
            }

            @Override
            public void onPause() {
                firePlayStateEvent(State.Paused, 1);
                fireDebug("Playback Paused");
            }

            @Override
            public void onSeek(double seconds) {
                _playTime = seconds;
            }

            @Override
            public void onMsg(String msg) {
                fireDebug("- " + msg);
            }

            @Override
            public void onMethod(String method, String retVal) {
                fireDebug("Method: " + method + ", Val: " + retVal);
                if (method.equalsIgnoreCase("getvideowidth")) {
                    _vidWidth = Integer.parseInt(retVal);
                } else if (method.equalsIgnoreCase("getvideoheight")) {
                    _vidHeight = Integer.parseInt(retVal);
                } else if (method.equalsIgnoreCase("getvolume")) {
                    _vol = Double.parseDouble(retVal);
                }
            }
        });
    }

    /**
     * Creates a VimeoUniversalPlayer widget to playback the specified
     * <code>videoId</code>.
     *
     * @param videoId the identifier of the video
     * @param autoplay <code>true</code> to start playback
     * automatically, <code>false</code> otherwise
     * @param width the width of the player widget, in CSS units
     * @param height the height of the player widget, in CSS units
     * @throws PluginNotFoundException if the client browser is not HTML5 compliant
     * @throws PluginVersionException not exactly thrown. Provided for Base API compatibility
     */
    public VimeoUniversalPlayer(String videoId, boolean autoplay, String width, String height) throws PluginNotFoundException, PluginVersionException {
        this();
        upf = new PlayerWidget(VimeoPlayerProvider.PROVIDER_NAME, VimeoPlayerProvider.UNIVERSAL_PLAYER, playerId, videoId, autoplay);
        upf.addParam("autoplay", autoplay ? "1" : "0");
        upf.addParam("api", "1");
        upf.setSize(width, height);
        initWidget(upf);
    }

    /**
     * This implementation does nothing.  You will need to create another instance of the class to load a new media
     */
    @Override
    public void loadMedia(String mediaURL) throws LoadException {
    }

    @Override
    public void playMedia() throws PlayException {
        checkAvailable();
        impl.play();
    }

    @Override
    public void stopMedia() {
        checkAvailable();
        impl.pause();
        impl.seekTo(0);
    }

    @Override
    public void pauseMedia() {
        checkAvailable();
        impl.pause();
    }

    @Override
    public long getMediaDuration() {
        checkAvailable();
        return (long) _duration;
    }

    @Override
    public double getPlayPosition() {
        checkAvailable();
        return _playTime;
    }

    @Override
    public void setPlayPosition(double position) {
        checkAvailable();
        impl.seekTo(position / 1000);
    }

    @Override
    public double getVolume() {
        checkAvailable();
        return _vol;
    }

    @Override
    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(volume);
    }

    @Override
    public void setRepeatMode(RepeatMode mode) {
        switch (mode) {
            case REPEAT_ALL:
                if (isPlayerOnPage(playerId)) {
                    impl.setLoop(true);
                } else {
                    upf.addParam("loop", "1");
                }
                repeatMode = mode;
                break;
            case REPEAT_OFF:
                if (isPlayerOnPage(playerId)) {
                    impl.setLoop(false);
                } else {
                    upf.addParam("loop", "0");
                }
                repeatMode = mode;
        }
    }

    @Override
    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    @Override
    public int getVideoHeight() {
        checkAvailable();
        return _vidHeight;
    }

    @Override
    public int getVideoWidth() {
        checkAvailable();
        return _vidWidth;
    }

    /**
     * Returns the visibility state of the player controls.
     * 
     * <p>This implementation ALWAYS return <code>true</code>
     * 
     * @return <code>true</code>.  Controller is always visible
     */
    @Override
    public boolean isControllerVisible() {
        return true;
    }

    @Override
    public <C extends ConfigParameter> void setConfigParameter(C param, Object value) {
        super.setConfigParameter(param, value);
        if (param instanceof VimeoConfigParameters) {
            switch (VimeoConfigParameters.valueOf(param.getName())) {
                case ShowByline:
                    upf.addParam("byline", (Boolean) value ? "1" : "0");
                    break;
                case ShowPortrait:
                    upf.addParam("portrait", (Boolean) value ? "1" : "0");
                    break;
                case ShowTitle:
                    upf.addParam("title", (Boolean) value ? "1" : "0");
                    break;
                case EnableFullscreen:
                    upf.addParam("fullscreen", (Boolean) value ? "1" : "0");
                    break;
                case Color:
                    upf.addParam("color", (String) value);
                    break;
            }
        }
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }
}
