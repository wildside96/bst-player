/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a footer of the License at
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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Showcase extends Composite implements EntryPoint, ValueChangeHandler<String> {

    private AbstractCase home = getCase(Cases.home);
    private SimplePanel panel;
    private StackPanel menu;

    public Showcase() {
        panel = new SimplePanel();

        Label banner = new Label("BST Player Showcase");
        banner.setStyleName("showcase-Banner");

        HTML footer = new HTML("Copyright &copy; 2009 Braheem Sikiru<br/>" +
                "All other product, service names, brands, or trademarks, are " +
                "the property of their respective owners.");
        footer.setStyleName("showcase-Footer");

        menu = new StackPanel();
        menu.setWidth("100%");
        Cases cases[] = Cases.values();
        for (Cases caze : cases) {
            addCaseItems(caze);
        }

        DockPanel dp = new DockPanel();
        dp.setWidth("100%");
        dp.setHeight("100%");
        dp.setSpacing(5);
        dp.add(banner, DockPanel.NORTH);
        dp.add(footer, DockPanel.SOUTH);
        dp.add(menu, DockPanel.WEST);
        dp.setCellWidth(menu, "200px");
        dp.add(panel, DockPanel.CENTER);
        initWidget(dp);

        History.addValueChangeHandler(this);
    }

    public void onModuleLoad() {
        RootPanel.get("loading").setVisible(false);
        RootPanel.get().add(this);
        
        History.fireCurrentHistoryState();
    }

    private void addCaseItems(Cases caze) {
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(10);
        String[] names = null;
        String[] links = null;
        String header = null;

        switch (caze) {
            case wmp:
                names = WMPShowcase.caseNames;
                links = WMPShowcase.caseLinks;
                header = "Windows Media Player";
                break;
            case dyn:
                names = DynaShowcase.caseNames;
                links = DynaShowcase.caseLinks;
                header = "Custom Audio Player";
                break;
            case dynvd:
                names = DynaVideoShowcase.caseNames;
                links = DynaVideoShowcase.caseLinks;
                header = "Custom Video Player";
                break;
            case home:
                names = SumShowcase.caseNames;
                links = SumShowcase.caseLinks;
                header = "Home";
                break;
//            case link:
//                names = LinkShowcase.caseNames;
//                links = LinkShowcase.caseLinks;
//                header = "Playable Links";
//                break;
            case list:
                names = PlaylistShowcase.caseNames;
                links = PlaylistShowcase.caseLinks;
                header = "Playlists";
                break;
            case misc:
                names = MiscShowcase.caseNames;
                links = MiscShowcase.caseLinks;
                header = "Miscellaneous Examples";
                break;
            case qt:
                names = QTShowcase.caseNames;
                links = QTShowcase.caseLinks;
                header = "QuickTime Plugin";
                break;
            case swf:
                names = SWFShowcase.caseNames;
                links = SWFShowcase.caseLinks;
                header = "Flash Plugin";
                break;
            case vlc:
                names = VLCShowcase.caseNames;
                links = VLCShowcase.caseLinks;
                header = "VLC Media Player";
                break;
        }

        for (int i = 0; i < names.length; i++) {
            vp.add(new Hyperlink(names[i], links[i]));
        }

        menu.add(vp, header);
    }

    public void onValueChange(ValueChangeEvent<String> event) {
        String cazeId = event.getValue();
        if (cazeId.contains("/")) {
            cazeId = cazeId.substring(0, cazeId.indexOf("/"));
        }

        AbstractCase caze = null;
        try {
            caze = getCase(Cases.valueOf(cazeId));
            menu.showStack(Cases.valueOf(cazeId).ordinal());
        } catch (Exception e) {
            menu.showStack(0);
            caze = home;
        }

        panel.setWidget(caze);
        caze.init(event.getValue());
    }

    private AbstractCase getCase(Cases caze) {
        AbstractCase cazze = null;
        switch (caze) {
            case home:
                cazze = new SumShowcase();
                break;
            case wmp:
                cazze = new WMPShowcase();
                break;
            case qt:
                cazze = new QTShowcase();
                break;
            case swf:
                cazze = new SWFShowcase();
                break;
            case vlc:
                cazze = new VLCShowcase();
                break;
            case dyn:
                cazze = new DynaShowcase();
                break;
            case dynvd:
                cazze = new DynaVideoShowcase();
                break;
            case list:
                cazze = new PlaylistShowcase();
                break;
//            case link:
//                cazze = new LinkShowcase();
//                break;
            case misc:
                cazze = new MiscShowcase();
                break;
        }
        return cazze;
    }

    private enum Cases {

        home, wmp, qt, swf, vlc,
        dyn, dynvd, list, 
//        link,
        misc
    }
}
