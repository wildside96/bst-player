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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback;
import com.bramosystems.oss.player.core.client.spi.ConfigurationContext;
import com.bramosystems.oss.player.core.client.spi.PlayerElement;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory;
import com.bramosystems.oss.player.core.client.ui.*;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.JavaScriptObject;
import java.util.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
@PlayerProvider("core")
public class CorePlayerProvider implements PlayerProviderFactory {

    private String wmpFFMimeType = "application/x-ms-wmp", wmpAppMimeType = "application/x-mplayer2";
    private ConfigurationContext context;

    @Override
    public void init(ConfigurationContext context) {
        this.context = context;
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay, String height, String width)
            throws PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer player = null;
        if (playerName.equals(Plugin.DivXPlayer.name())) {
                player = new DivXPlayer(mediaURL, autoplay, height, width);
        } else if (playerName.equals(Plugin.FlashPlayer.name())) {
                player = new FlashMediaPlayer(mediaURL, autoplay, height, width);
        } else if (playerName.equals(Plugin.Native.name())) {
                player = new NativePlayer(mediaURL, autoplay, height, width);
        } else if (playerName.equals(Plugin.QuickTimePlayer.name())) {
                player = new QuickTimePlayer(mediaURL, autoplay, height, width);
        } else if (playerName.equals(Plugin.VLCPlayer.name())) {
                player = new VLCPlayer(mediaURL, autoplay, height, width);
        } else if (playerName.equals(Plugin.WinMediaPlayer.name())) {
                player = new WinMediaPlayer(mediaURL, autoplay, height, width);
        } else {
                throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
        return player;
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay) throws PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer player = null;
        if (playerName.equals(Plugin.DivXPlayer.name())) {
                player = new DivXPlayer(mediaURL, autoplay);
        } else if (playerName.equals(Plugin.FlashPlayer.name())) {
                player = new FlashMediaPlayer(mediaURL, autoplay);
        } else if (playerName.equals(Plugin.Native.name())) {
                player = new NativePlayer(mediaURL, autoplay);
        } else if (playerName.equals(Plugin.QuickTimePlayer.name())) {
                player = new QuickTimePlayer(mediaURL, autoplay);
        } else if (playerName.equals(Plugin.VLCPlayer.name())) {
                player = new VLCPlayer(mediaURL, autoplay);
        } else if (playerName.equals(Plugin.WinMediaPlayer.name())) {
                player = new WinMediaPlayer(mediaURL, autoplay);
        } else {
                throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
        return player;
    }

    @Override
    public PlayerElement getPlayerElement(String playerName, String playerId, String mediaURL, boolean autoplay, HashMap<String, String> params) {
        if (playerName.equals(Plugin.DivXPlayer.name())) {
            return getDivXElement(playerId, mediaURL, autoplay, params);
        } else if (playerName.equals(Plugin.Native.name())) {
            return getNativeElement(playerId, mediaURL, autoplay);
        } else if (playerName.equals(Plugin.QuickTimePlayer.name())) {
            return getQTElement(playerId, mediaURL, autoplay, params);
        } else if (playerName.equals(Plugin.VLCPlayer.name())) {
            return getVLCElement(playerId, "", false);
        } else if (playerName.equals(Plugin.WinMediaPlayer.name())) {
            return getWMPElement(playerId, mediaURL, autoplay, params);
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }

    protected PlayerElement getVLCElement(String playerId, String mediaURL, boolean autoplay) {
        PlayerElement e = new PlayerElement(PlayerElement.Type.EmbedElement, playerId, "application/x-vlc-plugin");
        e.addParam("loop", "" + false);
        e.addParam("target", "");
        e.addParam("autoplay", "" + autoplay);
        e.addParam("events", "true");
        e.addParam("version", "VideoLAN.VLCPlugin.2");
        return e;
    }

    protected PlayerElement getWMPElement(String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params) {
        PlayerElement e = new PlayerElement(PlayerElement.Type.EmbedElement, playerId, hasWMPFFPlugin() ? wmpFFMimeType : wmpAppMimeType);
        e.addParam("autostart", hasWMPFFPlugin() ? Boolean.toString(autoplay) : (autoplay ? "1" : "0"));
        //       e.addParam(hasWMPFFPlugin() ? "URL" : "SRC", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            e.addParam(name, params.get(name));
        }
        return e;
    }

