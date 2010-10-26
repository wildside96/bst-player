/*
 *  Copyright 2010 Sikiru.
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
package com.bramosystems.oss.player.showcase.client.panes;

import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.bramosystems.oss.player.showcase.client.AppOptions;
import com.bramosystems.oss.player.showcase.client.event.PluginChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PluginChangeHandler;
import com.bramosystems.oss.player.showcase.client.images.Bundle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikiru
 */
public class PluginPane extends FlowPanel implements ValueChangeHandler<String> {

    public PluginPane() {
        add(new PPanel(Plugin.FlashPlayer));
        add(new PPanel(Plugin.QuickTimePlayer));
        add(new PPanel(Plugin.WinMediaPlayer));
        add(new PPanel(Plugin.VLCPlayer));
        add(new PPanel(Plugin.Native));
        add(new PPanel(Plugin.DivXPlayer));

        Label l = new Label();
        l.setStyleName(ResourceBundle.bundle.styles().clear());
        History.addValueChangeHandler(this);
    }

    public HandlerRegistration addChangeHandler(PluginChangeHandler handler) {
        return addHandler(handler, PluginChangeEvent.TYPE);
    }

    @Override
    protected void onLoad() {
        fireEvent(new PluginChangeEvent(Plugin.Auto));
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        AppOptions option = AppOptions.home;
        try {
            option = AppOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }

        switch (option) {
            case capsule:
            case core:
            case flat:
                for (int i = 0; i < getWidgetCount(); i++) {
                    PPanel pp = ((PPanel) getWidget(i));
                    pp.setEnabled(hasPlugin(pp.plugin));
                }
                break;
            case home:
                break;
            case matrix:
                for (int i = 0; i < getWidgetCount(); i++) {
                    PPanel pp = ((PPanel) getWidget(i));
                    pp.setEnabled(hasMatrixSupport(pp.plugin));
                }
                break;
//            case mimes:
            case plugins:
            case pool:
        }
    }

    private boolean hasMatrixSupport(Plugin plugin) {
        boolean has = false;
        switch (plugin) {
            case FlashPlayer:
            case QuickTimePlayer:
                has = hasPlugin(plugin);
        }
        return has;
    }

    private boolean hasPlugin(Plugin plugin) {
        if (plugin.equals(Plugin.Native)) {
            return PlayerUtil.isHTML5CompliantClient();
        }

        try {
            PluginVersion req = plugin.getVersion();
            PluginVersion v = null;
            switch (plugin) {
                case DivXPlayer:
                    v = PlayerUtil.getDivXPlayerPluginVersion();
                    break;
                case FlashPlayer:
                    v = PlayerUtil.getFlashPlayerVersion();
                    break;
                case QuickTimePlayer:
                    v = PlayerUtil.getQuickTimePluginVersion();
                    break;
                case VLCPlayer:
                    v = PlayerUtil.getVLCPlayerPluginVersion();
                    break;
                case WinMediaPlayer:
                    v = PlayerUtil.getWindowsMediaPlayerPluginVersion();
                    break;
            }
            return v.compareTo(req) >= 0;
        } catch (PluginNotFoundException ex) {
            return false;
        }
    }

    private class PPanel extends Label {

        private boolean enabled;
        private Plugin plugin;

        public PPanel(final Plugin p) {
            plugin = p;
            setText(p.name());
            switch (plugin) {
                case VLCPlayer:
                    setStylePrimaryName(Bundle.bundle.css().vlcPlugin());
                    break;
                default:
                    setStylePrimaryName(Bundle.bundle.css().pluginBox());
            }
            addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (enabled) {
                        PluginPane.this.fireEvent(new PluginChangeEvent(p));
                    }
                }
            });
        }

        public final void setEnabled(boolean enabled) {
            this.enabled = enabled;
            if (enabled) {
                removeStyleName(Bundle.bundle.css().disabledPlugin());
            } else {
                addStyleName(Bundle.bundle.css().disabledPlugin());
            }
        }
    }
}
