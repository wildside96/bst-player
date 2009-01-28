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
package com.bramosystems.gwt.player.client;

/**
 * An enum of supported media player plugins
 *
 * @author Sikirulai Braheem
 */
public enum Plugin {

    /**
     * Dynamically determine the plugin to use based on its
     * availability on the browser as well as suitability to playback
     * the specified media.
     *
     * @see PlayerUtil#getPlayer(java.lang.String, boolean, java.lang.String, java.lang.String)
     */
    Auto,
    /**
     * The Window Media Player plugin
     */
    WinMediaPlayer,
    /**
     * The QuickTime plugin
     */
    QuickTimePlayer,
    /**
     * Specifies the Flash Player plugin
     */
    FlashMP3Player;
}
