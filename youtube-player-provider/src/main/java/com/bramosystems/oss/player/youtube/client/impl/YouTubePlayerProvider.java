/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
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
package com.bramosystems.oss.player.youtube.client.impl;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.spi.ConfigurationContext;
import com.bramosystems.oss.player.core.client.spi.PlayerElement;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory;
import com.bramosystems.oss.player.youtube.client.ChromelessPlayer;
import com.bramosystems.oss.player.youtube.client.PlayerParameters;
import com.bramosystems.oss.player.youtube.client.YouTubeConfigParameter;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.core.client.JavaScriptObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
@PlayerProvider(YouTubePlayerProvider.PROVIDER_NAME)
public class YouTubePlayerProvider implements PlayerProviderFactory {

    public static final String PROVIDER_NAME = "bst.youtube";
    private ConfigurationContext ctx;

    @Override
    public void init(ConfigurationContext context) {
        ctx = context;
        initCallbackImpl(ctx.getGlobalJSStack());
    }

    private native void initCallbackImpl(JavaScriptObject utube) /*-{
     $wnd.onYouTubePlayerReady = function(playerApiId){
     utube[playerApiId].onInit();
     }
     }-*/;

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay, String height, String width)
            throws PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer player = null;
        PlayerParameters pp = new PlayerParameters();
        pp.setAutoplay(autoplay);

        if (playerName.equals("YouTube")) {
            player = new YouTubePlayer(mediaURL, width, height);
            player.setConfigParameter(YouTubeConfigParameter.URLParameters, pp);
        } else if (playerName.equals("Chromeless")) {
            player = new ChromelessPlayer(mediaURL, width, height);
            player.setConfigParameter(YouTubeConfigParameter.URLParameters, pp);
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
        return player;
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay)
            throws PluginNotFoundException, PluginVersionException {
        return getPlayer(playerName, mediaURL, autoplay, "350px", "100%");
    }

    @Override
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException {
        if (playerName.equals("YouTube") || playerName.equals("Chromeless")) {
            return PlayerUtil.getFlashPlayerVersion();
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }

    @Override
    public PluginInfo getDetectedPluginInfo(String playerName) throws PluginNotFoundException {
        if (playerName.equals("YouTube") || playerName.equals("Chromeless")) {
            return PlayerUtil.getPluginInfo(Plugin.FlashPlayer);
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }

    @Override
    public PlayerElement getPlayerElement(String playerName, String playerId, String mediaURL, boolean autoplay, HashMap<String, String> params) {
        if (playerName.equals("YouTube") || playerName.equals("Chromeless")) {
            PlayerElement e = new PlayerElement(PlayerElement.Type.EmbedElement, playerId, "application/x-shockwave-flash");
            e.addParam("src", mediaURL);
            e.addParam("name", playerId);

            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String name = keys.next();
                e.addParam(name, params.get(name));
            }
            return e;
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }

    public final void init(String playerApiId, EventHandler handler) {
        initImpl(playerApiId, ctx.getGlobalJSStack(), handler);
    }

    public final void close(String playerApiId) {
        closeImpl(playerApiId, ctx.getGlobalJSStack());
    }

    public final String getHandlerPrefix() {
        return ctx.getGlobalJSStackName();
    }

    private native void initImpl(String playerApiId, JavaScriptObject utube, EventHandler handler) /*-{
     utube[playerApiId] = new Object();
     utube[playerApiId].onInit = function(){
     handler.@com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider.EventHandler::onInit()();
     }
     utube[playerApiId].onStateChanged = function(changeCode){
     handler.@com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider.EventHandler::onYTStateChanged(I)(changeCode);
     }
     utube[playerApiId].onQualityChanged = function(quality){
     handler.@com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider.EventHandler::onYTQualityChanged(Ljava/lang/String;)(quality);
     }
     utube[playerApiId].onError = function(errorCode){
     handler.@com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider.EventHandler::onYTError(I)(errorCode);
     }
     }-*/;

    private native void closeImpl(String playerApiId, JavaScriptObject utube) /*-{
     delete utube[playerApiId];
     }-*/;

    @Override
    public Set<String> getPermittedMimeTypes(String playerName, PluginVersion version) {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getPermittedMediaProtocols(String playerName, PluginVersion version) {
         return new HashSet<String>();
   }

    public static interface EventHandler {

        public void onInit();

        public void onYTStateChanged(int state);

        public void onYTQualityChanged(String quality);

        public void onYTError(int errorCode);
    }
}
