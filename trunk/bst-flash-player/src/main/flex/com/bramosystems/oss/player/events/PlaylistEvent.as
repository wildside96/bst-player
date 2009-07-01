package com.bramosystems.oss.player.events {

    import flash.events.*;

    public class PlaylistEvent extends Event {

        public static const CHANGED:String = "listChanged";

        private var evtType:String;

        public function PlaylistEvent(evtType:String) {
            super(evtType);
            this.evtType = evtType;
        }

        override public function clone():Event {
            return new PlaylistEvent(evtType);
        }
    }
}