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

package com.bramosystems.oss.player.playlist {

    import com.bramosystems.oss.player.external.Log;
    import com.bramosystems.oss.player.*;
    import com.bramosystems.oss.player.events.*;
    import flash.events.*;
    import flash.net.*;

    public class Playlist extends EventDispatcher {

        private var playlist:Array;
        private var _size:uint = 0;

        public function Playlist() {
            playlist = new Array();
        }

        public function size():uint {
            return _size;
        }

        public function clear():void {
            playlist.splice(0);
            _size = 0;
            Log.info("Playlist cleared");
            dispatchEvent(new PlaylistEvent(PlaylistEvent.CLEARED));
        }

        public function remove(index:int):void {
            Log.info("'" + playlist.splice(index, 1) + "' removed from playlist!");
//            playlist.splice(index, 1);
            _size = playlist.length;
            dispatchEvent(new PlaylistEvent(PlaylistEvent.REMOVED));
            Log.info(_size + " entries left in playlist");
        }

        public function getEntry(index:int):PlaylistEntry {
            return playlist[index];
        }

        public function add(mediaUrl:String):void {
            if(mediaUrl.search(".m3u") >= 0) {
                Log.info("Adding playlist at " + mediaUrl);
                loadM3U(mediaUrl, true);
            } else {    // add single file...
                Log.info("Adding '" + mediaUrl + "' to playlist");
                _size = playlist.push(new PlaylistEntry(0, mediaUrl, mediaUrl));
                Log.info(_size + (_size > 1 ? " entries in playlist!" : " entry in playlist!"));
                dispatchEvent(new PlaylistEvent(PlaylistEvent.ADDED));
             }
        }

        /************************* PLAYLIST SHUFFLING *****************************/
        public function getShuffledIndexes():Array {
            return playlist.sort(shuffler, Array.RETURNINDEXEDARRAY);
        }

        private function shuffler(e1:PlaylistEntry, e2:PlaylistEntry):Number {
            var pos:Number = 0;
            switch(Math.round(Math.random() * 2)) {
                case 0:
                    pos = -1;
                    break;
                case 1:
                    pos = 0;
                    break;
                case 2:
                    pos = 1;
            }
            return pos;
        }

        /************************* M3U PLAYLIST SUPPORT ****************************/
        private var loader:URLLoader;
        private var m3uParser:M3UParser;
        private var mediaUrl:String;

        private function getBaseURL(url:String):String {
            return url.substring(0, url.lastIndexOf("/"));
        }

        private function loadM3U(m3uUrl:String, append:Boolean):void {
            m3uParser = new M3UParser(getBaseURL(m3uUrl));
            mediaUrl = m3uUrl;

            loader = new URLLoader();
            loader.addEventListener(IOErrorEvent.IO_ERROR, loadingErrorHandler);
            loader.addEventListener(Event.COMPLETE, loaderCompleteHandler);

            try {
                loader.load(new URLRequest(m3uUrl));
            } catch (error:Error) {
            }
        }

        private function loaderCompleteHandler(event:Event):void {
            var mp3s:Array = m3uParser.parse(loader.data);

            for(var i:uint = 0; i < mp3s.length; i++) {
                _size = playlist.push(mp3s[i]);
            }
            dispatchEvent(new PlaylistEvent(PlaylistEvent.ADDED));
            Log.info("Playlist Entries : " + _size);
        }

        private function loadingErrorHandler(event:IOErrorEvent):void {
            var txt:String = event.text.toLowerCase();
            if(txt.search("2032") >= 0) {
                Log.error("Error loading media at "+mediaUrl+".  The stream could not be opened, please check your network connection.");
            } else {
                Log.error(event.text);
            }
        }
    }
}
