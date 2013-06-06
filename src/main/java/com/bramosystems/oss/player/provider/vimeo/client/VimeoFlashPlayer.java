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
import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerAFImpl;
import com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Widget to embed Vimeo Flash Player.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new VimeoFlashPlayer("video-id", false, "100%", "350px");
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
@Player(name = "FlashPlayer", minPluginVersion = "10.0.0", providerFactory = VimeoPlayerProvider.class)
public class VimeoFlashPlayer extends AbstractMediaPlayer {

    private SWFWidget swf;
    private VimeoPlayerAFImpl impl;
    private VimeoPlayerProvider provider;
    private RepeatMode repeatMode;
    private String _width, _height;

    private VimeoFlashPlayer() throws PluginNotFoundException, PluginVersionException {
        provider = ((VimeoPlayerProvider) getWidgetFactory(VimeoPlayerProvider.PROVIDER_NAME));
        PluginVersion pv = provider.getDetectedPluginVersion(VimeoPlayerProvider.FLASH_PLAYER);
        if (pv.compareTo(PluginVersion.get(10, 0, 0)) < 0) {
            throw new PluginVersionException(provider.getDetectedPluginInfo(
                    VimeoPlayerProvider.FLASH_PLAYER).getPlugin(), "10.0.0", pv.toString());
        }
        repeatMode = RepeatMode.REPEAT_OFF;
        initWidget(new SimplePanel());
    }

    /**
     * Creates a VimeoFlashPlayer widget to playback the specified
     * <code>videoId</code>.
     *
     * @param videoId the identifier of the video
     * @param autoplay <code>true</code> to start playback automatically, <code>false</code> otherwise
     * @param width the width of the player widget, in CSS units
     * @param height the height of the player widget, in CSS units
     * @throws PluginNotFoundException if the required Adobe Flash player plugin in not installed on the browser
     * @throws PluginVersionException if the required Adobe Flash player plugin version is not installed
     */
    public VimeoFlashPlayer(String videoId, boolean autoplay, String width, String height) throws PluginNotFoundException, PluginVersionException {
        this();
        _width = width;
        _height = height;
        swf = new SWFWidget("http://vimeo.com/moogaloop.swf?server=vimeo.com&clip_id=" + videoId, width, height, PluginVersion.get(10, 0, 0));
        swf.addProperty("allowScriptAccess", "always");
        swf.addProperty("allowFullScreen", "true");
        swf.setFlashVar("autoplay", autoplay ? 1 : 0);
        swf.setFlashVar("api", 1);
        swf.setFlashVar("player_id", swf.getId());
        swf.setFlashVar("api_ready", provider.getInitFunctionRef(swf.getId()));

        provider.initHandlers(swf.getId(), new VimeoPlayerProvider.EventHandler() {
            @Override
            public void onMethod(String method, String retVal) {
            }

            @Override
            public void onMsg(String msg) {
            }

            @Override
            public void onInit() {
                fireDebug("Vimeo Flash Player");
                impl = VimeoPlayerAFImpl.getPlayerImpl(swf.getId());
                impl.registerHandlers(provider.getEvtFunctionBaseName(swf.getId()));
                firePlayerStateEvent(PlayerStateEvent.State.Ready);
            }

            @Override
            public void onLoadingProgress(double progress, double duration) {
                fireLoadingProgress(progress);
            }

            @Override
            public void onPlayingProgress(double seconds) {
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
            }
        });
    }

    @Override
    protected void onLoad() {
        swf.commitFlashVars();
        ((SimplePanel) getWidget()).setWidget(swf);
        setSize(_width, _height);
    }

    /**
     * This implementation does nothing. You will need to create another
     * instance of the class to load a new media
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
                if (isPlayerOnPage(swf.getId())) {
                    impl.setLoop(true);
                } else {
                    swf.setFlashVar("loop", 1);
                }
                repeatMode = mode;
                break;
            case REPEAT_OFF:
                if (isPlayerOnPage(swf.getId())) {
                    impl.setLoop(false);
                } else {
                    swf.setFlashVar("loop", 0);
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
    public <C extends ConfigParameter> void setConfigParameter(C param, Object value) {
        super.setConfigParameter(param, value);
        if (param instanceof DefaultConfigParameter) {
            switch (DefaultConfigParameter.valueOf(param.getName())) {
                case TransparencyMode:
                    swf.addProperty("wmode", ((TransparencyMode) value).name().toLowerCase());
                    break;
                case BackgroundColor:
                    swf.addProperty("bgcolor", (String) value);
            }
        } else if (param instanceof VimeoConfigParameters) {
            switch (VimeoConfigParameters.valueOf(param.getName())) {
                case ShowByline:
                    swf.setFlashVar("byline", (Boolean) value ? 1 : 0);
                    break;
                case ShowPortrait:
                    swf.setFlashVar("portrait", (Boolean) value ? 1 : 0);
                    break;
                case ShowTitle:
                    swf.setFlashVar("title", (Boolean) value ? 1 : 0);
                    break;
                case EnableFullscreen:
                    swf.setFlashVar("fullscreen", (Boolean) value ? 1 : 0);
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
}
