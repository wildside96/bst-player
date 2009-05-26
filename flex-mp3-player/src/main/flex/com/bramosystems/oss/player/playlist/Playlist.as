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

    public class Playlist {

        private var playlist:Array, _shuffledList:Array;
        private var listIndex:int;
        private var repeatMode:RepeatMode;
        private var playCount:int, __playCount:int;
        private var shuffleOn:Boolean;

        public function Playlist() {
            playlist = new Array();
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

            return shuffleOn ? playlist[_shuffledList[listIndex]] : playlist[listIndex];
        }

        public function getNextURLEntry():String {
            updateListIndex();
            if(listIndex < 0)
                return null;

            return shuffleOn ? playlist[_shuffledList[listIndex]].getFileName() :
                playlist[listIndex].getFileName();
        }

        public function enableShuffle(enable:Boolean):void {
            shuffleOn = enable;
            if(enable) {
                _shuffledList = playlist.sort(shuffler, Array.RETURNINDEXEDARRAY);
            }
        }

        public function isShuffleEnabled():Boolean {
            return shuffleOn;
        }

        public function checkShuffle():void {
            if(shuffleOn) {
                _shuffledList = playlist.sort(shuffler, Array.RETURNINDEXEDARRAY);
            }
        }

        private function updateListIndex():void {
            switch(repeatMode) {
                case RepeatMode.NO_REPEAT:
                    if(listIndex < (playlist.length - 1)) {
                        listIndex++;
                    } else {
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

        // shuffle algorithm ...
        private function shuffler(value1:PlaylistEntry, value2:PlaylistEntry):int {
            if(Math.random() > 0.5) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
