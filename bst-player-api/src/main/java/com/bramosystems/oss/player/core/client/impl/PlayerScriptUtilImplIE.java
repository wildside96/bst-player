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

import com.bramosystems.oss.player.core.client.impl.PlayerScriptUtil.PlayerScriptUtilImpl;

/**
 * IE specific native implementation of the PlayerScriptUtil class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see PlayerScriptUtil
 */
public class PlayerScriptUtilImplIE extends PlayerScriptUtilImpl {

    @Override
    public String getVLCPlayerScript(String playerId, int height, int width) {
        return "<object id='" + playerId + "' events='True' width='" + width + "px' " +
                "height='" + height + "px' classid='clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921'>" +
                "<param name='AutoPlay' value='False' />" +
                "<param name='AutoLoop' value='False' />" +
                "<param name='Toolbar' value='True' />" +
                "<param name='Src' value='' />" +
                "</object>";
    }
}
