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
public class LoadingProgressEvent extends GwtEvent<LoadingProgressHandler> {
    public static final Type<LoadingProgressHandler> TYPE = new Type<LoadingProgressHandler>();
    private double progress;
  
    protected LoadingProgressEvent(double progress) {
        this.progress = progress;
    }

    public static <S extends HasMediaStateHandlers> void fire(S source,
            double progress) {
        source.fireEvent(new LoadingProgressEvent(progress));
    }

    @Override
    public Type<LoadingProgressHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LoadingProgressHandler handler) {
        handler.onLoadingProgress(this);
    }
    
    public double getProgress() {
        return progress;
    }
}
