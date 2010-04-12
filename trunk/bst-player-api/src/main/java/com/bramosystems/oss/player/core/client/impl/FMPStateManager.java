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

import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.Command;
import java.util.HashMap;

/**
 * Native implementation of the FlashMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see FlashMediaPlayer
 */
public class FMPStateManager {

    private HashMap<String, EventHandler> cache;

    public FMPStateManager() {
        cache = new HashMap<String, EventHandler>();
        initGlobalCallbacks(this);
    }

    public static interface FMPStateCallback {
        public void onInit();
        public void onMessage(int type, String message);
        public void onProgress(double progress);
        public void onMediaInfo(String info);
        public void onEvent(int type, boolean buttonDown, boolean alt, boolean ctrl,
            boolean shift, boolean cmd, int stageX, int stageY);
        public void onStateChanged(int stateId, int listIndex);
    }

    public final void init(String playerId, FlashMediaPlayer player, Command initCommand) {
        cache.put(playerId, new EventHandler(player, initCommand));
    }

    public final boolean isPlayerInCache(String playerId) {
        return cache.containsKey(playerId);
    }

    public final void closeMedia(String playerId) {
        cache.remove(playerId);
    }

    private void onState(String playerId, int stateId, int listIndex) {
        cache.get(playerId).onStateChange(stateId, listIndex);
    }

    private void onInit(String playerId) {
        cache.get(playerId).initComplete();
    }

    private void onMessage(String playerId, int type, String message) {
        cache.get(playerId).onMessage(type, message);
    }

    private void onProgress(String playerId, double progress) {
        cache.get(playerId).onLoading(progress);
    }

    private void onMediaInfo(String playerId, String info) {
        MediaInfo mi = new MediaInfo();
        fillMediaInfoImpl(info, mi);
        cache.get(playerId).onMediaInfo(mi);
    }

    private void onEvent(String playerId, int type, boolean buttonDown, boolean alt, boolean ctrl,
            boolean shift, boolean cmd, int stageX, int stageY) {
        cache.get(playerId).onEvent(type, buttonDown, alt, ctrl, shift, cmd, stageX, stageY);
    }

    private native void initGlobalCallbacks(FMPStateManager impl) /*-{
    $wnd.bstSwfMdaEvent = function(playerId,type,buttonDown,alt,ctrl,shift,cmd,stageX_keyCode,stageY_charCode){
    impl.@com.bramosystems.oss.player.core.client.impl.FMPStateManager::onEvent(Ljava/lang/String;IZZZZZII)(playerId,type,buttonDown,alt,ctrl,shift,cmd,stageX_keyCode,stageY_charCode);
    }
    $wnd.bstSwfMdaMediaStateChanged = function(playerId, state, listIndex){
    impl.@com.bramosystems.oss.player.core.client.impl.FMPStateManager::onState(Ljava/lang/String;II)(playerId, state, listIndex);
    }
    $wnd.bstSwfMdaInit = function(playerId){
    impl.@com.bramosystems.oss.player.core.client.impl.FMPStateManager::onInit(Ljava/lang/String;)(playerId);
    }
    $wnd.bstSwfMdaLoadingProgress = function(playerId, progress){
    impl.@com.bramosystems.oss.player.core.client.impl.FMPStateManager::onProgress(Ljava/lang/String;D)(playerId, progress);
    }
    $wnd.bstSwfMdaMessage = function(playerId, type, message){
    impl.@com.bramosystems.oss.player.core.client.impl.FMPStateManager::onMessage(Ljava/lang/String;ILjava/lang/String;)(playerId, type, message);
    }
    $wnd.bstSwfMdaMetadata = function(playerId, id3){
    impl.@com.bramosystems.oss.player.core.client.impl.FMPStateManager::onMediaInfo(Ljava/lang/String;Ljava/lang/String;)(playerId, id3);
    }
    }-*/;

