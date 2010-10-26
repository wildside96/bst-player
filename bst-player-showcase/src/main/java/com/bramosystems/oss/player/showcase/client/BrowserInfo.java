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
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.bramosystems.oss.player.util.client.BrowserPlugin;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
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
        switch(option) {
            case pool:
                doMimePool();
                break;
//            case mimes:
//                doMimeTypes();
//                break;
            case plugins:
                doPlugins();
        }
    }

    private void doPlugins() {
        addPluginRow("Name" , "FileName", "Description");
        JsArray<BrowserPlugin> plugins = BrowserPlugin.getPlugins();
        for (int row = 1; row < plugins.length(); row++) {
            BrowserPlugin bp = plugins.get(row);
            addPluginRow(bp.getName(), bp.getFileName(), bp.getDescription());
        }
    }

    private void doMimeTypes() {
        addMimesRow("Type", "Description", "Enabled", "Suffixes");        
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
            
            addMimesRow(_type, _mb.getDesc(), "", "");

            for (int k = 0; k < _mb.getPlugins().size(); k++, row++) {
//                setHTML(row, 2, _mb.getPlugins().get(k).getName());
//                setHTML(row, 3, _mb.getPlugins().get(k).getSuffixes());
            }
            row++;
        }
    }

    private void doMimePool() {
        addPoolRow("Plugin", "Suffixes");
        MimePool pool = MimePool.instance;
        for (Plugin plug : Plugin.values()) {
            Set<String> suf = pool.getRegisteredExtensions(plug);
            addPoolRow(plug.name(), suf != null ? suf.toString() : "-");
        }
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

    private void addPluginRow(String name, String filename, String desc) {
        FlowPanel fp = new FPanel();
        fp.add(new XLabel(name, ResourceBundle.bundle.styles().pct30()));
        fp.add(new XLabel(filename, ResourceBundle.bundle.styles().pct30()));
        fp.add(new XLabel(desc, ResourceBundle.bundle.styles().pct40()));
        fp.add(new XLabel(ResourceBundle.bundle.styles().clear()));
        add(fp);
    }

    private void addMimesRow(String type, String desc, String enabled, String suffs) {
        FlowPanel fp = new FPanel();
        fp.add(new XLabel(type, ResourceBundle.bundle.styles().pct20()));
        fp.add(new XLabel(desc, ResourceBundle.bundle.styles().pct20()));
        fp.add(new XLabel(enabled, ResourceBundle.bundle.styles().pct20()));
        fp.add(new XLabel(suffs, ResourceBundle.bundle.styles().pct20()));
        fp.add(new XLabel(ResourceBundle.bundle.styles().clear()));
        add(fp);
    }

    private void addPoolRow(String plugin, String suffs) {
        FlowPanel fp = new FPanel();
        fp.add(new XLabel(plugin, ResourceBundle.bundle.styles().pct20()));
        fp.add(new XLabel(suffs, ResourceBundle.bundle.styles().pct80()));
        fp.add(new XLabel(ResourceBundle.bundle.styles().clear()));
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

        public FPanel() {
            setStyleName(ResourceBundle.bundle.styles().spacedPanel());
        }
    }
}
