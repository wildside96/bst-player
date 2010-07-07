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

        private var setting:Setting;
        private var playlist:Array;
        private var _index:int;
        private var repeatMode:RepeatMode;
        private var shuffleOn:Boolean;
        private var loopCount:int;

        public function Playlist(setting:Setting) {
            playlist = new Array();
            _index = -1;
            repeatMode = RepeatMode.NO_REPEAT;

            shuffleOn = setting.isShuffleEnabled();
            loopCount = setting.getLoopCount();
            setting.addEventListener(SettingChangedEvent.SHUFFLE_CHANGED, updateShuffle);
            setting.addEventListener(SettingChangedEvent.LOOP_COUNT_CHANGED, updateLoopCount);
        }

        public function size():uint {
            return playlist.length;
        }

        public function clear():void {
            playlist.splice(0);
            Log.info("Playlist cleared");
            dispatchEvent(new PlaylistEvent(PlaylistEvent.CLEARED));
            _index = -1;
        }

        public function remove(index:int):void {
            Log.info("'" + playlist.splice(index, 1) + "' removed from playlist!");
            dispatchEvent(new PlaylistEvent(PlaylistEvent.REMOVED));
            Log.info(playlist.length + " entries left in playlist");
        }

        public function getEntry(index:int):PlaylistEntry {
            return playlist[index];
        }

        public function getIndex():int {
            return _index;
        }

        public function add(mediaUrl:String):void {
            if(mediaUrl.search(".m3u") >= 0) {
                Log.info("Adding playlist at " + mediaUrl);
                loadM3U(mediaUrl, true);
            } else {    // add single file...
                Log.info("Adding '" + mediaUrl + "' to playlist");
                var _size:uint = playlist.push(new PlaylistEntry(0, mediaUrl, mediaUrl));
                Log.info(_size + (_size > 1 ? " entries in playlist!" : " entry in playlist!"));
                dispatchEvent(new PlaylistEvent(PlaylistEvent.ADDED));
             }
        }

        /************************ REPEAT SUPPORT *******************************/
        public function setRepeat(mode:RepeatMode):void {
            repeatMode = mode;
        }

        /************************ NEXT/PREV SUPPORT *******************************/
        public function getNext():PlaylistEntry {
            if(!computeIndex(true)) {
                return playlist[_index];
            } else {
                return null;
            }
        }

        public function getNextURLEntry():String {
            var entry:PlaylistEntry = getNext();
            if(entry != null) {
                return entry.getFileName();
            } else {
                return null;
            }
        }

        public function getPrev():PlaylistEntry {
            if(!computeIndex(false)) {
                return playlist[_index];
            } else {
                return null;
            }
        }

        public function getPrevURLEntry():String {
            var entry:PlaylistEntry = getPrev();
            if(entry != null) {
                return entry.getFileName();
            } else {
                return null;
            }
        }

        public function hasNext():Boolean {
            var has:Boolean = true;
            switch(repeatMode) {
                case RepeatMode.NO_REPEAT:
                case RepeatMode.CUSTOM_REPEAT:
                    has = hasPlaylistNext();
                    break;
                case RepeatMode.REPEAT_ONE:
                case RepeatMode.REPEAT_ALL:
                    has = true;
            }
            return has;
        }

        public function hasPrev():Boolean {
            var has:Boolean = true;
            switch(repeatMode) {
                case RepeatMode.NO_REPEAT:
                case RepeatMode.CUSTOM_REPEAT:
                    has = hasPlaylistPrev();
                    break;
                case RepeatMode.REPEAT_ONE:
                case RepeatMode.REPEAT_ALL:
                    has = true;
            }
            return has;
        }

        private function computeIndex(up:Boolean):Boolean {
            var endOfList:Boolean = false;
            var checkLoopCount:Boolean = false;
            switch(repeatMode) {
                case RepeatMode.CUSTOM_REPEAT:
                    checkLoopCount = true;
                case RepeatMode.NO_REPEAT:
                    if(up && hasNext()) {
                        _index++;
                    } else if(!up && hasPrev()) {
                        _index--;
                    } else {
                        _index = 0;
                        if(checkLoopCount && (loopCount > 1)) {
                            loopCount--;
                        } else {
                            endOfList = true;
                        }
//                            if(shuffleOn) {
//                                shuffleList();
//                            }
                    }
                    break;
                case RepeatMode.REPEAT_ONE:
                    break;
                case RepeatMode.REPEAT_ALL:
                    if(up) {
                        _index++;
                        if(_index >= size()) {
                            _index = 0;
                        }
                    } else {
                        _index--;
                        if(_index < 0) {
                            _index = size() - 1;
                        }
                    }
                    break;
            }
            return endOfList;
        }

        public function hasPlaylistNext():Boolean {
            return _index < (size() - 1);
        }

        public function hasPlaylistPrev():Boolean {
            return _index > 0;
        }

        /************************* EVENT HANDLERS ********************************/
        private function updateShuffle(event:SettingChangedEvent):void {
            shuffleOn = Boolean(event.newValue);
            if(shuffleOn) {
//                shuffleList();
            }
        }

        private function updatePlaylist(event:PlaylistEvent):void {
            if(shuffleOn) {
//                shuffleList();
            }
        }

        private function updateLoopCount(event:SettingChangedEvent):void {
            loopCount = parseInt(event.newValue);
            if(loopCount < 0) {
                repeatMode = RepeatMode.REPEAT_ALL;
            } else if(loopCount == 1){
                repeatMode = RepeatMode.NO_REPEAT;
            } else {
                repeatMode = RepeatMode.CUSTOM_REPEAT;
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
                playlist.push(mp3s[i]);
            }
            Log.info("Playlist Entries : " + playlist.length);
            dispatchEvent(new PlaylistEvent(PlaylistEvent.ADDED));
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
