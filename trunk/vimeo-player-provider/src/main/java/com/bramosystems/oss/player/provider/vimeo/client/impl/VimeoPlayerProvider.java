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
package com.bramosystems.oss.player.provider.vimeo.client.impl;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.spi.ConfigurationContext;
import com.bramosystems.oss.player.core.client.spi.PlayerElement;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory;
import com.bramosystems.oss.player.provider.vimeo.client.VimeoFlashPlayer;
import com.google.gwt.core.client.JavaScriptObject;
import java.util.HashMap;

/**
 *
 * @author sbraheem
 */
@PlayerProvider(VimeoPlayerProvider.PROVIDER_NAME)
public class VimeoPlayerProvider implements PlayerProviderFactory {

    public static final String PROVIDER_NAME = "bst.vimeo";
    private ConfigurationContext context;

    @Override
    public void init(ConfigurationContext context) {
        this.context = context;
    }

    @Override
    public PlayerElement getPlayerElement(String playerName, String playerId, String mediaURL, boolean autoplay, HashMap<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException {
        if(playerName.equals("VimeoFlashPlayer")) {
            return PlayerUtil.getFlashPlayerVersion();
        } else {
            throw new IllegalArgumentException("Player '" + playerName + "' not known to this provider");
        }
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        if(playerName.equals("VimeoFlashPlayer")) {
            return new VimeoFlashPlayer(mediaURL, autoplay, width, height);
        } else {
            throw new IllegalArgumentException("Player '" + playerName + "' not known to this provider");
        }
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay) throws LoadException, PluginNotFoundException, PluginVersionException {
        if(playerName.equals("VimeoFlashPlayer")) {
            return new VimeoFlashPlayer(mediaURL, autoplay, "400px", "300px");
        } else {
            throw new IllegalArgumentException("Player '" + playerName + "' not known to this provider");
        }
    }

    /************************* Native Utils ***************************************/
    public void initHandlers(String playerId, EventHandler handler) {
        initHandlersImpl(playerId, context.getGlobalJSStack(), handler);
    }
    
    public void closeHandlers(String playerId) {
        closeImpl(playerId, context.getGlobalJSStack());
    }

    public String getInitFunctionRef(String playerId) {
        return context.getGlobalJSStackName() + "." + playerId + ".onInit";
    }

    public String getEvtFunctionBaseName(String playerId) {
        return context.getGlobalJSStackName() + "." + playerId;
    }

    private native void closeImpl(String playerId, JavaScriptObject vim) /*-{
    delete vim[playerId];
    }-*/;

    private native void initHandlersImpl(String playerId, JavaScriptObject vim, EventHandler handler) /*-{
    vim[playerId] = new Object();
    vim[playerId].onInit = function(evt){
    handler.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onInit()();
    }
    vim[playerId].onLoadProgress = function(evt){
    handler.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onLoadingPrgress(D)(evt.percent);
    }
    vim[playerId].onPlayProgress = function(evt){
    }
    vim[playerId].onPlay = function(evt){
    handler.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onPlay()();
    }
    vim[playerId].onPause = function(evt){
    handler.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onPause()();
    }
    vim[playerId].onFinish = function(evt){
    handler.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onFinish()();
    }
    vim[playerId].onSeek = function(evt){
    handler.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onSeek()();
    }
    }-*/;

    /*************************** Event Handler Interface *********************************/
    public static interface EventHandler {

        public void onInit();

        public void onLoadingPrgress(double progress);

        public void onPlayingProgress();

        public void onPlay();

        public void onFinish();

        public void onPause();

        public void onSeek();
    }
}