    protected PlayerElement getQTElement(String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params) {
        PlayerElement e = new PlayerElement(PlayerElement.Type.EmbedElement, playerId, "video/quicktime");
        e.addParam("autoplay", Boolean.toString(autoplay));
//        xo.addParam("src", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            e.addParam(name, params.get(name));
        }
        return e;
    }

    protected PlayerElement getNativeElement(String playerId, String mediaURL, boolean autoplay) {
        PlayerElement e = new PlayerElement(PlayerElement.Type.VideoElement, playerId, "");
        e.addParam("src", mediaURL);
        if (autoplay) {
            e.addParam("autoplay", Boolean.toString(autoplay));
        }
        e.addParam("controls", "true");
        return e;
    }

    private boolean hasWMPFFPlugin() {
        // check for firefox plugin mime type...
        MimeType mt = MimeType.getMimeType(wmpFFMimeType);
        if (mt != null) {
            return true;
        } else {
            return false;
        }
    }

    protected PlayerElement getDivXElement(String playerId, String mediaURL,
            boolean autoplay, HashMap<String, String> params) {
        PlayerElement xo = new PlayerElement(PlayerElement.Type.EmbedElement, playerId, "video/divx");
        xo.addParam("autoPlay", Boolean.toString(autoplay));
//        xo.addParam("src", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo;
    }

    public boolean isWMPProgrammableEmbedModeSupported() {
        try {
            PluginInfo.PlayerPluginWrapperType w = PlayerUtil.getPluginInfo(Plugin.WinMediaPlayer).getWrapperType();
            return w.equals(PluginInfo.PlayerPluginWrapperType.WMPForFirefox) || w.equals(PluginInfo.PlayerPluginWrapperType.Totem);
        } catch (PluginNotFoundException ex) {
            return false;
        }
    }

    /**
     * ******************************************* Plugin detection
     * *****************************************************
     */
    @Override
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException {
        PluginVersion pv = new PluginVersion();
        Plugin plugin = Plugin.valueOf(playerName);

        switch (plugin) {
            case DivXPlayer:
                pv = PlayerUtil.getDivXPlayerPluginVersion();
                break;
            case FlashPlayer:
                pv = PlayerUtil.getFlashPlayerVersion();
                break;
            case QuickTimePlayer:
                pv = PlayerUtil.getQuickTimePluginVersion();
                break;
            case VLCPlayer:
                pv = PlayerUtil.getVLCPlayerPluginVersion();
                break;
            case WinMediaPlayer:
                pv = PlayerUtil.getWindowsMediaPlayerPluginVersion();
                break;
            case Native:
                if (PlayerUtil.isHTML5CompliantClient()) {
                    pv = PluginVersion.get(5, 0, 0);
                }
                break;
        }
        return pv;
    }

    @Override
    public PluginInfo getDetectedPluginInfo(String playerName) throws PluginNotFoundException {
        return PlayerUtil.getPluginInfo(Plugin.valueOf(playerName));
    }

    /**
     * ********************************** Native Utils for Flash Player
     * **********************************
     */
    public void initFMPHandler(String playerId, FMPStateCallback callback) {
        initFMPHandlerImpl(context.getGlobalJSStack(), playerId, callback);
    }

    public String getFMPHandlerPrefix(String playerId) {
        return context.getGlobalJSStackName() + ".swf." + playerId;
    }

    public void closeFMPHandler(String playerId) {
        closeFMPHandlerImpl(context.getGlobalJSStack(), playerId);
    }

    public void initDivXHandlers(String playerId, DivXStateManager.StateCallback callback) {
        initDivxHandlersImpl(context.getGlobalJSStack(), playerId, callback);
    }

    public String getDivXHandlerPrefix(String playerId) {
        return context.getGlobalJSStackName() + ".divx." + playerId;
    }

    public void closeDivXHandlers(String playerId) {
        clearDivXHandlerImpl(context.getGlobalJSStack(), playerId);
    }

    private native void initDivxHandlersImpl(JavaScriptObject global, String playerId, DivXStateManager.StateCallback callback) /*-{
     if(global.divx == null) {
     global.divx = new Object();
     }
     global.divx[playerId] = new Object();
     global.divx[playerId].stateChanged = function(eventId){
     callback.@com.bramosystems.oss.player.core.client.impl.DivXStateManager.StateCallback::onStatusChanged(I)(parseInt(eventId));
     }
     global.divx[playerId].downloadState = function(current, total){
     callback.@com.bramosystems.oss.player.core.client.impl.DivXStateManager.StateCallback::onLoadingChanged(DD)(parseFloat(current),parseFloat(total));
     }
     global.divx[playerId].timeState = function(time){
     callback.@com.bramosystems.oss.player.core.client.impl.DivXStateManager.StateCallback::onPositionChanged(D)(parseFloat(time));
     }
     }-*/;

    private native void clearDivXHandlerImpl(JavaScriptObject global, String playerId) /*-{
     delete global.divx[playerId];
     }-*/;

    private native void closeFMPHandlerImpl(JavaScriptObject global, String playerId) /*-{
     delete global.swf[playerId];
     }-*/;

    private native void initFMPHandlerImpl(JavaScriptObject global, String playerId, FMPStateCallback callback) /*-{
     if(global.swf == null) {
     global.swf = new Object();
     }
     global.swf[playerId] = new Object();
     global.swf[playerId].onInit = function(){
     callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onInit()();
     }
     global.swf[playerId].onEvent = function(type,buttonDown,alt,ctrl,shift,cmd,stageX_keyCode,stageY_charCode){
     callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onEvent(IZZZZZII)(type,buttonDown,alt,ctrl,shift,cmd,stageX_keyCode,stageY_charCode);
     }
     global.swf[playerId].onStateChanged = function(state, listIndex){
     callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onStateChanged(II)(state,listIndex);
     }
     global.swf[playerId].onLoadingProgress = function(progress){
     callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onProgress(D)(progress);
     }
     global.swf[playerId].onMessage = function(type, message){
     callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onMessage(ILjava/lang/String;)(type,message);
     }
     global.swf[playerId].onMetadata = function(id3){
     callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onMediaInfo(Ljava/lang/String;)(id3);
     }
     global.swf[playerId].onFullscreen = function(fs){
     callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onFullScreen(Z)(fs);
     }
     }-*/;

    @Override
    public Set<String> getPermittedMimeTypes(String playerName, PluginVersion version) {
        HashSet<String> types = new HashSet<String>();
        Plugin plugin = Plugin.valueOf(playerName);
        switch (plugin) {
            case DivXPlayer:
                types.add("video/divx");
                if (version.compareTo(2, 1, 0) >= 0) {
                    types.add("video/mp4");
                    types.add("video/x-mov");
                }
                break;
            case FlashPlayer:
                types.add("audio/x-m4a");
                types.add("audio/mpeg");
                types.add("audio/x-mpegurl");
                types.add("video/flv");
                types.add("video/x-flv");
                types.add("video/mp4");
                break;
            case QuickTimePlayer:
                types.add("audio/wav");
                types.add("audio/mid");
                types.add("audio/basic");
                types.add("audio/x-aiff");
                types.add("audio/ac3");
                types.add("audio/aac");
                types.add("audio/amr");
                types.add("audio/x-gsm");
                types.add("audio/3gpp");
                types.add("audio/3gpp2");
                types.add("audio/x-mpegurl");
                types.add("audio/mpeg");
                types.add("audio/mp4");
                types.add("audio/x-m4a");
                types.add("audio/x-m4b");
                types.add("audio/x-m4p");
                types.add("audio/x-caf");
                types.add("video/sd-video");
                types.add("video/mpeg");
                types.add("video/mp4");
                types.add("video/quicktime");
                break;
            case VLCPlayer:
                types.add("audio/mpeg");
                types.add("audio/x-matroska");
                types.add("audio/x-m4a");
                types.add("audio/x-mpegurl");
                types.add("audio/x-ms-wma");
                types.add("audio/wav");
                types.add("audio/3gpp");
                types.add("audio/3gpp2");
                types.add("video/mpeg");
                types.add("video/mpeg-system");
                types.add("video/mp4");
                types.add("video/x-msvideo");
                types.add("video/quicktime");
                types.add("video/x-ms-asf-plugin");
                types.add("video/x-ms-asf");
                types.add("video/x-ms-wmv");
                types.add("video/3gpp");
                types.add("video/3gpp2");
                types.add("video/divx");
                types.add("video/flv");
                types.add("video/x-matroska");
                break;
            case WinMediaPlayer:
                types.add("audio/mid");
                types.add("audio/basic");
                types.add("audio/x-ms-wma");
                types.add("audio/x-ms-wax");
                types.add("video/x-ms-asf-plugin");
                types.add("video/x-ms-asf");
                types.add("video/x-ms-wm");
                types.add("video/x-ms-wmv");
                types.add("video/x-ms-wvx");
                if (version.compareTo(12, 0, 0) >= 0) {
                    types.add("audio/mp4");
                    types.add("audio/aac");
                    types.add("audio/aiff");
                    types.add("audio/3gpp");
                    types.add("audio/3gpp2");
                    types.add("video/mp4");
                    types.add("video/3gpp");
                    types.add("video/3gpp2");
                    types.add("video/mp2t");
                }
                break;
            case Native:
                types.addAll(PlayerUtil.getHMTL5MimeTypes());
                break;
        }
        return types;
    }

    @Override
    public Set<String> getPermittedMediaProtocols(String playerName, PluginVersion version) {
        HashSet<String> types = new HashSet<String>();
        Plugin plugin = Plugin.valueOf(playerName);
        switch (plugin) {
            case FlashPlayer:
                types.add("rtmp");
                break;
            case QuickTimePlayer:
                types.add("rtsp");
                types.add("rts");
                break;
            case VLCPlayer:
                types.add("rtp");
                types.add("rtsp");
                types.add("mms");
                types.add("udp");
                break;
            case WinMediaPlayer:
                types.add("rtsp");
                types.add("rtspu");
                types.add("rtspt");
                types.add("mms");
                types.add("mmsu");
                types.add("mmst");
                types.add("wmpcd");
                types.add("wmpdvd");
                break;
        }
        return types;
    }
}
