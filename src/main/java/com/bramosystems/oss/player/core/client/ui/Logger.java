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
package com.bramosystems.oss.player.core.client.ui;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
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
    private LoggerConsoleImpl impl;

    /**
     * Constructs a Logger object
     */
    public Logger() {
        impl = GWT.create(LoggerConsoleImpl.class);

        // build the indicator...
        DisclosurePanel dp = new DisclosurePanel(imgpack.disclosurePanelOpen(),
                imgpack.disclosurePanelClosed(), "");
        dp.setAnimationEnabled(true);
        dp.setStyleName("");
        dp.add(impl.getConsole());
        initWidget(dp);
        setWidth("100%");
    }

    /**
     * Convenience method to return a Logger object attached to the specified
     * <code>player</code>. All debug messages of the player are logged by the
     * returned object.
     *
     * @param player the player whose debug messages is to be logged
     * @return the Logger object
     * @since 2.0
     */
    public static Logger getLogger(AbstractMediaPlayer player) {
        final Logger l = new Logger();
        player.addDebugHandler(new DebugHandler() {
            @Override
            public void onDebug(DebugEvent event) {
                l.log(event.getMessage(), true);
            }
        });
        return l;
    }

    /**
     * Adds <code>message</code> to the list of messages in this objects' log
     *
     * @param message message to add to the log
     * @param asHTML <code>true</code> if {@code message} should be interpreted as
     * HTML, <code>false</code> otherwise.
     */
    public void log(String message, boolean asHTML) {
        impl.log(message, asHTML);
    }

    interface ImgPack extends ClientBundle {

        @Source("expandLogger.png")
        ImageResource disclosurePanelClosed();

        @Source("collapseLogger.png")
        ImageResource disclosurePanelOpen();
    }

    static class LoggerConsoleImpl {

        private FlowPanel console;
        private ScrollPanel sp;

        public LoggerConsoleImpl() {
        }

        public Widget getConsole() {
            console = new FlowPanel();
            sp = new ScrollPanel(console);
            DOM.setStyleAttribute(sp.getElement(), "background", "white");
            DOM.setStyleAttribute(sp.getElement(), "border", "1px solid #ccc");
            DOM.setStyleAttribute(sp.getElement(), "fontSize", "10pt");
            DOM.setStyleAttribute(sp.getElement(), "padding", "5px");
            sp.setHeight("200px");
            return sp;
        }

        public void log(String message, boolean asHTML) {
            if (asHTML) {
                console.add(new HTML("- " + message));
            } else {
                console.add(new Label("- " + message));
            }
            sp.scrollToBottom();
        }
    }

    static class NullLoggerConsoleImpl extends LoggerConsoleImpl {

        public NullLoggerConsoleImpl() {
        }

        @Override
        public Widget getConsole() {
            return new SimplePanel();
        }

        @Override
        public void log(String message, boolean asHTML) {
        }
    }
}
