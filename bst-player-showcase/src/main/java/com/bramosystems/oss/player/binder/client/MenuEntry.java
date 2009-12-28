/*
 *  Copyright 2009 Sikiru Braheem <sbraheem at bramosystems . com>.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.bramosystems.oss.player.binder.client;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public enum MenuEntry {

    home("Home", "intro:Introduction", "docs:Documentation", "plugins:Available Plugins",
    "mimetypes:Mime Types"),

    wmp("Windows Media Player", "basic:Embed Windows Media Player", "logger:With Logger widget visible",
    "video:Embedding Video", "playlist:Windows Media Playlist"),

    qt("QuickTime Plugin", "basic:Embed QuickTime Plugin", "logger:With Logger widget visible",
    "video:Embedding QuickTime Video"),

    swf("Flash Plugin", "basic:Embed Flash plugin", "logger:With Logger widget visible",
    "video:Embedding Video", "playlist:Using M3U playlist"),

    vlc("VLC Media Player", "basic:Embed VLC Player", "logger:Using the Logger widget",
    "video:Embedding Video"),

    _native("HTML 5 Native Player", "basic:Embed Native player", "logger:With Logger widget visible",
    "video:Embedding Video"),

    list("Playlists", "swf:Playlists with Flash Player", "vlc:Playlists with VLC Media Player",
    "auto:Custom player with playlist support"),

    dyn("Custom Audio Player", "wmp:Custom controls with Windows Media Player plugin",
    "swf:Custom controls with Flash plugin", "qt:Custom controls with QuickTime plugin",
    "vlc:Custom controls with VLC Player Plugin", "ntv:Custom controls with HTML 5 Native player",
    "dyna:Plugin determined at runtime"),

    dynvd("Custom Video Player", "wmp:Custom controls with Windows Media Player plugin",
    "swf:Custom controls with Flash plugin", "qt:Custom controls with QuickTime plugin",
    "vlc:Custom controls with VLC Media Player", "ntv:Custom controls with HTML 5 media elements",
    "dyna:Plugin determined at runtime"),

    matrix("Matrix Transformation", "basic:Matrix Support"),

    youtube("YouTube Videos", "basic:Embed YouTube video", "custom:Custom YouTube player"),

    misc("Miscellaneous Examples", "util:Just play my file!", "loopy:Using playback repeats");
    
    private String desc;
    private String[] links, texts;

    private MenuEntry(String desc, String... entries) {
        this.desc = desc;
        links = new String[entries.length];
        texts = new String[entries.length];

        int index = 0;
        for (String entry : entries) {
            String[] tokens = entry.split(":");
            links[index] = tokens[0];
            texts[index] = tokens[1];
            index++;
        }
    }

    @Override
    public String toString() {
        return desc;
    }

    public String[] getLinks() {
        return links;
    }

    public String[] getTexts() {
        return texts;
    }
}
