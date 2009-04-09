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
package com.bramosystems.gwt.player.core.client.impl;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Native IE implementation of the SWFObject class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.SWFObject
 */
public class SWFWidgetImplIE extends SWFWidgetImpl {

    @Override
    public void injectScript(String playerId, String swfURL, int width,
            int height, HashMap<String, String> params) {
        Iterator<String> keys = params.keySet().iterator();
        StringBuilder script = new StringBuilder("<object id='" + playerId + "' " +
                "classid='CLSID:D27CDB6E-AE6D-11cf-96B8-444553540000' ");
        script.append("width='" + width + "px' height='" + height + "px'> ");
        script.append("<param value='" + swfURL + "' name='movie' />");
        while(keys.hasNext()) {
            String name = keys.next();
            script.append("<param value='" + params.get(name) + "' name='" + name + "' />");
        }
        script.append("</object>");

        injectScriptImpl(playerId + "_div", script.toString());
    }
}