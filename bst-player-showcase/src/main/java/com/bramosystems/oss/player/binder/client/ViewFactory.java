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
package com.bramosystems.oss.player.binder.client;

import com.bramosystems.oss.player.binder.client.cases.TemplateFactory;
import com.bramosystems.oss.player.binder.client.cases.TemplateFactory.*;
import com.bramosystems.oss.player.common.client.Links;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class ViewFactory {

    public static ViewFactory instance = new ViewFactory();
    private Views[] views;

    private ViewFactory() {
        views = Views.values();
    }

    public Widget getView(Links link) {
        return views[link.ordinal()].view;
    }

    private enum Views {

        homeIntro((UiBinder<Widget, TemplateFactory>) GWT.create(HomeIntro.class)),
        homeDocs((UiBinder<Widget, TemplateFactory>) GWT.create(HomeDocs.class)),
        homePlugins((UiBinder<Widget, TemplateFactory>) GWT.create(HomePlugin.class)),
        homeMimetypes((UiBinder<Widget, TemplateFactory>) GWT.create(HomeMimeTypes.class)),
        wmpBasic((UiBinder<Widget, TemplateFactory>) GWT.create(WMPBasic.class)),
        wmpLogger((UiBinder<Widget, TemplateFactory>) GWT.create(WMPLogger.class)),
        wmpVideo((UiBinder<Widget, TemplateFactory>) GWT.create(WMPVideo.class)),
        wmpVideoAuto(null),
        wmpPlaylist((UiBinder<Widget, TemplateFactory>) GWT.create(WMPPlaylist.class)),
        qtBasic((UiBinder<Widget, TemplateFactory>) GWT.create(QTBasic.class)),
        qtLogger((UiBinder<Widget, TemplateFactory>) GWT.create(QTLogger.class)),
        qtVideo((UiBinder<Widget, TemplateFactory>) GWT.create(QTVideo.class)),
        qtVideoAuto(null),
        vlcBasic((UiBinder<Widget, TemplateFactory>) GWT.create(VLCBasic.class)),
        vlcLogger((UiBinder<Widget, TemplateFactory>) GWT.create(VLCLogger.class)),
        vlcVideo((UiBinder<Widget, TemplateFactory>) GWT.create(VLCVideo.class)),
        vlcVideoAuto(null),
        divxBasic((UiBinder<Widget, TemplateFactory>) GWT.create(VLCBasic.class)),
        divxLogger((UiBinder<Widget, TemplateFactory>) GWT.create(VLCLogger.class)),
        divxVideo((UiBinder<Widget, TemplateFactory>) GWT.create(VLCVideo.class)),
        divxVideoAuto(null),
        swfBasic((UiBinder<Widget, TemplateFactory>) GWT.create(SWFBasic.class)),
        swfLogger((UiBinder<Widget, TemplateFactory>) GWT.create(SWFLogger.class)),
        swfVideo((UiBinder<Widget, TemplateFactory>) GWT.create(SWFVideo.class)),
        swfVideoAuto(null),
        swfPlaylist((UiBinder<Widget, TemplateFactory>) GWT.create(SWFPlaylist.class)),
        ntiveBasic((UiBinder<Widget, TemplateFactory>) GWT.create(NTVBasic.class)),
        ntiveLogger((UiBinder<Widget, TemplateFactory>) GWT.create(NTVLogger.class)),
        ntiveVideo((UiBinder<Widget, TemplateFactory>) GWT.create(NTVVideo.class)),
        ntiveVideoAuto(null),
        listSwf((UiBinder<Widget, TemplateFactory>) GWT.create(ListSwf.class)),
        listVlc((UiBinder<Widget, TemplateFactory>) GWT.create(ListVlc.class)),
        listAuto((UiBinder<Widget, TemplateFactory>) GWT.create(ListAuto.class)),
        dynAuto((UiBinder<Widget, TemplateFactory>) GWT.create(CustomAuto.class)),
        dynVdAuto((UiBinder<Widget, TemplateFactory>) GWT.create(CustomVidAuto.class)),
        matrixBasic(null),
        ytubeBasic((UiBinder<Widget, TemplateFactory>) GWT.create(YTubeBasic.class)),
        ytubeChrome((UiBinder<Widget, TemplateFactory>) GWT.create(YTubeChrome.class)),
        miscBasic((UiBinder<Widget, TemplateFactory>) GWT.create(MiscBasic.class)),
        miscFlash((UiBinder<Widget, TemplateFactory>) GWT.create(MiscFlash.class));

        private Widget view;

        private Views(UiBinder<Widget, TemplateFactory> view) {
            if (view != null) {
                this.view = view.createAndBindUi(null);
            }
        }
    }
}
