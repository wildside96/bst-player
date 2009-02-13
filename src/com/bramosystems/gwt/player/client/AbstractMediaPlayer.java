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

import com.google.gwt.user.client.ui.Composite;
import java.util.ArrayList;

/**
 * Abstract implementation of an EmbeddedMediaPlayer.  It implements the
 * handling of MediaStateListeners.
 *
 * @author Sikiru Braheem
 * @see EmbeddedMediaPlayer
 */
public abstract class AbstractMediaPlayer extends Composite implements EmbeddedMediaPlayer {

    private ArrayList<MediaStateListener> callbacks;

    /**
     * Constructor method.
     */
    public AbstractMediaPlayer () {
        callbacks = new ArrayList<MediaStateListener>();
    }

    public final void addMediaStateListener(MediaStateListener listener) {
        callbacks.add(listener);
    }

    public final void removeMediaStateListener(MediaStateListener listener) {
        callbacks.remove(listener);
    }

    /**
     * Calls <code>onPlayFinished</code> on registered MediaStateListeners.
     *
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onPlayFinished()
     */
    public final void firePlayFinished() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onPlayFinished();
        }
    }

    /**
     * Calls <code>onPlayStarted</code> on registered MediaStateListeners.
     *
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onPlayStarted()
     */
    public final void firePlayStarted() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onPlayStarted();
        }
    }

    /**
     * Calls <code>onPlayerReady</code> on registered MediaStateListeners.
     *
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onPlayerReady()
     */
    public final void firePlayerReady() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onPlayerReady();
        }
    }

    /**
     * Calls <code>onLoadingComplete</code> on registered MediaStateListeners.
     *
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onLoadingComplete()
     */
    public final void fireLoadingComplete() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onLoadingComplete();
        }
    }

    /**
     * Calls <code>onError</code> on registered MediaStateListeners.
     *
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onError()
     */
    public final void fireError(String description) {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onError(description);
        }
    }

    /**
     * Calls <code>onDebug</code> on registered MediaStateListeners.
     *
     * @param report the message that is sent to a debugging console
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onDebug(java.lang.String)
     */
    public final void fireDebug(String report) {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onDebug(report);
        }
    }

    /**
     * Calls <code>onLoadingProgress</code> on registered MediaStateListeners.
     *
     * @param progress the progress of the loading operation
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onLoadingProgress(double) 
     */
    public final void fireLoadingProgress(double progress) {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onLoadingProgress(progress);
        }
    }

}
