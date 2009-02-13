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

import com.bramosystems.gwt.player.client.LoadException;
import com.bramosystems.gwt.player.client.PlayerUtil;
import com.bramosystems.gwt.player.client.Plugin;
import com.bramosystems.gwt.player.client.PluginNotFoundException;
import com.bramosystems.gwt.player.client.PluginVersionException;
import com.bramosystems.gwt.player.client.ui.QuickTimePlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class QTShowcase implements Case {
    private VerticalPanel vp;
    private QuickTimePlayer p1, p2;

    public QTShowcase() {
        vp = new VerticalPanel();
        vp.setSpacing(20);

        vp.add(new Label("Playing sound automatically"));
        Widget wmp = null;
        try {
            p1 = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/applause.mp3");
            wmp = p1;
            wmp.setWidth("80%");
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            wmp = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        } catch (PluginNotFoundException ex) {
            wmp = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
        vp.add(wmp);

        vp.add(new Label("Playing sound with autoplay set to false"));
        Widget wmp2 = null;
        try {
            p2 = new QuickTimePlayer(GWT.getHostPageBaseURL() + "media/thunder.mp3", false);
            wmp2 = p2;
            wmp2.setWidth("80%");
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            wmp2 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        } catch (PluginNotFoundException ex) {
            wmp2 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
        vp.add(wmp2);
    }

    public String getSummary() {
        return "Using QuickTime Player plugin";
    }

    public Widget getContentWidget() {
        return vp;
    }

    public void stopAllPlayers() {
        if (p1 != null) {
            p1.stopMedia();
        }
        if (p2 != null) {
            p2.stopMedia();
        }
    }
}
