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

import com.bramosystems.oss.player.youtube.client.PlayerParameters;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;

/**
 * Native implementation of the YouTubePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see YouTubePlayer
 */
public class YouTubeIPlayerImpl extends YouTubePlayerImpl {
    
    public static native YouTubeIPlayerImpl getIPlayerImpl(String playerId, String vId, PlayerParameters params, int _autoHide) /*-{
     return new $wnd.YT.Player(playerId, {
     videoId: vId,
     playerVars: {
     'autoplay': params.@com.bramosystems.oss.player.youtube.client.PlayerParameters::autoplay,
     'autohide': _autoHide,
//     'color':
     'controls': params.@com.bramosystems.oss.player.youtube.client.PlayerParameters::showControls,
//     'iv_load_policy':
     'loop': params.@com.bramosystems.oss.player.youtube.client.PlayerParameters::loop,
     'modestbranding': params.@com.bramosystems.oss.player.youtube.client.PlayerParameters::modestBranding,
//     'playlist':
     'rel': params.@com.bramosystems.oss.player.youtube.client.PlayerParameters::loadRelatedVideos,
     'showinfo': params.@com.bramosystems.oss.player.youtube.client.PlayerParameters::showInfo,
     'start': params.@com.bramosystems.oss.player.youtube.client.PlayerParameters::startTime
     },
     events: {
     'onReady': 'bst_youtube_' + playerId + '_onReady',
     'onStateChange': 'bst_youtube_' + playerId + '_onStateChanged',
     'onPlaybackQualityChange':  'bst_youtube_' + playerId + '_onQualityChanged',
     'onError': 'bst_youtube_' + playerId + '_onError'
     }
     });
     }-*/;

    protected YouTubeIPlayerImpl() {
    }
}
