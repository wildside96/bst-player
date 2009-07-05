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
     * Specifies any media player plugin that is available on the browser and features basic
     * playback support.
     *
     * <p>Basic playback support is as defined by the {@linkplain AbstractMediaPlayer} class.
     *
     * @see PlayerUtil#getPlayer(java.lang.String, boolean, java.lang.String, java.lang.String)
     */
    Auto(""),

    /**
     * Similar to {@linkplain #Auto}, specifies any media player plugin that supports
     * client-side playlist management as defined by the {@linkplain PlaylistSupport}
     * interface.
     *
     * @see PlayerUtil#getPlayer(Plugin, java.lang.String, boolean, java.lang.String, java.lang.String)
     * @see PlaylistSupport
     * @since 1.0
     */
    PlaylistSupport(""),

    /**
     * Specifies the Window Media Player&trade; plugin
     */
    WinMediaPlayer("http://www.microsoft.com/windowsmedia"),

    /**
     * Specifies the QuickTime&trade; Player plugin
     */
    QuickTimePlayer("http://www.apple.com/quicktime/download"),

    /**
     * Specifies the Flash Player plugin
     */
    FlashPlayer("http://www.adobe.com/go/getflash"),
    
    /**
     * Specifies the VLC Media Player plugin
     * @since 1.0
     */
    VLCPlayer("http://www.videolan.org");

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
