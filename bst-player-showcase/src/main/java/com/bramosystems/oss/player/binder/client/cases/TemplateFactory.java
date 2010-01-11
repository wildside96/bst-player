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
package com.bramosystems.oss.player.binder.client.cases;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class TemplateFactory {

    @UiTemplate("index.ui.xml")
    public static interface HomeIntro extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("index-docs.ui.xml")
    public static interface HomeDocs extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("index-mimeTypes.ui.xml")
    public static interface HomeMimeTypes extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("index-plugin.ui.xml")
    public static interface HomePlugin extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("wmp-basic.ui.xml")
    public static interface WMPBasic extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("wmp-logger.ui.xml")
    public static interface WMPLogger extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("wmp-video.ui.xml")
    public static interface WMPVideo extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("wmp-playlist.ui.xml")
    public static interface WMPPlaylist extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("qt-basic.ui.xml")
    public static interface QTBasic extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("qt-logger.ui.xml")
    public static interface QTLogger extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("qt-video.ui.xml")
    public static interface QTVideo extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("swf-basic.ui.xml")
    public static interface SWFBasic extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("swf-logger.ui.xml")
    public static interface SWFLogger extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("swf-video.ui.xml")
    public static interface SWFVideo extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("swf-playlist.ui.xml")
    public static interface SWFPlaylist extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("vlc-basic.ui.xml")
    public static interface VLCBasic extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("vlc-logger.ui.xml")
    public static interface VLCLogger extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("vlc-video.ui.xml")
    public static interface VLCVideo extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("ntv-basic.ui.xml")
    public static interface NTVBasic extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("ntv-logger.ui.xml")
    public static interface NTVLogger extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("ntv-video.ui.xml")
    public static interface NTVVideo extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("ntv-video-auto.ui.xml")
    public static interface NTVVideoAuto extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("ytube-basic.ui.xml")
    public static interface YTubeBasic extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("ytube-chrome.ui.xml")
    public static interface YTubeChrome extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("misc-basic.ui.xml")
    public static interface MiscBasic extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("pll-swf.ui.xml")
    public static interface ListSwf extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("pll-vlc.ui.xml")
    public static interface ListVlc extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("pll-dyn.ui.xml")
    public static interface ListAuto extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("aud-swf.ui.xml")
    public static interface CustomSwf extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("aud-vlc.ui.xml")
    public static interface CustomVlc extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("aud-wmp.ui.xml")
    public static interface CustomWmp extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("aud-qt.ui.xml")
    public static interface CustomQt extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("aud-ntv.ui.xml")
    public static interface CustomNtv extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("aud-auto.ui.xml")
    public static interface CustomAuto extends UiBinder<Widget, TemplateFactory> {
    }

    @UiTemplate("vid-swf.ui.xml")
    public static interface CustomVidSwf extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("vid-vlc.ui.xml")
    public static interface CustomVidVlc extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("vid-wmp.ui.xml")
    public static interface CustomVidWmp extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("vid-qt.ui.xml")
    public static interface CustomVidQt extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("vid-ntv.ui.xml")
    public static interface CustomVidNtv extends UiBinder<Widget, TemplateFactory> {
    }
    @UiTemplate("vid-auto.ui.xml")
    public static interface CustomVidAuto extends UiBinder<Widget, TemplateFactory> {
    }
}
