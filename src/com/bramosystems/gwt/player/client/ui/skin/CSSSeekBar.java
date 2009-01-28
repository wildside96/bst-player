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
package com.bramosystems.gwt.player.client.ui.skin;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;

/**
 * CSS based implementation of the MediaSeekBar.  The playing and loading
 * progress indicators as well as the seek track can be modified via
 * CSS style names defined in stylesheets.
 *
 * @author Sikirulai Braheem
 */
public class CSSSeekBar extends MediaSeekBar {

    private Label playing,  loading;

    /**
     * Constructs <code>CSSSeekBar</code> of the specified height with a {@code gray} track, using
     * a {@code yellow} loading progress indicator and a {@code blue} playback progress indicator.
     *
     * @param height the height of the seek bar in pixels
     */
    public CSSSeekBar(int height) {
        super(height);
        playing = new Label();
        playing.setHeight(String.valueOf(height));
        playing.addMouseListener(this);

        loading = new Label();
        loading.setHeight(String.valueOf(height));
        loading.addMouseListener(this);

        DOM.setStyleAttribute(playing.getElement(), "cursor", "pointer");
        DOM.setStyleAttribute(playing.getElement(), "background", "#6600ff");

        DOM.setStyleAttribute(loading.getElement(), "cursor", "pointer");
        DOM.setStyleAttribute(loading.getElement(), "background", "#ffff99");

        DOM.setStyleAttribute(seekTrack.getElement(), "vertical-align", "middle");
        DOM.setStyleAttribute(seekTrack.getElement(), "background", "#dadada");
        initSeekBar(loading, playing);
    }

    /**
     * Assigns a CSS style class name to the playback progress indicator
     *
     * @param styleName CSS style class name
     */
    public void setPlayingStyleName(String styleName) {
        playing.setStyleName(styleName);
    }

    /**
     * Assigns a CSS style class name to the loading progress indicator
     *
     * @param styleName CSS style class name
     */
    public void setLoadingStyleName(String styleName) {
        loading.setStyleName(styleName);
    }

    /**
     * Assigns a CSS style class name to the seek track
     *
     * @param styleName CSS style class name
     */
    public void setTrackStyleName(String styleName) {
        seekTrack.setStyleName(styleName);
    }

    /**
     * Assigns the specified style value to the named style on the
     * playback progress indicator.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value value of the style.
     */
    public void setPlayingStyle(String name, String value) {
        DOM.setStyleAttribute(playing.getElement(), name, value);
    }

    /**
     * Assigns the specified style value to the named style on the
     * loading progress indicator.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value value of the style.
     */
    public void setLoadingStyle(String name, String value) {
        DOM.setStyleAttribute(loading.getElement(), name, value);
    }

    /**
     * Assigns the specified style value to the named style on the
     * seek track.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value value of the style.
     */
    public void setTrackStyle(String name, String value) {
        DOM.setStyleAttribute(seekTrack.getElement(), name, value);
    }
}
