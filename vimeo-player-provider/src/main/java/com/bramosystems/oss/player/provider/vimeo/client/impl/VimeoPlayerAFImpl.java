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
public class VimeoPlayerAFImpl extends JavaScriptObject {

    protected VimeoPlayerAFImpl() {
    }

    public static native VimeoPlayerAFImpl getPlayerImpl(String playerId)/*-{
    return $doc.getElementById(playerId);
    }-*/;

    public final native String getUrl() /*-{
    return this.api_getVideoUrl();
    }-*/;

    public final native String getEmbedCode() /*-{
    return this.api_getVideoEmbedCode();
    }-*/;

    public final native void play() /*-{
    this.api_play();
    }-*/;

    public final native void pause() /*-{
    this.api_pause();
    }-*/;

    public final native void clear() /*-{
    this.api_unload();
    }-*/;

    public final native double getCurrentTime() /*-{
    return this.api_getCurrentTime() * 1000;
    }-*/;

    public final native void seekTo(double seconds) /*-{
    this.api_seekTo(seconds / 1000);
    }-*/;

    public final native double getDuration() /*-{
    return this.api_getDuration() * 1000;
    }-*/;

    public final native double getVolume() /*-{
    return this.api_getVolume();
    }-*/;

    public final native void setVolume(double volume) /*-{
    this.api_setVolume(volume);
    }-*/;

    public final native int getVideoHeight() /*-{
    return this.api_getVideoHeight();
    }-*/;

    public final native int getVideoWidth() /*-{
    return this.api_getVideoWidth();
    }-*/;

    public final native String getColor() /*-{
    return this.api_getColor();
    }-*/;

    public final native void setColor(String color) /*-{
    this.api_setColor(color);
    }-*/;

    public final native void registerHandlers(String functBase) /*-{
    this.api_addEventListener("loadProgress", functBase + ".onLoadProgress");
    this.api_addEventListener("playProgress", functBase + ".onPlayProgress");
    this.api_addEventListener("play", functBase + ".onPlay");
    this.api_addEventListener("pause", functBase + ".onPause");
    this.api_addEventListener("finish", functBase + ".onFinish");
    this.api_addEventListener("seek", functBase + ".onSeek");
    }-*/;

    public final native void setLoop(boolean loop) /*-{
    this.api_setLoop(loop);
    }-*/;
}
