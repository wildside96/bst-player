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
        private var _usedIndices:Array;
        private var _index:int;
        private var repeatMode:RepeatMode;
        private var shuffleOn:Boolean;
        private var loopCount:int;

        public function Playlist(_setting:Setting) {
            setting = _setting;
            playlist = new Array();
            _usedIndices = new Array();
            _index = -1;
            repeatMode = RepeatMode.OFF;

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

        public function getEntry(idx:int, updateIndex:Boolean):PlaylistEntry {
            if(updateIndex) {
                if (idx >= 0 && idx < size()) { 
                    _index = idx;
                    if(_usedIndices.indexOf(idx) < 0) { // remember index is used
                        _usedIndices.push(idx);
                    }
                }
            }
            return playlist[idx];
        }

        public function getIndex():int {
            return _index;
        }

        public function add(mediaUrl:String):void {
            if(mediaUrl.search(".m3u") >= 0) {
                Log.info("Adding playlist at " + mediaUrl);
                loadM3U(mediaUrl, true);
            } else {    // add single file...
                Log.info("Playlist: #" + 
                    (playlist.push(new PlaylistEntry(0, mediaUrl, mediaUrl)) - 1) +
                    " - '" + mediaUrl + "'");
                dispatchEvent(new PlaylistEvent(PlaylistEvent.ADDED));
             }
        }

        /************************ REPEAT SUPPORT *******************************/
        public function setRepeatMode(mode:String):void {
            repeatMode = RepeatMode.getMode(mode);
            Log.info("Repeat Mode changed : '" + repeatMode.getName() + "'");
        }

        public function setRepeat(mode:RepeatMode):void {
            repeatMode = mode;
        }

        public function getRepeat():RepeatMode {
            return repeatMode;
        }

        /************************ NEXT/PREV SUPPORT *******************************/
        public function getNext(ignoreLoopCount:Boolean):String {
            if(!computeIndex(true, ignoreLoopCount)) {
                return playlist[_index].getFileName();
            } else {
                return null;
            }
        }

        public function getPrev(ignoreLoopCount:Boolean):String {
            if(!computeIndex(false, ignoreLoopCount)) {
                return playlist[_index].getFileName();
            } else {
                return null;
            }
        }

        /************************* EVENT HANDLERS ********************************/
        private function updateShuffle(event:SettingChangedEvent):void {
            shuffleOn = Boolean(event.newValue);
        }

        private function updateLoopCount(event:SettingChangedEvent):void {
            loopCount = parseInt(event.newValue);
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
                Log.info("Playlist: #" + (playlist.push(mp3s[i]) - 1) + " - '" + mp3s[i].getFileName + "'");
            }
            if(mp3s.length > 0) {
                dispatchEvent(new PlaylistEvent(PlaylistEvent.ADDED));
            }
        }

        private function loadingErrorHandler(event:IOErrorEvent):void {
            var txt:String = event.text.toLowerCase();
            if(txt.search("2032") >= 0) {
                Log.error("Error loading media at "+mediaUrl+".  The stream could not be opened, please check your network connection.");
            } else {
                Log.error(event.text);
            }
        }

        /************************* Loop Management *************************************/
        private function computeIndex(up:Boolean, ignoreLoopCount:Boolean):Boolean {
            if (setting.getLoopCount() < 0) {
                repeatMode = RepeatMode.ALL;
            }

            var endOfList:Boolean = false;
            switch(repeatMode) {
                case RepeatMode.OFF:
                    endOfList = _suggestIndex(up, false);
                    break;
                case RepeatMode.ONE:
                    if(ignoreLoopCount) {
                        loopCount = setting.getLoopCount() - 1;
                        endOfList = _suggestIndex(up, false);
                    } else {
                        _index = _index < 0 ? 0 : _index;
                        if(setting.getLoopCount() > 1) {
                            if(loopCount-- == 0) {
                                loopCount = setting.getLoopCount() - 1;
                                endOfList = _suggestIndex(up, false);
                            }
                        }
                    }
                    break;
                case RepeatMode.ALL:
                    if(setting.getLoopCount() > 1) {
                        endOfList = _suggestIndex(up, false);
                        loopCount = endOfList ? loopCount - 1 : loopCount;
                        endOfList = loopCount == 0;
                        _index = !endOfList && (_index < 0) ? 0 : _index;
                        if(endOfList && (loopCount == 0)) {
                            loopCount = setting.getLoopCount();
                            endOfList = true;
                        }
                    } else {
                        endOfList = _suggestIndex(up, true);
                    }
                    break;
            }
            return endOfList;
        }

        private function _suggestIndex(up:Boolean, canRepeat:Boolean):Boolean {
            if (_index < 0 && canRepeat) {  // prepare for another iteration ...
                _usedIndices.splice(0);
                _index = up ? 0 : size();
            } else {
                _index = _suggestIndexImpl(up);
            }

            if (shuffleOn) {
                var _count:int = 0;
                while ((_index < 0) || (_usedIndices.indexOf(_index) >= 0)) {
                    _index = _suggestIndexImpl(up);
                    _count++;
                    if (_count == size() * 3) {
                        _index = -1;
                        break;
                    }
                }
            }

            if (_index == size()) {
                _index = -1;
            }

            if (_index < 0 && canRepeat) {  // prepare for another iteration ...
                _usedIndices.splice(0);
                _index = up ? 0 : size();
            }

            if (_index >= 0) { // keep the used index ...
                _usedIndices.push(_index);
                return false; // valid index
            }
            return true;  // end of list
        }

        private function _suggestIndexImpl(up:Boolean):int {
            return shuffleOn ? Math.round(Math.random() * size() - 1) : (up ? ++_index : --_index);
        }
    }
}
