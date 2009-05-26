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

    public class MP3Engine {
            private var playerId:String;
            private var debug:Boolean = true;

            private var sound:Sound;
            private var channel:SoundChannel;
            private var sndTransform:SoundTransform = new SoundTransform(0.5);
            private var mediaUrl:String = "";
            private var soundDuration:Number = 0;
            private var position:Number = 0;
            private var isPlaying:Boolean = false, isPaused:Boolean = false;

            private var playlist:Playlist;

            public function MP3Engine(id:String) {
                playerId = id;
            }

            public function setPlaylist(playlist:Playlist):void {
                this.playlist = playlist;

                var url:String = playlist.getNextURLEntry();
                if((url != null) && (url != mediaUrl)) {
                    loadMP3(url);
                }
            }

            public function getPlaylist():Playlist {
                return playlist;
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
                    log("Player not loaded, load player first");
                    return;
                }

                if(channel != null) {
                    channel.stop();
                }
                channel = sound.play(position, 0, sndTransform);
                playStatedHandler();
                channel.addEventListener(Event.SOUND_COMPLETE, playFinishedHandler);
            }

            public function stop():void {
                if(channel == null) {
                    log("Stop playback call ignored, sound not played yet!");
                    return;
                }

                channel.stop();
                position = 0;
                isPlaying = false;
                isPaused = false;
                log("Player stopped");
            }

            public function pause():void {
                if(channel == null) {
                    log("Pause playback call ignored, sound not played yet!");
                    return;
                }

                position = channel.position;
                channel.stop();
                isPlaying = false;
                isPaused = true;
                log("Player paused");
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
                log("Volume set to " + (vol * 100).toFixed(0) + "%");
            }

            public function close():void {
                try {
                    sound.close();
                    log("Player closed");
                } catch(err:IOError) {
                }
            }

            public function setDebugEnabled(enabled:Boolean):void {
                debug = enabled;
            }

            private function loadMP3(mediaUrl:String):void {
                try {
                    log("Loading sound at " + mediaUrl);
                    sound = new Sound();
                    sound.addEventListener(Event.COMPLETE, loadingCompleteHandler);
                    sound.addEventListener(Event.OPEN, loadingStartedHandler);
                    sound.addEventListener(IOErrorEvent.IO_ERROR, loadingErrorHandler);
                    sound.addEventListener(ProgressEvent.PROGRESS, loadingProgressHandler);
                    sound.addEventListener(Event.ID3, id3Handler);
                    sound.load(new URLRequest(mediaUrl), new SoundLoaderContext(1000, true));
                } catch(err:Error){
                    logError(err.message);
                }
            }

            /********************* Javascript call impls. *************************/
            private function loadingStartedHandler(event:Event):void {
               ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 1);
               log("Media loading started");
            }

            private function playStatedHandler():void {
                isPlaying = true;
                isPaused = false;
                log("Media playback started");
                ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 2);
            }

            private function loadingCompleteHandler(event:Event):void {
               if(sound != null)
                    soundDuration = sound.length;

               ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 10);
               log("Loading complete");
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
                    logError("Error loading media at " + mediaUrl +
                        ".  The stream could not be opened, please check your network connection.");
                } else {
                    logError(event.text);
                }
            }

            private function id3Handler(event:Event):void {
                try {
                    log("ID3 data available");
                    ExternalInterface.call("bstSwfSndID3", playerId, sound.id3);
                } catch(err:Error) {
                    log(err.message);
                }
            }

            private function playFinishedHandler(event:Event):void {
                position = 0;
                var url:String = playlist.getNextURLEntry();
                if(url != null) {
                    if(url != mediaUrl) {
                        loadMP3(url);
                    }
                    play();
                } else {
                   log("Media playback finished");
                   ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 9);
                }
            }

            // Event handlers...
            private function log(report:String):void {
                ExternalInterface.call("bstSwfSndDebug", playerId, report);
            }

            private function logError(error:String):void {
                ExternalInterface.call("bstSwfSndError", playerId, error);
            }
    }
}