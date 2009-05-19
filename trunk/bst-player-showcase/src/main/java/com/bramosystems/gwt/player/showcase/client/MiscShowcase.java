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

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class MiscShowcase extends AbstractCase {

    public MiscShowcase() {
        Widget w = null;
        try {
            AbstractMediaPlayer mp = PlayerUtil.getPlayer(GWT.getHostPageBaseURL() +
                    "media/applause.mp3", false, "50px", "100%");
            mp.showLogger(true);
            w = mp;
        } catch (LoadException ex) {
            Window.alert("Load Exp");
        } catch (PluginVersionException ex) {
            w = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
        } catch (PluginNotFoundException ex) {
            w = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
        }
        addCase(w, "sources/misc/util.html");

        Widget w0 = null;
        try {
            AbstractMediaPlayer mp = PlayerUtil.getPlayer(GWT.getHostPageBaseURL() +
                    "media/o-na-som.mp3", false, "50px", "100%");
            mp.showLogger(true);
            mp.setLoopCount(2);
            w0 = mp;
        } catch (LoadException ex) {
            Window.alert("Load Exp");
        } catch (PluginVersionException ex) {
            w0 = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
        } catch (PluginNotFoundException ex) {
            w0 = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
        }
        addCase(w0, "sources/misc/loopy.html");

        Widget w1 = null;
        try {
            // bill clinton in 9ja about emegwuali
            SWFWidget v2 = new SWFWidget("http://video.google.com/googleplayer.swf?" +
                    "docid=-8228484564915781123&hl=en&fs=true",
                    "100%", "350px", PluginVersion.get(9, 0, 0));
            v2.addProperty("bgcolor", "#000000");
            w1 = v2;
        } catch (PluginVersionException ex) {
            w1 = SWFWidget.getMissingPluginNotice(PluginVersion.get(9, 0, 0));
        } catch (PluginNotFoundException ex) {
            w1 = SWFWidget.getMissingPluginNotice(PluginVersion.get(9, 0, 0));
        }
        addCase(w1, "sources/misc/google.html");

        Widget w2 = null;
        try {
            // barack obama on election night...
            SWFWidget v = new SWFWidget("http://www.youtube.com/v/HfHbw3n0EIM&hl=en&fs=1",
                    "100%", "350px", PluginVersion.get(9, 0, 0));
            v.addProperty("bgcolor", "#000000");
            w2 = v;
        } catch (PluginVersionException ex) {
            w2 = SWFWidget.getMissingPluginNotice(PluginVersion.get(9, 0, 0));
        } catch (PluginNotFoundException ex) {
            w2 = SWFWidget.getMissingPluginNotice(PluginVersion.get(9, 0, 0));
        }
        addCase(w2, "sources/misc/youtube.html");

    }

    public String getSummary() {
        return "Miscellaneous media playback examples";
    }
}
