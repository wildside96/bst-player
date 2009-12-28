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
package com.bramosystems.oss.player.binder.client.cases;

import com.bramosystems.oss.player.binder.client.MenuEntry;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class CaseFactory {

    @UiTemplate("wmp-basic.ui.xml")
    interface WMPBasic extends UiBinder<Widget, CaseFactory> {}
    @UiTemplate("wmp-basic.ui.xml")
    interface WMPLogger extends UiBinder<Widget, CaseFactory> {}
    @UiTemplate("wmp-video.ui.xml")
    interface WMPVideo extends UiBinder<Widget, CaseFactory> {}
    @UiTemplate("wmp-basic.ui.xml")
    interface WMPPlaylist extends UiBinder<Widget, CaseFactory> {}

    public Widget getCase(MenuEntry entry, String history) {
        Widget panel = null;
        switch (entry) {
            case _native:
                break;
            case dyn:
                break;
            case dynvd:
                break;
            case home:
                break;
            case list:
                break;
            case matrix:
                break;
            case misc:
                break;
            case qt:
                break;
            case swf:
                break;
            case vlc:
                break;
            case wmp:
                if (isLogger(history)) {
                    WMPLogger wmpb = GWT.create(WMPLogger.class);
                    panel = wmpb.createAndBindUi(this);
                } else if (isPlaylist(history)) {
                    WMPPlaylist wmpb = GWT.create(WMPPlaylist.class);
                    panel = wmpb.createAndBindUi(this);
                } else if (isVideo(history)) {
                    WMPVideo wmpb = GWT.create(WMPVideo.class);
                    panel = wmpb.createAndBindUi(this);
                } else {
                    WMPBasic wmpb = GWT.create(WMPBasic.class);
                    panel = wmpb.createAndBindUi(this);
                }
                break;
            case youtube:
                break;
        }
        return panel;
    }

    private boolean isLogger(String history) {
        return history.contains("logger");
    }

    private boolean isVideo(String history) {
        return history.contains("video");
    }

    private boolean isPlaylist(String history) {
        return history.contains("playlist");
    }
}
