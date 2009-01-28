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
package com.bramosystems.gwt.player.client.impl;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * IE specific native implementation of the WinMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.WinMediaPlayer
 */
public class WinMediaPlayerImplIE extends WinMediaPlayerImpl {

    WinMediaPlayerImplIE() {
    }

    @Override
    public String getPlayerScript(String mediaURL, String playerId, boolean autoplay,
            String height, String width) {
        String uiMode = (height.equals("0") && width.equals("0")) ? "invisible" : "full";
        return "<object id='" + playerId + "' classid='CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6' " +
                "width='" + width + "' height='" + height + "' >" +
                "<param name='showControls' value='false' />" +
                "<param name='autoStart' value='" + autoplay + "' />" +
                "<param name='uiMode' value='" + uiMode + "' /> " +
                "<param name='URL' value='" + mediaURL + "' />" +
                "</object>";
    }

//    @Override
//    public native void loadSound(String playerId, String mediaURL) /*-{
//    var player = $doc.getElementById(playerId);
//    player.URL = mediaURL;
//    }-*/;

    @Override
    protected void initGlobalEventListeners(JavaScriptObject jso) {
        // override this and do nothing to avoid IE errors. Method is just a
        // workaround for Mozilla browsers.
    }

    @Override
    protected native void registerMediaStateListenerImpl(JavaScriptObject jso, String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    playr.attachEvent('buffering', jso[playerId].buffering);
    playr.attachEvent('playStateChange', jso[playerId].playStateChange);
    }-*/;
}
