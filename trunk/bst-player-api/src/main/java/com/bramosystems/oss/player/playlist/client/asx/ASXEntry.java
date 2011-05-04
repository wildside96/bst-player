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
package com.bramosystems.oss.player.playlist.client.asx;

import com.bramosystems.oss.player.core.client.PlayTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class ASXEntry {
    private boolean clientSkip, skipIfRef;
    
    private String _abstract, author, baseHref, copyright, moreInfoHref, title;
    private PlayTime duration, startTime;
    private HashMap<String, String> params;
    private List<Ref> refs;

    public ASXEntry() {
        clientSkip = true;
        skipIfRef = false;
        params = new HashMap<String, String>();
        refs = new ArrayList<Ref>();
    }
    
    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBaseHref() {
        return baseHref;
    }

    public void setBaseHref(String baseHref) {
        this.baseHref = baseHref;
    }

    public boolean isClientSkip() {
        return clientSkip;
    }

    public void setClientSkip(boolean clientSkip) {
        this.clientSkip = clientSkip;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public PlayTime getDuration() {
        return duration;
    }

    public void setDuration(PlayTime duration) {
        this.duration = duration;
    }

    public String getMoreInfoHref() {
        return moreInfoHref;
    }

    public void setMoreInfoHref(String moreInfoHref) {
        this.moreInfoHref = moreInfoHref;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public List<Ref> getRefs() {
        return refs;
    }

    public void setRefs(List<Ref> refs) {
        this.refs = refs;
    }

    public boolean isSkipIfRef() {
        return skipIfRef;
    }

    public void setSkipIfRef(boolean skipIfRef) {
        this.skipIfRef = skipIfRef;
    }

    public PlayTime getStartTime() {
        return startTime;
    }

    public void setStartTime(PlayTime startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ASXEntry{" + "_abstract=" + _abstract + ", author=" + author + ", copyright=" + copyright + 
                ", moreInfoHref=" + moreInfoHref + ", title=" + title + ", duration=" + duration +
                ", startTime=" + startTime + ", params=" + params + ", refs=" + refs + '}';
    }
}
