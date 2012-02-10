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
package com.bramosystems.oss.player.provider.vimeo.client.impl;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the VimeoPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @since 1.4
 */
public class VimeoPlayerIFImpl extends JavaScriptObject {

    protected VimeoPlayerIFImpl() {
    }

    public static native VimeoPlayerIFImpl getPlayerImpl(String playerId)/*-{
    return $doc.getElementById(playerId);
    }-*/;

    public final native String getUrl() /*-{
    var v = this.postMessage({'method':'getVideoUrl'});
    return v['value'];
    }-*/;

    public final native String getEmbedCode() /*-{
    var v = this.postMessage({'method':'getVideoEmbedCode'});
    return v['value'];
    }-*/;

    public final void play() {
        postMessage("play");
    }

    public final void pause() {
        postMessage("pause");
    }

    public final native void stop() /*-{
    }-*/;

    public final void clear() {
        postMessage("unload");
    }

    public final native double getCurrentTime() /*-{
    var v = this.postMessage({'method':'getCurrentTime'});
    return parseFloat(v['value']) * 1000;
    }-*/;

    public final void seekTo(double seconds) {
        postMessage("seekTo", Double.toString(seconds));
    }

    public final native double getDuration() /*-{
    var v = this.postMessage({'method':'getDuration'});
    return parseFloat(v['value']) * 1000;
    }-*/;

    public final native double getVolume() /*-{
    var v = this.postMessage({'method':'getVolume'});
    return parseFloat(v['value']);
    }-*/;

    public final void setVolume(double volume) {
        postMessage("setVolume", Double.toString(volume));
    }

    public final native int getVideoHeight() /*-{
    var v = this.postMessage({'method':'getVideoHeight'});
    return parseInt(v['value']);
    }-*/;

    public final native int getVideoWidth() /*-{
    var v = this.postMessage({'method':'getVideoWidth'});
    return parseInt(v['value']);
    }-*/;

    public final native String getColor() /*-{
    var v = this.postMessage({'method':'getColor'});
    return v['value'];
    }-*/;

    public final void setColor(String color) {
        postMessage("setColor", color);
    }

    public final void registerHandlers(String functBase) {
        postMessage("addEventListener", "loadProgress");
        postMessage("addEventListener", "playProgress");
        postMessage("addEventListener", "play");
        postMessage("addEventListener", "pause");
        postMessage("addEventListener", "finish");
        postMessage("addEventListener", "seek");

    }

    public final void setLoop(boolean loop) {
        postMessage("setLoop", Boolean.toString(loop));
    }
    
    private native void postMessage(String method, String value) /*-{
    this.contentWindow.postMessage(JSON.stringify({"method": method, "value": value}), "http://player.vimeo.com");
    }-*/;
    
    private native void postMessage(String method) /*-{
    this.contentWindow.postMessage(JSON.stringify({"method": method}), "http://player.vimeo.com");
    }-*/;
}
