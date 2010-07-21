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
package com.bramosystems.oss.player.dev.client;

import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.impl.LoopManager;
import com.bramosystems.oss.player.core.client.impl.VLCPlayerImpl;
import java.util.ArrayList;

public class VLCStateManager1 {

//    private Timer statePooler;
    private final int poolerPeriod = 200;
    private int previousState, metaDataWaitCount, index;
    private boolean isBuffering, stoppedByUser, canDoMetadata;
    private ArrayList<Integer> playlistIndexCache, shuffledIndexCache;
    private PlayStateEvent.State currentState;
//    private VLCPlayer player;
    private VLCPlayerImpl impl;
    private LoopManager loopManager;

    public VLCStateManager1() {
        previousState = -10;
        stoppedByUser = false;
        canDoMetadata = true;
        metaDataWaitCount = -1;

//        statePooler = new Timer() {

//            @Override
//            public void run() {
//                checkPlayState();
//            }
//        };

        index = -1;
        playlistIndexCache = new ArrayList<Integer>();
        currentState = PlayStateEvent.State.Finished;
        /*
        loopManager = new LoopManager(true, new LoopManager.LoopCallback() {

            @Override
            public void onLoopFinished() {
                PlayStateEvent.fire(player, PlayStateEvent.State.Finished, index);
                DebugEvent.fire(player, DebugEvent.MessageType.Info, "Playback finished");
            }

            @Override
            public void loopForever(boolean loop) {
                try {
                    if (player.getPlaylistSize() == 1) {
                        canDoMetadata = false;
                    }
                    next(true);
                } catch (PlayException ex) {
                    DebugEvent.fire(player, DebugEvent.MessageType.Info, ex.getMessage());
                }
            }

            @Override
            public void playNextLoop() {
                try {
                    if (player.getPlaylistSize() == 1) {
                        canDoMetadata = false;
                    }
                    next(true);
                } catch (PlayException ex) {
                    DebugEvent.fire(player, DebugEvent.MessageType.Info, ex.getMessage());
                }
            }
        });
        */
    }

//    public void start(VLCPlayer _player, VLCPlayerImpl _impl) {
//        player = _player;
//        player.addPlayStateHandler(new PlayStateHandler() {

//            @Override
//            public void onPlayStateChanged(PlayStateEvent event) {
//                currentState = event.getPlayState();
//            }
//        });
//        impl = _impl;
//        statePooler.scheduleRepeating(poolerPeriod);
//    }

    /**
     * playback is finished, check if their is need to raise play-finished event
     *
    private void checkFinished() {
        try {
            next(false);    // move to next item in list
        } catch (PlayException ex) {
            loopManager.notifyPlayFinished();
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
                    DebugEvent.fire(player, DebugEvent.MessageType.Info, "Playback stopped");
                    firePlayStateEvent(PlayStateEvent.State.Stopped, index);
                } else {
                    DebugEvent.fire(player, DebugEvent.MessageType.Info,
                            "Playback complete item #" + index);
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
                DebugEvent.fire(player, DebugEvent.MessageType.Info, "Playback started");
                stoppedByUser = false;
                firePlayStateEvent(PlayStateEvent.State.Started, index);
                //                    loadingComplete();
                break;
            case 4:    // paused
                DebugEvent.fire(player, DebugEvent.MessageType.Info, "Playback paused");
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
        return loopManager.getLoopCount();
    }

    public void setLoopCount(int loopCount) {
        loopManager.setLoopCount(loopCount);
    }

    public void close() {
        statePooler.cancel();
    }

    public void shuffle() {
        Integer[] shuffled = playlistIndexCache.toArray(new Integer[0]);
        Arrays.sort(shuffled, new Comparator<Integer>() {

            @Override
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
        shuffledIndexCache = new ArrayList<Integer>(Arrays.asList(shuffled));
    }

    public void addToPlaylist(String mediaUrl, String options) {
        int _index = options == null ? impl.addToPlaylist(mediaUrl) : impl.addToPlaylist(mediaUrl, options);
        fireDebug("Added '" + mediaUrl + "' to playlist @ #" + _index
                + (options == null ? "" : " with options [" + options + "]"));
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

    /**
     * get next playlist index.  can repeat playlist if looping is allowed...
     * @param loop
     * @return
     * @throws PlayException
     *
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

    /**
     * play next item in list
     * @param canLoop
     * @throws PlayException
     *
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
*/
    public final native void registerEventCallbacks(VLCPlayerImpl player, VLCEventCallback callback) /*-{
    try {
    player.onMediaPlayerNothingSpecial = function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onIdle()();
    };
    player.onMediaPlayerEncounteredError = function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onError()();
    };
    player.addEventListener('MediaPlayerNothingSpecial', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onIdle()();
    }, false);
    }catch(e){
    $wnd.alert(e);
    }
    }-*/;

