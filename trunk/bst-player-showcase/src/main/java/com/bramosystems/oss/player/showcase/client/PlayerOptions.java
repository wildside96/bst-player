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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
public class PlayerOptions extends Composite {

    private static PlayerOptionsUiBinder uiBinder = GWT.create(PlayerOptionsUiBinder.class);

    @UiTemplate("xml/PlayerOptions.ui.xml")
    interface PlayerOptionsUiBinder extends UiBinder<Widget, PlayerOptions> {
    }
    private OptionsChangeHandler callback;

    public PlayerOptions() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setOptionsChangeHandler(OptionsChangeHandler callback) {
        this.callback = callback;
    }

    @UiHandler("controls")
    public void onControlsChange(ValueChangeEvent<Boolean> event) {
        if (callback != null) {
            callback.onShowControls(event.getValue());
        }
    }

    @UiHandler("logger")
    public void onLoggerChange(ValueChangeEvent<Boolean> event) {
        if (callback != null) {
            callback.onShowLogger(event.getValue());
        }
    }
    
    @UiHandler("resizeToVideo")
    public void onResizeChange(ValueChangeEvent<Boolean> event) {
        if (callback != null) {
            callback.onResizeToVideoSize(event.getValue());
        }
    }
    
    @UiField
    CheckBox controls, resizeToVideo, logger;

    public static interface OptionsChangeHandler {

        public void onShowControls(boolean show);

        public void onShowLogger(boolean show);

        public void onResizeToVideoSize(boolean resize);
    }
}
