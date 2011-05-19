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

package com.bramosystems.oss.player.core.client.spi;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.google.gwt.dom.client.Element;
import java.util.HashMap;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public interface PlayerProviderFactory {
    
    public Element getWidgetElement(String playerName, String playerId, String mediaURL, boolean autoplay, 
            HashMap<String, String> params);
    
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException;
    
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL,
            boolean autoplay, String height, String width) throws LoadException, PluginNotFoundException, PluginVersionException;

    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL,
            boolean autoplay) throws LoadException, PluginNotFoundException, PluginVersionException;
}
