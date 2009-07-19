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
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.HasMediaStateHandlers;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressHandler;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Abstract implementation of a media player.  It implements the handling
 * of MediaStateListeners.
 *
 * @author Sikiru Braheem
 */
public abstract class AbstractMediaPlayer extends Composite implements HasMediaStateHandlers {

    private ArrayList<MediaStateListener> callbacks;
    private HashMap<String, Command> readyCmdQueue;
    private ArrayList<String> cmdKeys;

    /**
     * Constructor method.
     */
    public AbstractMediaPlayer() {
        callbacks = new ArrayList<MediaStateListener>();
        readyCmdQueue = new HashMap<String, Command>();
        cmdKeys = new ArrayList<String>();

        addPlayerStateHandler(new PlayerStateHandler() {

            public void onPlayerStateChanged(PlayerStateEvent event) {
                if (event.getPlayerState().equals(PlayerStateEvent.State.Ready)) {
                    // ensure commands are executed in the same sequence as added ...
                    Iterator<String> keys = cmdKeys.iterator();
                    while (keys.hasNext()) {
                        readyCmdQueue.get(keys.next()).execute();
                    }
                    cmdKeys.clear();
                    readyCmdQueue.clear();
                }
            }
        });
    }

    /**
     * Adds a {@code MediaStateListener} object to the player implementation. The listener is
     * notified of media state changes.
     *
     * @param listener the listener to add the player
     * @see MediaStateListener
     * @see #removeMediaStateListener(MediaStateListener)
     * @deprecated Will be removed in a future version
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
     * @deprecated Will be removed in a future version
     */
    public final void removeMediaStateListener(MediaStateListener listener) {
        callbacks.remove(listener);
    }

