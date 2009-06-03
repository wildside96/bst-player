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

    public class VideoEngine implements Engine {

            private var playerId:String;
            private var debug:Boolean = true;
            private var video:VideoDisplay;

            public function VideoEngine(id:String, playFinishedHandler:Function) {
                playerId = id;
                video = new VideoDisplay();
                video.addEventListener(ProgressEvent.PROGRESS, loadingProgressHandler);
                video.addEventListener(MetadataEvent.METADATA_RECEIVED, metadataHandler);
                video.addEventListener(VideoEvent.COMPLETE, playFinishedHandler);
                video.addEventListener(VideoEvent.STATE_CHANGE, stateHandler);
            }

            /**************************** PLAYER IMPL ******************************/
            private var mediaURL:String;
            private var playlist:Playlist;
            private var propagateMeta:Boolean = false;

            public function getDisplayComponent():VideoDisplay {
                return video;
            }

            public function load(url:String, autoplay:Boolean):void {
                if((url != null) && (url != mediaURL)) {
                    video.autoPlay = autoplay;
                    loadVideo(url);
                }
            }

            public function loadVideo(mediaURL:String):void {
                 Log.info("Loading media at " + mediaURL);
                 this.mediaURL = mediaURL;

                 propagateMeta = true;
                 video.source = mediaURL;
                 playerReadyHandler();
            }

            public function play():void {
                video.play();
            }

            public function stop(rewind:Boolean):void {
                if(rewind) {
                    video.stop();
                } else {
                    video.pause();
                }
            }

            public function setVolume(vol:Number):void {
                video.volume = vol;
                Log.info("Volume set to " + (vol * 100).toFixed(0) + "%");
            }

            public function getVolume():Number {
                return video.volume;
            }

            public function getDuration():Number {
                return video.totalTime * 1000.0;
            }

            public function getPlayPosition():Number {
                return video.playheadTime * 1000.0;
            }

            public function setPlayPosition(pos:Number):void {
                video.playheadTime = pos / 1000.0;
            }

            public function close():void {
                video.close();
            }

            public function setDebugEnabled(enabled:Boolean):void {
                debug = enabled;
            }

            /********************* Javascript call impls. *************************/
            private function initCompleteNotify():void {
                ExternalInterface.call("bstSwfSndInit", playerId);
            }

            private function metadataHandler(meta:MetadataEvent):void {
                if(propagateMeta) {  // workarround for multiple firing ...
                    var hdwr:String = "Audio: ";
                    switch(meta.info.audiocodecid) {
                        case 0:
                            hdwr += "Uncompressed";
                            break;
                        case 1:
                            hdwr += "ADPCM";
                            break;
                        case 2:
                            hdwr += "MP3";
                            break;
                        case 5:
                            hdwr += "Nellymoser 8kHz Mono";
                            break;
                        case 6:
                            hdwr += "Nellymoser";
                            break;
                        default:
                            hdwr += "Unknown";
                    }

                    hdwr += ", Video: ";
                    switch(meta.info.videocodecid) {
                        case 2:
                            hdwr += "Sorenson H.263";
                            break;
                        case 3:
                            hdwr += "Screen video";
                            break;
                        case 4:
                            hdwr += "VP6 video";
                            break;
                        case 5:
                            hdwr += "VP6 video with alpha channel";
                            break;
                        default:
                            hdwr += "Unknown";
                    }
                    hdwr += ", Frame rate: " + meta.info.framerate;

                    Log.info("Media Metadata available");
                    ExternalInterface.call("bstSwfSndMetadata", playerId, meta.info.duration, hdwr);
                    propagateMeta = false;
                }
            }

            private function stateHandler(event:VideoEvent):void {
                switch(event.state) {
                    case VideoEvent.PLAYING:
                        Log.info("Media playback started");
                        playStatedHandler();
                        break;
                    case VideoEvent.STOPPED:
                        Log.info("Media playback stopped ...");
                        break;
                    case VideoEvent.PAUSED:
                        Log.info("Playback paused ...");
                        break;
//                    case NetStream.Play.StreamNotFound:
//                        Log.error("Media file not found. If the file is located on the " +
//                        "Internet, connect to the Internet. If the file is located on a " +
//                        "removable storage media, insert the storage media.");
//                        break;
                    case VideoEvent.CONNECTION_ERROR:
                        Log.error("Connection cannot be established!");
                        break;
                }
            }

            private function playerReadyHandler():void {
                ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 1);
            }

            private function playStatedHandler():void {
                Log.info("Media playback started");
                ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 2);
            }

            private function loadingProgressHandler(event:ProgressEvent):void {
                var prog:Number = event.bytesLoaded / event.bytesTotal;
                if(prog < 1.0) {
                    ExternalInterface.call("bstSwfSndLoadingProgress", playerId, prog);
                } else {
                    ExternalInterface.call("bstSwfSndMediaStateChanged", playerId, 10);
                    Log.info("Loading complete");
                }
            }
    }
}
