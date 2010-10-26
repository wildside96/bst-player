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

import com.bramosystems.oss.player.showcase.client.event.PlayerOptionsChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlayerOptionsChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikiru
 */
public class PlayerOptionsPane extends Composite {

    private static PlayerOptionsUiBinder uiBinder = GWT.create(PlayerOptionsUiBinder.class);

    @UiTemplate("xml/PlayerOptions.ui.xml")
    interface PlayerOptionsUiBinder extends UiBinder<Widget, PlayerOptionsPane> {
    }
    private PlayerOptions options;

    public PlayerOptionsPane() {
        initWidget(uiBinder.createAndBindUi(this));
        options = new PlayerOptions();
        controls.setValue(options.isShowControls(), false);
        logger.setValue(options.isShowLogger(), false);
        resizeToVideo.setValue(options.isResizeToVideo(), false);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        fireEvent(new PlayerOptionsChangeEvent(options));
    }

    public HandlerRegistration addChangeHandler(PlayerOptionsChangeHandler handler) {
        return addHandler(handler, PlayerOptionsChangeEvent.TYPE);
    }

    @UiHandler("controls")
    public void onControlsChange(ValueChangeEvent<Boolean> event) {
        options.setShowControls(event.getValue());
        fireEvent(new PlayerOptionsChangeEvent(options));
    }

    @UiHandler("logger")
    public void onLoggerChange(ValueChangeEvent<Boolean> event) {
        options.setShowLogger(event.getValue());
        fireEvent(new PlayerOptionsChangeEvent(options));
    }
    
    @UiHandler("resizeToVideo")
    public void onResizeChange(ValueChangeEvent<Boolean> event) {
        options.setResizeToVideo(event.getValue());
        fireEvent(new PlayerOptionsChangeEvent(options));
    }
    
    @UiField CheckBox controls, resizeToVideo, logger;
}
