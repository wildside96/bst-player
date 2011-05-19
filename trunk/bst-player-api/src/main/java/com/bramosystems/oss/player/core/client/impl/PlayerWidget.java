/*
 *  Copyright 2010 Sikiru Braheem <sbraheem at bramosystems . com>.
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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.impl.plugin.PlayerManager;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class PlayerWidget extends Widget {

    private BeforeUnloadCallback callback;
    private HashMap<String, String> params;
    private String playerName, playerProvider, playerId, mediaURL, _height, _width;
    private boolean autoplay;

    private PlayerWidget() {
        setElement(DOM.createDiv());
        params = new HashMap<String, String>();
        _width = "100%";
        _height = "10px";
    }

    public PlayerWidget(String playerProvider, String playerName, String playerId, String mediaURL,
            boolean autoplay, BeforeUnloadCallback callback) {
        this();
        this.callback = callback;
        this.playerId = playerId;
        this.autoplay = autoplay;
        this.mediaURL = mediaURL;
        this.playerName = playerName;
        this.playerProvider = playerProvider;
    }

    public void addParam(String name, String value) {
        params.put(name, value);
    }

    public void removeParam(String name) {
        params.remove(name);
    }

    public String getParam(String name) {
        return params.get(name);
    }

    @Override
    protected void onLoad() {
        injectWidget(false);
    }

    @Override
    protected void onUnload() {
        if (callback != null) {
            callback.onBeforeUnload();
        }
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        if (getElement().hasChildNodes()) {
            getElement().getFirstChildElement().setAttribute("height", height);
            getElement().getFirstChildElement().getStyle().setProperty("height", height);
        } else {
            _height = height;
        }
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        if (getElement().hasChildNodes()) {
            getElement().getFirstChildElement().setAttribute("width", width);
            getElement().getFirstChildElement().getStyle().setProperty("width", width);
        } else {
            _width = width;
        }
    }

    public void replace(String playerProvider, String playerName, String playerId, String mediaURL, boolean autoplay) {
        this.playerId = playerId;
        this.autoplay = autoplay;
        this.mediaURL = mediaURL;
        this.playerProvider = playerProvider;
        this.playerName = playerName;
        injectWidget(true);
    }

    private void injectWidget(boolean updateDimension) {
        Element e = PlayerManager.getInstance().getProviderFactory(playerProvider).getWidgetElement(
                playerName, playerId, mediaURL, autoplay, params);
        if (updateDimension) {
            String curHeight = getElement().getFirstChildElement().getAttribute("height");
            String curWidth = getElement().getFirstChildElement().getAttribute("width");
            e.setAttribute("height", curHeight);
            e.getStyle().setProperty("height", curHeight);
            e.setAttribute("width", curWidth);
            e.getStyle().setProperty("width", curWidth);
        }
        getElement().setInnerHTML(e.getString());
    }
}
