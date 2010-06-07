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
    import mx.events.ResizeEvent;

    public class Controller extends HBox { //UIComponent {
        private var play:UIComponent, next:UIComponent;
        private var prev:UIComponent, toggler:UIComponent;
        private var timeLabel:Label;
        private var playButtonShowPlay:Boolean = true;
        private var isFullScreen:Boolean = false;

        private var player:Player;
        private var playlist:Playlist;
        private var seekbar:Seekbar;

        private const _size:int = 18;
        private const _margin:int = 2;
        private const _activeColor:uint = 0xffaaff;
        private const _inactiveColor:uint = 0xcccccc;

        public function Controller(_player:Player, _setting:Setting, _playlist:Playlist) {
            player = _player;
            playlist = _playlist;

            x = 0;
            setStyle("verticalAlign", "middle");
            setStyle("horizontalGap", 4);

            percentWidth = 100;
//            addEventListener(Event.ADDED, onAdded);

            seekbar = new Seekbar();

            addChild(getPlay());
            addChild(getPrev());
            addChild(getNext());
            addChild(seekbar);
            addChild(getTimeLabel());
            addChild(new VolumeControl(_setting));
            addChild(getScreenToggler());

            Application.application.stage.addEventListener(FullScreenEvent.FULL_SCREEN, onFullScreen);
            updateDisplay(false);
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

        private function updateDisplay(fullScreen:Boolean):void {
//            setStyle("paddingTop", fullScreen ? 5 : );
            setStyle("paddingBottom", fullScreen ? 5 : 2);
            setStyle("paddingLeft", fullScreen ? 5 : 2);
            setStyle("paddingRight", fullScreen ? 5 : 2);

            renderPlay();
            renderNext();
            renderPrev();

            renderToggler(fullScreen);
            renderToggler(!fullScreen);
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
                case 1:
                    onLoadingProgress(0.0);
                    break;
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
                    onPlayingProgress(0.0);
                    break;
                case 10:
                    onLoadingProgress(1.0);
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

        public function onPlayingProgress(progress:Number):void {
            seekbar.updatePlayingProgress(progress, player.getDuration());
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

        private function onToggleScreen(event:MouseEvent):void {
            switch(Application.application.stage.displayState) {
                case StageDisplayState.NORMAL:
                    Application.application.stage.displayState = StageDisplayState.FULL_SCREEN;
                    break;
                case StageDisplayState.FULL_SCREEN:
                default:
                    Application.application.stage.displayState = StageDisplayState.NORMAL;
                    break;
            }
        }

        private function onFullScreen(event:FullScreenEvent):void {
//            updateDisplay(event.fullScreen);
        }

        /********************** UI Stuffs *******************************/
        private function getPlay():UIComponent {
            play = new UIComponent();
            play.buttonMode = true;
            play.addEventListener(MouseEvent.CLICK, togglePlay);
            play.width = _size * 3/4;
            play.height = _size;
            return play;
        }

        private function getTimeLabel():Label {
            timeLabel = new Label();
            timeLabel.text = "00:00";
            return timeLabel;
        }

        private function getScreenToggler():UIComponent {
            toggler = new UIComponent();
            toggler.width = _size;
            toggler.height = _size;
            toggler.addEventListener(MouseEvent.CLICK, onToggleScreen);
            toggler.buttonMode = true;
            return toggler;
        }

        private function getNext():UIComponent {
            next = new UIComponent();
            next.addEventListener(MouseEvent.CLICK, onNext);
            next.buttonMode = true;
            next.width = _size * 3 / 4;
            next.height = _size;
            return next;
        }

        private function getPrev():UIComponent {
            prev = new UIComponent();
            prev.addEventListener(MouseEvent.CLICK, onPrev);
            prev.width = _size * 3 / 4;
            prev.height = _size;
            prev.buttonMode = true;
            return prev;
        }

        /******************** RENDERING METHODS ****************************/
        private function renderPlay():void {
            var _color:uint = play.enabled ? _activeColor : _inactiveColor;

            play.graphics.clear();
            play.graphics.beginFill(_color);
            play.graphics.lineStyle(1, _color);
            if(playButtonShowPlay) {
                play.graphics.moveTo(_margin, _margin);
                play.graphics.lineTo(play.width - (_margin * 2), (play.height - (_margin * 2)) / 2);
                play.graphics.lineTo(_margin, play.height - (_margin * 2));
                play.graphics.lineTo(_margin, _margin);
                play.graphics.endFill();
            } else { // show pause
                play.graphics.drawRoundRect(_margin, _margin, (play.width - (_margin * 2)) / 3,
                        play.height - (_margin * 2), 1);
                play.graphics.drawRoundRect(_margin + ((play.width - (_margin * 2)) * 2 / 3),
                        _margin, (play.width - (_margin * 2)) / 3, play.height - (_margin * 2), 1);
            }
            play.graphics.endFill();
        }

        private function renderNext():void {
            var _color:uint = next.enabled ? _activeColor : _inactiveColor;

            next.graphics.lineStyle(1, _color);
            next.graphics.beginFill(_color);
            next.graphics.moveTo(_margin, _margin);
            next.graphics.lineTo(next.width - (_margin * 2), (next.height - (_margin * 2)) / 2);
            next.graphics.lineTo(_margin, next.height - (_margin * 2));
            next.graphics.lineTo(_margin, _margin);

            next.graphics.moveTo(next.width - (_margin * 2), _margin);
            next.graphics.lineTo(next.width - (_margin * 2), next.height - (_margin * 2));
            next.graphics.endFill();
        }

        private function renderPrev():void {
            var _color:uint = prev.enabled ? _activeColor : _inactiveColor;

            prev.graphics.lineStyle(1, _color);
            prev.graphics.beginFill(_color);
            prev.graphics.moveTo(prev.width - (_margin * 2), _margin);
            prev.graphics.lineTo(prev.width - (_margin * 2), prev.height - (_margin * 2));
            prev.graphics.lineTo(_margin, (prev.height - (_margin * 2)) / 2);
            prev.graphics.lineTo(prev.width - (_margin * 2), _margin);

            prev.graphics.moveTo(_margin, _margin);
            prev.graphics.lineTo(_margin, prev.height - (_margin * 2));
            prev.graphics.endFill();
        }

        private function renderToggler(front:Boolean):void {
            toggler.graphics.lineStyle(1, front ? _activeColor : _inactiveColor);
            toggler.graphics.beginFill(front ? _activeColor : _inactiveColor);
            if(front) {
                toggler.graphics.drawRoundRect(6, 6, 14, 14, 1);
            } else {
                toggler.graphics.drawRoundRect(2, 2, 14, 14, 1);
            }
            toggler.graphics.endFill();
        }
    }
}