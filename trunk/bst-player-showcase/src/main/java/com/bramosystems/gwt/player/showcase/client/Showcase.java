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

        VerticalPanel bar = new VerticalPanel();
        bar.add(getSectionLink("Home", 0));
        bar.add(getSectionLink("Windows Media Player", 1));
        bar.add(getSectionLink("QuickTime", 2));
        bar.add(getSectionLink("Flash", 3));
        bar.add(getSectionLink("Custom Audio Player", 4));
        bar.add(getSectionLink("Custom Video Player", 5));
        bar.add(getSectionLink("Playable Links", 6));
        bar.add(getSectionLink("Miscellaneous", 7));

        StackPanel sp = new StackPanel();
        sp.add(bar, "Examples");

        DockPanel dp = new DockPanel();
        dp.setWidth("100%");
        dp.add(banner, DockPanel.NORTH);
        dp.add(sp, DockPanel.WEST);
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
                panel.setWidget(new LinkShowcase());
                break;
            case 7:
                panel.setWidget(new MiscShowcase());
                break;
            default:
                panel.setWidget(home);
                break;
        }
    }
}
