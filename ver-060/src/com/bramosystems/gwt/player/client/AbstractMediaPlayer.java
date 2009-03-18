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
 * Abstract implementation of a media player.  It implements the
 * handling of MediaStateListeners.
 *
 * @author Sikiru Braheem
 */
public abstract class AbstractMediaPlayer extends Composite implements EmbeddedMediaPlayer {

    private ArrayList<MediaStateListener> callbacks;

    /**
     * Constructor method.
     */
    public AbstractMediaPlayer() {
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
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onError(java.lang.String) 
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

    /**
     * Calls <code>onMediaInfoAvailable</code> on registered MediaStateListeners.
     *
     * @param info the metadata of the loaded media
     * @see com.bramosystems.gwt.player.client.MediaStateListener#onMediaInfoAvailable(MediaInfo)
     * @since 0.6
     */
    public final void fireMediaInfoAvailable(MediaInfo info) {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onMediaInfoAvailable(info);
        }
    }

    /**
     * Displays or hides the players' logger widget.  The logger widget logs debug messages which can
     * be useful during development.
     *
     * @param show <code>true</code> to make the logger widget visible, <code>false</code> otherwise.
     * @see com.bramosystems.gwt.player.client.ui.Logger
     * @since 0.6
     */
    public void showLogger(boolean show) {
    }
    

    /**
     * Displays or hides the player controls.  This implementation does nothing.  Subclasses that
     * permit showing/hiding of controls should override this method and implement accordingly.
     *
     * @param show <code>true</code> to make the player controls visible, <code>false</code> otherwise.
     * @since 0.6
     */
    public void setControllerVisible(boolean show) {
    }
    
    /**
     * Checks whether the player controls are visible.  This implementation return true.  Subclasses that
     * permit showing/hiding of controls should override this method and implement accordingly.
     *
     * @return <code>true</code> if player controls are visible, <code>false</code> otherwise.
     * @since 0.6
     */
    public boolean isControllerVisible() {
        return true;
    }

    /**
     * Sets the number of times the current media file should loop playback before stopping.
     * This implementation does nothing, subclasses should override and implement accordingly.
     *
     * @param loop number of times to loop playback. A negative value makes playback loop forever.
     * @since 0.6
     */
    public void setLoopCount(int loop) {
    }

    /**
     * Returns the remaining number of times this player loops playback before stopping. This
     * implementation returns <code>0</code>, subclasses should override and implement
     * accordingly.
     *
     * @return the remaining number of times this player will loop playback before stopping.
     * @since 0.6
     */
    public int getLoopCount() {
        return 0;
    }
}
