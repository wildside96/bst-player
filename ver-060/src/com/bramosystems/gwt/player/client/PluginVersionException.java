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
 * Thrown to indicate that a required plugin version cannot be found on the client's browser.
 *
 * <p>
 * On catching this exception, a message might be displayed to the user describing
 * how to download the appropriate plugin version.
 *
 * @author Sikiru Braheem
 * @see com.bramosystems.gwt.player.client.PluginNotFoundException
 */
public class PluginVersionException extends Exception {
    private String requiredVersion, versionFound;

    /**
     * Constructs a <code>PluginVersionException</code> specifing the required version
     * and the version found on the clients browser.
     *
     * <p>
     * The <code>requiredVersion</code> and <code>versionFound</code> parameters are
     * of the format <code>major.minor.revision</code>
     *
     * @param requiredVersion the required plugin version
     * @param versionFound the plugin version found
     */
    public PluginVersionException(String requiredVersion, String versionFound) {
        super("Plugin version " + requiredVersion + " is required, " + versionFound + " found");
        this.requiredVersion = requiredVersion;
        this.versionFound = versionFound;
    }

    /**
     * Returns the required version of the plugin
     * @return required plugin version
     */
    public String getRequiredVersion() {
        return requiredVersion;
    }

    /**
     * Returns the plugin version found.
     * @return plugin version found.
     */
    public String getVersionFound() {
        return versionFound;
    }
}
