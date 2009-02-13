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
package com.bramosystems.gwt.player.client;

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
     */
    public void onPlayStarted();

    /**
     * Invoked when the media playback has finished
     *
     * @see #onPlayStarted()
     */
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
}
