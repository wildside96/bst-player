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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the FlashMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see FlashMediaPlayer
 */
public class FlashMediaPlayerImpl extends JavaScriptObject {

    protected FlashMediaPlayerImpl() {
    }

    public static native FlashMediaPlayerImpl getPlayer(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    public native final String getPluginVersion() /*-{
    return this.getMdaPlayerVer();
    }-*/;

    public native final void loadMedia(String url) /*-{
    this.loadMda(url);
    }-*/;

    public native final void playMedia() /*-{
    this.playMda();
    }-*/;

    public native final boolean playMedia(int index) /*-{
    return this.playMdaIndex(index);
    }-*/;

    public native final boolean playNext() /*-{
    return this.playNextMda();
    }-*/;

    public native final boolean playPrevious() /*-{
    return this.playPrevMda();
    }-*/;

    public native final void stopMedia() /*-{
    this.stopMda(true);
    }-*/;

    public native final void pauseMedia() /*-{
    this.stopMda(false);
    }-*/;

    public native final void closeMedia() /*-{
    try {
    this.closeMda();
    }catch(err){}
    }-*/;

    public native final double getPlayPosition() /*-{
    return this.getMdaPlayPosition();
    }-*/;

    public native final void setPlayPosition(double position) /*-{
    this.setMdaPlayPosition(position);
    }-*/;

    public native final double getMediaDuration() /*-{
    return this.getMdaDuration();
    }-*/;

    public native final void addToPlaylist(String mediaURL) /*-{
    this.addToMdaPlaylist(mediaURL);
    }-*/;

    public native final void removeFromPlaylist(int index) /*-{
    this.removeFromMdaPlaylist(index);
    }-*/;

    public native final void clearPlaylist() /*-{
    this.clearMdaPlaylist();
    }-*/;

    public native final int getPlaylistCount() /*-{
    return this.getMdaPlaylistSize();
    }-*/;

    public native final double getVolume() /*-{
    return this.getMdaVolume();
    }-*/;

    public native final void setVolume(double volume) /*-{
    this.setMdaVolume(volume);
    }-*/;

    public native final void setLoopCount(int count) /*-{
    this.setMdaLoopCount(count);
    }-*/;

    public native final int getLoopCount() /*-{
    this.getMdaLoopCount();
    }-*/;

    public native final boolean isShuffleEnabled() /*-{
    return this.isMdaShuffleEnabled();
    }-*/;

    public native final void setShuffleEnabled(boolean enable) /*-{
    this.setMdaShuffleEnabled(enable);
    }-*/;

    public native final int getVideoHeight() /*-{
    return this.getMdaVideoHeight();
    }-*/;

    public native final int getVideoWidth() /*-{
    return this.getMdaVideoWidth();
    }-*/;

    public native final String getMatrix() /*-{
    return this.getMdaMatrix();
    }-*/;

    public native final void setMatrix(double a, double b, double c, double d, double tx, double ty) /*-{
    this.setMdaMatrix(a, b, c, d, tx, ty);
    }-*/;
}