    public final native void registerEventCallbacks2(VLCPlayerImpl player, VLCEventCallback callback) /*-{
    try {
    player.onMediaPlayerNothingSpecial = function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onIdle()();
    };
    player.addEventListener('MediaPlayerNothingSpecial', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onIdle()();
    }, false);
    player.addEventListener('MediaPlayerOpening', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onOpening()();
    }, false);
    player.addEventListener('MediaPlayerBuffering', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onBuffering()();
    }, false);
    player.addEventListener('MediaPlayerPlaying', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onPlaying()();
    }, false);
    player.addEventListener('MediaPlayerPaused', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onPaused()();
    }, false);
    player.addEventListener('MediaPlayerForward', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onForward()();
    }, false);
    player.addEventListener('MediaPlayerBackward', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onBackward()();
    }, false);
    player.addEventListener('MediaPlayerEncounteredError', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onError()();
    }, false);
    player.addEventListener('MediaPlayerEndReached', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onEndReached()();
    }, false);
    player.addEventListener('MediaPlayerTimeChanged', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onTimeChanged()();
    }, false);
    player.addEventListener('MediaPlayerPositionChanged', function(event) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onPositionChanged()();
    }, false);
    player.addEventListener('MediaPlayerMouseGrab', function(event,x,y) {
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onMouseGrabed(DD)(x,y);
    }, false);
    }catch(e){
    $wnd.alert(e);
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager1.VLCEventCallback::onError()();
    }
    }-*/;

    /**
     * Callback interface for VLC events:
     *
     * MediaPlayerNothingSpecial: vlc is in idle state doing nothing but waiting for a command to be issued
     * MediaPlayerOpening: vlc is opening an media resource locator (MRL)
     * MediaPlayerBuffering: vlc is buffering
     * MediaPlayerPlaying: vlc is playing a media
     * MediaPlayerPaused: vlc is in paused state
     * MediaPlayerForward: vlc is fastforwarding through the media (works only when an input supports forward playback)
     * MediaPlayerBackward: vlc is going backwards through the media (works only when an input supports backwards playback)
     * MediaPlayerEncounteredError: vlc has encountered an error and is unable to continue
     * MediaPlayerEndReached: vlc has reached the end of current playlist
     * MediaPlayerTimeChanged: time has changed
     * MediaPlayerPositionChanged: media position has changed
     * MediaPlayerSeekableChanged: media seekable flag has changed (true means media is seekable, false means it is not)
     * MediaPlayerPausableChanged: media pausable flag has changed (true means media is pauseable, false means it is not)
     * MediaPlayerMouseGrab: vlc video filter (eg: logo) detected a object movement in its video window and returns new X and Y coordinates
     */
    public static interface VLCEventCallback {
        public void onIdle();
        public void onOpening();
        public void onBuffering();
        public void onPlaying();
        public void onPaused();
        public void onForward();
        public void onBackward();
        public void onError();
        public void onEndReached();
        public void onPositionChanged();
        public void onTimeChanged();
        public void onMouseGrabed(double x, double y);
    }

    public final native void registerEventCallbackds(VLCPlayerImpl player, VLCEventCallback callback) /*-{
    player.attachEvent('playStateChange', function(NewState) {
    impl.@com.bramosystems.oss.player.core.client.impl.WMPStateManagerIE::firePlayStateChanged(Ljava/lang/String;I)(player.id, NewState);
    });
    player.attachEvent('buffering', function(Start) {
    impl.@com.bramosystems.oss.player.core.client.impl.WMPStateManagerIE::fireBuffering(Ljava/lang/String;Z)(player.id, Start);
    });
    player.attachEvent('error', function() {
    impl.@com.bramosystems.oss.player.core.client.impl.WMPStateManagerIE::fireError(Ljava/lang/String;)(player.id);
    });
    impl.@com.bramosystems.oss.player.core.client.impl.WMPStateManagerIE::firePlayStateChanged(Ljava/lang/String;I)(player.id, player.playState);
    }-*/;
}
