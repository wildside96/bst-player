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
package com.bramosystems.oss.player.showcase.client.panes;

import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikiru
 */
public class PlayerLogPane extends ScrollPanel implements DebugHandler {

    private FlowPanel panel;

    public PlayerLogPane() {
        panel = new FlowPanel();
        setWidget(panel);
    }

    private void log(String message) {
        Label l = new Label("+ " + message);
        l.setWordWrap(true);
        panel.add(l);
        scrollToBottom();
    }

    @Override
    public void onDebug(DebugEvent event) {
        log(event.getMessage());
    }
}
