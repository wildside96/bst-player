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
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import java.util.ArrayList;
import java.util.HashSet;

public class VLCStateManager {

    private VLCStateCallback callback;
    private PoollingStateManager stateMgr;
    private VLCPlaylistManager playlistMgr;

    public VLCStateManager(VLCStateCallback callback) {
        this.callback = callback;
        stateMgr = new PoollingStateManager();
    }

    public void start(VLCPlayer _player, VLCPlayerImpl _impl) {
        stateMgr.initManager(_impl);
        playlistMgr = new VLCPlaylistManager(_impl, callback);
    }

    public void close() {
        stateMgr.stop();
    }

    public VLCPlaylistManager getPlaylistManager() {
        return playlistMgr;
    }

    protected class PoollingStateManager {

        private final int POOL_RATE = 300;
        private int _previousState, _metaDataWaitCount, _previousIndex;
        private boolean _isBuffering;
        private Timer _timer;
        private VLCPlayerImpl _impl;

        public PoollingStateManager() {
            _previousState = -20;
            _previousIndex = -1;
            _timer = new Timer() {

                @Override
                public void run() {
                    checkState();
                }
            };
        }

        public void initManager(VLCPlayerImpl impl) {
            _impl = impl;
            _timer.scheduleRepeating(POOL_RATE);
        }

        public void stop() {
            _timer.cancel();
        }

        private void checkState() {
            int state = _impl.getPlayerState();
            int _index = playlistMgr.getPlaylistIndex();

            if (state == _previousState) {
                if ((_metaDataWaitCount > 0) && (--_metaDataWaitCount <= 0)) {
                    MediaInfo info = new MediaInfo();
                    _impl.fillMediaInfo(info);
                    callback.onMediaInfo(info);
                }
                return;
            }

            callback.onInfo("state : " + state + ", index : " + _index);

            switch (state) {
                case -1:   // no input yet...
                    break;
                case 0:    // idle/close
                    callback.onIdle();
                    break;
                case 1:    // opening media
                    callback.onOpening(_index);
                    break;
                case 2:    // buffering
                    _isBuffering = true;
                    callback.onBuffering(true);
                    break;
                case 3:    // playing
                    if (_isBuffering) {
                        _isBuffering = false;
                        callback.onBuffering(false);
                    }

                    if (_index != _previousIndex) {
                        _metaDataWaitCount = 4;  // implement some kind of delay, no extra timers...
                    }
                    playlistMgr.setCurrentState(State.Started);
                    playlistMgr.setStoppedByUser(false);
                    callback.onPlaying(_index);
                    //                    loadingComplete();
                    break;
                case 4:    // paused
                    playlistMgr.setCurrentState(State.Paused);
                    callback.onPaused(_index);
                    break;
                case 5:    // stopping
                    callback.onInfo("stopping ...");
                    break;
                case 6:    // finished
                    if (playlistMgr.isStoppedByUser()) {
                        playlistMgr.setCurrentState(State.Stopped);
                        callback.onStopped(_index);
                    } else {
                        callback.onEndReached(_index);
                        try {
                            playlistMgr.next(false);
                        } catch (PlayException ex) {
                            playlistMgr.setCurrentState(State.Finished);
                        }
                    }
                    break;
                case 7:    // error
                    break;
                default:    // unknown state ...
                    callback.onInfo("[unknown state] " + state);
            }
            _previousState = state;
            _previousIndex = _index;
        }
    }

    public static interface VLCStateCallback {

        public void onIdle();

        public void onOpening(int index);

        public void onBuffering(boolean started);

        public void onPlaying(int index);

        public void onPaused(int index);

        public void onStopped(int index);
//        public void onForward();
//        public void onBackward();

        public void onError(String message);

        public void onInfo(String message);

        public void onEndReached(int index);
//        public void onPositionChanged();
//        public void onTimeChanged();
//        public void onMouseGrabed(double x, double y);

        public void onMediaInfo(MediaInfo info);
    }

    public static class VLCPlaylistManager {

        private ArrayList<Integer> _indexCache;
        private VLCStateCallback _callback;
        private VLCPlayerImpl _impl;
        private boolean stoppedByUser;
        private PlayStateEvent.State _currentState;
        private IndexOracle _indexOracle;

        VLCPlaylistManager(VLCPlayerImpl impl, VLCStateCallback callback) {
            _indexCache = new ArrayList<Integer>();
            _indexOracle = new IndexOracle();
            _impl = impl;
            _callback = callback;
            _currentState = PlayStateEvent.State.Stopped;
        }

        public void setCurrentState(State currentState) {
            _currentState = currentState;
            switch (_currentState) {
                case Finished:
                    _indexOracle.reset();
            }
        }

        public int getPlaylistIndex() {
            return _indexOracle.getCurrentIndex();
        }

