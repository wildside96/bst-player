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
package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.*;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the base implementation of player widgets with UiBinder support and custom missing
 * plugin notifications.
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @param <T> the player implementation type
 * @since 1.3
 */
public abstract class PlayerWrapperExt<T extends AbstractMediaPlayer> extends PlayerWrapper<T> {

    /**
     * The constructor
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to autoplay, {@code false} otherwise
     * @param height the height of the player (in CSS units)
     * @param width the width of the player (in CSS units)
     */
    protected PlayerWrapperExt(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @UiChild(limit = 1, tagname = "missingPluginNotice")
    public void setMissingPluginNotice(Widget w) {
        missingPluginNotice = w;
    }

    @UiChild(limit = 1, tagname = "missingPluginVersionNotice")
    public void setMissingPluginVersionNotice(Widget w) {
        missingPluginVersionNotice = w;
    }
}
