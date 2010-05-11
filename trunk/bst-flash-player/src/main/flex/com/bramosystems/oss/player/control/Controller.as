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
    import mx.core.UIComponent;
    import mx.containers.*;
    import mx.controls.*;
    import mx.events.ResizeEvent;

    public class Controller extends HBox { //UIComponent {
        private var play:UIComponent, next:UIComponent;
        private var prev:UIComponent, scr:UIComponent;
        private var timeLabel:Label;
        private var playButtonShowPlay:Boolean = true;
        private var isFullScreen:Boolean = false;

        private var player:Player;
        private var playlist:Playlist;
        private var seekbar:Seekbar;

        public function Controller(_player:Player, _setting:Setting, _playlist:Playlist) {
            player = _player;
            playlist = _playlist;

            x = 0;
            setStyle("bottom", "0");
            setStyle("verticalAlign", "middle");
            setStyle("horizontalGap", 4);

//            height = 20;
            percentWidth = 100;
//            addEventListener(Event.ADDED, onAdded);

            seekbar = new Seekbar();

            addChild(getPlay());
            addChild(getPrev());
            addChild(getNext());
            addChild(seekbar);
            addChild(getTimeLabel());
            addChild(new VolumeControl(_setting));
            addChild(getToggleScreen());

            renderPlay();
            renderNext();
            renderPrev();
        }

        public function updateSize(event:ResizeEvent): void {
//            drawControls();
        }

        private function onAdded(event:Event):void {
//            drawControls();
        }

        private function enableButton(button:UIComponent, enable:Boolean):void {
            button.enabled = enable;
            button.mouseEnabled = enable;
        }

        /********************** Events Callback Hooks *************************/
        /**
         * state IDs:
         *  1: loading started...
         *  2: play started...
         *  3: play stopped...
         *  4: play paused...
         *  9: play finished...
         * 10: loading complete ...
         */
        public function onMediaStateChanged(state:int, listIndex:int):void {
            switch(state) {
                case 2:
                    playButtonShowPlay = false;
                    break;
                case 3:
                    playButtonShowPlay = true;
                    break;
                case 4:
                    playButtonShowPlay = true;
                    break;
                case 9:
                    playButtonShowPlay = false;
                    break;
                case 10:
                    break;
            }

            enableButton(next, (listIndex + 1) < playlist.size());
            enableButton(prev, listIndex > 0);

            renderPlay();
            renderPrev();
            renderNext();
        }

        public function onLoadingProgress(progress:Number):void {
            seekbar.updateLoadingProgress(progress);
        }

        /**************** BUTTON CLICK EVENTS ****************************/
        private function togglePlay(event:MouseEvent):void {
            playButtonShowPlay = !playButtonShowPlay;
            if(playButtonShowPlay) {
                player.stop(false);
            } else {
                player.play();
            }
        }

        private function onNext(event:MouseEvent):void {
            player.playNext();
        }

        private function onPrev(event:MouseEvent):void {
            player.playPrev();
        }

        /********************** UI Stuffs *******************************/
        private function getPlay():UIComponent {
            play = new UIComponent();
            play.buttonMode = true;
            play.addEventListener(MouseEvent.CLICK, togglePlay);
            play.width = 20;
            play.height = 20;
            return play;
        }

        private function getTimeLabel():Label {
            timeLabel = new Label();
            timeLabel.text = "00:00";
            return timeLabel;
        }

        private function getToggleScreen():UIComponent {
            scr = new UIComponent();
            scr.width = 20;
            scr.height = 20;
            scr.addEventListener(MouseEvent.CLICK, onToggleScreen);
            scr.buttonMode = true;

            _drawToggler(!isFullScreen);
            _drawToggler(isFullScreen);
            return scr;
        }

        private function onToggleScreen(event:MouseEvent):void {
            isFullScreen = !isFullScreen;
            _drawToggler(isFullScreen);
            _drawToggler(!isFullScreen);
        }

        private function _drawToggler(front:Boolean):void {
            scr.graphics.lineStyle(1, front ? 0xffaaff : 0x000000);
            scr.graphics.beginFill(front ? 0xffaaff : 0x000000);
            if(front) {
                scr.graphics.drawRoundRect(6, 6, 14, 14, 1);
            } else {
                scr.graphics.drawRoundRect(2, 2, 14, 14, 1);
            }
            scr.graphics.endFill();
        }

        private function getNext():UIComponent {
            next = new UIComponent();
            next.addEventListener(MouseEvent.CLICK, onNext);
            next.buttonMode = true;
            next.width = 20;
            next.height = 20;
            return next;
        }

        private function getPrev():UIComponent {
            prev = new UIComponent();
            prev.addEventListener(MouseEvent.CLICK, onPrev);
            prev.width = 20;
            prev.height = 20;
            prev.buttonMode = true;
            return prev;
        }

        private function getSlider():HSlider {
            var slid:HSlider = new HSlider();
            slid.minimum = 0;
            slid.maximum = 100;
            slid.liveDragging = true;
            slid.percentWidth = 100;
            slid.setStyle("dataTipOffset", "5");

//            slid.buttonMode = true;
//            slid.graphics.lineStyle(2, 0x000000);
//            slid.graphics.moveTo(0, 0);
//            slid.graphics.lineTo(12, 8);
//            slid.graphics.lineTo(0, 16);
//            slid.graphics.lineTo(0, 0);
            return slid;
        }


        /******************** RENDERING METHODS ****************************/
        private function renderPlay():void {
            var _color:uint = play.enabled ? 0xffaaff : 0xcccccc;

            play.graphics.clear();
            play.graphics.beginFill(_color);
            if(playButtonShowPlay) {
                play.graphics.lineStyle(2, _color);
                play.graphics.moveTo(4, 2);
                play.graphics.lineTo(12, 8);
                play.graphics.lineTo(4, 16);
                play.graphics.lineTo(4, 2);
                play.graphics.endFill();
            } else {
                play.graphics.lineStyle(1, _color);
                play.graphics.drawRoundRect(3, 3, 3, 14, 1);

//                play.graphics.lineStyle(1, 0xffffff);
//                play.graphics.beginFill(0xffffff);
//                play.graphics.drawRoundRect(6, 3, 4, 14, 1);
//                play.graphics.endFill();

//                play.graphics.lineStyle(1, _color);
//                play.graphics.beginFill(_color);
                play.graphics.drawRoundRect(10, 3, 3, 14, 1);
            }
            play.graphics.endFill();
        }

        private function renderNext():void {
            var _color:uint = next.enabled ? 0xffaaff : 0xcccccc;

            next.graphics.lineStyle(1, _color);
            next.graphics.beginFill(_color);
            next.graphics.moveTo(4, 2);
            next.graphics.lineTo(12, 8);
            next.graphics.lineTo(4, 16);
            next.graphics.lineTo(4, 2);

            next.graphics.moveTo(12, 2);
            next.graphics.lineTo(12, 16);
            next.graphics.endFill();
        }

        private function renderPrev():void {
            var _color:uint = prev.enabled ? 0xffaaff : 0xcccccc;

            prev.graphics.lineStyle(1, _color);
            prev.graphics.beginFill(_color);

            prev.graphics.moveTo(12, 2);
            prev.graphics.lineTo(12, 16);
            prev.graphics.lineTo(4, 8);
            prev.graphics.lineTo(12, 2);

            prev.graphics.moveTo(4, 2);
            prev.graphics.lineTo(4, 16);
            prev.graphics.endFill();
        }
    }
}