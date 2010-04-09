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

import com.bramosystems.oss.player.core.client.Plugin;
import com.google.gwt.core.client.GWT;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Utility class to get the file types associated with browser plugins
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.1
 */
public class MimePool {

    private HashMap<Plugin, HashSet<String>> pool;
    private HashMap<Plugin, HashSet<String>> protPool;
    private static MimePool instance;

    // TODO: Find mime-types for these extensions:
    // "wav,bwf,mid,midi,smf,au,snd,aiff,aif,aifc,"
    //  "cdda,ac3,caf,aac,adts,amr,amc,gsm,3gp,3gpp,3g2,3gp2,mp2,mp3,mp4,mov,"
    //  "qt,mqv,mpeg,mpg,m3u,sdv,m1s,m1a,m1v,mpm,mpv,mpa,m2a,m4a,m4p,m4b");

    
    private static final String[][] AudioMap = new String[][] {
        {"audio/mpeg","mp3"},
        {"audip/mp4","m4a,m4b,aac"},
        {"audio/ogg","ogg"},
        {"audio/x-wav","wav"},
        {"audio/x-mpegurl","m3u"}
    };

    private static final String[][] VideoMap = new String[][] {
        {"video/mpeg","mpg,mpeg"},
        {"video/mp4","mp4"},
        {"video/ogg","ogv"}
    };

    /**
     * Returns the MimePool object
     *
     * @return MimePool object
     */
    public static MimePool get() {
        if (instance == null) {
            instance = GWT.create(MimePool.class);
        }
        return instance;
    }

    private MimePool() {
        pool = new HashMap<Plugin, HashSet<String>>();
        protPool = new HashMap<Plugin, HashSet<String>>();

        initPools();
    }

    /**
     * Called by the constructor method to fill the mime pools
     */
    protected void initPools() {
        // fill the mimetype pool...
        addPluginExtensions(Plugin.QuickTimePlayer, "wav,bwf,mid,midi,smf,au,snd,aiff,aif,aifc,"
                + "cdda,ac3,caf,aac,adts,amr,amc,gsm,3gp,3gpp,3g2,3gp2,mp2,mp3,mp4,mov,"
                + "qt,mqv,mpeg,mpg,m3u,sdv,m1s,m1a,m1v,mpm,mpv,mpa,m2a,m4a,m4p,m4b");
        addPluginExtensions(Plugin.WinMediaPlayer, "asf,aif,aifc,aiff,au,avi,mid,mpe,mpeg,mpg,mpv2,mp2,mp3," +
                "m1v,snd,wav,wm,wma,wmv");
        addPluginExtensions(Plugin.FlashPlayer, "flv,f4v,m4a,mp4v,mp3,m3u"); // 3gp,3g2,mov,mp4
        addPluginExtensions(Plugin.VLCPlayer, "mp2,mp3,mpga,mpega,mpg,mpeg,mpe,vob,mp4,mpg4,avi,mov,"
                + "qt,ogg,ogv,vlc,asf,asx,wmv,wav,3gp,3gpp,3g2,3gpp2,divx,flv,mkv,mka,xspf,m4a,m3u,wma");
        addPluginExtensions(Plugin.DivXPlayer, "divx,avi,mkv");
        addNativePluginExtensions();

        addPluginProtocols(Plugin.QuickTimePlayer, "rtsp,rts");
        addPluginProtocols(Plugin.VLCPlayer, "rtp,rtsp,mms,udp");
        addPluginProtocols(Plugin.WinMediaPlayer, "rtsp,rtspu,rtspt,mms,mmsu,mmst,wmpcd,wmpdvd");
        
    }

    private void addNativePluginExtensions() {
        NativePlayerUtil npu = NativePlayerUtil.get();
        for (int i=0;i<AudioMap.length;i++) {
            String mime = AudioMap[i][0];
            String ext = AudioMap[i][1];
            if (npu.canPlayAudio(mime)) {
                addPluginExtensions(Plugin.Native, ext);
            }
        }
        for (int i=0;i<VideoMap.length;i++) {
            String mime = VideoMap[i][0];
            String ext = VideoMap[i][1];
            if (npu.canPlayVideo(mime)) {
                addPluginExtensions(Plugin.Native, ext);
            }
        }
    }

    /**
     * Returns the file extensions registered on the specified plugin
     *
     * @param plugin the desired plugin
     * @return the registered file extensions
     */
    public final HashSet<String> getRegisteredExtensions(Plugin plugin) {
        return pool.get(plugin);
    }

    /**
     * Returns the streaming protocols registered on the specified plugin
     *
     * @param plugin the desired plugin
     * @return the registered streaming protocols
     */
    public final HashSet<String> getRegisteredProtocols(Plugin plugin) {
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
            HashSet<String> suffx = new HashSet<String>();
            if (pool.containsKey(plugin)) {
                suffx = pool.get(plugin);
            } else {
                pool.put(plugin, suffx);
            }

            String[] suffxs = extensions.split(",");
            for (String suf : suffxs) {
                suffx.add(suf);
            }
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
            HashSet<String> suffx = new HashSet<String>();
            if (protPool.containsKey(plugin)) {
                suffx = protPool.get(plugin);
            } else {
                protPool.put(plugin, suffx);
            }

            String[] suffxs = protocols.split(",");
            for (String suf : suffxs) {
                suffx.add(suf);
            }
        }
    }

}
