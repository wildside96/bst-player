/*
 *  Copyright 2010 Sikiru Braheem <sbraheem at bramosystems . com>.
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
package com.bramosystems.oss.player.common.client;

import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.resources.client.ExternalTextResource;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public enum Links {

    homeIntro("Introduction", ResourceBundle.bundle.home()),
    homeDocs("Documentation", ResourceBundle.bundle.homeDocs()),
    homePlugins("Available Plugins", null),
    homeMimetypes("Mime Types", null),

    wmpBasic("Embed Windows Media Player", ResourceBundle.bundle.wmpBasic()),
    wmpLogger("With Logger widget visible", ResourceBundle.bundle.wmpLogger()),
    wmpVideo("Embedding Video", ResourceBundle.bundle.wmpVideo()),
    wmpVideoAuto("Embedding Video", ResourceBundle.bundle.wmpVideoAuto()),
    wmpPlaylist("Windows Media Playlist", ResourceBundle.bundle.wmpPlaylist()),

    qtBasic("Embed QuickTime Plugin", ResourceBundle.bundle.qtBasic()),
    qtLogger("With Logger widget visible", ResourceBundle.bundle.qtLogger()),
    qtVideo("Embedding QuickTime Video", ResourceBundle.bundle.qtVideo()),
    qtVideoAuto("Embedding QuickTime Video", ResourceBundle.bundle.qtVideoAuto()),

    vlcBasic("Embed VLC Player", ResourceBundle.bundle.vlcBasic()),
    vlcLogger("Using the Logger widget", ResourceBundle.bundle.vlcLogger()),
    vlcVideo("Embedding Video", ResourceBundle.bundle.vlcVideo()),
    vlcVideoAuto("Embedding Video", ResourceBundle.bundle.vlcVideoAuto()),

    swfBasic("Embed Flash plugin", ResourceBundle.bundle.swfBasic()),
    swfLogger("With Logger widget visible", ResourceBundle.bundle.swfLogger()),
    swfVideo("Embedding Video", ResourceBundle.bundle.swfVideo()),
    swfVideoAuto("Embedding Video", ResourceBundle.bundle.swfVideoAuto()),
    swfPlaylist("Using M3U playlist", ResourceBundle.bundle.swfPlaylist()),

    ntiveBasic("Embed Native player", ResourceBundle.bundle.nativeBasic()),
    ntiveLogger("With Logger widget visible", ResourceBundle.bundle.nativeLogger()),
    ntiveVideo("Embedding Video", ResourceBundle.bundle.nativeVideo()),
    ntiveVideoAuto("Embedding Video", ResourceBundle.bundle.nativeVideoAuto()),

    listSwf("Playlists with Flash Player", ResourceBundle.bundle.pllSwf()),
    listVlc("Playlists with VLC Media Player", ResourceBundle.bundle.pllVlc()),
    listAuto("Playlist with any suitable player", ResourceBundle.bundle.pllAuto()),

    dynWmp("Custom controls with Windows Media Player plugin", ResourceBundle.bundle.audWmp()),
    dynSwf("Custom controls with Flash plugin", ResourceBundle.bundle.audSwf()),
    dynQt("Custom controls with QuickTime plugin", ResourceBundle.bundle.audQt()),
    dynVlc("Custom controls with VLC Player Plugin", ResourceBundle.bundle.audVlc()),
    dynNtv("Custom controls with HTML 5 media elements", ResourceBundle.bundle.audNtv()),
    dynAuto("Plugin determined at runtime", ResourceBundle.bundle.audAuto()),

    dynvdWmp("Custom controls with Windows Media Player plugin", ResourceBundle.bundle.vidWmp()),
    dynvdSwf("Custom controls with Flash plugin", ResourceBundle.bundle.vidSwf()),
    dynvdQt("Custom controls with QuickTime plugin", ResourceBundle.bundle.vidQt()),
    dynvdVlc("Custom controls with VLC Media Player", ResourceBundle.bundle.vidVlc()),
    dynvdNtv("Custom controls with HTML 5 media elements", ResourceBundle.bundle.vidNtv()),
    dynvdAuto("Plugin determined at runtime", ResourceBundle.bundle.vidAuto()),

    matrixBasic("Matrix Support", null),

    ytubeBasic("Embed YouTube video", ResourceBundle.bundle.ytubeBasic()),
    ytubeChrome("Custom YouTube player", ResourceBundle.bundle.ytubeChrome()),

    miscBasic("Just play my file!", ResourceBundle.bundle.miscBasic());

    private ExternalTextResource source;
    private String title;

    private Links(String title, ExternalTextResource source) {
        this.title = title;
        this.source = source;
    }

    public ExternalTextResource getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }
}
