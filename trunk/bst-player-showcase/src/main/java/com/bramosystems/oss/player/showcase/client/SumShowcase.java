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

import com.bramosystems.oss.player.common.client.BrowserInfo;
import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.user.client.ui.FlexTable;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class SumShowcase extends AbstractCase {
    public static AbstractCase instance = new SumShowcase();

    private SumShowcase() {
    }

    @Override
    public void initCase(Links link) {
        clearCases();
        switch (link) {
            case homeDocs:
                addCase(null, null, null, ResourceBundle.bundle.homeDocs());
                break;
            case homePlugins:
                FlexTable table = new BrowserInfo(BrowserInfo.InfoType.plugins);
                addCase("Browser Plugins", null, table, null);
                break;
            case homeMimetypes:
                FlexTable tbl = new BrowserInfo(BrowserInfo.InfoType.mimePool);
                addCase("Plugin/Registered Mime Types", null, tbl, null);

                FlexTable tble = new BrowserInfo(BrowserInfo.InfoType.mimeType);
                addCase("Registered Mime Types", null, tble, null);
                break;
            default:
                addCase(null, null, null, ResourceBundle.bundle.home());
        }
    }
}
