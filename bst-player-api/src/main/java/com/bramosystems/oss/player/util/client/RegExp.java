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
package com.bramosystems.oss.player.util.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Utility class that wraps the <code>navigator.plugins</code> information of
 * the clients' browser
 *
 * @author Sikirulai Braheem
 */
public class RegExp extends JavaScriptObject {

    /**
     * Create a new BrowserPlugin object
     */
    protected RegExp() {
    }

    /**
     * Returns the description of the plug-in
     *
     * @return the description of the plug-in
     */
    public final native String isGlobal() /*-{
    return this.global;
    }-*/;
    public final native boolean isIgnoreCase() /*-{
    return this.ignoreCase;
    }-*/;
    public final native int getLastIndex() /*-{
    return this.listIndex;
    }-*/;
    public final native void setLastIndex(int _lastIndex) /*-{
    this.listIndex = _lastIndex;
    }-*/;
    public final native boolean isMultiline() /*-{
    return this.multiline;
    }-*/;
    public final native String getSource() /*-{
    return this.source;
    }-*/;

    public final native boolean test(String exp) /*-{
    return this.test(exp);
    }-*/;

    public static final native RegExp getRegExp(String pattern, String flags) /*-{
    return new RegExp(pattern, flags);
    }-*/;
}
