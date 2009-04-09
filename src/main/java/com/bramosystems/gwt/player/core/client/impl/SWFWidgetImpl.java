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
 * Native implementation of the SWFObject class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see com.bramosystems.gwt.player.client.ui.SWFObject
 */
public class SWFWidgetImpl {

    public void injectScript(String playerId, String swfURL, int width,
            int height, HashMap<String, String> params) {
        Iterator<String> keys = params.keySet().iterator();
        StringBuilder script = new StringBuilder("<embed type='application/x-shockwave-flash' " +
                "src='" + swfURL + "' ");
        script.append("id='" + playerId + "' name='" + playerId + "' ");
        script.append("width='" + width + "px' height='" + height + "px' ");
        while(keys.hasNext()) {
            String name = keys.next();
            script.append(name + "='" + params.get(name) + "' ");
        }
        script.append("></embed>");

        injectScriptImpl(playerId + "_div", script.toString());
    }

    protected final native void injectScriptImpl(String divId, String script) /*-{
        var e = $doc.getElementById(divId);
        e.innerHTML = script;
    }-*/;

}