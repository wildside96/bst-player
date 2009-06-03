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

package com.bramosystems.oss.player {
    import com.bramosystems.oss.player.playlist.*;

    import flash.external.*;
    import flash.display.*;
    import flash.errors.*;
    import flash.media.*;
    import flash.events.*;
    import flash.net.*;
    import flash.utils.Timer;
    import mx.core.*;
    import mx.controls.*;
    import mx.events.*;

    public class Controller extends UIComponent {

            private var playerId:String;
            private var player:Engine, mp3:Engine, vdu:VideoEngine;
            private var _initAutoplay:Boolean;

            public function Controller(id:String, autoplay:Boolean) {
                playerId = id;
                _initAutoplay = autoplay;

                mp3 = new MP3Engine(playerId, playFinishedHandler);
                vdu = new VideoEngine(playerId, playFinishedHandler);
                addChild(vdu.getDisplayComponent());
            }

            /**************************** PLAYER IMPL ******************************/
            private var mediaURL:String;
            private var playlist:Playlist;

            public function setPlaylist(playlist:Playlist):void {
                this.playlist = playlist;

                var url:String = playlist.getNextURLEntry();
                if((url != null) && (url != mediaURL)) {
                    load(url);
                }
            }

            public function getPlaylist():Playlist {
                return playlist;
            }

            public function load(mediaURL:String):void {
                if(mediaURL.search(".mp3") >= 0) {
                    player = mp3
                    Log.info("Using MP3Engine ...");
                } else {
                    player = vdu;
                    Log.info("Using VideoEngine ...");
                }

                player.load(mediaURL, _initAutoplay);
                if(_initAutoplay){
                    _initAutoplay = false;
                }
            }

            public function play():void {
                player.play();
            }

            public function stop(rewind:Boolean):void {
                player.stop(rewind);
            }

            public function setVolume(vol:Number):void {
                player.setVolume(vol);
            }

            public function getVolume():Number {
                return player.getVolume();
            }

            public function getDuration():Number {
                return player.getDuration();
            }

            public function getPlayPosition():Number {
                return player.getPlayPosition();
            }

            public function setPlayPosition(pos:Number):void {
                player.setPlayPosition(pos);
            }

            public function close():void {
                player.close();
            }

            public function setDebugEnabled(enabled:Boolean):void {
                player.setDebugEnabled(enabled);
            }

            /*************************** PLAY FINISHED HANDLERS *********************/
            private function playFinishedHandler(event:Event):void {
                var url:String = playlist.getNextURLEntry();
                if(url != null) {
                    load(url);
                    play();
                } else {
                   Log.info("Media playback finished");
                   ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 9);
                }
            }
    }
}
