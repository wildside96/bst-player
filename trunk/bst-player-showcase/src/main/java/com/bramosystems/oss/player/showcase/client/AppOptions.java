/*
 *  Copyright 2010 Sikiru.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.bramosystems.oss.player.showcase.client;

/**
 *
 * @author Sikiru
 */
public enum AppOptions {
    home("Introduction"),
    plugins("Browser Plugins"),
//    mimes("Plugin/Registered Mime Types"),
    pool("Registered Mime Types"),
    core("Core Player Widgets"),
    capsule("Custom Audio Player"),
    flat("Custom Video Player"),
    matrix("Matrix Transforms"),
    ytube("YouTube Videos");

    private AppOptions(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    private String title;
}