        void setStoppedByUser(boolean stoppedByUser) {
            this.stoppedByUser = stoppedByUser;
        }

        boolean isStoppedByUser() {
            return stoppedByUser;
        }

        public void play() throws PlayException {
            switch (_currentState) {
                case Paused:
                    _impl.togglePause();
                    break;
                case Finished:
                    _impl.playMediaAt(getNextPlayIndex(true));
                    break;
                case Stopped:
                    _impl.playMediaAt(_indexCache.get(_indexOracle.suggestIndex(true, false)));
            }
        }

        public void stop() {
            stoppedByUser = true;
            _impl.stop();
        }

        public void playItemAt(int __index) {
            switch (_currentState) {
                case Started:
                case Paused:
                    _impl.stop();
                case Finished:
                case Stopped:
                    _impl.playMediaAt(_indexCache.get(__index));
            }
        }

        public void next(boolean canLoop) throws PlayException {
            _impl.playMediaAt(getNextPlayIndex(canLoop));
        }

        public void previous(boolean canLoop) throws PlayException {
            _impl.playMediaAt(getPreviousPlayIndex(canLoop));
        }

        public void addToPlaylist(String mediaUrl, String options) {
            _indexCache.add(options == null ? _impl.addToPlaylist(mediaUrl)
                    : _impl.addToPlaylist(mediaUrl, options));
            _callback.onInfo("Added '" + mediaUrl + "' to playlist"
                    + (options == null ? "" : " with options [" + options + "]"));
            _indexOracle.incrementIndexSize();
        }

        public void removeFromPlaylist(int index) {
            _impl.removeFromPlaylist(_indexCache.get(index));
            _indexCache.remove(index);
            _indexOracle.removeFromCache(index);
        }

        public void clearPlaylist() {
            _impl.clearPlaylist();
            _indexCache.clear();
            _indexOracle.reset();
        }

        /**
         * get next playlist index.  can repeat playlist if looping is allowed...
         * @param loop
         * @return
         * @throws PlayException
         */
        private int getNextPlayIndex(boolean loop) throws PlayException {
            int ind = _indexOracle.suggestIndex(true, loop);
            if (ind < 0) {
                throw new PlayException("End of playlist");
            }
            return _indexCache.get(ind);
        }

        private int getPreviousPlayIndex(boolean loop) throws PlayException {
            int ind = _indexOracle.suggestIndex(false, loop);
            if (ind < 0) {
                throw new PlayException("Beginning of playlist reached");
            }
            return _indexCache.get(ind);
        }

        public boolean isShuffleEnabled() {
            return _indexOracle.isRandomMode();
        }

        public void enableShuffle(boolean enable) {
            _indexOracle.setRandomMode(enable);
        }
    }

    private static class IndexOracle {

        private int _currentIndex, _indexSize;
        private boolean _randomMode;
        private HashSet<Integer> _usedRandomIndices;

        public IndexOracle() {
            this(0);
        }

        public IndexOracle(int indexSize) {
            _indexSize = indexSize;
            _usedRandomIndices = new HashSet<Integer>();
        }

        public void setRandomMode(boolean _randomMode) {
            this._randomMode = _randomMode;
        }

        public boolean isRandomMode() {
            return _randomMode;
        }

        public void incrementIndexSize() {
            _indexSize++;
        }

        public void reset() {
            _usedRandomIndices.clear();
            _indexSize = 0;
            _currentIndex = 0;
        }

        public int getCurrentIndex() {
            return _currentIndex;
        }

        public void removeFromCache(int index) {
            _usedRandomIndices.remove(Integer.valueOf(index));
            _indexSize--;
        }

        public int suggestIndex(boolean up, boolean canRepeat) {
            _currentIndex = suggestIndexImpl(up);
            if (_randomMode) {
                int _count = 0;
                while (_usedRandomIndices.contains(_currentIndex)) {
                    _currentIndex = suggestIndexImpl(up);
                    _count++;
                    if (_count == _indexSize) {
                        _currentIndex = -1;
                        break;
                    }
                }
            } else {
                if (_currentIndex == _indexSize) {
                    _currentIndex = -1;
                }
            }

            if (_currentIndex < 0 && canRepeat) {  // prepare for another iteration ...
                _usedRandomIndices.clear();
                _currentIndex = up ? 0 : _indexSize;
                _currentIndex = suggestIndex(up, canRepeat);
            }

            if (_currentIndex >= 0) { // keep the used index ...
                _usedRandomIndices.add(_currentIndex);
            }
            return _currentIndex;
        }

        private int suggestIndexImpl(boolean up) {
            return _randomMode ? Random.nextInt(_indexSize)
                    : (up ? _currentIndex++ : _currentIndex--);
        }
    }
}
