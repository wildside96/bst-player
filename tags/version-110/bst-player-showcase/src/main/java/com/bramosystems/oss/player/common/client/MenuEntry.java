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
package com.bramosystems.oss.player.common.client;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public enum MenuEntry {

    home("Home", Links.homeIntro, Links.homeDocs, Links.homePlugins, Links.homeMimetypes),
    wmp("Windows Media Player", Links.wmpBasic, Links.wmpLogger, Links.wmpVideo, Links.wmpPlaylist),
    qt("QuickTime Plugin", Links.qtBasic, Links.qtLogger, Links.qtVideo),
    swf("Flash Plugin", Links.swfBasic, Links.swfLogger, Links.swfVideo, Links.swfPlaylist),
    vlc("VLC Media Player", Links.vlcBasic, Links.vlcLogger, Links.vlcVideo),
    ntive("HTML 5 Native Player", Links.ntiveBasic, Links.ntiveLogger, Links.ntiveVideo),
    list("Playlists", Links.listSwf, Links.listVlc, Links.listAuto),
    dyn("Custom Audio Player", Links.dynWmp, Links.dynSwf, Links.dynQt, Links.dynVlc, Links.dynNtv, Links.dynAuto),
    dynvd("Custom Video Player", Links.dynvdWmp, Links.dynvdSwf, Links.dynvdQt, Links.dynvdVlc, Links.dynvdNtv, Links.dynvdAuto),
    matrix("Matrix Transformation", Links.matrixBasic),
    ytube("YouTube Videos", Links.ytubeBasic, Links.ytubeChrome),
    misc("Miscellaneous Examples", Links.miscBasic, Links.miscFlash);
    private String desc;
    private Links[] links;

    private MenuEntry(String desc, Links... entries) {
        this.desc = desc;
        links = new Links[entries.length];

        int index = 0;
        for (Links entry : entries) {
            links[index] = entry;
            index++;
        }
    }

    @Override
    public String toString() {
        return desc;
    }

    public Links[] getLinks() {
        return links;
    }
}
