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
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Provides Playlist emulation support for media plugins
 *
 * @since 1.2
 * @author Sikiru
 */
public class DelegatePlaylistManager implements PlaylistSupport {

    private ArrayList<MRL> urls;
    private ArrayList<String> msgCache;
    private PlayerCallback callback;
    private PlaylistIndexOracle indexOracle;
    private int pIndex;
    private boolean useCache;

    public DelegatePlaylistManager() {
        urls = new ArrayList<MRL>();
        msgCache = new ArrayList<String>();
        indexOracle = new PlaylistIndexOracle();
        useCache = true;
        callback = initCallback();
    }

    public DelegatePlaylistManager(final AbstractMediaPlayer _player) {
        this();
        callback = new PlayerCallback() {

            @Override
            public void play() throws PlayException {
                _player.playMedia();
            }

            @Override
            public void load(String url) {
                try {
                    _player.loadMedia(url);
                } catch (LoadException ex) {
                    onDebug(ex.getMessage());
                }
            }

            @Override
            public void onDebug(String message) {
                DebugEvent.fire(_player, DebugEvent.MessageType.Info, message);
            }
        };
        _player.addPlayerStateHandler(new PlayerStateHandler() {

            @Override
            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case Ready:
                        flushMessageCache();
                }
            }
        });
    }

    protected PlayerCallback initCallback() {
        return new PlayerCallback() {

            @Override
            public void play() throws PlayException {
            }

            @Override
            public void load(String url) {
            }

            @Override
            public void onDebug(String message) {
            }
        };
    }

    protected void flushMessageCache() {
        if (msgCache != null) {
            useCache = false;
            for (String msg : msgCache) {
                callback.onDebug(msg);
            }
            msgCache = null;
        }
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
        urls.add(new MRL(mediaURL));
        indexOracle.incrementIndexSize();
        _debug("Added to playlist - '" + mediaURL + "'");
    }

    /**
     * For use with NativePlayer.
     * @param mediaURLs
     */
    public void addToPlaylist(String... mediaURLs) {
        urls.add(new MRL(mediaURLs));
        indexOracle.incrementIndexSize();
        _debug("Added to playlist - '" + mediaURLs + "'");
    }

    public void addToPlaylist(ArrayList<String> mediaURLs) {
        MRL mrl = new MRL();
        mrl.addAll(mediaURLs);
        urls.add(mrl);
        indexOracle.incrementIndexSize();
        _debug("Added to playlist - '" + mediaURLs + "'");
    }

    @Override
    public void removeFromPlaylist(int index) {
        _debug("Removed from playlist - '" + urls.remove(index) + "'");
        indexOracle.removeFromCache(index);
    }

    @Override
    public void clearPlaylist() {
        urls.clear();
        indexOracle.reset(false);
    }

    @Override
    public void playNext() throws PlayException {
        playNext(false);
    }

    @Override
    public void playPrevious() throws PlayException {
        playPrevious(false);
    }

    public void playNext(boolean force) throws PlayException {
        int ind = indexOracle.suggestIndex(true, force);
        if (ind < 0) {
            throw new PlayException("End of playlist");
        }
        pIndex = ind;
        _playOrLoadMedia(ind, true);
    }

    public void playPrevious(boolean force) throws PlayException {
        int ind = indexOracle.suggestIndex(false, force);
        if (ind < 0) {
            throw new PlayException("Beginning of playlist reached");
        }
        pIndex = ind;
        _playOrLoadMedia(ind, true);
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        try {
            indexOracle.setCurrentIndex(index);
            _playOrLoadMedia(index, true);
        } catch (PlayException ex) {
            _debug(ex.getMessage());
        }
    }

    /**
     * Play another alternative URL for the current resource index ...
     * @param index
     * @throws IndexOutOfBoundsException
     */
    public void loadAlternative() throws LoadException {
        try {
            callback.load(urls.get(indexOracle.getCurrentIndex()).getNextResource(false));
        } catch (Exception e) {
            throw new LoadException(e.getMessage());
        }
    }

    public void load(int index) {
        try {
            indexOracle.setCurrentIndex(index);
            _playOrLoadMedia(index, false);
        } catch (PlayException ex) {
            _debug(ex.getMessage());
        }
    }

    public void loadNext() {
        pIndex = indexOracle.suggestIndex(true, true);
        try {
            _playOrLoadMedia(pIndex, false);
        } catch (PlayException ex) {
            _debug(ex.getMessage());
        }
    }

    public String getCurrentItem() {
        return urls.get(getPlaylistIndex()).getCurrentResource();
    }

    @Override
    public int getPlaylistSize() {
        return urls.size();
    }

    public int getPlaylistIndex() {
        int ind = indexOracle.getCurrentIndex();
        return ind < 0 ? pIndex : ind;
    }

    private void _playOrLoadMedia(int index, boolean play) throws PlayException {
        callback.load(urls.get(index).getNextResource(true));
        if (play) {
            callback.play();
        }
    }

    private void _debug(String msg) {
        if (useCache) {
            msgCache.add(msg);
        } else {
            callback.onDebug(msg);
        }
    }

    public static interface PlayerCallback {

        public void play() throws PlayException;

        public void load(String url);

        public void onDebug(String message);
    }

    private class MRL extends ArrayList<String> {

        private int index = -1;

        public MRL() {
            super();
        }

        public MRL(String... urls) {
            addAll(Arrays.asList(urls));
        }

        public String getNextResource(boolean roll) {
            index++;
            index = (roll && (index == size())) ? 0 : index;
            return get(index);
        }

        public String getCurrentResource() {
            return get(index);
        }
    }
}
