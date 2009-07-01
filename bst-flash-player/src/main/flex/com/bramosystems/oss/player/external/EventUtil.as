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

package com.bramosystems.oss.player.external {
    import flash.external.*;
    import flash.media.ID3Info;

    public class EventUtil {
        public static var playerId:String = "";

        public static function fireApplicationInitialized():void {
            ExternalInterface.call("bstSwfMdaInit", playerId);
        }

        public static function fireMediaStateChanged(state:int, playlistIndex:int = -1):void {
            ExternalInterface.call("bstSwfMdaMediaStateChanged", playerId, state, playlistIndex);
        }

        public static function fireLoadingProgress(progress:Number):void {
            ExternalInterface.call("bstSwfMdaLoadingProgress", playerId, progress);
        }

        public static function fireID3Metadata(info:ID3Info):void {
            // parse into CSV like values ...
            // year[$]albumTitle[$]artists[$]comment[$]genre[$]title[$]
            // contentProviders[$]copyright[$]duration[$]hardwareSoftwareRequirements[$]
            // publisher[$]internetStationOwner[$]internetStationName

            var id3:String = info.year + "[$]" + info.album + "[$]" + info.artist  + "[$]" +
                                info.comment + "[$]" + info.genre + "[$]" + info.songName + "[$]" +
                                info.TOLY + "[$]" + info.TOWN + "[$]" + info.TLEN + "[$]" +
                                info.TSSE + "[$]" + info.TPUB + "[$]" + info.TRSO + "[$]" +
                                info.TRSN;
            ExternalInterface.call("bstSwfMdaMetadata", playerId, id3);
        }

        public static function fireVideoMetadata(duration:Number, info:String):void {
            // parse into CSV like values ...
            // year[$]albumTitle[$]artists[$]comment[$]genre[$]title[$]
            // contentProviders[$]copyright[$]duration[$]hardwareSoftwareRequirements[$]
            // publisher[$]internetStationOwner[$]internetStationName

            var id3:String = "0[$] [$] [$] [$] [$] [$] [$] [$]" + (duration * 1000) +
                             "[$]" + info + "[$] [$] [$] ";
            ExternalInterface.call("bstSwfMdaMetadata", playerId, id3);
        }
    }
}