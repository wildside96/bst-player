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

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.spi.ConfigurationContext;
import com.bramosystems.oss.player.core.client.spi.PlayerElement;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory;
import com.bramosystems.oss.player.provider.vimeo.client.VimeoFlashPlayer;
import com.bramosystems.oss.player.provider.vimeo.client.VimeoUniversalPlayer;
import com.google.gwt.core.client.JavaScriptObject;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author sbraheem
 */
@PlayerProvider(VimeoPlayerProvider.PROVIDER_NAME)
public class VimeoPlayerProvider implements PlayerProviderFactory {

    public static final String PROVIDER_NAME = "bst.vimeo",
            UNIVERSAL_PLAYER = "UniversalPlayer", FLASH_PLAYER = "FlashPlayer";
    private ConfigurationContext context;

    @Override
    public void init(ConfigurationContext context) {
        this.context = context;
        initMessageHandlersImpl(context.getGlobalJSStack());
    }
    
    private native void initMessageHandlersImpl(JavaScriptObject vim) /*-{    
    $wnd.addEventListener('message', function(evt){
    if(evt.origin != 'http://player.vimeo.com') return;
    var dt = JSON.parse(evt.data);
    var pid = dt.player_id;
    if(vim[pid] != null) {
        if(dt.event) {
           switch(dt.event) {
            case "loadProgress":
              vim[pid].onLoadProgress(dt.data);
              break;
            case "ready":
              vim[pid].onInit(null);
              break;
            case "play":
              vim[pid].onPlay(null);
              break;
            case "pause":
              vim[pid].onPause(null);
              break;
            case "finish":
              vim[pid].onFinish(null);
              break;
            case "seek":
              vim[pid].onSeek(null);
              break;
           }
        }
    }
    }, false);
    }-*/;


    @Override
    public PlayerElement getPlayerElement(String playerName, String playerId, String mediaURL, boolean autoplay, HashMap<String, String> params) {
        if (playerName.equals(UNIVERSAL_PLAYER)) {
            params.put("player_id", playerId);
            
            StringBuilder src = new StringBuilder("http://player.vimeo.com/video/");
            src.append(mediaURL).append("?");
            
            Iterator<String> it = params.keySet().iterator();
            while(it.hasNext()) {
                String ky = it.next();
                src.append(ky).append("=").append(params.get(ky)).append("&");
            }
            src.deleteCharAt(src.lastIndexOf("&"));
            
            PlayerElement pe = new PlayerElement(PlayerElement.Type.IFrameElement, playerId, "");
            pe.addParam("frameborder", "0");
            pe.addParam("src", src.toString());
            return pe;
        } else {
            throw new IllegalArgumentException("Player '" + playerName + "' not known to this provider");
        }
    }

    @Override
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException {
        if (playerName.equals(FLASH_PLAYER)) {
            return PlayerUtil.getFlashPlayerVersion();
        } else if (playerName.equals(UNIVERSAL_PLAYER)) {
            if (PlayerUtil.isHTML5CompliantClient()) {
                return PluginVersion.get(5, 0, 0);
            } else {
                throw new PluginNotFoundException(Plugin.Native, "An HTML5 compliant browser is required!");
            }
        } else {
            throw new IllegalArgumentException("Player '" + playerName + "' not known to this provider");
        }
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        if (playerName.equals(FLASH_PLAYER)) {
            return new VimeoFlashPlayer(mediaURL, autoplay, width, height);
        } else if (playerName.equals(UNIVERSAL_PLAYER)) {
            return new VimeoUniversalPlayer(mediaURL, autoplay, width, height);
        } else {
            throw new IllegalArgumentException("Player '" + playerName + "' not known to this provider");
        }
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        if (playerName.equals(FLASH_PLAYER)) {
            return new VimeoFlashPlayer(mediaURL, autoplay, "400px", "300px");
        } else if (playerName.equals(UNIVERSAL_PLAYER)) {
            return new VimeoUniversalPlayer(mediaURL, autoplay, "400px", "300px");
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
    vim[playerId].onMsg = function(evt){
    handler.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onMsg(Ljava/lang/String;)(evt);
    }
    vim[playerId].onLoadProgress = function(evt){
    handler.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onLoadingPrgress(D)(parseFloat(evt.percent));
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
        
        public void onMsg(String msg);
    }
    }
