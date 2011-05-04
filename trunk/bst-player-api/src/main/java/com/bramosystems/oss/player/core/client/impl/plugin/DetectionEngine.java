/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bramosystems.oss.player.core.client.impl.plugin;

import com.bramosystems.oss.player.core.client.PlayerInfo;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.spi.PlayerWidgetFactory;
import com.bramosystems.oss.player.util.client.RegExp;
import com.bramosystems.oss.player.util.client.RegExp.RegexException;
import com.google.gwt.core.client.GWT;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public abstract class DetectionEngine {

    private static DetectionEngine instance = GWT.create(DetectionEngine.class);
    private HashSet<String> matrixSupports = new HashSet<String>(), playlistSupports = new HashSet<String>();

    public static DetectionEngine getInstance() {
        return instance;
    }

    protected final void addPlayerSupports(Plugin plugin, String playerName) {
        switch (plugin) {
            case MatrixSupport:
                matrixSupports.add(playerName);
                break;
            case PlaylistSupport:
                playlistSupports.add(playerName);
                break;
        }
    }

    protected final boolean canHandleMedia(String player, String protocol, String ext) {
        PlayerInfo pif = PluginManager.getPlayerInfo(player);

        if (pif.getDetectedPluginVersion().compareTo(pif.getRequiredPluginVersion()) >= 0) {   // req plugin found...
            // check for streaming protocol & extension ...
            Set<String> types = pif.getRegisteredExtensions();
            Set<String> prots = pif.getRegisteredProtocols();
            return ((protocol != null) && (prots != null) && prots.contains(protocol.toLowerCase()))
                    || ((ext != null) && (types != null) && types.contains(ext.toLowerCase()));
        }
        return false;
    }

    protected final String extractExt(String mediaURL) {
        return mediaURL.substring(mediaURL.lastIndexOf(".") + 1);
    }

    protected final String extractProtocol(String mediaURL) {
        if (mediaURL.contains("://")) {
            return mediaURL.substring(0, mediaURL.indexOf("://"));
        } else {
            return null;
        }
    }

    /**
     * TODO: provide support for property file based sorting ...
     * @param plugin
     * @param mediaURL
     * @return 
     */
    private String getSupportedPlayer(Plugin plugin, String mediaURL) {
        String protocol = extractProtocol(mediaURL);
        String ext = extractExt(mediaURL);
        HashSet<String> pnames = new HashSet<String>();
        String pname = null;

        switch (plugin) {
            case MatrixSupport:
                pnames.addAll(matrixSupports);
                break;
            case PlaylistSupport:
                pnames.addAll(playlistSupports);
                break;
            case Auto:
                pnames.addAll(getRegisteredPlayerNames());
                break;
        }

        Iterator<String> it = pnames.iterator();
        while (it.hasNext()) {
            String pn = it.next();
            if (canHandleMedia(pn, protocol, ext)) {
                pname = pn;
                break;
            }
        }
        return pname;
    }

    /**
     * Util method to instantiate the player implementation for the specified name
     *
     * @param plugin
     * @param mediaURL
     * @param autoplay
     * @param height
     * @param width
     * @return
     * @throws LoadException
     * @throws PluginVersionException
     * @throws PluginNotFoundException
     */
    public AbstractMediaPlayer getPlayer(Plugin plugin, String mediaURL,
            boolean autoplay, String height, String width) throws LoadException, PluginVersionException, PluginNotFoundException {
        String pName = getSupportedPlayer(plugin, mediaURL);
        return getWidgetFactory(pName).getPlayer(pName, mediaURL, autoplay, height, width);
    }

    public AbstractMediaPlayer getPlayer(Plugin plugin, String mediaURL,
            boolean autoplay) throws LoadException, PluginVersionException, PluginNotFoundException {
        String pName = getSupportedPlayer(plugin, mediaURL);
        return getWidgetFactory(pName).getPlayer(pName, mediaURL, autoplay);
    }

    public PluginVersion getRequiredPluginVersion(String playerName) {
        try {
            RegExp.RegexResult res = RegExp.getRegExp("(\\d+).(\\d+).(\\d+)", "").exec(getRequiredPluginVersionImpl(playerName));
            return PluginVersion.get(Integer.parseInt(res.getMatch(1)),
                    Integer.parseInt(res.getMatch(2)), Integer.parseInt(res.getMatch(3)));
        } catch (RegexException ex) {
            throw new IllegalArgumentException("Player not registered - '" + playerName + "'");
        }
    }

    protected abstract String getRequiredPluginVersionImpl(String playerName);

    public abstract PlayerWidgetFactory getWidgetFactory(String playerName);

    public abstract Set<String> getRegisteredPlayerNames();
}
