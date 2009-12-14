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

import com.bramosystems.oss.player.showcase.client.matrix.MatrixShowcase;
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

        Label version = new Label("Version 1.1-SNAPSHOT");
        version.setStyleName("showcase-Version");

        HTML footer = new HTML("Copyright &copy; 2009 Braheem Sikiru<br/>" +
                "All other product, service names, brands, or trademarks, are " +
                "the property of their respective owners.");
        footer.setStyleName("showcase-Footer");

        menu = new StackPanel();
        menu.setWidth("200px");
        Cases cases[] = Cases.values();
        for (Cases caze : cases) {
            addCaseItems(caze);
        }

        DockPanel dp = new DockPanel();
        dp.setWidth("100%");
        dp.setHeight("100%");
        dp.setSpacing(5);
        dp.add(banner, DockPanel.NORTH);
        dp.add(version, DockPanel.NORTH);
        dp.add(footer, DockPanel.SOUTH);
        dp.add(menu, DockPanel.WEST);
//        dp.setCellWidth(menu, "200px");
        dp.add(panel, DockPanel.CENTER);
        dp.setCellWidth(panel, "100%");
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

        switch (caze) {
            case wmp:
                names = WMPShowcase.caseNames;
                links = WMPShowcase.caseLinks;
                break;
            case dyn:
                names = DynaShowcase.caseNames;
                links = DynaShowcase.caseLinks;
                break;
            case dynvd:
                names = DynaVideoShowcase.caseNames;
                links = DynaVideoShowcase.caseLinks;
                break;
            case home:
                names = SumShowcase.caseNames;
                links = SumShowcase.caseLinks;
                break;
//            case link:
//                names = LinkShowcase.caseNames;
//                links = LinkShowcase.caseLinks;
//                header = "Playable Links";
//                break;
            case list:
                names = PlaylistShowcase.caseNames;
                links = PlaylistShowcase.caseLinks;
                break;
            case misc:
                names = MiscShowcase.caseNames;
                links = MiscShowcase.caseLinks;
                break;
            case qt:
                names = QTShowcase.caseNames;
                links = QTShowcase.caseLinks;
                break;
            case swf:
                names = SWFShowcase.caseNames;
                links = SWFShowcase.caseLinks;
                break;
            case vlc:
                names = VLCShowcase.caseNames;
                links = VLCShowcase.caseLinks;
                break;
            case _native:
                names = NativeShowcase.caseNames;
                links = NativeShowcase.caseLinks;
                break;
            case matrix:
                names = MatrixShowcase.caseNames;
                links = MatrixShowcase.caseLinks;
                break;
        }

        for (int i = 0; i < names.length; i++) {
            vp.add(new Hyperlink(names[i], links[i]));
        }

        menu.add(vp, caze.getHeader());
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
            case _native:
                cazze = new NativeShowcase();
                break;
//            case link:
//                cazze = new LinkShowcase();
//                break;
            case misc:
                cazze = new MiscShowcase();
                break;
            case matrix:
                cazze = new MatrixShowcase();
                break;
        }
        return cazze;
    }

    private enum Cases {

        home("Home"),
        wmp("Windows Media Player"),
        qt("QuickTime Plugin"),
        swf("Flash Plugin"),
        vlc("VLC Media Player"),
        _native("HTML 5 Native Player"),
        dyn("Custom Audio Player"),
        dynvd("Custom Video Player"),
        list("Playlists"),
        //        link,
        matrix("Matrix Transformation"),
        misc("Miscellaneous Examples");
        String header;

        private Cases(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }
}
