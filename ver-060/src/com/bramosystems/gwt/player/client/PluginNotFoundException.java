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

package com.bramosystems.gwt.player.client;

/**
 * Thrown to indicate that a required plugin cannot be found on the client's browser.
 *
 * <p>
 * On catching this exception, a message might be displayed to the user describing
 * how to download the plugin.
 *
 * @author Sikiru Braheem
 * @see com.bramosystems.gwt.player.client.PluginVersionException
 */
public class PluginNotFoundException extends Exception {

    /**
     * Constructor method
     */
    public PluginNotFoundException() {
        super("Required Plugin is not available");
    }

}
