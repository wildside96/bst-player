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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private native void initMessageHandlersImpl(JavaScriptObject v) /*-{    
     $wnd.addEventListener('message', function(evt){
     if(evt.origin != 'http://player.vimeo.com') return;
     var dt = JSON.parse(evt.data);
     var p = dt.player_id;
     if(v[p] != null) {
     if(dt.event) {
     switch(dt.event) {
     case "ready":
     v[p].onInit();
     break;
     case "loadProgress":
     v[p].onLoadProgress(dt.data);
     break;
     case "playProgress":
     v[p].onPlayProgress(dt.data);
     break;
     case "play":
     v[p].onPlay();
     break;
     case "pause":
     v[p].onPause();
     break;
     case "finish":
     v[p].onFinish();
     break;
     case "seek":
     v[p].onSeek(dt.data);
     break;
     }
     } else {
     v[p].onMth(dt);
     }
     }
     }, false);
     }-*/;

    @Override
    public PlayerElement getPlayerElement(String playerName, String playerId, String mediaId, boolean autoplay, HashMap<String, String> params) {
        if (playerName.equals(UNIVERSAL_PLAYER)) {
            params.put("player_id", playerId);

            StringBuilder src = new StringBuilder("http://player.vimeo.com/video/");
            src.append(mediaId).append("?");

            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
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
            throws PluginNotFoundException, PluginVersionException {
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
            throws PluginNotFoundException, PluginVersionException {
        return getPlayer(playerName, mediaURL, autoplay, "400px", "300px");
    }

    @Override
    public PluginInfo getDetectedPluginInfo(String playerName) throws PluginNotFoundException {
        if (playerName.equals(FLASH_PLAYER)) {
            return PlayerUtil.getPluginInfo(Plugin.FlashPlayer);
        } else if (playerName.equals(UNIVERSAL_PLAYER)) {
            return PlayerUtil.getPluginInfo(Plugin.Native);
        } else {
            throw new IllegalArgumentException("Player '" + playerName + "' not known to this provider");
        }
    }

    @Override
    public Set<String> getPermittedMimeTypes(String playerName, PluginVersion version) {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getPermittedMediaProtocols(String playerName, PluginVersion version) {
        return new HashSet<String>();
    }

    /**
     * *********************** Native Utils **************************************
     */
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

    private native void closeImpl(String p, JavaScriptObject v) /*-{
     delete v[p];
     }-*/;

    private native void initHandlersImpl(String p, JavaScriptObject v, EventHandler h) /*-{
     v[p] = new Object();
     v[p].onInit = function(){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onInit()();
     }
     v[p].onMth = function(evt){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onMethod(Ljava/lang/String;Ljava/lang/String;)(evt.method,evt.value);
     }
     v[p].onMsg = function(evt){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onMsg(Ljava/lang/String;)(evt);
     }
     v[p].onLoadProgress = function(evt){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onLoadingProgress(DD)(parseFloat(evt.percent),parseFloat(evt.duration));
     }
     v[p].onPlayProgress = function(evt){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onPlayingProgress(D)(parseFloat(evt.seconds));
     }
     v[p].onPlay = function(){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onPlay()();
     }
     v[p].onPause = function(){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onPause()();
     }
     v[p].onFinish = function(){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onFinish()();
     }
     v[p].onSeek = function(evt){
     h.@com.bramosystems.oss.player.provider.vimeo.client.impl.VimeoPlayerProvider.EventHandler::onSeek(D)(evt.seconds);
     }
     }-*/;

    /*************************** Event Handler Interface *********************************/
    public static interface EventHandler {

        public void onInit();

        public void onLoadingProgress(double progress,double duration);

        public void onPlayingProgress(double seconds);

        public void onPlay();

        public void onFinish();

        public void onPause();

        public void onSeek(double seconds);

        public void onMsg(String msg);
        
        public void onMethod(String method, String retVal);
    }
}
