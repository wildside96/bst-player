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
package com.bramosystems.oss.player.resources.sources;

import com.bramosystems.oss.player.binder.client.cases.TemplateFactory;
import com.bramosystems.oss.player.binder.client.cases.TemplateFactory.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public enum Links {

    homeIntro("Introduction", ResourceBundle.bundle.home(), (UiBinder<Widget, TemplateFactory>) GWT.create(HomeIntro.class)),
    homeDocs("Documentation", ResourceBundle.bundle.homeDocs(), (UiBinder<Widget, TemplateFactory>) GWT.create(HomeDocs.class)),
    homePlugins("Available Plugins", null, null),
    homeMimetypes("Mime Types", null, null),

    wmpBasic("Embed Windows Media Player", ResourceBundle.bundle.wmpBasic(), (UiBinder<Widget, TemplateFactory>) GWT.create(WMPBasic.class)),
    wmpLogger("With Logger widget visible", ResourceBundle.bundle.wmpLogger(), (UiBinder<Widget, TemplateFactory>) GWT.create(WMPLogger.class)),
    wmpVideo("Embedding Video", ResourceBundle.bundle.wmpVideo(), (UiBinder<Widget, TemplateFactory>) GWT.create(WMPVideo.class)),
    wmpVideoAuto("Embedding Video", ResourceBundle.bundle.wmpVideoAuto(), null),
    wmpPlaylist("Windows Media Playlist", ResourceBundle.bundle.wmpPlaylist(), (UiBinder<Widget, TemplateFactory>) GWT.create(WMPPlaylist.class)),

    qtBasic("Embed QuickTime Plugin", ResourceBundle.bundle.qtBasic(), (UiBinder<Widget, TemplateFactory>) GWT.create(QTBasic.class)),
    qtLogger("With Logger widget visible", ResourceBundle.bundle.qtLogger(), (UiBinder<Widget, TemplateFactory>) GWT.create(QTLogger.class)),
    qtVideo("Embedding QuickTime Video", ResourceBundle.bundle.qtVideo(), (UiBinder<Widget, TemplateFactory>) GWT.create(QTVideo.class)),
    qtVideoAuto("Embedding QuickTime Video", ResourceBundle.bundle.qtVideoAuto(), null),

    vlcBasic("Embed VLC Player", ResourceBundle.bundle.vlcBasic(), (UiBinder<Widget, TemplateFactory>) GWT.create(VLCBasic.class)),
    vlcLogger("Using the Logger widget", ResourceBundle.bundle.vlcLogger(), (UiBinder<Widget, TemplateFactory>) GWT.create(VLCLogger.class)),
    vlcVideo("Embedding Video", ResourceBundle.bundle.vlcVideo(), (UiBinder<Widget, TemplateFactory>) GWT.create(VLCVideo.class)),
    vlcVideoAuto("Embedding Video", ResourceBundle.bundle.vlcVideoAuto(), null),

    swfBasic("Embed Flash plugin", ResourceBundle.bundle.swfBasic(), (UiBinder<Widget, TemplateFactory>) GWT.create(SWFBasic.class)),
    swfLogger("With Logger widget visible", ResourceBundle.bundle.swfLogger(), (UiBinder<Widget, TemplateFactory>) GWT.create(SWFLogger.class)),
    swfVideo("Embedding Video", ResourceBundle.bundle.swfVideo(), (UiBinder<Widget, TemplateFactory>) GWT.create(SWFVideo.class)),
    swfVideoAuto("Embedding Video", ResourceBundle.bundle.swfVideoAuto(), null),
    swfPlaylist("Using M3U playlist", ResourceBundle.bundle.swfPlaylist(), (UiBinder<Widget, TemplateFactory>) GWT.create(SWFPlaylist.class)),

    ntiveBasic("Embed Native player", ResourceBundle.bundle.nativeBasic(), (UiBinder<Widget, TemplateFactory>) GWT.create(NTVBasic.class)),
    ntiveLogger("With Logger widget visible", ResourceBundle.bundle.nativeLogger(), (UiBinder<Widget, TemplateFactory>) GWT.create(NTVLogger.class)),
    ntiveVideo("Embedding Video", ResourceBundle.bundle.nativeVideo(), (UiBinder<Widget, TemplateFactory>) GWT.create(NTVVideo.class)),
    ntiveVideoAuto("Embedding Video", ResourceBundle.bundle.nativeVideoAuto(), null),

    listSwf("Playlists with Flash Player", ResourceBundle.bundle.pllSwf(), (UiBinder<Widget, TemplateFactory>) GWT.create(ListSwf.class)),
    listVlc("Playlists with VLC Media Player", ResourceBundle.bundle.pllVlc(), (UiBinder<Widget, TemplateFactory>) GWT.create(ListVlc.class)),
    listAuto("Playlist with any suitable player", ResourceBundle.bundle.pllAuto(), (UiBinder<Widget, TemplateFactory>) GWT.create(ListAuto.class)),

    dynWmp("Custom controls with Windows Media Player plugin", ResourceBundle.bundle.audWmp(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomWmp.class)),
    dynSwf("Custom controls with Flash plugin", ResourceBundle.bundle.audSwf(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomSwf.class)),
    dynQt("Custom controls with QuickTime plugin", ResourceBundle.bundle.audQt(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomQt.class)),
    dynVlc("Custom controls with VLC Player Plugin", ResourceBundle.bundle.audVlc(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomVlc.class)),
    dynNtv("Custom controls with HTML 5 media elements", ResourceBundle.bundle.audNtv(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomNtv.class)),
    dynAuto("Plugin determined at runtime", ResourceBundle.bundle.audAuto(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomAuto.class)),

    dynvdWmp("Custom controls with Windows Media Player plugin", ResourceBundle.bundle.vidWmp(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomVidWmp.class)),
    dynvdSwf("Custom controls with Flash plugin", ResourceBundle.bundle.vidSwf(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomVidSwf.class)),
    dynvdQt("Custom controls with QuickTime plugin", ResourceBundle.bundle.vidQt(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomVidQt.class)),
    dynvdVlc("Custom controls with VLC Media Player", ResourceBundle.bundle.vidVlc(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomVidVlc.class)),
    dynvdNtv("Custom controls with HTML 5 media elements", ResourceBundle.bundle.vidNtv(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomVidNtv.class)),
    dynvdAuto("Plugin determined at runtime", ResourceBundle.bundle.vidAuto(), (UiBinder<Widget, TemplateFactory>) GWT.create(CustomVidAuto.class)),
    
    matrixBasic("Matrix Support", null, null),

    ytubeBasic("Embed YouTube video", ResourceBundle.bundle.ytubeBasic(), (UiBinder<Widget, TemplateFactory>) GWT.create(YTubeBasic.class)),
    ytubeChrome("Custom YouTube player", ResourceBundle.bundle.ytubeChrome(), (UiBinder<Widget, TemplateFactory>) GWT.create(YTubeChrome.class)),

    miscBasic("Just play my file!", ResourceBundle.bundle.miscBasic(), (UiBinder<Widget, TemplateFactory>) GWT.create(MiscBasic.class));

    private UiBinder<Widget, TemplateFactory> view;
    private ExternalTextResource source;
    private String title;

    private Links(String title, ExternalTextResource source, UiBinder<Widget, TemplateFactory> view) {
        this.title = title;
        this.source = source;
        this.view = view;
    }

    public ExternalTextResource getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public UiBinder<Widget, TemplateFactory> getView() {
        return view;
    }
}
