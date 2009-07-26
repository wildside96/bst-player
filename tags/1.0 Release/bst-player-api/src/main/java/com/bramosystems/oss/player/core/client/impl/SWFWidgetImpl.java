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

import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Native implementation of the SWFWidget class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see SWFWidget
 */
public class SWFWidgetImpl {

    public String getScript(String playerId, String swfURL, String width,
            String height, HashMap<String, String> params) {
        Iterator<String> keys = params.keySet().iterator();
        StringBuilder script = new StringBuilder("<embed type='application/x-shockwave-flash' " +
                "src='" + swfURL + "' ");
        script.append("id='" + playerId + "' name='" + playerId + "' ");
        script.append("width='" + width + "' height='" + height + "' ");
        while(keys.hasNext()) {
            String name = keys.next();
            script.append(name + "='" + params.get(name) + "' ");
        }
        script.append("></embed>");
        return script.toString();
    }
}