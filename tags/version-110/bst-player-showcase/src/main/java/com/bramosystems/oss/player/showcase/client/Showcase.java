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

import com.bramosystems.oss.player.common.client.Menu;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Showcase extends Composite implements EntryPoint {

    public Showcase() {
        HTML banner = new HTML("<div class='app-banner'>BST Player Showcase</div>" +
                "<div class='app-version'>Version 1.1-SNAPSHOT</div>");

        HTML footer = new HTML("Copyright &copy; 2009 Braheem Sikiru<br/>" +
                "All other product, service names, brands, or trademarks, are " +
                "the property of their respective owners.");
        footer.setStyleName("app-footer");

        DockLayoutPanel dp = new DockLayoutPanel(Unit.PX);
        dp.setStyleName("app-wrapper");
        dp.addNorth(banner, 50);
        dp.addSouth(footer, 40);
        dp.addWest(new Menu(), 200);
        dp.add(new CaseHandler());
        initWidget(dp);
    }

    public void onModuleLoad() {
        ResourceBundle.bundle.styles().ensureInjected();

        RootPanel.get("loading").setVisible(false);
        RootLayoutPanel.get().add(this);

        History.fireCurrentHistoryState();
}
}
