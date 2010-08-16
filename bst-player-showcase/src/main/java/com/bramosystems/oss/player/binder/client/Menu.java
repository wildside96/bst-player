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

import com.bramosystems.oss.player.common.client.*;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.bramosystems.oss.player.resources.sources.ShowcaseStyles;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class Menu extends FlowPanel {

    private ShowcaseStyles style;
    private MenuLinks links;

    public Menu() {
        style = ResourceBundle.bundle.styles();
        setStyleName(style.menuPanel());

        links = new MenuLinks();
        for (MenuEntry entry : MenuEntry.values()) {
            add(new MenuContent(entry));
        }
    }

    private class MenuContent extends FlowPanel implements ValueChangeHandler<String> {

        private MenuEntry entry;

        public MenuContent(MenuEntry entry) {
            this.entry = entry;

            Label header = new Label(entry.toString());
            header.setStyleName(style.header());
            header.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    showLinks();
                }
            });
            add(header);
            History.addValueChangeHandler(this);
        }

        private void showLinks() {
            add(links);
            links.updateLinks(entry);
        }

        public void onValueChange(ValueChangeEvent<String> event) {
            if (event.getValue().startsWith(entry.name())) {
                showLinks();
            }
        }
    }

    private class MenuLinks extends FlowPanel {

        public MenuLinks() {
            setStyleName(style.content());
        }

        public void updateLinks(MenuEntry entry) {
            clear();
            Links[] links = entry.getLinks();
            for (int i = 0; i < links.length; i++) {
                Hyperlink link = new Hyperlink(links[i].getTitle(), links[i].name());
                link.setStyleName(style.link());
                add(link);
            }
        }
    }
}
