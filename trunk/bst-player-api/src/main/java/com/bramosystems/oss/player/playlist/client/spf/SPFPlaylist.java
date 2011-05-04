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
package com.bramosystems.oss.player.playlist.client.spf;

import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.playlist.Playlist;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.JsDate;
import java.util.Date;

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

    public final Date getDate(){
        JsDate jd = JsDate.create(getDateImpl());
        return new Date((long)jd.getTime());
    }
    
    public final void setDate(Date date) {
        JsDate jd = JsDate.create(date.getTime());
        setDate(jd.toLocaleString());
    }
    
    private native String getDateImpl() /*-{
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

    public final Playlist toPlaylist() {
        Playlist p = new Playlist();
        p.setName(getTitle());
        p.setAuthor(getCreator());
        
        JsArray<Track> ts = getTracks();
        for(int i = 0; i < ts.length(); i++) {
           Track t = ts.get(i);
           
           MRL m = new MRL(t.getTitle(), t.getCreator());
           JsArrayString js = t.getLocation();
           for(int j = 0; j < js.length(); j++) {
               m.addURL(js.get(i));
           }
           p.add(m);
        }
        return p;
    }
}
