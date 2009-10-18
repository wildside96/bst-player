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

import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.google.gwt.user.client.Timer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class VLCStateManager {

    private Timer statePooler;
    private final int poolerPeriod = 200;
    private int loopCount,  _loopCount,  previousState,  index,  metaDataWaitCount;
    private boolean isBuffering,  stoppedByUser,  canDoMetadata;
    private ArrayList<Integer> playlistIndexCache,  shuffledIndexCache;
    private PlayStateEvent.State currentState;
    private VLCPlayer player;
    private VLCPlayerImpl impl;

    public VLCStateManager() {
        loopCount = 1;
        _loopCount = 1;
        previousState = -10;
        stoppedByUser = false;
        canDoMetadata = true;
        metaDataWaitCount = -1;

        statePooler = new Timer() {

            @Override
            public void run() {
                checkPlayState();
            }
        };

        index = -1;
        playlistIndexCache = new ArrayList<Integer>();
        currentState = PlayStateEvent.State.Finished;
    }

    public void start(VLCPlayer _player, VLCPlayerImpl _impl) {
        player = _player;
        player.addPlayStateHandler(new PlayStateHandler() {

            public void onPlayStateChanged(PlayStateEvent event) {
                currentState = event.getPlayState();
            }
        });
        impl = _impl;
        statePooler.scheduleRepeating(poolerPeriod);
    }

    /**
     * playback is finished, check if their is need to raise play-finished event
     */
    private void checkFinished() {
        try {
            next(false);    // move to next item in list
        } catch (PlayException ex) {
            try {
                int _list = player.getPlaylistSize();
                if (_loopCount > 1) {   // play over again ...
                    _loopCount--;
                    if (_list == 1) {
                        canDoMetadata = false;
                    }
                    next(true);
                } else if (_loopCount < 0) {    // loop forever ...
                    if (_list == 1) {
                        canDoMetadata = false;
                    }
                    next(true);
                } else {
                    PlayStateEvent.fire(player, PlayStateEvent.State.Finished, index);
                    DebugEvent.fire(player, DebugEvent.MessageType.Info, "Media playback complete");
                }
            } catch (PlayException ex1) {
                DebugEvent.fire(player, DebugEvent.MessageType.Info, ex1.getMessage());
            }
        }
    }

    private void checkPlayState() {
        int state = impl.getPlayerState();

        if (state == previousState) {
            if (metaDataWaitCount >= 0) {
                metaDataWaitCount--;
                if (metaDataWaitCount < 0) {
                    MediaInfo info = new MediaInfo();
                    impl.fillMediaInfo(info);
                    MediaInfoEvent.fire(player, info);
                }
            }
            return;
        }

        switch (state) {
            case -1:   // no input yet...
                break;
            case 0:    // idle/close
                DebugEvent.fire(player, DebugEvent.MessageType.Info, "Idle ...");
                break;
            case 6:    // finished
                if (stoppedByUser) {
                    DebugEvent.fire(player, DebugEvent.MessageType.Info, "Media playback stopped");
                    firePlayStateEvent(PlayStateEvent.State.Stopped, index);
                } else {
                    DebugEvent.fire(player, DebugEvent.MessageType.Info,
                            "Finished playlist item playback #" + index);
                    checkFinished();
                }
                break;
            case 1:    // opening media
                DebugEvent.fire(player, DebugEvent.MessageType.Info, "Opening playlist item #" + index);
                canDoMetadata = true;
            //                    break;
            case 2:    // buffering
                DebugEvent.fire(player, DebugEvent.MessageType.Info, "Buffering started");
                firePlayerStateEvent(PlayerStateEvent.State.BufferingStarted);
                isBuffering = true;
                break;
            case 3:    // playing
                if (isBuffering) {
                    DebugEvent.fire(player, DebugEvent.MessageType.Info, "Buffering stopped");
                    firePlayerStateEvent(PlayerStateEvent.State.BufferingFinished);
                    isBuffering = false;
                }

                if (canDoMetadata) {
                    canDoMetadata = false;
                    metaDataWaitCount = 4;  // implement some kind of delay, no extra timers...
                }

//                    fireDebug("Current Track : " + getCurrentAudioTrack(id));
                DebugEvent.fire(player, DebugEvent.MessageType.Info, "Media playback started");
                stoppedByUser = false;
                firePlayStateEvent(PlayStateEvent.State.Started, index);
                //                    loadingComplete();
                break;
            case 4:    // paused
                DebugEvent.fire(player, DebugEvent.MessageType.Info, "Media playback paused");
                firePlayStateEvent(PlayStateEvent.State.Paused, index);
                break;
            case 5:    // stopping
                firePlayStateEvent(PlayStateEvent.State.Stopped, index);
                break;
            case 7:    // error
                break;
            case 8:    // playback complete
                break;
        }
        previousState = state;
    }

    private void loadingComplete() {
        LoadingProgressEvent.fire(player, 1.0);
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
        _loopCount = loopCount;
        fireDebug("Loop Count set : " + loopCount);
    }

    public void close() {
        statePooler.cancel();
    }

    public void shuffle() {
        Integer[] shuffled = playlistIndexCache.toArray(new Integer[0]);
        Arrays.sort(shuffled, new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                int pos = 0;
                switch (Math.round((float) (Math.random() * 2.0))) {
                    case 0:
                        pos = -1;
                        break;
                    case 1:
                        pos = 0;
                        break;
                    case 2:
                        pos = 1;
                }
                return pos;
            }
        });
        shuffledIndexCache = new ArrayList<Integer>();
        for (Integer _index : shuffled) {
            shuffledIndexCache.add(_index);
        }
    }

    public void addToPlaylist(String mediaUrl, String options) {
        int _index = options == null ? impl.addToPlaylist(mediaUrl) : impl.addToPlaylist(mediaUrl, options);
        fireDebug("Added '" + mediaUrl + "' to playlist @ #" + _index +
                (options == null ? "" : " with options [" + options + "]"));
        playlistIndexCache.add(_index);
        if (player.isShuffleEnabled()) {
            shuffle();
        }
    }

    public void removeFromPlaylist(int index) {
        impl.removeFromPlaylist(playlistIndexCache.get(index));
        playlistIndexCache.remove(index);
        if (player.isShuffleEnabled()) {
            shuffle();
        }
    }

    public void clearPlaylist() {
        impl.clearPlaylist();
        playlistIndexCache.clear();
    }

    private int getNextPlayIndex(boolean loop) throws PlayException {
        if (index >= (playlistIndexCache.size() - 1)) {
            index = -1;
            if (!loop) {
                throw new PlayException("No more entries in playlist");
            }
        }
        return player.isShuffleEnabled() ? shuffledIndexCache.get(++index) : playlistIndexCache.get(++index);
    }

    private int getPreviousPlayIndex(boolean loop) throws PlayException {
        if (index < 0) {
            index = playlistIndexCache.size();
            if (!loop) {
                throw new PlayException("Beginning of playlist reached");
            }
        }
        return player.isShuffleEnabled() ? shuffledIndexCache.get(--index) : playlistIndexCache.get(--index);
    }

    public void play() throws PlayException {
        switch (currentState) {
            case Paused:
                impl.togglePause();
                break;
            case Finished:
                impl.playMediaAt(getNextPlayIndex(true));
                break;
            case Stopped:
                impl.playMediaAt(
                        player.isShuffleEnabled() ? shuffledIndexCache.get(index) : playlistIndexCache.get(index));
        }
    }

    public void playItemAt(int _index) {
        switch (currentState) {
            case Started:
            case Paused:
                stop();
            case Finished:
            case Stopped:
                impl.playMediaAt(playlistIndexCache.get(_index));
        }
    }

    public void next(boolean canLoop) throws PlayException {
        impl.playMediaAt(getNextPlayIndex(canLoop));
    }

    public void previous(boolean canLoop) throws PlayException {
        impl.playMediaAt(getPreviousPlayIndex(canLoop));
    }

    public void stop() {
        stoppedByUser = true;
        impl.stop();
    }

    private void fireDebug(String message) {
        DebugEvent.fire(player, DebugEvent.MessageType.Info, message);
    }

    private void firePlayStateEvent(PlayStateEvent.State state, int index) {
        PlayStateEvent.fire(player, state, index);
    }

    private void firePlayerStateEvent(PlayerStateEvent.State state) {
        PlayerStateEvent.fire(player, state);
    }
}

