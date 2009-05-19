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

    protected void addCase(Widget player, String codeSrc) {
        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("100%");
        hp.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
        hp.setSpacing(10);
        hp.add(player);
        hp.add(new IPage(GWT.getHostPageBaseURL() + codeSrc));
        hp.setCellWidth(player, "50%");
        main.add(hp);
    }
}
