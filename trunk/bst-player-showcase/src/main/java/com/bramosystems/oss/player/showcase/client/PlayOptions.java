/*
 *  Copyright 2010 Sikiru.
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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.core.client.Plugin;

/**
 *
 * @author Sikiru
 */
public enum PlayOptions {
    homeIntro(Plugin.Auto, "Introduction"),
    homeDocs(Plugin.Auto, "Documentation"),
    homePlugins(Plugin.Auto, "Browser Plugins"),
    homeMimetypes(Plugin.Auto, "Plugin/Registered Mime Types"),
    homeMimePools(Plugin.Auto, "Registered Mime Types"),

    vlc(Plugin.VLCPlayer, "VLC Media Player"),
    qt(Plugin.QuickTimePlayer, "Quicktime Player"),
    divx(Plugin.DivXPlayer, "DivX Web Player"),
    swf(Plugin.FlashPlayer, "Flash Player"),
    wmp(Plugin.WinMediaPlayer, "Windows Media Player"),
    ntive(Plugin.Native, "HTML5 Player"),
    auto(Plugin.Auto, "Auto");

    private PlayOptions(Plugin plugin, String title) {
        this.plugin = plugin;
        this.title = title;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getTitle() {
        return title;
    }

    private Plugin plugin;
    private String title;
}
