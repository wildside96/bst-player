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

import com.bramosystems.oss.player.core.client.MediaInfo;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class MediaInfoEvent extends GwtEvent<MediaInfoHandler> {
    public static final Type<MediaInfoHandler> TYPE = new Type<MediaInfoHandler>();
    private MediaInfo info;

    protected MediaInfoEvent(MediaInfo info) {
        this.info = info;
    }

    public static <S extends HasMediaStateHandlers> void fire(S source,
            MediaInfo info) {
        source.fireEvent(new MediaInfoEvent(info));
    }

    @Override
    public Type<MediaInfoHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MediaInfoHandler handler) {
        handler.onMediaInfoAvailable(this);
    }

    public MediaInfo getMediaInfo() {
        return info;
    }
}
