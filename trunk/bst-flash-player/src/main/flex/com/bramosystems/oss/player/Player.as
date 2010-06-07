/*
 * Copyright 2010 Sikirulai Braheem
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
    import com.bramosystems.oss.player.external.*;
    import com.bramosystems.oss.player.events.*;

    import flash.display.Graphics;
    import flash.errors.*;
    import flash.events.*;
    import flash.external.*;
    import flash.geom.*;
    import flash.media.*;
    import flash.net.*;
    import flash.utils.*;

    import mx.core.*;
    import mx.events.*;

    public class Player extends LayoutContainer {
        private const PLAYING:int = 1;
        private const STOPPED:int = 2;
        private const PAUSED:int = 3;
        private const LOADED:int = 4;
        private const FINISHED:int = 5;

        private var mediaURL:String;
        private var state:int = STOPPED;
        private var player:Engine, mp3:MP3Engine, vdu:VideoEngine;
        private var playlist:Playlist;
        private var listManager:PlaylistManager;
        private var playTimer:Timer;

        public function Player(setting:Setting, _playlist:Playlist) {
            x = 0;
            percentWidth = 100;
            percentHeight = 100;
            setStyle("top", "0");
            setStyle("bottom", "20");
            setStyle("horizontalAlign", "center");
            setStyle("verticalAlign", "middle");
            setStyle("backgroundColor", "0x000000");

            // init engines ...
            mp3 = new MP3Engine();
            mp3.addEventListener(PlayStateEvent.PLAY_STARTED, playStarted);
            mp3.addEventListener(PlayStateEvent.PLAY_STOPPED, playStopped);
            mp3.addEventListener(PlayStateEvent.PLAY_PAUSED, playPaused);
            mp3.addEventListener(PlayStateEvent.PLAY_FINISHED, playFinished);

            vdu = new VideoEngine();
            vdu.addEventListener(PlayStateEvent.PLAY_STARTED, playStarted);
            vdu.addEventListener(PlayStateEvent.PLAY_STOPPED, playStopped);
            vdu.addEventListener(PlayStateEvent.PLAY_PAUSED, playPaused);
            vdu.addEventListener(PlayStateEvent.PLAY_FINISHED, playFinished);
            vdu.addEventListener(MetadataEvent.METADATA_RECEIVED, metaDoVDUSize);

            setting.addEventListener(SettingChangedEvent.VOLUME_CHANGED, onUpdateVolumeEvent); // add vol mgmt on settings
            updateVolume(setting.getVolume());

            playlist = _playlist;
            listManager = new PlaylistManager(playlist, setting);

            // add position timer ...
            playTimer = new Timer(1000);
            playTimer.addEventListener(TimerEvent.TIMER, function(event:TimerEvent):void {
                EventUtil.firePlayingProgress(player.getPlayPosition());
            });
        }

        public function setDebugEnabled(enabled:Boolean):void {
//            debug = enabled;
        }

        /************************ PLAYER FUNCTIONS *******************************/
        public function load(mediaURL:String):void {
            playlist.addEventListener(PlaylistEvent.ADDED, playlistAddComplete);
            playlist.clear();
            playlist.add(mediaURL);
        }

        public function playlistAddComplete(event:PlaylistEvent):void {
            playlist.removeEventListener(PlaylistEvent.ADDED, playlistAddComplete);
            var url:String = listManager.getNextURLEntry();
            _load(url);
        }

        public function play():void {
            switch(state) {
                case LOADED:
                case PAUSED:
                case STOPPED:
                    player.play();
                    break;
                case FINISHED:
                    playNext();
                    break;
            }
        }

        public function playNext():Boolean {
            var url:String = listManager.getNextURLEntry();
            if(url != null) {
                _load(url);
                player.play();
                return true;
            } else {
                state = STOPPED;
                return false;
            }
        }

        public function playPrev():Boolean {
            var url:String = listManager.getPrevURLEntry();
            if(url != null) {
                _load(url);
                player.play();
                return true;
            } else {
                state = STOPPED;
                return false;
            }
        }

        public function playIndex(index:int):Boolean {
            var url:String = listManager.getURLEntry(index);
            if(url != null) {
                _load(url);
                player.play();
                return true;
            } else {
                state = STOPPED;
                return false;
            }
        }

        public function stop(rewind:Boolean):void {
            if(rewind) {
                player.stop();
            } else {
                player.pause();
            }
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
            if(player != null) {
               player.close();
            }
        }

        public function getVideoHeight():Number {
            if((player is VideoEngine) && (vdu.metadata.videodatarate)) {
                return vdu.metadata.height;
            }
            return 0;
        }

        public function getVideoWidth():Number {
            if((player is VideoEngine) && (vdu.metadata.videodatarate)) {
                return vdu.metadata.width;
            }
            return 0;
        }

        /************************ MATRIX FUNCTIONS *******************************/
        public function getMatrix():String {
            var _matrix:Matrix = transform.matrix;
            return _matrix.a + "," + _matrix.b + "," + _matrix.c + "," + _matrix.d +
                    "," + _matrix.tx + "," + _matrix.ty;
        }

        public function setMatrix(a:Number, b:Number, c:Number, d:Number, tx:Number, ty:Number):void {
            var _matrix:Matrix = new Matrix(a, b, c, d, tx, ty);
            transform.matrix = _matrix;
        }

        /*************************** PLAY STATE HANDLERS *********************/
        private function playFinished(event:PlayStateEvent):void {
            EventUtil.fireMediaStateChanged(9, listManager.getListIndex());
            if(!playNext()) {
                playTimer.stop();
                Log.info("Media playback finished");
                EventUtil.fireMediaStateChanged(9);
                state = FINISHED;
            }
        }

        private function playStopped(event:PlayStateEvent):void {
            state = STOPPED;
            playTimer.stop();
            Log.info("Playback stopped");
            EventUtil.fireMediaStateChanged(3, listManager.getListIndex());
        }

        private function playPaused(event:PlayStateEvent):void {
            state = PAUSED;
            playTimer.stop();
            Log.info("Playback paused");
            EventUtil.fireMediaStateChanged(4, listManager.getListIndex());
        }

        private function playStarted(event:PlayStateEvent):void {
            switch(state) {
                case PAUSED:
                    Log.info("Playback resumed");
                    break;
                default:
                    Log.info("Playlist item " + listManager.getListIndex() + " : playback started");
            }
            playTimer.start();
            state = PLAYING;
            EventUtil.fireMediaStateChanged(2, listManager.getListIndex());
        }

        /************************** HELPER FUNCTIONS *****************************/
        private function _load(mediaURL:String):void {
            if(player != null) {
                player.stop(); // stop playback if any ...
                removeChildAt(0);
            }
            state = STOPPED;
            Log.info("Loading media at '" + mediaURL + "'");

            if(mediaURL.search(".mp3") >= 0) {
                player = mp3
                addChild(mp3);
                Log.info("Using MP3Engine ...");
            } else {
                player = vdu;
                addChild(vdu);
                Log.info("Using VideoEngine ...");
            }

            player._load(mediaURL);
            state = LOADED;
        }

        public function onUpdateVolumeEvent(event:SettingChangedEvent):void {
            updateVolume(Number(event.newValue));
        }

        public function updateVolume(vol:Number):void {
            mp3.setVolume(vol);
            vdu.setVolume(vol);
        }

        /******* fix streaching/cropping issue, let app auto-adjust video size *******/
        private function metaDoVDUSize(event:MetadataEvent):void {
            updateVDUSize();
        }

        public function resizeDoVDUSize(event:ResizeEvent):void {
            if(player && (player is VideoEngine)) {
                updateVDUSize();
            }
        }

// TODO: verify behaviour with fullscreen in/out
        public function updateVDUSize():void {
            var _vduHeight:int = vdu.metadata ? vdu.metadata.height : vdu.height;
            var _vduWidth:int = vdu.metadata ? vdu.metadata.width : vdu.width;

            if((height < _vduHeight) || (width < _vduWidth)) {
                vdu.percentHeight = 100;
                vdu.percentWidth = 100;
            } else {
                if(vdu.metadata) {
//                    vdu.height = vdu.metadata.height;
//                    vdu.width = vdu.metadata.width;
                }
            }
        }
    }
}