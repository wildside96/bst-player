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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public abstract class AbstractCase extends Composite {

    private VerticalPanel main;

    public AbstractCase() {
        DockPanel dc = new DockPanel();
        dc.setSpacing(0);
        dc.setWidth("100%");
        initWidget(dc);

        Label sum = new Label(getSummary());
        sum.setStyleName("showcase-Summary");
        dc.add(sum, DockPanel.NORTH);

        main = new VerticalPanel();
        main.setStyleName("showcase-Case");
        main.setWidth("100%");
        main.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
        dc.add(main, DockPanel.CENTER);
    }

    public abstract String getSummary();

    public abstract void init(String token);

    protected void addCase(String title, String description, Widget player, String codeSrc) {
        VerticalPanel hp = new VerticalPanel();
        hp.setWidth("80%");
        hp.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
        hp.setSpacing(10);

        if (title != null) {
            Label tlbl = new Label(title);
            tlbl.setStyleName("section-desc");
            hp.add(tlbl);
        }

        if (description != null) {
            Label dlbl = new Label(description);
            dlbl.setStyleName("media-desc");
            hp.add(dlbl);
        }

        if (player != null) {
            hp.add(player);
            hp.setCellWidth(player, "50%");
        }

        if (codeSrc != null && codeSrc.length() > 0) {
            hp.add(new IPage(GWT.getHostPageBaseURL() + codeSrc));
        }
        main.add(hp);
    }

    protected void clearCases() {
        main.clear();
    }

    protected int getTokenLinkIndex(String[] caseLinks, String token) {
        int index = 0;
        for (int i = 0; i < caseLinks.length; i++) {
            if (caseLinks[i].equals(token)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
