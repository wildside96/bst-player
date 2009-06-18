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
 * The interface for players that have client-side playlist support.
 *
 * @author Sikiru Braheem
 * @since 1.0
 */
public interface PlaylistSupport {

    /**
     * Enables or disables players' shuffle mode. 
     *
     * @param enable {@code true} to enable shuffle, {@code false} otherwise
     */
    public void setShuffleEnabled(boolean enable);

    /**
     * Checks if this player is in shuffle mode. 
     *
     * @return {@code true} if player is in shuffle mode, {@code false} otherwise.
     */
    public boolean isShuffleEnabled();

    /**
     * Adds the media at the specified URL to the players' playlist.
     *
     * <p>In respect of the <code>same domain</code> policy of some plugins,
     * the URL should point to a destination on the same domain where the
     * application is hosted.
     *
     * @param mediaURL the URL of the media.
     */
    public void addToPlaylist(String mediaURL);

    /**
     * Removes the entry at the specified index from the players' playlist.
     *
     * @param index the index of the playlist entry.
     */
    public void removeFromPlaylist(int index);

    public void clearPlaylist();

    public void playNext();

    public void playPrevious();
}
