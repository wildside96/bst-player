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

public class VLCStateManagerIE extends VLCStateManager {

    public VLCStateManagerIE() {
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void registerEventCallbacks() {
        super.registerEventCallbacks();
    }


    public final native void registerEventCallbacks(VLCPlayerImpl player, VLCStateCallback callback) /*-{
    try {
    player.addEventListener('MediaPlayerNothingSpecial', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onIdle()();
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: nothing special');
    }, false);
    player.addEventListener('MediaPlayerOpening', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onOpening()();
    }, false);
    player.addEventListener('MediaPlayerBuffering', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onBuffering()();
    }, false);
    player.addEventListener('MediaPlayerPlaying', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onPlaying()();
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: playing');
    }, false);
    player.addEventListener('MediaPlayerPaused', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onPaused()();
    }, false);
    player.addEventListener('MediaPlayerForward', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onForward()();
    }, false);
    player.addEventListener('MediaPlayerBackward', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onBackward()();
    }, false);
    player.addEventListener('MediaPlayerEncounteredError', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onError()();
    }, false);
    player.addEventListener('MediaPlayerEndReached', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onEndReached()();
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: end reached');
    }, false);
    player.addEventListener('MediaPlayerTimeChanged', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onTimeChanged()();
    }, false);
    player.addEventListener('MediaPlayerPositionChanged', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onPositionChanged()();
    }, false);
    player.addEventListener('MediaPlayerMouseGrab', function(event,x,y) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onMouseGrabed(DD)(x,y);
    }, false);
    }catch(e){
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: ' + e);
    }
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: callbacks regd');
    }-*/;

    public final native void registerEventCallbacksIE(VLCPlayerImpl player, VLCStateCallback callback) /*-{
    try {
    player.attachEvent('MediaPlayerNothingSpecial', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onIdle()();
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: nothing special');
    });
    player.attachEvent('MediaPlayerOpening', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onOpening()();
    });
    player.attachEvent('MediaPlayerBuffering', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onBuffering()();
    });
    player.attachEvent('MediaPlayerPlaying', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onPlaying()();
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: playing');
    });
    player.attachEvent('MediaPlayerPaused', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onPaused()();
    });
    player.attachEvent('MediaPlayerForward', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onForward()();
    });
    player.attachEvent('MediaPlayerBackward', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onBackward()();
    });
    player.attachEvent('MediaPlayerEncounteredError', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onError()();
    });
    player.attachEvent('MediaPlayerEndReached', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onEndReached()();
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: end reached');
    });
    player.attachEvent('MediaPlayerTimeChanged', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onTimeChanged()();
    });
    player.attachEvent('MediaPlayerPositionChanged', function(event) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onPositionChanged()();
    });
    player.attachEvent('MediaPlayerMouseGrab', function(event,x,y) {
//    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onMouseGrabed(DD)(x,y);
    });
    }catch(e){
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: ' + e);
    }
    callback.@com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback::onInfo(Ljava/lang/String;)('vlc-evt: callbacks regd');
    }-*/;
}
