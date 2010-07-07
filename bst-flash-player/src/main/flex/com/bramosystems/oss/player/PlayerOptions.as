/*
 * Copyright 2010 Sikirulai Braheem
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

package com.bramosystems.oss.player {
    import mx.core.Application;

    public class PlayerOptions {

        public static function get playerId():String {
            return Application.application.parameters.playerId;
        }

        public static function get autoplay():Boolean {
            return Application.application.parameters.autoplay != "false";
        }

        public static function get mediaURL():String {
            return Application.application.parameters.mediaURL;
        }
    }
}
