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
 * @since 2.0
 */
public class VimeoPlayerIFImpl extends JavaScriptObject {

    protected VimeoPlayerIFImpl() {
    }

    public static native VimeoPlayerIFImpl getPlayerImpl(String playerId)/*-{
     return $doc.getElementById(playerId);
     }-*/;

    public final void getUrl() {
        postMessage("getVideoUrl");
    }

    public final void getEmbedCode() {
        postMessage("getVideoEmbedCode");
    }

    public final void play() {
        postMessage("play");
    }

    public final void pause() {
        postMessage("pause");
    }

    public final void clear() {
        postMessage("unload");
    }

    public final void seekTo(double seconds) {
        postMessage("seekTo", Double.toString(seconds));
    }

    public final void getVolume() {
        postMessage("getVolume");
    }

    public final void setVolume(double volume) {
        postMessage("setVolume", Double.toString(volume));
    }

    public final void getVideoHeight() {
        postMessage("getVideoHeight");
    }

    public final void getVideoWidth() {
        postMessage("getVideoWidth");
    }

    public final void getColor() {
        postMessage("getColor");
    }

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

    private native void postMessage(String m, String v) /*-{
     this.contentWindow.postMessage({"method":m,"value":v},"http://player.vimeo.com");
     }-*/;

    private native void postMessage(String m) /*-{
     this.contentWindow.postMessage({"method":m},"http://player.vimeo.com");
     }-*/;
}
