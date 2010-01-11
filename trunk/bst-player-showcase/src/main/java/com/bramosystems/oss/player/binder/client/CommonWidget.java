/*
 *  Copyright 2010 Sikiru Braheem <sbraheem at bramosystems . com>.
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

import com.bramosystems.oss.player.common.client.BrowserInfo;
import com.bramosystems.oss.player.common.client.Links;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class CommonWidget extends Composite {

    @UiConstructor
    public CommonWidget(String name) {
        Links link = Links.valueOf(name);
        switch(link) {
            case homePlugins:
                initWidget(new BrowserInfo(BrowserInfo.InfoType.plugins));
                break;
            case homeMimetypes:
                FlowPanel fp = new FlowPanel();
                fp.add(new BrowserInfo(BrowserInfo.InfoType.mimePool));
                fp.add(new BrowserInfo(BrowserInfo.InfoType.mimeType));
                initWidget(fp);
        }
    }
}
