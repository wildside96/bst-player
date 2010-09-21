/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a footer of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Showcase extends Composite implements EntryPoint {

    public Showcase() {
        initWidget(uiBinder.createAndBindUi(this));
        playerOptions.setOptionsChangeHandler(player);

        player.setPlaylist(playlist);
    }

    @Override
    public void onModuleLoad() {
        ResourceBundle.bundle.styles().ensureInjected();

        RootPanel.get("loading").setVisible(false);
        RootLayoutPanel.get().add(this);

        History.fireCurrentHistoryState();
    }

    @UiField PlayerPane player;
    @UiField PlayerOptions playerOptions;
    @UiField PlaylistPane playlist;

    private static ShowcaseUiBinder uiBinder = GWT.create(ShowcaseUiBinder.class);

    @UiTemplate("xml/Showcase.ui.xml")
    interface ShowcaseUiBinder extends UiBinder<DockLayoutPanel, Showcase> {
    }
}
