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
public class PlayStateEvent extends GwtEvent<PlayStateHandler> {
    public static final Type<PlayStateHandler> TYPE = new Type<PlayStateHandler>();
    private State state;
    private int itemIndex;

    protected PlayStateEvent(State state, int itemIndex) {
        this.state = state;
        this.itemIndex = itemIndex;
    }

    public static <S extends HasMediaStateHandlers> void fire(S source,
            State state, int itemIndex) {
        source.fireEvent(new PlayStateEvent(state, itemIndex));
    }

    @Override
    public Type<PlayStateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PlayStateHandler handler) {
        handler.onPlayStateChanged(this);
    }
    
    public State getPlayState() {
        return state;
    }
    
    public int getItemIndex() {
        return itemIndex;
    }

    public enum State {
        Started, Finished, Paused, Stopped
    }

}
