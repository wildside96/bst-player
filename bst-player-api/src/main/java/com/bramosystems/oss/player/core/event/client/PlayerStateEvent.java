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

package com.bramosystems.oss.player.core.event.client;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class PlayerStateEvent extends GwtEvent<PlayerStateHandler> {
    public static final Type<PlayerStateHandler> TYPE = new Type<PlayerStateHandler>();
    private State state;
  
    protected PlayerStateEvent(State state) {
        this.state = state;
    }

    public static <S extends HasMediaStateHandlers> void fire(S source,
            State state) {
        source.fireEvent(new PlayerStateEvent(state));
    }

    @Override
    public Type<PlayerStateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PlayerStateHandler handler) {
        handler.onPlayerStateChanged(this);
    }
    
    public State getPlayerState() {
        return state;
    }
    
    public enum State {
        Ready, BufferingStarted, BufferingFinished
    }
}
