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
package com.bramosystems.oss.player.core.client;

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
    Auto(""),

    /**
     * The Window Media Player plugin
     */
    WinMediaPlayer("http://www.microsoft.com/windowsmedia"),

    /**
     * The QuickTime plugin
     */
    QuickTimePlayer("http://www.apple.com/quicktime/download"),

    /**
     * Specifies the Flash Player plugin for MP3 sound
     */
    FlashMP3Player("http://www.adobe.com/go/getflash"),
    
    /**
     * Specifies the Flash Player plugin for Flash Videos
     */
    FlashVideoPlayer("http://www.adobe.com/go/getflash");
    

    private String downloadURL;

    Plugin(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    /**
     * Gets the URL of the plugins' download page
     *
     * @return the plugin download URL
     * @since 0.6
     */
    public String getDownloadURL() {
        return downloadURL;
    }
}
