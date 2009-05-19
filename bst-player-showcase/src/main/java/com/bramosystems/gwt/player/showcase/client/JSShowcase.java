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

import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.skin.FlatVideoPlayer;
import com.bramosystems.oss.player.core.client.ui.FlashMP3Player;
import com.bramosystems.oss.player.core.client.ui.FlashVideoPlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.MissingResourceException;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class JSShowcase extends Composite implements EntryPoint {

    public JSShowcase() {
    }

    public void onModuleLoad() {
        boolean finished = false;
        int index = 1;
        while (!finished) {
            String pid = "bst_player_" + index;
            try {
                Dictionary options = Dictionary.getDictionary(pid);
                if (options.keySet().contains("widget")) {
                    loadWidget(options);
                } else {
                    loadPlayer(options);
                }
                index++;
            } catch (MissingResourceException e) {
                finished = true;
            }
        }
    }

    private void loadPlayer(Dictionary options) {
        RootPanel container = RootPanel.get(options.get("divId"));
        container.clear();

        Plugin plugin = Plugin.valueOf(options.get("plugin"));
        try {
            String url = decodeURL(options.get("url"));
            boolean auto = Boolean.parseBoolean(options.get("autoplay"));
            String height = options.get("height");
            String width = options.get("width");
            AbstractMediaPlayer p = null;

            if (options.keySet().contains("skin")) {
                p = getPlayer(options.get("skin"), plugin, url, auto, height, width);
            } else {
                p = getPlayer(plugin, url, auto, height, width);
            }

            if (options.keySet().contains("showLogger")) {
                p.showLogger(Boolean.parseBoolean(options.get("showLogger")));
            }
            if (options.keySet().contains("loopCount")) {
                p.setLoopCount(Integer.parseInt(options.get("loopCount")));
            }
            container.add(p);
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            container.add(PlayerUtil.getMissingPluginNotice(plugin, ex.getRequiredVersion()));
        } catch (PluginNotFoundException ex) {
            container.add(PlayerUtil.getMissingPluginNotice(plugin));
        }
    }

    private void loadWidget(Dictionary options) {
        RootPanel container = RootPanel.get(options.get("divId"));
        container.clear();

        try {
            String url = decodeURL(options.get("url"));
            String height = options.get("height");
            String width = options.get("width");

            SWFWidget swf = new SWFWidget(url, width, height, PluginVersion.get(9, 0, 0));
            swf.addProperty("bgcolor", "#000000");
            container.add(swf);
        } catch (PluginVersionException ex) {
            container.add(SWFWidget.getMissingPluginNotice(PluginVersion.get(9, 0, 0)));
        } catch (PluginNotFoundException ex) {
            container.add(SWFWidget.getMissingPluginNotice(PluginVersion.get(9, 0, 0)));
        }
    }

    private String decodeURL(String url) {
        if (url.contains("://") || url.startsWith("/")) {
            return url;
        } else {
            return GWT.getHostPageBaseURL() + url;
        }
    }

    protected AbstractMediaPlayer getPlayer(Plugin plugin, String url, boolean autoplay,
            String height, String width) throws LoadException, PluginNotFoundException,
            PluginVersionException {
        AbstractMediaPlayer p = null;
        switch (plugin) {
            case WinMediaPlayer:
                p = new WinMediaPlayer(url, autoplay, height, width);
                break;
            case FlashMP3Player:
                p = new FlashMP3Player(url, autoplay, height, width);
                break;
            case FlashVideoPlayer:
                p = new FlashVideoPlayer(url, autoplay, height, width);
                break;
            case QuickTimePlayer:
                p = new QuickTimePlayer(url, autoplay, height, width);
                break;
            default:
                p = PlayerUtil.getPlayer(url, autoplay, height, width);
        }
        return p;
    }

    protected AbstractMediaPlayer getPlayer(String skin, Plugin plugin, String url,
            boolean autoplay, String height, String width) throws LoadException,
            PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer p = null;
        switch (Skin.valueOf(skin)) {
            case Capsule:
                p = new Capsule(plugin, url, autoplay);
                p.setWidth(width);
                break;
            case FlatVideoPlayer:
                p = new FlatVideoPlayer(plugin, url, autoplay, height, width);
                break;
            default:
                p = PlayerUtil.getPlayer(url, autoplay, height, width);
        }
        return p;
    }

    private enum Skin {

        Capsule, FlatVideoPlayer, Auto
    }
}
