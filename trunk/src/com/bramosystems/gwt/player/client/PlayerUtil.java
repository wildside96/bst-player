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
import com.bramosystems.gwt.player.client.ui.FlashVideoPlayer;
import com.bramosystems.gwt.player.client.ui.QuickTimePlayer;
import com.bramosystems.gwt.player.client.ui.WinMediaPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

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
     * format <code>hh:mm:ss</code>.
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
     * <p>A suitable player is determined based on the plugin available on the
     * browser as well as its suitability to playback the specified media.
     *
     * <p><b>NOTE:</b> If the media is served with a special streaming protocol such as
     * MMS and RTSP, {@code mediaURL} should be specified in its absolute form. Otherwise
     * {@code mediaURL} should end in a standard media format extension e.g.
     * {@code .mp3, .wma, .mov, .flv ...}
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @return a suitable player implementation
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

        String protocol = null;
        if (mediaURL.contains("://")) {
            protocol = mediaURL.substring(0, mediaURL.indexOf("://"));
        }

        String ext = mediaURL.substring(mediaURL.lastIndexOf(".") + 1);

        switch (impl.suggestPlayer(protocol, ext)) {
            case FlashVideoPlayer:
                player = new FlashVideoPlayer(mediaURL, autoplay, height, width);
                break;
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

    /**
     * Returns a widget that may be used to notify the user when a required plugin
     * is not available.  The widget provides a link to the plugin download page.
     *
     * <h4>CSS Style Rules</h4>
     * <ul>
     * <li>.player-MissingPlugin { the missing plugin widget }</li>
     * <li>.player-MissingPlugin-title { the title section }</li>
     * <li>.player-MissingPlugin-message { the message section }</li>
     * </ul>
     *
     * @param plugin the missing plugin
     * @param title the title of the message
     * @param message descriptive message to notify user about the missing plugin
     * @param asHTML {@code true} if {@code message} should be interpreted as HTML,
     *          {@code false} otherwise.
     *
     * @return missing plugin widget.
     * @since 0.6
     */
    public static Widget getMissingPluginNotice(final Plugin plugin, String title, String message,
            boolean asHTML) {
        DockPanel dp = new DockPanel() {

            @Override
            public void onBrowserEvent(Event event) {
                switch (event.getTypeInt()) {
                    case Event.ONCLICK:
                        if (plugin.getDownloadURL().length() > 0) {
                            Window.open(plugin.getDownloadURL(), "dwnload", "");
                        }
                }
            }
        };
        dp.setHorizontalAlignment(DockPanel.ALIGN_LEFT);
        dp.sinkEvents(Event.ONCLICK);
        dp.setWidth("200px");

        Label titleLb = null, msgLb = null;
        if (asHTML) {
            titleLb = new HTML(title);
            msgLb = new HTML(message);
        } else {
            titleLb = new Label(title);
            msgLb = new Label(message);
        }

        dp.add(titleLb, DockPanel.NORTH);
        dp.add(msgLb, DockPanel.CENTER);

        titleLb.setStylePrimaryName("player-MissingPlugin-title");
        msgLb.setStylePrimaryName("player-MissingPlugin-message");
        dp.setStylePrimaryName("player-MissingPlugin");

        DOM.setStyleAttribute(dp.getElement(), "cursor", "pointer");
        return dp;
    }

    /**
     * Convinience method to get a widget that may be used to notify the user when
     * a required plugin is not available.
     *
     * <p>This is same as calling {@code getMissingPluginNotice(plugin, "Missing Plugin",
     *      "<<message>>", false) }
     * <br/>
     * {@literal <<message>>} => [Plugin Name] [version] or later is required to play this media.
     * Click here to get [Plugin Name]
     *
     * @param plugin the required plugin
     * @param version the minimum version of the required plugin
     *
     * @return missing plugin widget.
     * @see #getMissingPluginNotice(Plugin, String, String, boolean)
     * @since 0.6
     */
    public static Widget getMissingPluginNotice(Plugin plugin, String version) {
        String title = "Missing Plugin", message = "";
        switch (plugin) {
            case WinMediaPlayer:
                message = "Windows Media Player " + version + " or later is required to play " +
                        "this media. Click here to get Windows Media Player.";
                break;
            case FlashMP3Player:
            case FlashVideoPlayer:
                message = "Adobe Flash Player " + version + " or later is required to play" +
                        "this media. Click here to get Flash";
                break;
            case QuickTimePlayer:
                message = "QuickTime Player " + version + " plugin or later is required to play " +
                        "this media. Click here to get QuickTime";
                break;
        }
        return getMissingPluginNotice(plugin, title, message, false);
    }

    /**
     * Convinience method to get a widget that may be used to notify the user when
     * a required plugin is not available.
     *
     * <p>This is same as calling {@code getMissingPluginNotice(plugin, "Missing Plugin",
     *      "<<message>>", false) }
     * <br/>
     * {@literal <<message>>} => [Plugin Name] is required to play this media.
     * Click here to get [Plugin Name]
     *
     * @param plugin the required plugin
     *
     * @return missing plugin widget.
     * @see #getMissingPluginNotice(Plugin, String, String, boolean)
     * @since 0.6
     */
    public static Widget getMissingPluginNotice(Plugin plugin) {
        String title = "Missing Plugin", message = "";
        switch (plugin) {
            case WinMediaPlayer:
                message = "Windows Media Player is required to play " +
                        "this media. Click here to get Windows Media Player.";
                break;
            case FlashMP3Player:
            case FlashVideoPlayer:
                message = "Adobe Flash Player is required to play" +
                        "this media. Click here to get Flash";
                break;
            case QuickTimePlayer:
                message = "QuickTime Player is required to play " +
                        "this media. Click here to get QuickTime";
                break;
            default:
                message = "A compatible plugin could not be found";
        }
        return getMissingPluginNotice(plugin, title, message, false);
    }
}