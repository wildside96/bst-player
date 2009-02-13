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
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Showcase extends Composite implements EntryPoint, TabListener {
    private int prevSelectedTab = -10;
    private TabPanel tp;

    public Showcase() {
        DockPanel dp = new DockPanel();
        dp.setSize("90%", "100%");
        initWidget(dp);

        Label banner = new Label("BST Player Showcase");
        banner.setStyleName("showcase-Banner");
        dp.add(banner, DockPanel.NORTH);

        tp = new TabPanel();
        tp.addTabListener(this);
        tp.add(new CaseImpl(new SumShowcase()), "Home");
        tp.add(new CaseImpl(new WMPShowcase()), "Windows Media Player");
        tp.add(new CaseImpl(new QTShowcase()), "QuickTime Player");
        tp.add(new CaseImpl(new SWFShowcase()), "Shockwave Flash Plugin");
        tp.add(new CaseImpl(new DynaShowcase()), "Custom Player");
        tp.setWidth("100%");

        dp.add(tp, DockPanel.CENTER);
    }

    public void onModuleLoad() {
        RootPanel.get().add(this);
        tp.selectTab(0);
    }

    public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
        if(prevSelectedTab >= 0) {
            ((CaseImpl)tp.getWidget(prevSelectedTab)).stopPlayers();
        }
        prevSelectedTab = tabIndex;
        return true;
    }

    public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
    }

    private class CaseImpl extends Composite {
        private Case caze;

        public CaseImpl(Case caze) {
            this.caze = caze;

            DockPanel dc = new DockPanel();
            dc.setSpacing(0);
            dc.setWidth("100%");
            initWidget(dc);

            Label sum = new Label(caze.getSummary());
            sum.setStyleName("showcase-Summary");
            dc.add(sum, DockPanel.NORTH);

            Widget ct = caze.getContentWidget();
            ct.setStyleName("showcase-Case");
            dc.add(ct, DockPanel.CENTER);
        }

        public void stopPlayers() {
            caze.stopAllPlayers();
        }
    }
}
