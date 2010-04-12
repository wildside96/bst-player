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

import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.HasMediaStateHandlers;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.google.gwt.i18n.client.NumberFormat;
import java.util.HashMap;

/**
 * Native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see QuickTimePlayer
 */
public class QTStateManager {

    private HashMap<String, EventHandler> cache;

    public QTStateManager() {
        cache = new HashMap<String, EventHandler>();
    }

    public void init(String playerId, HasMediaStateHandlers handler) {
        cache.put(playerId, new EventHandler(playerId, handler));
    }

    protected void onState(String playerId, int stateId) {
        cache.get(playerId).onStateChange(stateId);
    }

    public void close(String playerId) {
        cache.remove(playerId);
    }

    public void registerMediaStateListener(QuickTimePlayerImpl player, final String mediaUrl) {
        registerMediaStateListenerImpl(this, player);
    }

    public final int getLoopCount(String playerId) {
        return cache.get(playerId).getLoopCount();
    }

    public final void setLoopCount(String playerId, int count) {
        cache.get(playerId).setLoopCount(count);
    }

    private native void registerMediaStateListenerImpl(QTStateManager impl, QuickTimePlayerImpl playr) /*-{
    var playerId = playr.id;
    playr.addEventListener('qt_begin', function(){  // plugin init complete
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 1);
    }, false);
    playr.addEventListener('qt_load', function(){   // loading complete
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 2);
    }, false);
    playr.addEventListener('qt_play', function(){   // play started
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 3);
    }, false);
    playr.addEventListener('qt_ended', function(){  // playback finished
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 4);
    }, false);
    playr.addEventListener('qt_canplay', function(){    // player ready
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 5);
    }, false);
    playr.addEventListener('qt_volumechange', function(){   // volume changed
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 6);
    }, false);
    playr.addEventListener('qt_progress', function(){   // progress
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 7);
    }, false);
    playr.addEventListener('qt_error', function(){  // error
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 8);
    }, false);
    playr.addEventListener('qt_loadedmetadata', function(){
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 9);
    }, false);
    playr.addEventListener('qt_pause', function(){   // play paused
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 10);
    }, false);
    playr.addEventListener('qt_waiting', function(){   // buffering
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 11);
    }, false);
    playr.addEventListener('qt_stalled', function(){   // playback stalled
    impl.@com.bramosystems.oss.player.core.client.impl.QTStateManager::onState(Ljava/lang/String;I)(playerId, 12);
    }, false);
    }-*/;

    public class EventHandler {

        protected HasMediaStateHandlers handlers;
        protected String id;
        private NumberFormat volFmt = NumberFormat.getPercentFormat();
        private boolean isBuffering;
        private QuickTimePlayerImpl impl;
        private LoopManager loopManager;

        public EventHandler(String _id, HasMediaStateHandlers _handlers) {
            handlers = _handlers;
            id = _id;
            isBuffering = false;
            loopManager = new LoopManager(false, new LoopManager.LoopCallback() {

                public void onLoopFinished() {
                    PlayStateEvent.fire(handlers, PlayStateEvent.State.Finished, 0);
                    onDebug("Media playback complete");
                }

                public void loopForever(boolean loop) {
                    impl.setLoopingImpl(loop);
                }

                public void playNextLoop() {
                    impl.play();
                }
            });
        }

        // TODO: check for a way of generating stopped event...
        public void onStateChange(int newState) {
            switch (newState) {
                case 1: // plugin init complete ...
                    impl = QuickTimePlayerImpl.getPlayer(id);
                    onDebug("QuickTime Player plugin");
                    onDebug("Version : " + impl.getPluginVersionImpl());
                    break;
                case 2: // loading complete ...
                    onDebug("Media loading complete");
                    LoadingProgressEvent.fire(handlers, 1.0);
                    break;
                case 3: // play started ...
                    if (isBuffering) {
                        isBuffering = false;
                        PlayerStateEvent.fire(handlers, PlayerStateEvent.State.BufferingFinished);
                        onDebug("Buffering ended ...");
                    }
                    PlayStateEvent.fire(handlers, PlayStateEvent.State.Started, 0);
                    onDebug("Playing media at " + impl.getMovieURL());
                    break;
                case 4: // play finished, notify loop manager ...
                    loopManager.notifyPlayFinished();
                    break;
                case 5: // player ready ...
                    onDebug("Plugin ready for media playback");
                    PlayerStateEvent.fire(handlers, PlayerStateEvent.State.Ready);
                    break;
                case 6: // volume changed ...
                    onDebug("Volume changed to " + volFmt.format(impl.getVolume()));
                    break;
                case 7: // progress changed ...
                    LoadingProgressEvent.fire(handlers, impl.getMaxBytesLoaded() / (double) impl.getMovieSize());
                    break;
                case 8: // error event ...
                    onError(impl.getStatus() + " occured while loading media!");
                    break;
                case 9: // metadata stuffs ...
                    MediaInfo info = new MediaInfo();
                    impl.fillMediaInfo(info);
                    MediaInfoEvent.fire(handlers, info);
                    break;
                case 10: // playback paused ...
                    onDebug("Playback paused");
                    PlayStateEvent.fire(handlers, PlayStateEvent.State.Paused, 0);
                    break;
                case 11: // buffering ...
                    isBuffering = true;
                    onDebug("Buffering started ...");
                    PlayerStateEvent.fire(handlers, PlayerStateEvent.State.BufferingStarted);
                    break;
                case 12: // stalled ...
                    onDebug("Player stalled !");
                    break;
            }
        }

        public void onError(String description) {
            DebugEvent.fire(handlers, DebugEvent.MessageType.Error, description);
        }

        public void onDebug(String message) {
            DebugEvent.fire(handlers, DebugEvent.MessageType.Info, message);
        }

        public void setLoopCount(int count) {
            loopManager.setLoopCount(count);
        }

        public int getLoopCount() {
            return loopManager.getLoopCount();
        }
    }
}
