/*
 *  Copyright 2009 Sikiru Braheem <sbraheem at bramosystems . com>.
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
package com.bramosystems.oss.player.binder.client;

import com.bramosystems.oss.player.common.client.Links;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class Case extends FlowPanel implements ValueChangeHandler<String> {

    private Label caseHeader;
    private SimplePanel casePanel;

    public Case() {
        caseHeader = new Label();
        caseHeader.setStyleName("case-header");

        casePanel = new SimplePanel();
        casePanel.setStyleName("case-content");

        setStyleName("content-wrapper");
        add(caseHeader);
        add(casePanel);

        History.addValueChangeHandler(this);
    }

    public void onValueChange(ValueChangeEvent<String> event) {
        Links link = Links.homeIntro;
        try {
            link = Links.valueOf(event.getValue());
        } catch (Exception e) {
        }
        caseHeader.setText(link.getTitle());
        casePanel.setWidget(ViewFactory.instance.getView(link));
    }
}
