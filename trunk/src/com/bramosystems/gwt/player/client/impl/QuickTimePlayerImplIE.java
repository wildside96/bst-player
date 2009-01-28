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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * IE specific native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.QuickTimePlayer
 */
public class QuickTimePlayerImplIE extends QuickTimePlayerImpl {

    // Make package private
    QuickTimePlayerImplIE() {
    }

    @Override
    public String getPlayerScript(String qtSrc, String playerId, boolean autoplay, String height, String width) {
        boolean useQtSrc = (qtSrc != null) && (qtSrc.length() != 0);
        return "<object id='" + playerId + "' classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B' " +
                "width='" + width + "' height='" + height + "' style='behavior:url(#bst_qt_event);'> " +
                "<param name='src' value='" + GWT.getModuleBaseURL() + "qtmov.mov' >" +
                "<param name='AutoPlay' value='" + autoplay + "' >" +
                (useQtSrc ? "<param name='QTSrc' value='" + qtSrc + "'>" : "") +
                "<param name='EnableJavaScript' value='true' >" +
                "<param name='KioskMode' value='true' >" +
                "<param name='PostDomEvents' value='true' >" +
                "</object>";
    }

    @Override
    public String getEventSourceScript() {
        // This is required only for IE...
        return "<object id='" + EVENT_SOURCE_ID + "' width='0' height='0' " +
                "classid='clsid:CB927D12-4FF7-4a9e-A169-56E4B8A75598'></object>";
    }

    @Override
    protected native void registerMediaStateListenerImpl(JavaScriptObject jso, String playerId) /*-{
    var playr = $doc.getElementById(playerId);
    playr.attachEvent('onqt_load', jso[playerId].loadingComplete);
    playr.attachEvent('onqt_ended', jso[playerId].soundComplete);
    playr.attachEvent('onqt_error', jso[playerId].ioError);
    playr.attachEvent('onqt_progress', jso[playerId].loadingProgress);
    playr.attachEvent('onqt_play', jso[playerId].playStarted);
    }-*/;
}
