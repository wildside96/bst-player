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
    import flash.errors.*;
    import flash.media.*;
    import flash.events.*;
    import flash.net.*;

    public class MP3Engine implements Engine {
            private var playerId:String;
            private var debug:Boolean = true;

            private var sound:Sound;
            private var channel:SoundChannel;
            private var sndTransform:SoundTransform = new SoundTransform(0.5);
            private var mediaUrl:String = "";
            private var soundDuration:Number = 0;
            private var position:Number = 0;
            private var isPlaying:Boolean = false, isPaused:Boolean = false;
            private var propagateID3:Boolean = false;

            private var playFinishedHandler:Function;
            private var playlist:Playlist;

            public function MP3Engine(id:String, playFinishedHandler:Function) {
                playerId = id;
                this.playFinishedHandler = playFinishedHandler;
            }

            public function load(url:String, autoplay:Boolean):void {
                if((url != null) && (url != mediaUrl)) {
                    loadMP3(url);
                    if(autoplay) {
                        play();
                    }
                }
            }

            public function setPlayPosition(channelPosition:Number):void {
                if(isPaused || isPlaying) {
                    position = channelPosition;

                    if(isPlaying) {
                        play();
                    }
                }
            }

            public function play():void {
                if(sound == null) {
                    Log.info("Player not loaded, load player first");
                    return;
                }

                if(channel != null) {
                    channel.stop();
                }
                channel = sound.play(position, 0, sndTransform);
                playStartedHandler();
                channel.addEventListener(Event.SOUND_COMPLETE, _PlayFinishedHandler);
                channel.addEventListener(Event.SOUND_COMPLETE, playFinishedHandler);
            }

            public function stop(rewind:Boolean):void {
                if(channel == null) {
                    Log.info((rewind ? "Stop " : "Pause ") +
                         "playback call ignored, sound not played yet!");
                    return;
                }

                if(rewind) {    // stop...
                    position = 0;
                    isPaused = false;
                } else {    // pause ...
                    position = channel.position;
                    isPaused = true;
                }

                channel.stop();
                isPlaying = false;
                Log.info("Player " + (rewind ? "stopped" : "paused"));
            }

            public function getPlayPosition():Number {
                if(channel == null)
                    return 0;

                if(isPlaying)
                    position = channel.position;

                return position;
            }

            public function getDuration():Number {
                return soundDuration;
            }

            public function getVolume():Number {
                if(channel == null)
                    return 0;

                return channel.soundTransform.volume;
            }

            public function setVolume(vol:Number):void {
                if(channel == null)
                    return;

                sndTransform = new SoundTransform(vol);
                channel.soundTransform = sndTransform;
                Log.info("Volume set to " + (vol * 100).toFixed(0) + "%");
            }

            public function close():void {
                try {
                    sound.close();
                    Log.info("Player closed");
                } catch(err:IOError) {
                }
            }

            public function setDebugEnabled(enabled:Boolean):void {
                debug = enabled;
            }

            private function loadMP3(mediaUrl:String):void {
                try {
                    Log.info("Loading sound at " + mediaUrl);
                    position = 0;
                    propagateID3 = true;
                    sound = new Sound();
                    sound.addEventListener(Event.COMPLETE, loadingCompleteHandler);
                    sound.addEventListener(Event.OPEN, loadingStartedHandler);
                    sound.addEventListener(IOErrorEvent.IO_ERROR, loadingErrorHandler);
                    sound.addEventListener(ProgressEvent.PROGRESS, loadingProgressHandler);
                    sound.addEventListener(Event.ID3, id3Handler);
                    sound.load(new URLRequest(mediaUrl), new SoundLoaderContext(1000, true));
                } catch(err:Error){
                    Log.error(err.message);
                }
            }

            /********************* Javascript call impls. *************************/
            private function loadingStartedHandler(event:Event):void {
               ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 1);
               Log.info("Media loading started");
            }

            private function playStartedHandler():void {
                isPlaying = true;
                isPaused = false;
                Log.info("Media playback started");
                ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 2);
            }

            private function loadingCompleteHandler(event:Event):void {
               if(sound != null)
                    soundDuration = sound.length;

               ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 10);
               Log.info("Loading complete");
            }

            private function loadingProgressHandler(event:ProgressEvent):void {
               if(sound != null)
                    soundDuration = sound.length;

               ExternalInterface.call("bstSwfSndLoadingProgress", playerId,
                    event.bytesLoaded / event.bytesTotal);
            }

            private function loadingErrorHandler(event:IOErrorEvent):void {
                var txt:String = event.text.toLowerCase();
                if(txt.search("2032") >= 0) {
                    Log.error("Error loading media at " + mediaUrl +
                        ".  The stream could not be opened, please check your network connection.");
                } else {
                    Log.error(event.text);
                }
            }

            private function id3Handler(event:Event):void {
                try {
                    if(propagateID3) {  // workarround for multiple firing ...
                        Log.info("ID3 data available");
                        ExternalInterface.call("bstSwfSndID3", playerId, sound.id3);
                        propagateID3 = false;
                    }
                } catch(err:Error) {
                    Log.info(err.message);
                }
            }

            private function _PlayFinishedHandler(event:Event):void {
                position = 0;
            }
    }
}