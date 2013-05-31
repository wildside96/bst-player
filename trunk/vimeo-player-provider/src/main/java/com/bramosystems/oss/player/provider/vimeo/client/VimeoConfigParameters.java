/*
 * Copyright 2012 sbraheem.
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
package com.bramosystems.oss.player.provider.vimeo.client;

import com.bramosystems.oss.player.core.client.ConfigParameter;

/**
 * Configuration parameters for the Vimeo player widgets.
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 2.0
 */
public enum VimeoConfigParameters implements ConfigParameter {

    /**
     * Parameter to show the user's byline on the embedded video.
     *
     * <p>This parameter requires a boolean value type
     */
    ShowByline(Boolean.class),
    
    /**
     * Parameter to show the title of the embedded video. 
     * 
     * <p>This parameter requires a boolean value type
     */
    ShowTitle(Boolean.class),
    
    /**
     * Parameter to show the user's portrait on the embedded video
     * 
     * <p>This parameter requires a boolean value type
     */
    ShowPortrait(Boolean.class),
    
    /**
     * Parameter to allow the video player go into fullscreen.
     * 
     * <p>This parameter requires a boolean value type
     */
    EnableFullscreen(Boolean.class),
    
    /**
     * Parameter to specify the color of the video controls.  The default is
     * {@code 00adef}.
     * 
     * <p>This parameter requires a String value type
     */
    Color(String.class);
    private Class[] valueTypes;

    private VimeoConfigParameters(Class... valueTypes) {
        this.valueTypes = valueTypes;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public Class[] getValueType() {
        return valueTypes;
    }
}
