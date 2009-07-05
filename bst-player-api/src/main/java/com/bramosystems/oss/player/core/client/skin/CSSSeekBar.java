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
package com.bramosystems.oss.player.core.client.skin;

import com.google.gwt.user.client.ui.Label;

/**
 * CSS based implementation of the MediaSeekBar.  The playing and loading
 * progress indicators as well as the seek track can be modified via
 * CSS style names defined in stylesheets.
 *
 * <h4>CSS Styles</h4>
 * <code><pre>
 * .player-CSSSeekBar { the seekbar itself }
 * .player-CSSSeekBar .loading { the loading progress indicator }
 * .player-CSSSeekBar .playing { the playing progress indicator }
 * </pre></code>
 *
 * @author Sikirulai Braheem
 */
public class CSSSeekBar extends MediaSeekBar {

    private Label playing,  loading;

    /**
     * Constructs <code>CSSSeekBar</code> of the specified height.
     *
     * @param height the height of the seek bar in pixels
     */
    public CSSSeekBar(int height) {
        super(height);
        playing = new Label();
        playing.setHeight(height + "px");
        playing.addMouseUpHandler(this);

        loading = new Label();
        loading.setHeight(height + "px");
        loading.addMouseUpHandler(this);

        playing.setStyleName("playing");
        loading.setStyleName("loading");

        initSeekBar(loading, playing);
        setStylePrimaryName("player-CSSSeekBar");
    }

    /**
     * Assigns a CSS style class name to the playback progress indicator
     *
     * @param styleName CSS style class name
     * @deprecated All style properties should be placed in a stylesheet using the appropriate style names.
     * Will be removed in a future version
     */
    public void setPlayingStyleName(String styleName) {
    }

    /**
     * Assigns a CSS style class name to the loading progress indicator
     *
     * @param styleName CSS style class name
     * @deprecated All style properties should be placed in a stylesheet using the appropriate style names.
     * Will be removed in a future version
     */
    public void setLoadingStyleName(String styleName) {
    }

    /**
     * Assigns a CSS style class name to the seek track
     *
     * @param styleName CSS style class name
     * @deprecated All style properties should be placed in a stylesheet using the appropriate style names.
     * Will be removed in a future version
     */
    public void setTrackStyleName(String styleName) {
    }

    /**
     * Assigns the specified style value to the named style on the
     * playback progress indicator.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value value of the style.
     * @deprecated All style properties should be placed in a stylesheet using the appropriate style names.
     * Will be removed in a future version
     */
    public void setPlayingStyle(String name, String value) {
    }

    /**
     * Assigns the specified style value to the named style on the
     * loading progress indicator.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value value of the style.
     * @deprecated All style properties should be placed in a stylesheet using the appropriate style names.
     * Will be removed in a future version
     */
    public void setLoadingStyle(String name, String value) {
    }

    /**
     * Assigns the specified style value to the named style on the
     * seek track.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value value of the style.
     * @deprecated All style properties should be placed in a stylesheet using the appropriate style names.
     * Will be removed in a future version
     */
    public void setTrackStyle(String name, String value) {
    }
}
