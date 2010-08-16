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
import com.google.gwt.core.client.GWT;

/**
 *
 * @author Sikiru
 */
public enum PlayOptions {
    homeIntro(Plugin.Auto, "Introduction", 0, ""),
    homeDocs(Plugin.Auto, "Documentation", 0, ""),
    homePlugins(Plugin.Auto, "Browser Plugins", 0, ""),
    homeMimetypes(Plugin.Auto, "Plugin/Registered Mime Types", 0, ""),
    homeMimePools(Plugin.Auto, "Registered Mime Types", 0, ""),

    vlcBasic(Plugin.VLCPlayer, "Playing sound automatically", 0, GWT.getHostPageBaseURL() + "media/thunder.mp3"),
    vlcVideo(Plugin.VLCPlayer, "Embedding Video", 250, GWT.getHostPageBaseURL() + "media/traffic.flv"),

    qtBasic(Plugin.QuickTimePlayer, "Playing sound automatically", 0, GWT.getHostPageBaseURL() + "media/thunder.mp3"),
    qtVideo(Plugin.QuickTimePlayer, "Embedding video", 250, GWT.getHostPageBaseURL() + "media/traffic.mp4"),

    divxBasic(Plugin.DivXPlayer, "Playing sound automatically", 0, GWT.getHostPageBaseURL() + "media/thunder.mp3"),
    divxVideo(Plugin.DivXPlayer, "Embedding video", 250, GWT.getHostPageBaseURL() + "media/traffic.mp4"),

    swfBasic(Plugin.FlashPlayer, "Playing MP3 media automatically", 0, GWT.getHostPageBaseURL() + "media/applause.mp3"),
    swfVideo(Plugin.FlashPlayer, "Embedding video", 250, GWT.getHostPageBaseURL() + "media/traffic.flv"),
    swfPlaylist(Plugin.FlashPlayer, "Working with M3U playlists", 0, GWT.getHostPageBaseURL() + "media/playlist.m3u"),

    wmpBasic(Plugin.WinMediaPlayer, "Playing sound automatically", 0, GWT.getHostPageBaseURL() + "media/applause.mp3"),
    wmpVideo(Plugin.WinMediaPlayer, "Embedding video", 250, "http://bst-player.googlecode.com/svn/tags/showcase/media/islamic-jihad.wmv"),
    wmpPlaylist(Plugin.WinMediaPlayer, "Windows Media playlist", 0, GWT.getHostPageBaseURL() + "media/playlist.wpl"),

    listSwf(Plugin.FlashPlayer, "Using playlist with Adobe Flash", 0,
    GWT.getHostPageBaseURL() + "media/o-na-som.mp3", GWT.getHostPageBaseURL() + "media/thunder.mp3",
    GWT.getHostPageBaseURL() + "media/applause.mp3"),
    listVlc(Plugin.VLCPlayer, "Using playlist with VLC Media Player", 0, GWT.getHostPageBaseURL() + "media/thunder.mp3",
    GWT.getHostPageBaseURL() + "media/applause.mp3", GWT.getHostPageBaseURL() + "media/o-na-som.mp3"),
    listAuto(Plugin.PlaylistSupport, "Playlist handling using any suitable player plugin with shuffling enabled", 0, GWT.getHostPageBaseURL() + "media/o-na-som.mp3",
    GWT.getHostPageBaseURL() + "media/thunder.mp3",
    GWT.getHostPageBaseURL() + "media/applause.mp3"),
    
    ntiveBasic(Plugin.Native, "Playing MP3 media automatically", 0, GWT.getHostPageBaseURL() + "media/applause.mp3"),
    ntiveVideo(Plugin.Native, "Embedding Video", 250, GWT.getHostPageBaseURL() + "media/big-buck-bunny.mp4", GWT.getHostPageBaseURL() + "media/big-buck-bunny.ogv");

    private PlayOptions(Plugin plugin, String title, int height, String... url) {
        this.plugin = plugin;
        this.url = url;
        this.height = height > 0 ? height + "px" : null;
        this.title = title;
    }

    public String getHeight() {
        return height;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getTitle() {
        return title;
    }

    public String[] getUrl() {
        return url;
    }
    private Plugin plugin;
    private String[] url;
    private String height, title;
}
