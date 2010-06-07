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

package com.bramosystems.oss.player.control {

    import com.bramosystems.oss.player.*;
    import com.bramosystems.oss.player.playlist.Playlist;

    import flash.display.*;
    import flash.events.*;

    import mx.core.*;
    import mx.containers.*;
    import mx.controls.*;
    import mx.events.*;
    import mx.managers.ToolTipManager;

    public class Seekbar extends Canvas {
        private const HEIGHT:uint = 8;
        private var loading:Canvas, playing:Canvas;
        private var tip:ToolTip;

        public function Seekbar() {
            x = 0;
            y = 0;
            height = HEIGHT;
            percentWidth = 100;
            setStyle("backgroundColor", "0xcccccc");

            loading = new Canvas();
            playing = new Canvas();

            initProgressBar(loading, false);
            initProgressBar(playing, true);

            addChild(loading);
            addChild(playing);
        }

        /********************** Events Callback Hooks *************************/
        public function updateLoadingProgress(progress:Number):void {
            loading.percentWidth = progress * 100;
        }

        public function updatePlayingProgress(progress:Number, duration:Number):void {
            playing.percentWidth = progress / duration * 100;
        }
        /**************** BUTTON CLICK EVENTS ****************************/
        private function onPrev(event:MouseEvent):void {
        }

        /********************** UI Stuffs *******************************/
        private function initProgressBar(bar:Canvas, playing:Boolean):void {
            bar.x = 0;
            bar.y = 0;
            bar.height = HEIGHT;
            bar.buttonMode = true;
            bar.setStyle("backgroundColor", playing ? "0xffaa00" : "0xaa00ff");
//            bar.addEventListener(MouseEvent.CLICK, togglePlay);
            bar.addEventListener(MouseEvent.MOUSE_OVER, updateTip);
            bar.addEventListener(MouseEvent.MOUSE_OUT, clearTip);
            bar.addEventListener(MouseEvent.MOUSE_MOVE, updateTip);
        }

        /******************** TOOL TIP METHODS ****************************/
        private function updateTip(event:MouseEvent):void {
            if(tip) {
                clearTip(null);
            }
            var pt:uint = event.localX / width * 100;
            tip = ToolTipManager.createToolTip("" + pt, event.stageX - 20,
                            parent.y - parent.height) as ToolTip;
            
        }

        private function clearTip(event:MouseEvent):void {
            ToolTipManager.destroyToolTip(tip);
            tip = null;
        }
    }
}