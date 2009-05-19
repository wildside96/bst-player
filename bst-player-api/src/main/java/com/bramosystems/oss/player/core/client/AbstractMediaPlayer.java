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

import com.bramosystems.oss.player.core.client.ui.Logger;
import com.google.gwt.user.client.ui.Composite;
import java.util.ArrayList;

/**
 * Abstract implementation of a media player.  It implements the
 * handling of MediaStateListeners.
 *
 * @author Sikiru Braheem
 */
public abstract class AbstractMediaPlayer extends Composite {

    private ArrayList<MediaStateListener> callbacks;

    /**
     * Constructor method.
     */
    public AbstractMediaPlayer() {
        callbacks = new ArrayList<MediaStateListener>();
    }

    /**
     * Adds a {@code MediaStateListener} object to the player implementation. The listener is
     * notified of media state changes.
     *
     * @param listener the listener to add the player
     * @see MediaStateListener
     * @see #removeMediaStateListener(MediaStateListener)
     */
    public final void addMediaStateListener(MediaStateListener listener) {
        callbacks.add(listener);
    }

    /**
     * Removes specified listener for the list of registered {@code MediaStateListener}s object.
     *
     * @param listener the listener to remove from the player.
     * @see MediaStateListener
     * @see #addMediaStateListener(MediaStateListener)
     */
    public final void removeMediaStateListener(MediaStateListener listener) {
        callbacks.remove(listener);
    }

    /**
     * Loads the media at the specified URL into the player.
     *
     * <p>In respect of the <code>same domain</code> policy of some plugins,
     * the URL should point to a destination on the same domain
     * where the application is hosted.
     *
     * @param mediaURL the URL of the media to load into the player.
     * @throws LoadException if an error occurs while loading the media
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract void loadMedia(String mediaURL) throws LoadException;

    /**
     * Plays the media loaded into the player.
     *
     * @throws PlayException if an error occurs
     * during media playback.
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract void playMedia() throws PlayException;

    /**
     * Stops the media playback.
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract void stopMedia();

    /**
     * Pause the media playback
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract void pauseMedia();

    /**
     * @deprecated Will be removed in a future release
     * 
     * Ejects the media loaded into player. After this method is called, the
     * <code>loadMedia(String mediaURL)</code> method should be called first
     * before any other media playback methods are called
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
//    public abstract void ejectMedia();

    /**
     * Closes the player and all associated resources such as removing the media player
     * plugin from the page.
     *
     * Note: The player will remain unavailable after this method returns.  A new
     * instance will have to created.
     */
    public abstract void close();

    /**
     * Returns the duration of the loaded media in milliseconds. An IllegalStateException is
     * thrown is the player is not available
     *
     * @return the duration of the loaded media in milliseconds.
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract long getMediaDuration();

    /**
     * Gets the current position in the media that is being played.
     *
     * @return the current position of the media being played.
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract double getPlayPosition();

    /**
     * Sets the playback position of the current media
     *
     * @param position the new position from where to start playback
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract void setPlayPosition(double position);

    /**
     * Gets the volume ranging from {@code 0} (silent) to {@code 1} (full volume).
     *
     * @return volume.
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract double getVolume();

    /**
     * Sets the volume.
     *
     * @param volume {@code 0} (silent) to {@code 1} (full volume).
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * after the <code>{@link #close()}</code> method has been called on this player.
     */
    public abstract void setVolume(double volume);

    /**
     * Calls <code>onPlayFinished</code> on registered MediaStateListeners.
     *
     * @see MediaStateListener#onPlayFinished()
     */
    public final void firePlayFinished() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onPlayFinished();
        }
    }

    /**
     * Calls <code>onPlayStarted</code> on registered MediaStateListeners.
     *
     * @see MediaStateListener#onPlayStarted()
     */
    public final void firePlayStarted() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onPlayStarted();
        }
    }

    /**
     * Calls <code>onPlayerReady</code> on registered MediaStateListeners.
     *
     * @see MediaStateListener#onPlayerReady()
     */
    public final void firePlayerReady() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onPlayerReady();
        }
    }

    /**
     * Calls <code>onLoadingComplete</code> on registered MediaStateListeners.
     *
     * @see MediaStateListener#onLoadingComplete()
     */
    public final void fireLoadingComplete() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onLoadingComplete();
        }
    }

    /**
     * Calls <code>onError</code> on registered MediaStateListeners.
     *
     * @see MediaStateListener#onError(java.lang.String) 
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
     * @see MediaStateListener#onDebug(java.lang.String)
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
     * @see MediaStateListener#onLoadingProgress(double) 
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
     * @see MediaStateListener#onMediaInfoAvailable(MediaInfo)
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
     * @see Logger
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

    /**
     * Enables or disables players' shuffle mode. This implementation does nothing, subclasses
     * should override and implement accrodingly.
     *
     * @param enable {@code true} to enable shuffle, {@code false} otherwise
     * @since 1.0
     */
    public void setShuffleEnabled(boolean enable) {
    }

    /**
     * Checks if this player is in shuffle mode. This implementation returns false, subclasses should
     * override and implement accordingly.
     *
     * @return {@code true} if player is in shuffle mode, {@code false} otherwise.
     * @since 1.0
     */
    public boolean isShuffleEnabled() {
        return false;
    }

    /**
     * Adds the media at the specified URL to the players' playlist.
     *
     * <p>In respect of the <code>same domain</code> policy of some plugins,
     * the URL should point to a destination on the same domain
     * where the application is hosted.
     *
     * @param mediaURL the URL of the media.
     * @since 1.0
     */
    public void addToPlaylist(String mediaURL) {
    }

    /**
     * Removes the specified URL from the players' playlist.
     *
     * @param mediaURL the URL of the media.
     * @since 1.0
     */
    public void removeFromPlaylist(String mediaURL) {
    }
}
