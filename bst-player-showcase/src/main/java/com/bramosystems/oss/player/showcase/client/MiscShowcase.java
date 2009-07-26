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

    public static String[] caseNames = {"Just play my file!",
        "Using playback repeats", "Embedding YouTube Video"};
    public static String[] caseLinks = {"misc/util", "misc/loopy", "misc/youtube"};

    public MiscShowcase() {
    }

    public String getSummary() {
        return "Miscellaneous media playback examples";
    }

    @Override
    public void init(String token) {
        clearCases();
        Widget w = null;
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
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
                addCase("Playing with any supported plugin", null, w, "sources/misc/util.html");
                break;
            case 1:
                try {
                    AbstractMediaPlayer mp = PlayerUtil.getPlayer(GWT.getHostPageBaseURL() +
                            "media/o-na-som.mp3", false, "50px", "100%");
                    mp.showLogger(true);
                    mp.setLoopCount(2);
                    w = mp;
                } catch (LoadException ex) {
                    Window.alert("Load Exp");
                } catch (PluginVersionException ex) {
                    w = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
                } catch (PluginNotFoundException ex) {
                    w = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
                }
                addCase("Play media twice!", null,
                        w, "sources/misc/loopy.html");
                break;
            case 2:
                PluginVersion plugVer = PluginVersion.get(9, 0, 0);
                try {
                    SWFWidget v = new SWFWidget("http://www.youtube.com/v/QbwZL-EK6CY&hl=en&fs=1&",
                            "100%", "350px", plugVer);
                    v.addProperty("bgcolor", "#000000");
                    w = v;
                } catch (PluginVersionException ex) {
                    w = SWFWidget.getMissingPluginNotice(plugVer);
                } catch (PluginNotFoundException ex) {
                    w = SWFWidget.getMissingPluginNotice(plugVer);
                }
                addCase("Embedding YouTube video", "Raw Video: Restored Video " +
                        "of Apollo 11 Moonwalk", w, "sources/misc/youtube.html");
                break;
        }
    }
}
