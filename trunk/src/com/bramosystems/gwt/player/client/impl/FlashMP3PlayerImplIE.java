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
package com.bramosystems.gwt.player.client.impl;

/**
 * IE specific native implementation of the FlashMP3Player class. It is not recommended to
 * interact with this class directly.

 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.FlashMP3Player
 */
public class FlashMP3PlayerImplIE extends FlashMP3PlayerImpl {

    // make package private...
    FlashMP3PlayerImplIE() {
    }

    @Override
    protected String getPlayerScript(String playerId, String mediaURL, boolean autoplay, boolean visible) {
        String ht = "<object id='" + playerId + "' classid='CLSID:D27CDB6E-AE6D-11cf-96B8-444553540000' " +
                (visible ? "" : "width='1px' height='1px'") + " >" +
                "<param name='FlashVars' value='bridgeName=" + playerId + "&playerId=" + playerId +
                "&mediaSrc=" + mediaURL + "&autoplay=" + autoplay + "' />" +
                "<param value='" + SWF_URL + "' name='Movie' />" +
                "<param value='SameDomain' name='AllowScriptAccess' />" +
                "</object>";
        return ht;
    }
}
