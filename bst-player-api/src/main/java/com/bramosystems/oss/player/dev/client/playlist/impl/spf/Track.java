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

package com.bramosystems.oss.player.dev.client.playlist.impl.spf;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public final class Track extends JavaScriptObject {

    protected Track() {
    }

    public native String getAlbum() /*-{
        return this.album;
    }-*/;

    public native void setAlbum(String album) /*-{
        this.album = album;
    }-*/;

    public native String getAnnotation() /*-{
        return this.annotation;
    }-*/;

    public native void setAnnotation(String annotation) /*-{
        this.annotation = annotation;
    }-*/;

    public native String getCreator() /*-{
        return this.creator;
    }-*/;

    public native void setCreator(String creator) /*-{
        this.creator = creator;
    }-*/;

    public native double getDuration() /*-{
        return this.duration;
    }-*/;

    public native void setDuration(double duration) /*-{
        this.duration = duration;
    }-*/;

    public native JsArrayString getIdentifier() /*-{
        return this.identifier;
    }-*/;

    public native void setIdentifier(JsArrayString identifier) /*-{
        this.identifier = identifier;
    }-*/;

    public native String getImage() /*-{
        return this.image;
    }-*/;

    public native void setImage(String image) /*-{
        this.image = image;
    }-*/;

    public native String getInfo() /*-{
        return this.info;
    }-*/;

    public native void setInfo(String info) /*-{
        this.info = info;
    }-*/;

    public native JsArrayString getLocation() /*-{
        return this.location;
    }-*/;

    public native void setLocation(JsArrayString location) /*-{
        this.location = location;
    }-*/;

    public native String getTitle() /*-{
        return this.title;
    }-*/;

    public native void setTitle(String title) /*-{
        this.title = title;
    }-*/;

    public native double getTrackNumber() /*-{
        return this.trackNum;
    }-*/;

    public native void setTrackNumber(double trackNum) /*-{
        this.trackNum = trackNum;
    }-*/;    
}