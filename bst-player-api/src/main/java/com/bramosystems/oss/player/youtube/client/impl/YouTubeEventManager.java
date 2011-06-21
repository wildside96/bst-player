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
package com.bramosystems.oss.player.youtube.client.impl;

import com.bramosystems.oss.player.core.client.impl.CallbackUtility;
import com.bramosystems.oss.player.util.client.RegExp;
import com.bramosystems.oss.player.util.client.RegExp.RegexException;
import com.bramosystems.oss.player.util.client.RegExp.RegexResult;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;

/**
 * Handles global events for the YouTubePlayer
 *
 * @author Sikirulai Braheem
 */
public class YouTubeEventManager {

    private RegExp reg;

    public YouTubeEventManager() {
        try {
            reg = RegExp.getRegExp("http(s)?\\\\www\\.youtube\\.com\\v)\\(\\w+)(\\?\\w+)?", "gi");
        } catch (RegexException ex) {
        }
    }

    public final boolean isYouTubeURL(String url) {
        return reg.test(url);
    }

    public final String getVideoId(String url) throws RegexException {
        RegexResult rr = reg.exec(url);
        return rr.getMatch(2);
    }

    public final void init(String playerApiId, Command initCommand) {
        initImpl(playerApiId, CallbackUtility.getUTubeCallbackHandlers(), initCommand);
    }

    public final void close(String playerApiId) {
        closeImpl(playerApiId, CallbackUtility.getUTubeCallbackHandlers());
    }

    private native void closeImpl(String playerApiId, JavaScriptObject utube) /*-{
    delete utube[playerApiId];
    }-*/;

    private native void initImpl(String playerApiId, JavaScriptObject utube, Command initCommand) /*-{
    utube[playerApiId] = new Object();
    utube[playerApiId].init = function(){
    initCommand.@com.google.gwt.user.client.Command::execute()();
    }
    }-*/;

    static {
        initGlobalCallback(CallbackUtility.getUTubeCallbackHandlers());
    }

    private static native void initGlobalCallback(JavaScriptObject utube) /*-{
    $wnd.onYouTubePlayerReady = function(playerApiId){
    utube[playerApiId].init();
    }
    }-*/;
}
