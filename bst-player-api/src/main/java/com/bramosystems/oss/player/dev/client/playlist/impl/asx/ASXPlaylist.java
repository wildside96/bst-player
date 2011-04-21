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
package com.bramosystems.oss.player.dev.client.playlist.impl.asx;

import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.playlist.Playlist;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public final class ASXPlaylist  {
    private double version;
    private boolean previewMode;
    private BannerBar bannerBar;
        
    private String _abstract, author, baseHref, copyright, moreInfoHref, title;
    private Repeat repeat;
    private List<ASXEntry> entries;
    private List<String> entryRefs;
    private HashMap<String, String> params;

    public ASXPlaylist() {
        previewMode = false;
        bannerBar = BannerBar.Auto;
        params = new HashMap<String, String>();
        entries = new ArrayList<ASXEntry>();
        entryRefs = new ArrayList<String>();
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

    public BannerBar getBannerBar() {
        return bannerBar;
    }

    public void setBannerBar(BannerBar bannerBar) {
        this.bannerBar = bannerBar;
    }

    public String getBaseHref() {
        return baseHref;
    }

    public void setBaseHref(String baseHref) {
        this.baseHref = baseHref;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<ASXEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ASXEntry> entries) {
        this.entries = entries;
    }

    public List<String> getEntryRefs() {
        return entryRefs;
    }

    public void setEntryRefs(List<String> entryRefs) {
        this.entryRefs = entryRefs;
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

    public boolean isPreviewMode() {
        return previewMode;
    }

    public void setPreviewMode(boolean previewMode) {
        this.previewMode = previewMode;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ASXPlaylist{" + "version=" + version + ", _abstract=" + _abstract + ", author=" + author + 
                ", copyright=" + copyright + ", moreInfoHref=" + moreInfoHref + ", title=" + title + 
                ", entries=" + entries + ", entryRefs=" + entryRefs + ", params=" + params + '}';
    }
    
    public static enum BannerBar {
        Auto, Fixed;
    }
    
    public Playlist toPlaylist() {
        Playlist p = new Playlist();
        p.setName(title);
        p.setAuthor(author);
        
        Iterator<ASXEntry> it = entries.iterator();
        while(it.hasNext()) {
            ASXEntry ae = it.next();
            MRL m = new MRL(ae.getTitle(), ae.getAuthor());
            Iterator<Ref> refs = ae.getRefs().iterator();
            while(refs.hasNext()) {
                Ref ref = refs.next();
                m.addURL(ref.getHref());
            }
            p.add(m);
        }
        return p;
    }
}
