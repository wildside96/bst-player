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

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class SumShowcase extends AbstractCase {

    public static String[] caseNames = {"Introduction", "Documentation"}; //, "Available Plugins", "Registered Mime Types"};
    public static String[] caseLinks = {"home/intro", "home/docs"}; //, "home/plugins", "home/mimetypes"};

    public SumShowcase() {
    }

    @Override
    public String getSummary() {
        return "Introduction";
    }

    @Override
    public void init(String token) {
        clearCases();
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 1:
                HTML wait = new HTML("Please wait while you are being redirected ...");
                addCase(null, null, wait, null);
                Timer t = new Timer() {

                    @Override
                    public void run() {
                        Window.Location.replace("http://oss.bramosystems.com/bst-player");
                    }
                };
                t.schedule(3000);
                break;
            /*                FlexTable table = new FlexTable();
            table.setCellSpacing(5);
            table.setCellPadding(5);

            table.setHTML(0, 0, "Name");
            table.setHTML(0, 1, "FileName");
            table.setHTML(0, 2, "Description");

            JsArray<BrowserPlugin> plugins = BrowserPlugin.getPlugins();
            for (int row = 1; row < plugins.length(); row++) {
            BrowserPlugin bp = plugins.get(row);
            table.setHTML(row, 0, bp.getName());
            table.setHTML(row, 1, bp.getFileName());
            table.setHTML(row, 2, bp.getDescription());
            }
            addCase("Browser Plugins", null, table, null);
            break;
            case 2:
            FlexTable tble = new FlexTable();
            tble.setCellSpacing(5);
            tble.setCellPadding(5);

            tble.setHTML(0, 0, "Type");
            tble.setHTML(0, 1, "Description");
            tble.setHTML(0, 2, "Enabled");
            tble.setHTML(0, 3, "Suffixes");

            JsArray<MimeType> mimes = MimeType.getMimeTypes();
            for (int row = 1; row < mimes.length(); row++) {
            MimeType bp = mimes.get(row);
            tble.setHTML(row, 0, bp.getType());
            tble.setHTML(row, 1, bp.getDescription());
            tble.setHTML(row, 2, bp.getEnabledPlugin() != null ?
            bp.getEnabledPlugin().getName() : "");
            tble.setHTML(row, 3, bp.getSuffixes());
            }
            addCase("Registered Mime Types", null, tble, null);
            break;
             */
            default:
                addCase(null, null, null, "sources/index.html");
        }
    }
}
