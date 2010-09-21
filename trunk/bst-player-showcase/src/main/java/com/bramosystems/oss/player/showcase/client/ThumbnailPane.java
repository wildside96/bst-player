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

import com.bramosystems.oss.player.showcase.client.images.Bundle;
import com.bramosystems.oss.player.showcase.client.widgets.ThumbnailPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikiru
 */
public class ThumbnailPane extends Composite implements ValueChangeHandler<String> {

//    private MenuEntry[] mes = MenuEntry.values();

    public ThumbnailPane() {
        Bundle.bundle.css().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));

//        for (MenuEntry me : mes) {
        for (PlayOptions me : PlayOptions.values()) {
                thumbPanel.addThumbnail(me.getTitle(), me.name());
//            Links[] links = me.getLinks();
//            for (Links link : links) {
//                thumbPanel.addThumbnail(link.getTitle(), link.name());
//            }
        }

        enableLeftNav(thumbPanel.hasMore(false));
        enableRightNav(thumbPanel.hasMore(true));
//        History.addValueChangeHandler(this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        PlayOptions link = PlayOptions.homeIntro;
        try {
            link = PlayOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }

        int index = link.ordinal();
/*
        switch (link) {
            case homeDocs:
            case homeIntro:
            case homeMimetypes:
            case homePlugins:
                index = MenuEntry.home.ordinal();
                break;
//            case dynAuto:
//                c = DynaShowcase.instance;
//                break;
//            case dynVdAuto:
//                c = DynaVideoShowcase.instance;
//                index = MenuEntry.home.ordinal();
//                break;
//            case miscBasic:
//            case miscFlash:
//            case ytubeBasic:
//            case ytubeChrome:
//                index = MenuEntry.misc.ordinal();
//                break;
            case listAuto:
            case listSwf:
            case listVlc:
                              index = MenuEntry.list.ordinal();
                break;
            case ntiveBasic:
            case ntiveVideo:
                index = MenuEntry.ntive.ordinal();
                break;
            case qtBasic:
            case qtVideo:
                index = MenuEntry.qt.ordinal();
                break;
            case vlcBasic:
            case vlcVideo:
                index = MenuEntry.vlc.ordinal();
                break;
            case wmpBasic:
            case wmpPlaylist:
            case wmpVideo:
                index = MenuEntry.wmp.ordinal();
//                break;
//            case matrixBasic:
//                index = MenuEntry.matrix.ordinal();
                break;
            case swfBasic:
            case swfPlaylist:
            case swfVideo:
                index = MenuEntry.swf.ordinal();
                break;
            case divxBasic:
            case divxVideo:
                index = MenuEntry.divx.ordinal();
                break;
        }
*/
        thumbPanel.scrollTo(index);
        enableLeftNav(thumbPanel.hasMore(false));
        enableRightNav(thumbPanel.hasMore(true));
    }

    private void enableLeftNav(boolean enable) {
        if (enable) {
            navLeft.removeStyleName(Bundle.bundle.css().navLeftDisabled());
        } else {
            navLeft.addStyleName(Bundle.bundle.css().navLeftDisabled());
        }
        navLeft.setLayoutData(Boolean.valueOf(enable));
    }

    private void enableRightNav(boolean enable) {
        if (enable) {
            navRight.removeStyleName(Bundle.bundle.css().navRightDisabled());
        } else {
            navRight.addStyleName(Bundle.bundle.css().navRightDisabled());
        }
        navRight.setLayoutData(Boolean.valueOf(enable));
    }

    @UiHandler("navLeft")
    public void onLeftNav(ClickEvent ce) {
        if (!(Boolean) navLeft.getLayoutData()) {
            return;
        }

        thumbPanel.scrollToPrev();
        enableLeftNav(thumbPanel.hasMore(false));
        enableRightNav(thumbPanel.hasMore(true));
    }

    @UiHandler("navRight")
    public void onRightNav(ClickEvent ce) {
        if (!(Boolean) navRight.getLayoutData()) {
            return;
        }

        thumbPanel.scrollToNext();
        enableLeftNav(thumbPanel.hasMore(false));
        enableRightNav(thumbPanel.hasMore(true));
    }

    @UiField Label navRight, navLeft;
    @UiField ThumbnailPanel thumbPanel;

    @UiTemplate("xml/ThumbnailPane.ui.xml")
    interface ThumbnailPaneUiBinder extends UiBinder<Widget, ThumbnailPane> {}
    private static ThumbnailPaneUiBinder uiBinder = GWT.create(ThumbnailPaneUiBinder.class);
}
