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

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.skin.FlatVideoPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class DynaVideoShowcase extends AbstractCase {

    public DynaVideoShowcase() {
        Widget v = null;
        try {
            v = new FlatVideoPlayer(Plugin.WinMediaPlayer, 
                    "http://bst-player.googlecode.com/svn/tags/" +
                    "showcase/media/teaching-of-islam.wmv", false, "350px", "100%");
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            v = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            v = PlayerUtil.getMissingPluginNotice(Plugin.WinMediaPlayer);
        }
        addCase(v, "sources/custom-video/wmp.html");

        Widget v2 = null;
        try {
            FlatVideoPlayer vp = new FlatVideoPlayer(Plugin.FlashPlayer,
                    "http://movies.apple.com/movies/paramount/star_trek/startrek-tlr3_h.480.mov",
//                    "http://vixy.net/flv/vixy_net_podcaster_demo_director.flv",
                    false, "350px", "100%");
            vp.showLogger(true);
            v2 = vp;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            v2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            v2 = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
        }
        addCase(v2, "sources/custom-video/swf.html");

        Widget v3 = null;
        try {
            FlatVideoPlayer vv3 = new FlatVideoPlayer(Plugin.QuickTimePlayer, 
                    "http://movies.apple.com/movies/paramount/star_trek/" +
                    "startrek-tlr3_h.480.mov", false, "350px", "100%");
            vv3.showLogger(true);
            v3 = vv3;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            v3 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            v3 = PlayerUtil.getMissingPluginNotice(Plugin.QuickTimePlayer);
        }
        addCase(v3, "sources/custom-video/qt.html");

        Widget v4 = null;
        try {
            FlatVideoPlayer p = new FlatVideoPlayer(GWT.getHostPageBaseURL() + 
                    "media/sample.mov", false, "350px", "100%");
            p.showLogger(true);
            v4 = p;
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            v4 = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "Missing Plugin",
                    "No compatible video player plugin could be found", false);
        } catch (PluginNotFoundException ex) {
            v4 = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "Missing Plugin",
                    "No compatible video player plugin could be found", false);
        }
        addCase(v4, "sources/custom-video/dyn.html");
    }

    public String getSummary() {
        return "Working with custom video player controls.";
    }

}
