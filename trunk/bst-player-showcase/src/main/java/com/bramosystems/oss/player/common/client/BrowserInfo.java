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
package com.bramosystems.oss.player.common.client;

import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.MimePool;
import com.bramosystems.oss.player.util.client.BrowserPlugin;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.FlexTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class BrowserInfo extends FlexTable {

    public BrowserInfo(InfoType type) {
        setWidth("90%");
        setCellSpacing(5);
        setCellPadding(5);

        switch(type) {
            case mimePool:
                doMimePool();
                break;
            case mimeType:
                doMimeTypes();
                break;
            case plugins:
                doPlugins();
        }
    }

    private void doPlugins() {
        setHTML(0, 0, "Name");
        setHTML(0, 1, "FileName");
        setHTML(0, 2, "Description");

        JsArray<BrowserPlugin> plugins = BrowserPlugin.getPlugins();
        for (int row = 1; row < plugins.length(); row++) {
            BrowserPlugin bp = plugins.get(row);
            setHTML(row, 0, bp.getName());
            setHTML(row, 1, bp.getFileName());
            setHTML(row, 2, bp.getDescription());
        }

    }

    private void doMimeTypes() {
        setHTML(0, 0, "Type");
        setHTML(0, 1, "Description");
        setHTML(0, 2, "Enabled");
        setHTML(0, 3, "Suffixes");

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
            setHTML(row, 0, _type);
            setHTML(row, 1, _mb.getDesc());
            for (int k = 0; k < _mb.getPlugins().size(); k++, row++) {
                setHTML(row, 2, _mb.getPlugins().get(k).getName());
                setHTML(row, 3, _mb.getPlugins().get(k).getSuffixes());
            }
            row++;
        }
    }

    private void doMimePool() {
        setHTML(0, 0, "Plugin");
        setHTML(0, 1, "Suffixes");

        MimePool pool = MimePool.instance;
        int row = 0;
        for (Plugin plug : Plugin.values()) {
            Set<String> suf = pool.getRegisteredExtensions(plug);
            setHTML(row, 0, plug.name());
            setHTML(row++, 1, suf != null ? suf.toString() : "-");
        }
    }

    public static enum InfoType {
        mimePool, mimeType, plugins
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