    private native void fillMediaInfoImpl(String infoCSV, MediaInfo mData) /*-{
    // parse from CSV like values ...
    // year[$]albumTitle[$]artists[$]comment[$]genre[$]title[$]
    // contentProviders[$]copyright[$]duration[$]hardwareSoftwareRequirements[$]
    // publisher[$]internetStationOwner[$]internetStationName[$]videoWidth[$]videoHeight

    csv = infoCSV.split("[$]");
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::year = csv[0];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = csv[1];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::artists = csv[2];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::comment = csv[3];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::genre = csv[4];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::title = csv[5];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = csv[6];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = csv[7];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(csv[8]);
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = csv[9];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = csv[10];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = csv[11];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = csv[12];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = csv[13];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = csv[14];
    }-*/;

    private class EventHandler {

        private FlashMediaPlayer player;
        private Command initCompleteCommand;

        public EventHandler(FlashMediaPlayer handler, Command initCompleteCommand) {
            this.player = handler;
            this.initCompleteCommand = initCompleteCommand;
        }

        public void onStateChange(int newState, int listIndex) {
            switch (newState) {
                case 1: // loading started...
////                    listener.onPlayerReady();
                    break;
                case 2: // play started...
                    PlayStateEvent.fire(player, PlayStateEvent.State.Started, listIndex);
                    break;
                case 3: // play stopped...
                    PlayStateEvent.fire(player, PlayStateEvent.State.Stopped, listIndex);
                    break;
                case 4: // play paused...
                    PlayStateEvent.fire(player, PlayStateEvent.State.Paused, listIndex);
                    break;
                case 9: // play finished...
                    PlayStateEvent.fire(player, PlayStateEvent.State.Finished, listIndex);
                    break;
                case 10: // loading complete ...
                    LoadingProgressEvent.fire(player, 1.0);
                    break;
            }
        }

        public void initComplete() {
            initCompleteCommand.execute();
        }

        public void onLoading(double progress) {
            LoadingProgressEvent.fire(player, progress);
        }

        public void onMessage(int type, String description) {
            DebugEvent.fire(player, type == 1 ? DebugEvent.MessageType.Error : DebugEvent.MessageType.Info, description);
        }

        public void onMediaInfo(MediaInfo info) {
            MediaInfoEvent.fire(player, info);
        }

        public void onEvent(int type, boolean buttonDown, boolean alt, boolean ctrl,
                boolean shift, boolean cmd, int stageX_keyCode, int stageY_charCode) {

            int button = buttonDown ? NativeEvent.BUTTON_LEFT : NativeEvent.BUTTON_RIGHT;
            int screenX = -1, screenY = -1;

            Document _doc = Document.get();
            NativeEvent event = null;

            switch (type) {
                case 1: // mouse down
                    event = _doc.createMouseDownEvent(1, screenX, screenY, stageX_keyCode,
                            stageY_charCode, ctrl, alt, shift, cmd, button);
                    break;
                case 2: // mouse up
                    event = _doc.createMouseUpEvent(1, screenX, screenY, stageX_keyCode,
                            stageY_charCode, ctrl, alt, shift, cmd, button);
                    break;
                case 3: // mouse move
                    event = _doc.createMouseMoveEvent(1, screenX, screenY, stageX_keyCode,
                            stageY_charCode, ctrl, alt, shift, cmd, button);
                    break;
                case 10: // click
                    event = _doc.createClickEvent(1, screenX, screenY, stageX_keyCode,
                            stageY_charCode, ctrl, alt, shift, cmd);
                    break;
                case 11: // double click
                    event = _doc.createDblClickEvent(1, screenX, screenY, stageX_keyCode,
                            stageY_charCode, ctrl, alt, shift, cmd);
                    break;
                case 20: // key down
                    event = _doc.createKeyDownEvent(ctrl, alt, shift, cmd, stageX_keyCode,
                            stageY_charCode);
                    break;
                case 21: // key press
                    event = _doc.createKeyPressEvent(ctrl, alt, shift, cmd, stageX_keyCode,
                            stageY_charCode);
                    break;
                case 22: // key up
                    event = _doc.createKeyUpEvent(ctrl, alt, shift, cmd, stageX_keyCode,
                            stageY_charCode);
                    break;
            }

            DomEvent.fireNativeEvent(event, player);
        }
    }
}
