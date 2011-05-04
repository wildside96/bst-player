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
package com.bramosystems.oss.player.core.client;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * Wraps basic information about player widget registered with the APi.
 *
 * @author Sikirulai Braheem
 * @since 1.3
 */
public class PlayerInfo implements Serializable {

    private String playerName;
    private PluginVersion requiredPluginVersion, detectedPluginVersion;
    private Set<String> registeredExtensions = new TreeSet<String>(), registeredProtocols = new TreeSet<String>();

    public PlayerInfo(String playerName, PluginVersion requiredPluginVersion) {
        this.playerName = playerName;
        this.requiredPluginVersion = requiredPluginVersion;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PluginVersion getRequiredPluginVersion() {
        return requiredPluginVersion;
    }

    public PluginVersion getDetectedPluginVersion() {
        return detectedPluginVersion;
    }

    public void setDetectedPluginVersion(PluginVersion detectedPluginVersion) {
        this.detectedPluginVersion = detectedPluginVersion;
    }

    public Set<String> getRegisteredExtensions() {
        return registeredExtensions;
    }

    protected void setRegisteredExtensions(Set<String> registeredExtensions) {
        this.registeredExtensions = registeredExtensions;
    }

    public Set<String> getRegisteredProtocols() {
        return registeredProtocols;
    }

    protected void setRegisteredProtocols(Set<String> registeredProtocols) {
        this.registeredProtocols = registeredProtocols;
    }

    @Override
    public String toString() {
        return "PlayerInfo{player=" + playerName + ", required plugin version=" + requiredPluginVersion + "} ";
    }
}
