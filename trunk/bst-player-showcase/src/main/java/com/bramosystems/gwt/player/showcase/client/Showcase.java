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
package com.bramosystems.gwt.player.showcase.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Showcase extends Composite implements EntryPoint {

    private SumShowcase home;
    private SimplePanel panel;

    public Showcase() {
        panel = new SimplePanel();
        home = new SumShowcase();

        Label banner = new Label("BST Player Showcase");
        banner.setStyleName("showcase-Banner");

        VerticalPanel menu = new VerticalPanel();
        menu.add(getSectionLink("Home", 0));
        menu.add(getSectionLink("Windows Media Player", 1));
        menu.add(getSectionLink("QuickTime", 2));
        menu.add(getSectionLink("Flash", 3));
        menu.add(getSectionLink("Custom Audio Player", 4));
        menu.add(getSectionLink("Custom Video Player", 5));
        menu.add(getSectionLink("Playlists", 6));
        menu.add(getSectionLink("Playable Links", 7));
        menu.add(getSectionLink("Miscellaneous", 8));

        DockPanel dp = new DockPanel();
        dp.setWidth("100%");
        dp.setSpacing(5);
        dp.add(banner, DockPanel.NORTH);
        dp.add(menu, DockPanel.WEST);
        dp.setCellWidth(menu, "150px");
        dp.add(panel, DockPanel.CENTER);
        initWidget(dp);
    }

    public void onModuleLoad() {
        RootPanel.get("loading").setVisible(false);
        RootPanel.get().add(this);
        handleClick(0);
    }

    public Label getSectionLink(String text, final int index) {
        Label lbl = new Label(text);
        lbl.setStyleName("showcase-Menu");
        lbl.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                handleClick(index);
            }
        });
        return lbl;
    }

    private void handleClick(int index) {
        switch (index) {
            case 1:
                panel.setWidget(new WMPShowcase());
                break;
            case 2:
                panel.setWidget(new QTShowcase());
                break;
            case 3:
                panel.setWidget(new SWFShowcase());
                break;
            case 4:
                panel.setWidget(new DynaShowcase());
                break;
            case 5:
                panel.setWidget(new DynaVideoShowcase());
                break;
            case 6:
                panel.setWidget(new PlaylistShowcase());
                break;
            case 7:
                panel.setWidget(new LinkShowcase());
                break;
            case 8:
                panel.setWidget(new MiscShowcase());
                break;
            default:
                panel.setWidget(home);
                break;
        }
    }
}
