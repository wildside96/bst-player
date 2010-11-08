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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.MimePool;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.showcase.client.images.Bundle;
import com.bramosystems.oss.player.util.client.BrowserPlugin;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class BrowserInfo extends FlowPanel {

    public BrowserInfo() {
    }

    public void update(AppOptions option) {
        clear();
        switch (option) {
            case pool:
                doMimePool();
                break;
            case plugins:
                doPlugins();
        }
    }

    private void doPlugins() {
        addPluginRow("Name", "Plugin filename", "Description", EntryType.header);
        JsArray<BrowserPlugin> plugins = BrowserPlugin.getPlugins();
        for (int row = 1; row < plugins.length(); row++) {
            BrowserPlugin bp = plugins.get(row);
            addPluginRow(bp.getName(), bp.getFileName(), bp.getDescription(),
                    row % 2 != 0 ? EntryType.even : EntryType.odd);
        }
    }

    private void doMimeTypes() {
        addMimesRow("Type", "Description", "Enabled", "Suffixes", EntryType.header);
        HashMap<String, MimeBean> _mimes = new HashMap<String, MimeBean>();
        JsArray<MimeType> mimes = MimeType.getMimeTypes();
        for (int i = 0; i < mimes.length(); i++) {
            try {
                MimeType mt = mimes.get(i);
                MimeBean mb = _mimes.get(mt.getType());
                if (mb == null) {
                    mb = new MimeBean(mt.getType(), mt.getDescription());
                }
                mb.getPlugins().add(new PluginBean(
                        mt.getEnabledPlugin() != null ? mt.getEnabledPlugin().getName() : "",
                        mt.getSuffixes()));
                _mimes.put(mt.getType(), mb);
            } catch (PluginNotFoundException ex) {
            }
        }

        int row = 1;
        Iterator<String> types = _mimes.keySet().iterator();
        while (types.hasNext()) {
            String _type = types.next();
            MimeBean _mb = _mimes.get(_type);

            addMimesRow(_type, _mb.getDesc(), "", "", row % 2 != 0 ? EntryType.even : EntryType.odd);
            for (int k = 0; k < _mb.getPlugins().size(); k++, row++) {
//                setHTML(row, 2, _mb.getPlugins().get(k).getName());
//                setHTML(row, 3, _mb.getPlugins().get(k).getSuffixes());
            }
            row++;
        }
    }

    private void doMimePool() {
        addPoolRow("Player Widget", "Plugin Status", false, "Supported Filename Suffixes", EntryType.header);
        MimePool pool = MimePool.instance;
        ArrayList<Plugin> plugs = new ArrayList<Plugin>(Arrays.asList(Plugin.values()));
        plugs.remove(Plugin.Auto);
        plugs.remove(Plugin.MatrixSupport);
        plugs.remove(Plugin.PlaylistSupport);
        int row = 0;
        for (Plugin plug : plugs) {
            Boolean isSupported = null;
            String name = plug.name(), ver = null;
            PluginVersion pv = null;
            try {
                switch (plug) {
                    case DivXPlayer:
                        ver = "DivX Web Player ";
                        pv = PlayerUtil.getDivXPlayerPluginVersion();
                        ver += pv.toString();
                        isSupported = pv.compareTo(plug.getVersion()) >= 0;                        
                        break;
                    case QuickTimePlayer:
                        ver = "QuickTime Plug-in ";
                        pv = PlayerUtil.getQuickTimePluginVersion();
                        ver += pv.toString();
                        isSupported = pv.compareTo(plug.getVersion()) >= 0;
                        break;
                    case VLCPlayer:
                        ver = "VLC Multimedia Plug-in ";
                        pv = PlayerUtil.getVLCPlayerPluginVersion();
                        ver += pv.toString();
                        isSupported = pv.compareTo(plug.getVersion()) >= 0;
                        break;
                    case FlashPlayer:
                        name = "FlashMediaPlayer";
                        ver = "Shockwave Flash ";
                        pv = PlayerUtil.getFlashPlayerVersion();
                        isSupported = pv.compareTo(plug.getVersion()) >= 0;
                        ver += pv.toString();
                        break;
                    case WinMediaPlayer:
                        ver = "Windows Media Player Plugin";
                        pv = PlayerUtil.getWindowsMediaPlayerPluginVersion();
                        isSupported = pv.compareTo(plug.getVersion()) >= 0;
                        break;
                    case Native:
                        ver = "HTML5 <code>&lt;video&gt;</code>";
                        isSupported = PlayerUtil.isHTML5CompliantClient() ? true : null;
                        name = "NativePlayer";
                        break;
                }
            } catch (PluginNotFoundException ex) {
            }

            Set<String> suf = pool.getRegisteredExtensions(plug);
            addPoolRow(name, ver, isSupported,
                    suf != null ? suf.toString() : "-", row++ % 2 != 0 ? EntryType.even : EntryType.odd);
        }
    }

    private void addPluginRow(String name, String filename, String desc, EntryType type) {
        FlowPanel fp = new FPanel(type);
        fp.add(new XLabel(name, Bundle.bundle.css().pct30()));
        fp.add(new XLabel(filename, Bundle.bundle.css().pct30()));
        fp.add(new XLabel(desc, Bundle.bundle.css().pct40()));
        fp.add(new XLabel(Bundle.bundle.css().clear()));
        add(fp);
    }

    private void addMimesRow(String mime, String desc, String enabled, String suffs, EntryType type) {
        FlowPanel fp = new FPanel(type);
        fp.add(new XLabel(mime, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(desc, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(enabled, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(suffs, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(Bundle.bundle.css().clear()));
        add(fp);
    }

    private void addPoolRow(String plugin, String version, Boolean isSupted, String suffs, EntryType type) {
        FlowPanel fp = new FPanel(type);
        fp.add(new XLabel(plugin, Bundle.bundle.css().pct20()));
        if (type.equals(EntryType.header)) {
            fp.add(new XLabel(version, Bundle.bundle.css().pct20()));
        } else {
            XLabel lbl = new XLabel(version, "");
            if (isSupted == null) {
                lbl.setStyleName(Bundle.bundle.css().no());
                lbl.setTitle("Required plugin not found");
            } else {
                lbl.setStyleName(isSupted ? Bundle.bundle.css().yes() : Bundle.bundle.css().error());
                lbl.setTitle(isSupted ? "Installed plugin version" : "Installed plugin version not supported");
            }
            fp.add(lbl);
        }
        fp.add(new XLabel(suffs, Bundle.bundle.css().pct60()));
        fp.add(new XLabel(Bundle.bundle.css().clear()));
        add(fp);
    }

    private class XLabel extends HTML {

        public XLabel(String style) {
            setWordWrap(true);
            setStyleName(style);
        }

        public XLabel(String text, String style) {
            super(text, true);
            setStyleName(style);
        }
    }

    private class FPanel extends FlowPanel {

        public FPanel(EntryType entry) {
            setStyleName(Bundle.bundle.css().spacedPanel());
            switch (entry) {
                case header:
                    addStyleName(Bundle.bundle.css().headerRow());
                    break;
                case even:
                    addStyleName(Bundle.bundle.css().evenRow());
                    break;
                case odd:
                    addStyleName(Bundle.bundle.css().oddRow());
                    break;
            }
        }
    }

    private enum EntryType {

        header, even, odd
    }

    private class MimeBean implements Comparable<MimeBean> {

        private String type, desc;
        private ArrayList<PluginBean> plugins;

        public MimeBean(String type, String desc) {
            this.type = type;
            this.desc = desc;
            plugins = new ArrayList<PluginBean>();
        }

        public String getDesc() {
            return desc;
        }

        public String getType() {
            return type;
        }

        public ArrayList<PluginBean> getPlugins() {
            return plugins;
        }

        @Override
        public int compareTo(MimeBean o) {
            return type.compareTo(o.type);
        }
    }

    private class PluginBean {

        private String name, suffixes;

        public PluginBean(String name, String suffixes) {
            this.name = name;
            this.suffixes = suffixes;
        }

        public String getSuffixes() {
            return suffixes;
        }

        public String getName() {
            return name;
        }
    }
}
