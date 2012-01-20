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

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.ConfigParameter;
import com.bramosystems.oss.player.core.client.DefaultConfigParameter;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.RepeatMode;
import com.bramosystems.oss.player.core.client.TransparencyMode;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;
import com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerImpl;
import com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider;
import com.google.gwt.core.client.GWT;
import java.util.HashMap;

/**
 *
 * @author sbraheem
 */
@Player(name = "VimeoFlashPlayer", minPluginVersion = "10.0.0", providerFactory = VimeoPlayerProvider.class)
public class VimeoFlashPlayer extends AbstractMediaPlayer {

    private SWFWidget swf;
    private VimeoPlayerImpl impl;
    private VimeoPlayerProvider provider;

    public VimeoFlashPlayer() {
        provider = ((VimeoPlayerProvider) getWidgetFactory(VimeoPlayerProvider.PROVIDER_NAME));
    }

    public VimeoFlashPlayer(String clipId, boolean autoplay, String width, String height) throws PluginNotFoundException, PluginVersionException {
        this();
        swf = new SWFWidget("http://vimeo.com/moogaloop.swf?server=vimeo.com&clip_id=" + clipId, width, height, PluginVersion.get(10, 0, 0));
        swf.addProperty("allowScriptAccess", "always");
        swf.addProperty("allowFullScreen", "true");

        VimeoPlayerProvider.EventHandler evt = new VimeoPlayerProvider.EventHandler() {

            @Override
            public void onInit() {
                String funct = provider.getEvtFunctionBaseName(swf.getId());
                impl = VimeoPlayerImpl.getPlayerImpl(swf.getId());
                impl.registerHandlers(funct);
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
        };
        provider.initHandlers(swf.getId(), evt);

        StringBuilder sb = new StringBuilder();
        sb.append("autoplay=").append(autoplay ? 1 : 0).append("&");
        sb.append("api=1&");
        sb.append("byline=0&");
        sb.append("portrait=0&");
        sb.append("title=0&");
        sb.append("player_id=").append(swf.getId()).append("&");
        sb.append("api_ready=").append(provider.getInitFunctionRef(swf.getId())).append("&");
        sb.append("fullscreen=0");
        swf.addProperty("flashVars", sb.toString());

        /*
        addFlashVar("autoplay", autoplay ? 1 : 0);
        addFlashVar("api", 1);
        addFlashVar("player_id", swf.getId());
        addFlashVar("api_ready", provider.getInitFunctionRef(swf.getId()));
        addFlashVar("fullscreen", 1);
        addFlashVar("clip_id", clipId);
        addFlashVar("server", "vimeo.com");
         */

        initWidget(swf);
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        if (isPlayerOnPage(swf.getId())) {
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
    public RepeatMode getRepeatMode() {
        return super.getRepeatMode();
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
    public <T extends Object> void setConfigParameter(ConfigParameter param, T value) {
        super.setConfigParameter(param, value);
        if (param instanceof DefaultConfigParameter) {
            switch (DefaultConfigParameter.valueOf(param.getName())) {
                case TransparencyMode:
                    swf.addProperty("wmode", ((TransparencyMode) value).name().toLowerCase());
            }
        } else if (param instanceof VimeoConfigParameters) {
            switch (VimeoConfigParameters.valueOf(param.getName())) {
                case ShowByline:
                    addFlashVar("byline", (Boolean) value ? 1 : 0);
                    break;
                case ShowPortrait:
                    addFlashVar("portrait", (Boolean) value ? 1 : 0);
                    break;
                case ShowTitle:
                    addFlashVar("title", (Boolean) value ? 1 : 0);
                    break;
            }
        }
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(swf.getId())) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }
    private HashMap<String, String> flashVar = new HashMap<String, String>();

    private void addFlashVar(String name, int value) {
        flashVar.put(name, Integer.toString(value));
    }

    private void addFlashVar(String name, String value) {
        flashVar.put(name, value);
    }
}
