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
import com.google.gwt.core.client.JsArray;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public final class SPFPlaylist extends JavaScriptObject {

    protected SPFPlaylist() {
    }

    public native String getAnnotation() /*-{
        return this.annotation;
    }-*/;

    public native void setAnnotation(String annotation) /*-{
        this.annotation = annotation;
    }-*/;

    public native JsArray<Attribution> getAttributions() /*-{
        return this.attribution;
    }-*/;

    public native void setAttribution(JsArray<Attribution> attribution) /*-{
        this.attribution = attribution;
    }-*/;
    
    public native String getCreator() /*-{
        return this.creator;
    }-*/;

    public native void setCreator(String creator) /*-{
        this.creator = creator;
    }-*/;

    public native String getDate() /*-{
        return this.date;
    }-*/;

    public native void setDate(String date) /*-{
        this.date = date;
    }-*/;

    public native String getIdentifier() /*-{
        return this.identifier;
    }-*/;

    public native void setIdentifier(String identifier) /*-{
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

    public native String getLicense() /*-{
        return this.license;
    }-*/;

    public native void setLicense(String license) /*-{
        this.license = license;
    }-*/;

    public native String getLocation() /*-{
        return this.location;
    }-*/;

    public native void setLocation(String location) /*-{
        this.location = location;
    }-*/;

    public native String getTitle() /*-{
        return this.title;
    }-*/;

    public native void setTitle(String title) /*-{
        this.title = title;
    }-*/;

    public native JsArray<Track> getTracks() /*-{
        return this.track;
    }-*/;

    public native void setTracks(JsArray<Track> tracks) /*-{
        this.track = tracks;
    }-*/;
}
