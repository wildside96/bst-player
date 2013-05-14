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

/**
 * Native implementation of the QuickTimePlayer class for Firefox on Mac. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 */
public class QTStateManagerMacMozilla extends QTStateManager {

    public QTStateManagerMacMozilla() {}
        
    @Override
    public void initOnLoad(final String playerId, final OnLoadHandler onLoadHandler) {
        QuickTimePlayerImpl impl = QuickTimePlayerImpl.getPlayer(playerId);
        onLoadHandler.onDebug("Plugin Version : [Not available on Mac]");
        onLoadHandler.initImpl(impl);
    }

 }
