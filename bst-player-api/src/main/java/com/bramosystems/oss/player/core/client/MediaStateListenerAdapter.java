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

/**
 * Abstract implementation of the MediaStateListener interface.
 *
 * @author Sikirulai Braheem
 * @since 0.6
 */
public class MediaStateListenerAdapter implements MediaStateListener {

    public void onPlayerReady() {}

    /**
     * @deprecated
     */
    public final void onPlayStarted() {
        onPlayStarted(1);
    }

    /**
     * @deprecated
     */
    public final void onPlayFinished() {
        onPlayFinished(1);
    }

    public void onLoadingComplete() {}

    public void onLoadingProgress(double progress) {}

    public void onBuffering(boolean buffering) {}

    public void onError(String description) {}

    public void onDebug(String message) {}

    public void onMediaInfoAvailable(MediaInfo info) {}

    public void onPlayStarted(int index) {}

    public void onPlayFinished(int index) {}
}
