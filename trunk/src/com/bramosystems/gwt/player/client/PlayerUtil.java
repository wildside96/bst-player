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
package com.bramosystems.gwt.player.client;

import com.bramosystems.gwt.player.client.impl.PlayerUtilImpl;
import com.bramosystems.gwt.player.client.ui.FlashMP3Player;
import com.bramosystems.gwt.player.client.ui.QuickTimePlayer;
import com.bramosystems.gwt.player.client.ui.WinMediaPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;

/**
 * Utility class for various media player related functions.
 *
 * @author Sikirulai Braheem
 */
public class PlayerUtil {

    private static PlayerUtilImpl impl = GWT.create(PlayerUtilImpl.class);
    private static NumberFormat nf = NumberFormat.getFormat("00");

    /**
     * Formats the specified time (in milliseconds) into time string in the
     * format <code>hh:mm;ss</code>.
     *
     * <p>The hours part of the formatted time is omitted if {@code milliseconds} time
     * is less than 60 minutes.
     *
     * @param milliSeconds media time to be formatted
     * @return the formatted time as String
     */
    public static String formatMediaTime(long milliSeconds) {
        long secth = 0, secs = 0, min = 0, hrs = 0;

        try {
            secth = milliSeconds % 1000;    // millisecs.
            milliSeconds /= 1000;

            secs = milliSeconds % 60;   // secs.
            milliSeconds /= 60;

            min = milliSeconds % 60;   // min.
            milliSeconds /= 60;

            hrs = milliSeconds % 60;   // hrs.
            milliSeconds /= 60;
        } catch (Exception e) {
            // catch exceptions like division by zero...
        }

        String time = (hrs > 0 ? nf.format(hrs) + ":" : "") +
                nf.format(min) + ":" + nf.format(secs);
        return time;
    }

    /**
     * Utility method to get a player that best supports the specified {@code mediaURL}.
     *
     * <p>
     * A suitable player is determined based on the plugin
     * available on the browser as well as its suitability to playback
     * the specified media.
     *
     * <p><b>Note:</b> {@code mediaURL} should end in a standard media format extension e.g.
     * {@code .mp3, .wma, .mov, ...}
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws com.bramosystems.gwt.player.client.LoadException if an error occurs while loading the media.
     * @throws com.bramosystems.gwt.player.client.PluginVersionException if the required
     * plugin version is not installed on the client.
     * @throws com.bramosystems.gwt.player.client.PluginNotFoundException if the required plugin
     * is not installed on the client.
     */
    public static AbstractMediaPlayer getPlayer(String mediaURL,
            boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer player = null;
        
        switch(impl.suggestPlayer(mediaURL.substring(mediaURL.lastIndexOf(".") + 1))) {
//        switch(impl.suggestPlayer(mediaURL)) {
            case FlashMP3Player:
                player = new FlashMP3Player(mediaURL, autoplay, height, width);
                break;
            case QuickTimePlayer:
                player = new QuickTimePlayer(mediaURL, autoplay, height, width);
                break;
            case WinMediaPlayer:
                player = new WinMediaPlayer(mediaURL, autoplay, height, width);
                break;
        }
        return player;
    }

    /**
     * Detects the version of the Flash Player plugin available on the clients
     * browser.
     *
     * @return <code>PluginVersion</code> object wrapping the version numbers of the
     * Flash Player on the browser.
     *
     * @throws PluginNotFoundException if a Flash Player plugin could not be found
     * (especially if none is installed or the plugin is disabled).
     */
    public static PluginVersion getFlashPlayerVersion() throws PluginNotFoundException {
        PluginVersion v = new PluginVersion();
        impl.getFlashPluginVersion(v);
        if (v.equals(new PluginVersion())) {
            throw new PluginNotFoundException();
        }

        return v;
    }

    /**
     * Detects the version of the QuickTime plugin available on the clients browser.
     *
     * @return <code>PluginVersion</code> object wrapping the version numbers of the
     * QuickTime plugin on the browser.
     *
     * @throws PluginNotFoundException if a QuickTime plugin could not be found.
     * (especially if none is installed or the plugin is disabled).
     */
    public static PluginVersion getQuickTimePluginVersion() throws PluginNotFoundException {
        PluginVersion v = new PluginVersion();
        impl.getQuickTimePluginVersion(v);
        if (v.equals(new PluginVersion())) {
            throw new PluginNotFoundException();
        }

        return v;
    }

    /**
     * Detects the version of the Windows Media Player plugin available on the clients browser.
     *
     * @return <code>PluginVersion</code> object wrapping the version numbers of the
     * plugin on the browser.
     *
     * @throws PluginNotFoundException if a plugin could not be found.
     * (especially if none is installed or the plugin is disabled).
     */
    public static PluginVersion getWindowsMediaPlayerPluginVersion() throws PluginNotFoundException {
        PluginVersion v = new PluginVersion();
        impl.getWindowsMediaPlayerVersion(v);
        if (v.equals(new PluginVersion())) {
            throw new PluginNotFoundException();
        }

        return v;
    }
}
