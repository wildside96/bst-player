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

import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;

/**
 * IE specific native implementation of the WinMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see WinMediaPlayer
 */
public class WinMediaPlayerImplIE extends WinMediaPlayerImpl {

    WinMediaPlayerImplIE() {
    }

    @Override
    public String getPlayerScript(String mediaURL, String playerId, boolean autoplay,
            String uiMode, int height, int width) {
        return "<object id='" + playerId + "' classid='CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6' " +
                "width='" + width + "px' height='" + height + "px' >" +
                "<param name='autoStart' value='" + autoplay + "' />" +
                "<param name='uiMode' value='" + uiMode + "' /> " +
                "<param name='URL' value='" + mediaURL + "' />" +
                "</object>";
    }

    @Override
    protected void initGlobalEventListeners(WinMediaPlayerImpl jso) {
        // override this and do nothing to avoid IE errors. Method is just a
        // workaround for non IE browsers.
    }

    @Override
    public void registerMediaStateListener(String playerId) {
        registerMediaStateListenerImpl(this, playerId);
    }

    private native void registerMediaStateListenerImpl(WinMediaPlayerImplIE impl, String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    playr.attachEvent('playStateChange', function(NewState) {
    impl.@com.bramosystems.oss.player.core.client.impl.WinMediaPlayerImplIE::firePlayStateChanged(Ljava/lang/String;)(playerId);
    });
    playr.attachEvent('buffering', function(Start) {
    impl.@com.bramosystems.oss.player.core.client.impl.WinMediaPlayerImplIE::fireBuffering(Ljava/lang/String;Z)(playerId, Start);
    });
    playr.attachEvent('error', function() {
    impl.@com.bramosystems.oss.player.core.client.impl.WinMediaPlayerImplIE::fireError(Ljava/lang/String;)(playerId);
    });
    impl.@com.bramosystems.oss.player.core.client.impl.WinMediaPlayerImplIE::firePlayStateChanged(Ljava/lang/String;)(playerId);
    }-*/;

    private void firePlayStateChanged(String playerId) {
        cache.get(playerId).checkPlayState();
    }

    private void fireBuffering(String playerId, boolean buffering) {
        cache.get(playerId).doBuffering(buffering);
    }

    private void fireError(String playerId) {
        cache.get(playerId).onError(getErrorDiscriptionImpl(playerId));
    }

    @Override
    public void close(String playerId) {
        closeImpl(playerId);
        super.close(playerId);
    }

    private native void closeImpl(String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    playr.close();
    }-*/;

}
