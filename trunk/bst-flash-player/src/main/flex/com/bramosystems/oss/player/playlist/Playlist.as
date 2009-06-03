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

    import com.bramosystems.oss.player.Log;

    public class Playlist {

        private var playlist:Array, _shuffledList:Array;
        private var listIndex:int;
        private var repeatMode:RepeatMode;
        private var playCount:int, __playCount:int;
        private var shuffleOn:Boolean;

        public function Playlist() {
            playlist = new Array();
            _shuffledList = new Array();
            listIndex = -1;
            playCount = 1;
            __playCount = 1;
            repeatMode = RepeatMode.NO_REPEAT;
            enableShuffle(false);
        }

        public function setPlayCount(count:int):void {
            if(count == 0) {
                count++;
            }
            playCount = count;
            __playCount = count;
        }

        public function getPlayCount():int {
            return playCount;
        }

        public function setRepeatMode(mode:RepeatMode):void {
            repeatMode = mode;
        }

        public function getRepeatMode():RepeatMode {
            return repeatMode;
        }

        public function add(entry:PlaylistEntry):void {
            playlist.push(entry);
        }

        public function remove(index:int):void {
            playlist.splice(index, 1);
        }

        public function getNextEntry():PlaylistEntry {
            updateListIndex();
            if(listIndex < 0)
                return null;

            return playlist[listIndex];
        }

        public function getNextURLEntry():String {
            updateListIndex();
            if(listIndex < 0)
                return null;

            return playlist[listIndex].getFileName();
        }

        public function enableShuffle(enable:Boolean):void {
            shuffleOn = enable;
            Log.info(shuffleOn ? "Shuffle turned on" : "Shuffle turned off");
        }

        public function isShuffleEnabled():Boolean {
            return shuffleOn;
        }

        private function updateListIndex():void {
            if(shuffleOn) {
                var _count:int = -1;
                var _index:int = getRandomIndex();
                try {
                    while(_shuffledList.indexOf(_index) >= 0) {
                        _index = getRandomIndex();
                        _count++;
                        if(_count >= playlist.length) {
                            throw new RangeError();
                        }
                    }
                    _shuffledList.push(_index);
                    listIndex = _index;
                } catch(e:RangeError) {
                    // list exhausted ...
                    _shuffledList = new Array();
                    switch(repeatMode) {
                        case RepeatMode.NO_REPEAT:
                            listIndex = -1;     // list exhausted ...
                            break;
                        case RepeatMode.REPEAT_ALL:
                            listIndex = 0;     // start again ...
                            break;
                        case RepeatMode.REPEAT_ONE:
                            // change not the listIndex :-).
                            break;
                    }
                }
            } else {
                switch(repeatMode) {
                    case RepeatMode.NO_REPEAT:
                        if(listIndex < (playlist.length - 1)) {
                            listIndex++;
                            _shuffledList.push(listIndex);
                        } else {
                            _shuffledList = new Array();
                            if(__playCount > 1) {
                                __playCount--;
                                listIndex = 0;
                            } else {
                                listIndex = -1;     // list exhausted ...
                            }
                        }
                        break;
                    case RepeatMode.REPEAT_ALL:
                        if(listIndex < (playlist.length - 1)) {
                            listIndex++;
                        } else {
                            listIndex = 0;     // start again ...
                        }
                        break;
                    case RepeatMode.REPEAT_ONE:
                        // change not the listIndex :-).
                        break;
                }
            }
        }

        private function getRandomIndex():int {
            return int(Math.round(Math.random() * (playlist.length - 1)));
        }
    }
}
