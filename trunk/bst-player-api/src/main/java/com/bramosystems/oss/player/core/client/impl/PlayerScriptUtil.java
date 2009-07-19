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

import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.google.gwt.core.client.GWT;

/**
 * Native implementation of the PlayerUtil class. It is not recommended to
 * interact with this class directly.
 *
 * @see PlayerUtil
 * @author Sikirulai Braheem
 *
 */
public class PlayerScriptUtil {

    private static PlayerScriptUtilImpl impl = GWT.create(PlayerScriptUtilImpl.class);

    public static String getVLCPlayerScript(String playerId, int height, int width) {
        return impl.getVLCPlayerScript(playerId, height, width);
    }

    public static class PlayerScriptUtilImpl {

        public String getVLCPlayerScript(String playerId, int height, int width) {
            return "<embed id='" + playerId + "' name='" + playerId + "' loop='false' " +
                    "target='' autoplay='false' type='application/x-vlc-plugin' " +
                    "version='VideoLAN.VLCPlugin.2' width='" + width + "px' " +
                    "height='" + height + "px' toolbar='true'></embed>";
        }
    }
}
