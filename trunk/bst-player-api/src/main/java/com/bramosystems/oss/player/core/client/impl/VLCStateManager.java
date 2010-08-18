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
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;
import com.google.gwt.user.client.Timer;

public class VLCStateManager {

    private VLCStateCallback callback;
    private PoollingStateManager stateMgr;
    private VLCPlaylistManager playlistMgr;

    public VLCStateManager(VLCStateCallback callback, VLCPlayerImplCallback _impl) {
        this.callback = callback;
        stateMgr = new PoollingStateManager(_impl);
        playlistMgr = new VLCPlaylistManager(_impl, callback);
    }

    public void start() {
        stateMgr.start();
        playlistMgr.flushMessageCache();
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
        private VLCPlayerImplCallback _impl;

        public PoollingStateManager(VLCPlayerImplCallback impl) {
            _impl = impl;
            _previousState = -20;
            _previousIndex = -1;
            _timer = new Timer() {

                @Override
                public void run() {
                    checkState();
                }
            };
        }

        public void start() {
            _timer.scheduleRepeating(POOL_RATE);
        }

        public void stop() {
            _timer.cancel();
        }

        private void checkState() {
            int state = _impl.getImpl().getPlayerState();
            int _index = playlistMgr.getPlaylistIndex();

            if (state == _previousState) {
                if ((_metaDataWaitCount > 0) && (--_metaDataWaitCount <= 0)) {
                    MediaInfo info = new MediaInfo();
                    _impl.getImpl().fillMediaInfo(info);
                    callback.onMediaInfo(info);
                }
                return;
            }

            //TODO: remove b4 release ...
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
                        playlistMgr.setCurrentState(State.Finished);
                        callback.onEndReached(_index);
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

    public static class VLCPlaylistManager extends DelegatePlaylistManager {

        private VLCStateCallback _callback;
        private VLCPlayerImplCallback _impl;
        private boolean stoppedByUser;
        private PlayStateEvent.State _currentState;
        private int _vlcItemIndex;

        VLCPlaylistManager(VLCPlayerImplCallback impl, VLCStateCallback callback) {
            _impl = impl;
            _callback = callback;
            _currentState = PlayStateEvent.State.Stopped;
        }

        @Override
        protected PlayerCallback initCallback() {
            return new PlayerCallback() {

                @Override
                public void play() throws PlayException {
                    _impl.getImpl().playMediaAt(_vlcItemIndex);
                }

                @Override
                public void load(String url) {
                    _impl.getImpl().clearPlaylist();
                    _vlcItemIndex = _impl.getImpl().addToPlaylist(url);
                }

                @Override
                public void onDebug(String message) {
                    _callback.onInfo(message);
                }
            };
        }

        public void setCurrentState(State currentState) {
            _currentState = currentState;
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
                    _impl.getImpl().togglePause();
                    break;
                case Finished:
                    playNext(true);
                    break;
                case Stopped:
                    play(getPlaylistIndex());
            }
        }

        public void stop() {
            stoppedByUser = true;
            _impl.getImpl().stop();
        }
/*
        public void addToPlaylist(String mediaUrl, String options) {
            _indexCache.add(options == null ? _impl.addToPlaylist(mediaUrl)
                    : _impl.addToPlaylist(mediaUrl, options));
            _callback.onInfo("Added '" + mediaUrl + "' to playlist"
                    + (options == null ? "" : " with options [" + options + "]"));
            _indexOracle.incrementIndexSize();
        }
*/
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

    public static interface VLCPlayerImplCallback {
        public VLCPlayerImpl getImpl();
    }

}
