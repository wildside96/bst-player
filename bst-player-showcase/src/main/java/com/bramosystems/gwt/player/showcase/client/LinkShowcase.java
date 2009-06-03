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
import com.bramosystems.oss.player.link.client.AudioLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class LinkShowcase extends AbstractCase {

    public LinkShowcase() {
        Widget c = null;
        try {
            c = new AudioLink(GWT.getHostPageBaseURL() + "media/o-na-som.mp3",
                    "O na som", false);
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            c = PlayerUtil.getMissingPluginNotice(Plugin.Auto, ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            c = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
        }
        addCase(c, "sources/custom-audio/wmp.html");

    }

    public String getSummary() {
        return "Using playable links.";
    }
}
