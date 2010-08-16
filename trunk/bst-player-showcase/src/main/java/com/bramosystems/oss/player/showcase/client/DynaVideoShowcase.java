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

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.bramosystems.oss.player.common.client.Links;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class DynaVideoShowcase extends AbstractCase {
    public static AbstractCase instance = new DynaVideoShowcase();

    private DynaVideoShowcase() {
    }

    @Override
    public void initCase(Links link) {
        Widget v = null;
        switch (link) {
            case dynVdAuto:
                try {
                    FlatVideoPlayer p = new FlatVideoPlayer(GWT.getHostPageBaseURL() +
                            "media/big-buck-bunny.mp4", false, getCaseHeight(), "100%");
                    p.showLogger(true);
                    v = p;
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "Missing Plugin",
                            "No compatible video player plugin could be found", false);
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.Auto, "Missing Plugin",
                            "No compatible video player plugin could be found", false);
                }
                addCase("Embedding video with any suitable plugin", null, v,
                        ResourceBundle.bundle.vidAuto());
                break;
        }
    }
}