    /**
     * Returns true if this player contains the specified listener.
     *
     * @param listener whose presence in this player is to be tested
     * @return true if this list contains the specified element
     * @since 1.0
     */
    public final boolean containsMediaStateListener(MediaStateListener listener) {
        if (listener == null) {
            return false;
        }
        return callbacks.contains(listener);
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
     * @deprecated Will be removed in a future version
     */
    public final void firePlayFinished() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onPlayFinished(0);
        }
    }

    /**
     * Calls <code>onPlayStarted</code> on registered MediaStateListeners.
     *
     */
    protected final void firePlayStateEvent(PlayStateEvent.State state, int itemIndex) {
        PlayStateEvent.fire(this, state, itemIndex);
    }

    /**
     * Calls <code>onPlayStarted</code> on registered MediaStateListeners.
     * TODO: update docs -- Convinience method to fire events on registered handlers.
     *
     * @param state
     */
    protected final void firePlayerStateEvent(PlayerStateEvent.State state) {
        PlayerStateEvent.fire(this, state);
    }

    /**
     * Calls <code>onPlayerReady</code> on registered MediaStateListeners.
     *
     * @see MediaStateListener#onPlayerReady()
     * @deprecated Will be removed in a future version
     */
    public final void firePlayerReady() {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onPlayerReady();
        }

        // ensure commands are executed in the same sequence as added ...
        Iterator<String> keys = cmdKeys.iterator();
        while (keys.hasNext()) {
            readyCmdQueue.get(keys.next()).execute();
        }
        cmdKeys.clear();
        readyCmdQueue.clear();
    }

    /**
     * Calls <code>onError</code> on registered MediaStateListeners.
     *
     * @see MediaStateListener#onError(java.lang.String) 
     */
    protected final void fireError(String description) {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onError(description);
        }
        DebugEvent.fire(this, DebugEvent.MessageType.Error, description);
    }

    /**
     * Calls <code>onDebug</code> on registered MediaStateListeners.
     *
     * @param report the message that is sent to a debugging console
     * @see MediaStateListener#onDebug(java.lang.String)
     */
    protected final void fireDebug(String report) {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onDebug(report);
        }
        DebugEvent.fire(this, DebugEvent.MessageType.Info, report);
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
        LoadingProgressEvent.fire(this, progress);
    }

    /**
     * Calls <code>onMediaInfoAvailable</code> on registered MediaStateListeners.
     *
     * @param info the metadata of the loaded media
     * @see MediaInfoHandler#onMediaInfoAvailable(MediaInfoEvent)
     */
    public final void fireMediaInfoAvailable(MediaInfo info) {
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onMediaInfoAvailable(info);
        }
        MediaInfoEvent.fire(this, info);
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
     * Sets the number of times the current media file should repeat playback before stopping.
     * This implementation does nothing, subclasses should override and implement accordingly.
     *
     * @param loop number of times to repeat playback. A negative value makes playback repeat forever!.
     * @since 0.6
     */
    public void setLoopCount(int loop) {
    }

    /**
     * Returns the number of times this player repeats playback before stopping. This
     * implementation returns <code>0</code>, subclasses should override and implement
     * accordingly.
     *
     * @return the number of times this player will repeat playback before stopping.
     * @since 0.6
     */
    public int getLoopCount() {
        return 0;
    }

    /**
     * Adds the specified command to this players' command queue.  The command queue
     * is a hash-map of commands that are schedule for execution as soon as the underlying
     * plugin is ready for javascript interaction.
     *
     * <p>The execution of the commands is tied to the <code>Ready</code> state of the 
     * <code>PlayerState</code> event of the underlying plugin.
     * All scheduled commands are executed exactly once.
     *
     * <p><b>Note:</b> If multiple commands use the same key, only the last command is executed.
     *
     * @param key key with which the specified command is to be associated
     * @param command the command to execute when the player plugin is ready for interaction
     * @since 1.0
     */
    protected final void addToPlayerReadyCommandQueue(String key, Command command) {
        if (cmdKeys.contains(key)) {
            cmdKeys.remove(key);
        }
        cmdKeys.add(key);   // ensure commands are executed in the same sequence as added ...
        readyCmdQueue.put(key, command);
    }

    /**
     * Removes a queued command with the specified <code>key</code> from this players'
     * command queue.
     *
     * @param key key whose command is to be removed
     * @since 1.0
     * @see #addToPlayerReadyCommandQueue(java.lang.String, com.google.gwt.user.client.Command)
     */
    protected final void removeFromPlayerReadyCommandQueue(String key) {
        cmdKeys.remove(key);
        readyCmdQueue.remove(key);
    }

    /**
     * Adds the specified loading progress handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.0
     * @see LoadingProgressHandler
     */
    public final HandlerRegistration addLoadingProgressHandler(LoadingProgressHandler handler) {
        return addHandler(handler, LoadingProgressEvent.TYPE);
    }

    /**
     * Adds the specified MediaInfo handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.0
     * @see MediaInfoHandler
     */
    public final HandlerRegistration addMediaInfoHandler(MediaInfoHandler handler) {
        return addHandler(handler, MediaInfoEvent.TYPE);
    }

    /**
     * Adds the specified play-state handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @see PlayStateHandler
     * @since 1.0
     */
    public final HandlerRegistration addPlayStateHandler(PlayStateHandler handler) {
        return addHandler(handler, PlayStateEvent.TYPE);
    }

    /**
     * Adds the specified player-state handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.0
     * @see PlayerStateHandler
     */
    public final HandlerRegistration addPlayerStateHandler(PlayerStateHandler handler) {
        return addHandler(handler, PlayerStateEvent.TYPE);
    }

    /**
     * Adds the specified debug handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @see DebugHandler
     * @since 1.0
     */
    public final HandlerRegistration addDebugHandler(DebugHandler handler) {
        return addHandler(handler, DebugEvent.TYPE);
    }

    /**
     * If the current media is a video, sets the player to adjust its size to match the
     * dimensions of the video
     *
     * @param resize <code>true</code> if player should adjust its size,
     * <code>false</code> otherwise
     *
     * @since 1.0
     */
    public void setResizeToVideoSize(boolean resize) {
    }

    /**
     * Checks if player is set to adjust its size to match the dimensions of a video.
     *
     * @return <code>true</code> if player adjusts its size, <code>false</code> otherwise
     * @since 1.0
     */
    public boolean isResizeToVideoSize() {
        return false;
    }

    /**
     * Returns the height of the current media
     * 
     * @return the height in pixels
     * @since 1.0
     */
    public int getVideoHeight() {
        return 0;
    }

    /**
     * Returns the width of the current media
     *
     * @return the width in pixels
     * @since 1.0
     */
    public int getVideoWidth() {
        return 0;
    }

    /**
     * Checks if the player plugin with the specified playerId is attached to the
     * browsers' DOM
     *
     * <p>This method is provided for the convenience of player implementation classes.
     *
     * @param playerId the object ID of the player plugin
     * @return <code>true</code> if player plugin is attached, <code>false</code>
     * otherwise.
     * @since 1.0
     */
    protected static final native boolean isPlayerAvailable(String playerId) /*-{
    return ($doc.getElementById(playerId) != null);
    }-*/;
}
