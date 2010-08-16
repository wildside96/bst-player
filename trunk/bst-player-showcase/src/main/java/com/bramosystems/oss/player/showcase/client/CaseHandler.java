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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.showcase.client.matrix.MatrixShowcase;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class CaseHandler extends Composite implements ValueChangeHandler<String> {

//    private Label caseHeader;
    private ScrollPanel casePanel;

    public CaseHandler() {
//        DockLayoutPanel dc = new DockLayoutPanel(Unit.PX);
//        dc.setStyleName("content-wrapper");
//        initWidget(dc);

//        caseHeader = new Label();
//        caseHeader.setStyleName("case-header");
//        dc.addNorth(caseHeader, 40);

        casePanel = new ScrollPanel();
        casePanel.setStyleName("case-content");
//        dc.add(casePanel);
        initWidget(casePanel);

        History.addValueChangeHandler(this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        Links link = Links.homeIntro;
        try {
            link = Links.valueOf(event.getValue());
        } catch (Exception e) {
        }

        AbstractCase c = null;
        switch(link) {
            case homeDocs:
            case homeIntro:
            case homeMimetypes:
            case homePlugins:
                c = SumShowcase.instance;
                break;
            case dynAuto:
                c = DynaShowcase.instance;
                break;
            case dynVdAuto:
                c = DynaVideoShowcase.instance;
                break;
            case miscBasic:
            case miscFlash:
            case ytubeBasic:
            case ytubeChrome:
                c = MiscShowcase.instance;
                break;
            case listAuto:
            case listSwf:
            case listVlc:
                c = PlaylistShowcase.instance;
                break;
            case ntiveBasic:
//            case ntiveLogger:
            case ntiveVideo:
                c = NativeShowcase.instance;
                break;
            case qtBasic:
//            case qtLogger:
            case qtVideo:
                c = QTShowcase.instance;
                break;
            case vlcBasic:
//            case vlcLogger:
            case vlcVideo:
                c = VLCShowcase.instance;
                break;
            case wmpBasic:
//            case wmpLogger:
            case wmpPlaylist:
            case wmpVideo:
                c = WMPShowcase.instance;
                break;
            case matrixBasic:
                c = MatrixShowcase.instance;
                break;
            case swfBasic:
//            case swfLogger:
            case swfPlaylist:
            case swfVideo:
                c = SWFShowcase.instance;
                break;
        }
        c.clearCases();
        c.initCase(link);

//        caseHeader.setText(link.getTitle());
        casePanel.setWidget(c);
    }
}
