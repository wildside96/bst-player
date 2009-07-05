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
package com.bramosystems.oss.player.core.client;

import java.util.EventListener;

/**
 * Interface definition for media state handlers.
 *
 * @author Sikirulai Braheem
 */
public interface MediaStateListener extends EventListener {

    /**
     * Invoked when player can start media playback.
     */
    public void onPlayerReady();

    /**
     * Invoked when media playback has started.
     *
     * @see #onPlayFinished()
     * @deprecated As of version 1.0, replaced with onPlayStarted(int). Will
     * be removed in a future version
     */
    // TODO: deprecate this please ...
    public void onPlayStarted();

    /**
     * Invoked when the media playback has finished
     * 
     * @see #onPlayStarted()
     * @deprecated As of version 1.0, replaced with onPlayFinished(int). Will
     * be removed in a future version
     */
    // TODO: deprecate this please ...
    public void onPlayFinished();

    /**
     * Invoked when loading of the media has finished.
     *
     */
    public void onLoadingComplete();

    /**
     * Invoked when more media data has been loaded.
     *
     * @param progress ranges between {@code 0} (no data loaded) and {@code 1}
     *  (all data loaded).
     */
    public void onLoadingProgress(double progress);

   /**
     * Invoked when an error occurs.
     *
     * @param description describes the error
     */
    public void onError(String description);

    /**
     * Invoked to send messages to debugging consoles.
     *
     * @param message detailed message sent to the console
     */
    public void onDebug(String message);

    /**
     * Invoked when metadata information (such as MP3's ID3) about the media is available.
     *
     * @param info metadata information of the media
     * @since 0.6
     */
    public void onMediaInfoAvailable(MediaInfo info);

    /**
     * Invoked when media playback has started.
     *
     * @param index the index of the current media being played in the players' playlist
     * @see #onPlayFinished(int)
     * @since 1.0
     */
    public void onPlayStarted(int index);

    /**
     * Invoked when the media playback has finished
     *
     * @param index the index of the current media being played in the players' playlist
     * @see #onPlayStarted(int)
     * @since 1.0
     */
    public void onPlayFinished(int index);

    /**
     * Invoked when the player starts/stops reading data from the media stream.
     *
     * @param buffering <code>true</code> if buffering has started, <code>false</code> otherwise
     * @since 1.0
     */
    public void onBuffering(boolean buffering);
}
