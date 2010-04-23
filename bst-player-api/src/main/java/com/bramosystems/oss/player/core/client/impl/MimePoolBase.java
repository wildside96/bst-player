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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.*;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Utility class to get the file types associated with browser plugins
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * TODO: verify version numbers...
 * @since 1.1.1
 */
public abstract class MimePoolBase implements MimePool {

    private EnumMap<Plugin, TreeSet<String>> pool;
    private EnumMap<Plugin, TreeSet<String>> protPool;
    private HashMap<String, String> mimeTypes;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    protected MimePoolBase() {
        pool = new EnumMap<Plugin, TreeSet<String>>(Plugin.class);
        protPool = new EnumMap<Plugin, TreeSet<String>>(Plugin.class);
        mimeTypes = new HashMap<String, String>();

        initMimeTypes(mimeTypes);
        initPools();
        addNativePluginExtensions();
    }

    /**
     * Called by the constructor method to fill the mime pools
     */
    protected abstract void initPools();

    /**
     * Called by the constructor method to populate all known audio/video
     * mime types.
     *
     * @param mimeTypes the mimeType map to be filled.  The map is filled as
     * (mimeType,file-extension) value pairs.
     */
    protected abstract void initMimeTypes(HashMap<String, String> mimeTypes);

    @Override
    public HashMap<String, String> getMimeTypes() {
        return mimeTypes;
    }

    @Override
    public final Set<String> getRegisteredExtensions(Plugin plugin) {
        return pool.get(plugin);
    }

    @Override
    public final Set<String> getRegisteredProtocols(Plugin plugin) {
        return protPool.get(plugin);
    }

    /**
     * Adds the specified extensions to the pool of file types supported by the
     * plugin
     *
     * @param plugin the plugin
     * @param extensions the file types supported by the plugin, multiple types should
     * be separated by commas e.g. wav,wma
     */
    protected final void addPluginExtensions(Plugin plugin, String extensions) {
        if (plugin == null || extensions == null) {
            return;
        }

        if (extensions.length() > 0) {
            TreeSet<String> suffx = new TreeSet<String>();
            if (pool.containsKey(plugin)) {
                suffx = pool.get(plugin);
            } else {
                pool.put(plugin, suffx);
            }

            String[] suffxs = extensions.split(",");
            suffx.addAll(Arrays.asList(suffxs));
        }
    }

    /**
     * Adds the specified protocols to the pool of streaming protocols supported by the
     * plugin
     *
     * @param plugin the plugin
     * @param protocols the streaming protocol supported by the plugin, multiple types should
     * be separated by commas e.g. rtp,rtsp
     */
    protected final void addPluginProtocols(Plugin plugin, String protocols) {
        if (plugin == null || protocols == null) {
            return;
        }

        if (protocols.length() > 0) {
            TreeSet<String> suffx = new TreeSet<String>();
            if (protPool.containsKey(plugin)) {
                suffx = protPool.get(plugin);
            } else {
                protPool.put(plugin, suffx);
            }

            String[] suffxs = protocols.split(",");
            suffx.addAll(Arrays.asList(suffxs));
        }
    }

    /**
     * Adds all audio/video file types that have native support on the client.
     * This call has no effect on non-HTML5 compliant browsers.
     */
    protected void addNativePluginExtensions() {
        if (PlayerUtil.isHTML5CompliantClient()) {
            NativePlayerUtil.TestUtil test = NativePlayerUtil.getTestUtil();
            Iterator<String> mimeKeys = mimeTypes.keySet().iterator();
            while (mimeKeys.hasNext()) {
                String mime = mimeKeys.next();
                try {
                    switch (test.canPlayType(mime)) {
                        case maybe:
                        case probably:
                            addPluginExtensions(Plugin.Native, mimeTypes.get(mime));
                    }
                } catch (Exception e) { // mimeType cannot be played ...
                }
            }
        }
    }
}
