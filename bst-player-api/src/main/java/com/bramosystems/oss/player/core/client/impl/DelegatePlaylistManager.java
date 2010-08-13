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

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import java.util.ArrayList;

public class DelegatePlaylistManager implements PlaylistSupport {

    private ArrayList<String> urls;
    private AbstractMediaPlayer player;
    private PlaylistIndexOracle indexOracle;

    public DelegatePlaylistManager(AbstractMediaPlayer player) {
        this.player = player;
        urls = new ArrayList<String>();
        indexOracle = new PlaylistIndexOracle();
    }

    @Override
    public boolean isShuffleEnabled() {
        return indexOracle.isRandomMode();
    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        indexOracle.setRandomMode(enable);
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        urls.add(mediaURL);
        indexOracle.incrementIndexSize();
        DebugEvent.fire(player, DebugEvent.MessageType.Info,
                "Added to playlist - '" + mediaURL + "'");
    }

    @Override
    public void removeFromPlaylist(int index) {
        urls.remove(index);
        indexOracle.removeFromCache(index);
    }

    @Override
    public void clearPlaylist() {
        urls.clear();
        indexOracle.reset();
    }

    @Override
    public void playNext() throws PlayException {
        int ind = indexOracle.suggestIndex(true, true);
        if (ind < 0) {
            throw new PlayException("End of playlist");
        }
        _playOrLoadMedia(ind, true);
    }

    @Override
    public void playPrevious() throws PlayException {
        int ind = indexOracle.suggestIndex(false, true);
        if (ind < 0) {
            throw new PlayException("Beginning of playlist reached");
        }
         _playOrLoadMedia(ind, true);
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        try {
            _playOrLoadMedia(index, true);
        } catch (PlayException ex) {
            DebugEvent.fire(player, DebugEvent.MessageType.Error, ex.getMessage());
        }
    }

    public void load(int index) throws IndexOutOfBoundsException {
        try {
            _playOrLoadMedia(index, false);
        } catch (PlayException ex) {
            DebugEvent.fire(player, DebugEvent.MessageType.Error, ex.getMessage());
        }
    }

    @Override
    public int getPlaylistSize() {
        return urls.size();
    }

    private void _playOrLoadMedia(int index, boolean play) throws PlayException {
        try {
            player.loadMedia(urls.get(index));
            if (play) {
                player.playMedia();
            }
        } catch (LoadException ex) {
            DebugEvent.fire(player, DebugEvent.MessageType.Info, ex.getMessage());
        }
    }
}
