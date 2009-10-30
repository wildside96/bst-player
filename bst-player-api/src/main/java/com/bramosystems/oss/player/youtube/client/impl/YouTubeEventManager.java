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

import com.google.gwt.user.client.Command;
import java.util.HashMap;

/**
 * Handles global events for the YouTubePlayer
 *
 * @author Sikirulai Braheem
 */
public class YouTubeEventManager {

    private HashMap<String, Command> initCache;

    public YouTubeEventManager() {
        initCache = new HashMap<String, Command>();
        initGlobalCallbacks(this);
    }

    public final void init(String playerApiId, Command initCompleteCommand) {
        initCache.put(playerApiId, initCompleteCommand);
    }

    private void onInit(String playerApiId) {
        initCache.get(playerApiId).execute();
        initCache.remove(playerApiId);
    }

    private native void initGlobalCallbacks(YouTubeEventManager impl) /*-{
    $wnd.onYouTubePlayerReady = function(playerApiId){
    impl.@com.bramosystems.oss.player.youtube.client.impl.YouTubeEventManager::onInit(Ljava/lang/String;)(playerApiId);
    }
    }-*/;
}