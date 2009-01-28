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

/**
 * Interface defining methods required of an embedded sound player.
 *
 * @author Sikirulai Braheem
 */
public interface EmbeddedMediaPlayer {

    /**
     * Adds a {@code MediaStateListener} object to the player implementation. The listener is
     * notified of media state changes.
     *
     * @param listener the listener to add the player
     * @see com.bramosystems.gwt.player.client.MediaStateListener
     * @see #removeMediaStateListener(com.bramosystems.gwt.player.client.MediaStateListener)
     */
    public void addMediaStateListener(MediaStateListener listener);

    /**
     * Removes specified listener for the list of registered {@code MediaStateListener}s object.
     *
     * @param listener the listener to remove from the player.
     * @see com.bramosystems.gwt.player.client.MediaStateListener
     * @see #addMediaStateListener(com.bramosystems.gwt.player.client.MediaStateListener)
     */
    public void removeMediaStateListener(MediaStateListener listener);

    /**
     * Loads the media at the specified URL into the player.
     *
     * <p>In respect of the <code>same domain</code> policy of some plugins,
     * the URL should point to a destination on the same domain
     * where the application is hosted.
     *
     * @param mediaURL the URL of the media to load into the player.
     * @throws LoadException if an error occurs while loading the media
     */
    public void loadMedia(String mediaURL) throws LoadException;

    /**
     * Plays the media loaded into the player.
     *
     * @throws com.bramosystems.gwt.player.client.PlayException if an error occurs
     * during media playback.
     */
    public void playMedia() throws PlayException;

    /**
     * Stops the media playback.
     */
    public void stopMedia();

    /**
     * Pause the media playback
     */
    public void pauseMedia();

    /**
     * Ejects the media loaded into player. After this method is called, the
     * <code>loadMedia(String mediaURL)</code> method should be called first
     * before any other media playback methods are called
     */
    public void ejectMedia();

    /**
     * Closes the player and all associated resources such as the media stream.
     */
    public void close();

    /**
     * Returns the duration of the loaded media in milliseconds.
     *
     * @return the duration of the loaded media in milliseconds.
     */
    public long getMediaDuration();

    /**
     * Gets the current position in the media that is being played.
     *
     * @return the current position of the media being played.
     */
    public double getPlayPosition();

    /**
     * Sets the playback position of the current media
     *
     * @param position the new position from where to start playback
     */
    public void setPlayPosition(double position);

    /**
     * Gets the volume ranging from {@code 0} (silent) to {@code 1} (full volume).
     *
     * @return volume.
     */
    public double getVolume();

    /**
     * Sets the volume.
     *
     * @param volume {@code 0} (silent) to {@code 1} (full volume).
     */
    public void setVolume(double volume);
}
