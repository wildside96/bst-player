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

import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.impl.MimePool;
import com.bramosystems.oss.player.resources.sources.Links;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.bramosystems.oss.player.util.client.BrowserPlugin;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.FlexTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class SumShowcase extends AbstractCase {
    public static AbstractCase instance = new SumShowcase();

    private SumShowcase() {
    }

    @Override
    public void initCase(Links link) {
        super.initCase(link);
        clearCases();
        switch (link) {
            case homeDocs:
                addCase(null, null, null, ResourceBundle.bundle.homeDocs());
                break;
            case homePlugins:
                FlexTable table = new FlexTable();
                table.setWidth("90%");
                table.setCellSpacing(5);
                table.setCellPadding(5);

                table.setHTML(0, 0, "Name");
                table.setHTML(0, 1, "FileName");
                table.setHTML(0, 2, "Description");

                JsArray<BrowserPlugin> plugins = BrowserPlugin.getPlugins();
                for (int row = 1; row < plugins.length(); row++) {
                    BrowserPlugin bp = plugins.get(row);
                    table.setHTML(row, 0, bp.getName());
                    table.setHTML(row, 1, bp.getFileName());
                    table.setHTML(row, 2, bp.getDescription());
                }
                addCase("Browser Plugins", null, table, null);
                break;
            case homeMimetypes:
                FlexTable tbl = new FlexTable();
                tbl.setWidth("90%");
                tbl.setCellSpacing(5);
                tbl.setCellPadding(5);

                tbl.setHTML(0, 0, "Plugin");
                tbl.setHTML(0, 1, "Suffixes");

                MimePool pool = MimePool.get();
                int row = 0;
                for (Plugin plug : Plugin.values()) {
                    HashSet<String> suf = pool.getRegisteredExtensions(plug);
                    tbl.setHTML(row, 0, plug.name());
                    tbl.setHTML(row++, 1, suf != null ? suf.toString() : "-");
                }
                addCase("Plugin/Registered Mime Types", null, tbl, null);

                FlexTable tble = new FlexTable();
                tble.setWidth("90%");
                tble.setCellSpacing(5);
                tble.setCellPadding(5);

                tble.setHTML(0, 0, "Type");
                tble.setHTML(0, 1, "Description");
                tble.setHTML(0, 2, "Enabled");
                tble.setHTML(0, 3, "Suffixes");

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

                row = 1;
                Iterator<String> types = _mimes.keySet().iterator();
                while (types.hasNext()) {
                    String _type = types.next();
                    MimeBean _mb = _mimes.get(_type);
                    tble.setHTML(row, 0, _type);
                    tble.setHTML(row, 1, _mb.getDesc());
                    for (int k = 0; k < _mb.getPlugins().size(); k++, row++) {
                        tble.setHTML(row, 2, _mb.getPlugins().get(k).getName());
                        tble.setHTML(row, 3, _mb.getPlugins().get(k).getSuffixes());
                    }
                    row++;
                }

                addCase("Registered Mime Types", null, tble, null);
                break;
            default:
                addCase(null, null, null, ResourceBundle.bundle.home());
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
