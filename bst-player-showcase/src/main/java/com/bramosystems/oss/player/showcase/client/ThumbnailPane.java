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

import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.common.client.MenuEntry;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LazyPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikiru
 */
public class ThumbnailPane extends Composite implements ValueChangeHandler<String>{

    private DeckPanel dp;
    private MenuEntry[] mes = MenuEntry.values();
    private int index, size;

    public ThumbnailPane() {
        initWidget(uiBinder.createAndBindUi(this));

        size = MenuEntry.values().length;
        navRight.setEnabled(size > 1);
        navLeft.setEnabled(false);
        index = 0;

        dp = new DeckPanel();
        dp.setAnimationEnabled(true);
        for (MenuEntry me : mes) {
            dp.add(new ThumbPanel(me));
        }
        thumbPanel.setWidget(dp);
        History.addValueChangeHandler(this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        PlayOptions link = PlayOptions.homeIntro;
        try {
            link = PlayOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }

        switch(link) {
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
        dp.showWidget(index);
        navRight.setEnabled(index < size - 1);
        navLeft.setEnabled(index > 0);
    }

    @UiHandler("navLeft")
    public void onLeftNav(ClickEvent ce) {
        if (--index >= 0) {
            dp.showWidget(index);
            navLeft.setEnabled(index > 0);
        } else {
            navLeft.setEnabled(false);
        }
        navRight.setEnabled(index < size - 1);
    }

    @UiHandler("navRight")
    public void onRightNav(ClickEvent ce) {
        if (++index < size) {
            dp.showWidget(index);
            navRight.setEnabled(index < size - 1);
        } else {
            navRight.setEnabled(false);
        }
        navLeft.setEnabled(index > 0);
    }
    @UiField Button navRight, navLeft;
    @UiField SimplePanel thumbPanel;

    @UiTemplate("xml/ThumbnailPane.ui.xml")
    interface ThumbnailPaneUiBinder extends UiBinder<Widget, ThumbnailPane> {}
    @UiTemplate("xml/Thumbnail.ui.xml")
    interface ThumbnailBinder extends UiBinder<Label, ThumbPanel> {}
    private static ThumbnailBinder thumbBinder = GWT.create(ThumbnailBinder.class);
    private static ThumbnailPaneUiBinder uiBinder = GWT.create(ThumbnailPaneUiBinder.class);


    class ThumbPanel extends LazyPanel {

        private MenuEntry entry;

        public ThumbPanel(MenuEntry entry) {
            this.entry = entry;
        }

        @Override
        protected Widget createWidget() {
            HorizontalPanel hp = new HorizontalPanel();
            Links[] links = entry.getLinks();
            for (Links link : links) {
                hp.add(getThumbnail(link.getTitle(), link.name()));
            }
            return hp;
        }

        private Label getThumbnail(String text, final String link) {
            Label thumb = thumbBinder.createAndBindUi(this);
            thumb.setText(text);
            thumb.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    History.newItem(link);
                }
            });
            return thumb;
        }

        @Override
        protected void onLoad() {
            ensureWidget();
        }
    }
}
