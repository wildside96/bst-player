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
package com.bramosystems.oss.player.common.client;

import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.bramosystems.oss.player.resources.sources.ShowcaseStyles;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackLayoutPanel;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class Menu extends Composite {
    private ShowcaseStyles style;
    private StackLayoutPanel slp;

    public Menu() {
        style = ResourceBundle.bundle.styles();

        slp = new StackLayoutPanel(Unit.EM);
        slp.setStyleName(style.menuPanel());

        for (MenuEntry entry : MenuEntry.values()) {
            Label header = new Label(entry.toString());
            header.setStyleName(style.header());
            slp.add(new MenuContent(entry), header, 2);
        }
        initWidget(slp);
    }

    private class MenuContent extends FlowPanel implements ValueChangeHandler<String> {

        private MenuEntry entry;

        public MenuContent(MenuEntry entry) {
            this.entry = entry;
            setStyleName(style.content());

            Links[] links = entry.getLinks();
            for (int i = 0; i < links.length; i++) {
                Hyperlink link = new Hyperlink(links[i].getTitle(), links[i].name());
                link.setStyleName(style.link());
                add(link);
            }
            History.addValueChangeHandler(this);
        }

        public void onValueChange(ValueChangeEvent<String> event) {
            if (event.getValue().startsWith(entry.name())) {
                slp.showWidget(this);
            }
        }
    }
}
