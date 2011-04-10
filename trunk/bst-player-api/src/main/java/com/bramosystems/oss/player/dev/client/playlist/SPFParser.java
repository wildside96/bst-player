/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
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
package com.bramosystems.oss.player.dev.client.playlist;

import com.bramosystems.oss.player.dev.client.playlist.impl.XSPFHandler;
import com.bramosystems.oss.player.dev.client.playlist.impl.spf.SPFPlaylist;
import com.google.gwt.core.client.JsonUtils;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class SPFParser {

    public static native SPFPlaylist parseJspfPlaylist(String jspf) /*-{
    return eval('(' + jspf + ')').playlist;
    }-*/;

    public static SPFPlaylist parseJspfPlaylist2(String jspf) {
        return JsonUtils.unsafeEval(jspf);
    }

    public static SPFPlaylist parseXspfPlaylist(String xspf) {
        XSPFHandler xs = new XSPFHandler();
        return xs.getPlaylist(xspf);
    }
}
