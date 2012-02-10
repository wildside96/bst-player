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
 *
 * @author sbraheem
 */
@Player(name = VimeoPlayerProvider.UNIVERSAL_PLAYER, providerFactory = VimeoPlayerProvider.class, minPluginVersion = "5.0.0")
public class VimeoUniversalPlayer extends AbstractMediaPlayer {

    private PlayerWidget upf;
    private String playerId;
    private VimeoPlayerIFImpl impl;
    private VimeoPlayerProvider provider;
    private RepeatMode repeatMode;

    private VimeoUniversalPlayer() {
        playerId = DOM.createUniqueId().replace("-", "");
        repeatMode = RepeatMode.REPEAT_OFF;

        provider = ((VimeoPlayerProvider) getWidgetFactory(VimeoPlayerProvider.PROVIDER_NAME));
        provider.initHandlers(playerId, new VimeoPlayerProvider.EventHandler() {

            @Override
            public void onInit() {
                impl = VimeoPlayerIFImpl.getPlayerImpl(playerId);
                impl.registerHandlers(provider.getEvtFunctionBaseName(playerId));
                firePlayerStateEvent(PlayerStateEvent.State.Ready);
            }

            @Override
            public void onLoadingPrgress(double progress) {
                fireLoadingProgress(progress);
            }

            @Override
            public void onPlayingProgress() {
            }

            @Override
            public void onPlay() {
                firePlayStateEvent(State.Started, 1);
            }

            @Override
            public void onFinish() {
                firePlayStateEvent(State.Finished, 1);
            }

            @Override
            public void onPause() {
                firePlayStateEvent(State.Paused, 1);
            }

            @Override
            public void onSeek() {
            }

            @Override
            public void onMsg(String msg) {
                fireDebug(playerId + "- " + msg);
            }
        });
    }

    public VimeoUniversalPlayer(String clipId, boolean autoplay, String width, String height) throws PluginNotFoundException, PluginVersionException {
        this();
        upf = new PlayerWidget(VimeoPlayerProvider.PROVIDER_NAME, VimeoPlayerProvider.UNIVERSAL_PLAYER, playerId, clipId, autoplay);
        upf.addParam("autoplay", autoplay ? "1" : "0");
        upf.addParam("api", "1");
        upf.setSize(width, height);
        initWidget(upf);
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        if (isPlayerOnPage(playerId)) {
//            impl.
        }
    }

    @Override
    public void playMedia() throws PlayException {
        checkAvailable();
        impl.play();
    }

    @Override
    public void stopMedia() {
        checkAvailable();
        impl.stop();
    }

    @Override
    public void pauseMedia() {
        checkAvailable();
        impl.pause();
    }

    @Override
    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getDuration();
    }

    @Override
    public double getPlayPosition() {
        checkAvailable();
        return impl.getCurrentTime();
    }

    @Override
    public void setPlayPosition(double position) {
        checkAvailable();
        impl.seekTo(position);
    }

    @Override
    public double getVolume() {
        checkAvailable();
        return impl.getVolume();
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
        return impl.getVideoHeight();
    }

    @Override
    public int getVideoWidth() {
        checkAvailable();
        return impl.getVideoWidth();
    }

    @Override
    public boolean isControllerVisible() {
        return true;
    }

    @Override
    public <T> void setConfigParameter(ConfigParameter param, T value) {
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
