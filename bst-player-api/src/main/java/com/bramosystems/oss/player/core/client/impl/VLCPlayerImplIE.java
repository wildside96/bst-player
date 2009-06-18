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

import com.bramosystems.oss.player.core.client.ui.VLCPlayer;

/**
 * IE specific native implementation of the VLCPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see VLCPlayer
 */
public class VLCPlayerImplIE extends VLCPlayerImpl {

    @Override
    protected String getPlayerScript(String mediaURL, String playerId, boolean autoplay,
            String height, String width) {
        return "<object id='" + playerId + "' " +
                " classid='clsid:E23FE9C6-778E-49D4-B537-38FCDE4887D8' " +
                "classid='clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921' " +
                "width='" + width + "px' height='" + height + "px' events='True'>" +
                "<param name='AutoPlay' value='" + autoplay + "' />" +
                "<param name='ShowDisplay' value='True' />" +
                "<param name='Visible' value='True' />" +
                "<param name='AutoLoop' value='False' />" +
                "<param name='MRL' value='" + mediaURL + "' />" +
                "<param name='Src' value='" + mediaURL + "' />" +
                "<param name='Target' value='" + mediaURL + "' />" +
                "<param name='ToolBar' value='true' />" +
                "</object>";
    }
}
