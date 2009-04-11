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
package com.bramosystems.gwt.player.core.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

/**
 * Provides a widget for logging purposes especially useful during debugging.
 *
 * @since 0.6
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Logger extends Composite {

    private ImgPack imgpack = GWT.create(ImgPack.class);
    private VerticalPanel console;
    private ScrollPanel sp;

    /**
     * Constructs a Logger object
     */
    public Logger() {
        console = new VerticalPanel();

        sp = new ScrollPanel(console);
        DOM.setStyleAttribute(sp.getElement(), "background", "white");
        DOM.setStyleAttribute(sp.getElement(), "border", "1px solid #ccc");
        DOM.setStyleAttribute(sp.getElement(), "fontSize", "10pt");
        DOM.setStyleAttribute(sp.getElement(), "padding", "5px");
        sp.setHeight("200px");

        // build the indicator...
        DisclosurePanel dp = new DisclosurePanel(imgpack, "", false);
        dp.setAnimationEnabled(true);
        dp.setStyleName("");
        dp.add(sp);
        initWidget(dp);
        setWidth("100%");
    }

    /**
     * Adds <code>message</code> to the list of messages in this objects' log
     *
     * @param message message to add to the log
     * @param asHTML <code>true</code> if {@code message} should be interpreted as
     * HTML, <code>false</code> otherwise.
     */
    public void log(String message, boolean asHTML) {
        if (asHTML) {
            console.add(new HTML("- " + message));
        } else {
            console.add(new Label("- " + message));
        }
        sp.scrollToBottom();
    }

    interface ImgPack extends DisclosurePanelImages {

        @Resource("expandLogger.png")
        AbstractImagePrototype disclosurePanelClosed();

        @Resource("collapseLogger.png")
        AbstractImagePrototype disclosurePanelOpen();
    }
}
