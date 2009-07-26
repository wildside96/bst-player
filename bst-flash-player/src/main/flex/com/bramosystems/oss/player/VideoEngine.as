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
    import com.bramosystems.oss.player.external.*;
    import com.bramosystems.oss.player.events.*;

    import flash.media.*;
    import flash.events.*;
    import mx.controls.*;
    import mx.events.*;

    public class VideoEngine extends VideoDisplay implements Engine {

            public function VideoEngine() {
                addEventListener(ProgressEvent.PROGRESS, loadingProgressHandler);
                addEventListener(MetadataEvent.METADATA_RECEIVED, metadataHandler);
                addEventListener(VideoEvent.STATE_CHANGE, stateHandler);
            }

            /**************************** PLAYER IMPL ******************************/
            private var mediaURL:String = "";
            private var playlist:Playlist;
            private var propagateMeta:Boolean = false;

            public function _load(url:String):void {
                if((url != null) && (url != mediaURL)) {

                    visible = false;
                    autoPlay = false;
                    this.mediaURL = url;
                    propagateMeta = true;
                    source = url;
                    playerReadyHandler();
                }
            }

            override public function play():void {
                if(source == null) {
                    Log.info("Player not loaded, load player first");
                    throw new Error("Player not loaded, load player first");
                }

                super.play();
            }

            public function setVolume(volume:Number):void {
                this.volume = volume;
            }

            public function getDuration():Number {
                return totalTime * 1000.0;
            }

            public function getPlayPosition():Number {
                return playheadTime * 1000.0;
            }

            public function setPlayPosition(pos:Number):void {
                playheadTime = pos / 1000.0;
            }

            /********************* Javascript call impls. *************************/
            private function metadataHandler(meta:MetadataEvent):void {
                if(propagateMeta) {  // workarround for multiple firing ...
                    var hdwr:String = "Audio Codec: ";
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
                    hdwr += ", Audio data rate: " + meta.info.audiodatarate;

                    hdwr += ", Video: ";
                    switch(meta.info.videocodecid) {
                        case 2:
                            hdwr += "Sorenson H.263";
                            visible = true;
                            break;
                        case 3:
                            hdwr += "Screen video";
                            visible = true;
                            break;
                        case 4:
                            hdwr += "VP6 video";
                            visible = true;
                            break;
                        case 5:
                            hdwr += "VP6 video with alpha channel";
                            visible = true;
                            break;
                        default:
                            hdwr += "Unknown";
                    }
                    hdwr += ", Video data rate: " + meta.info.videodatarate;
                    hdwr += ", Frame rate: " + meta.info.framerate;

                    Log.info("Media Metadata available");
                    EventUtil.fireVideoMetadata(meta.info.duration, hdwr, meta.info.width, meta.info.height);
                    propagateMeta = false;
                }
            }

            private function stateHandler(event:VideoEvent):void {
                switch(event.state) {
                    case VideoEvent.COMPLETE:
                        dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_FINISHED));
                        break;
                    case VideoEvent.PLAYING:
                        dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_STARTED));
                        break;
                    case VideoEvent.STOPPED:
                        dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_STOPPED));
                        break;
                    case VideoEvent.PAUSED:
                        dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_PAUSED));
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
                EventUtil.fireMediaStateChanged(1);
            }

            private function loadingProgressHandler(event:ProgressEvent):void {
                var prog:Number = event.bytesLoaded / event.bytesTotal;
                if(prog < 1.0) {
                    EventUtil.fireLoadingProgress(prog);
                } else {
                    EventUtil.fireMediaStateChanged(10);
                    Log.info("Loading complete");
                }
            }
    }
}
