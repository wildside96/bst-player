/*
 *  Copyright 2010 Sikiru Braheem <sbraheem at bramosystems . com>.
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
package com.bramosystems.oss.player.resources.sources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ExternalTextResource;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public interface ResourceBundle extends ClientBundle {
    ResourceBundle bundle = GWT.create(ResourceBundle.class);

    @Source("index.html")
    public ExternalTextResource home();

    @Source("wmp-basic.html")
    public ExternalTextResource wmpBasic();
    @Source("wmp-logger.html")
    public ExternalTextResource wmpLogger();
    @Source("wmp-video.html")
    public ExternalTextResource wmpVideo();
    @Source("wmp-video-auto.html")
    public ExternalTextResource wmpVideoAuto();
    @Source("wmp-playlist.html")
    public ExternalTextResource wmpPlaylist();

    @Source("qt-basic.html")
    public ExternalTextResource qtBasic();
    @Source("qt-logger.html")
    public ExternalTextResource qtLogger();
    @Source("qt-video.html")
    public ExternalTextResource qtVideo();
    @Source("qt-video-auto.html")
    public ExternalTextResource qtVideoAuto();

    @Source("vlc-basic.html")
    public ExternalTextResource vlcBasic();
    @Source("vlc-logger.html")
    public ExternalTextResource vlcLogger();
    @Source("vlc-video.html")
    public ExternalTextResource vlcVideo();
    @Source("vlc-video-auto.html")
    public ExternalTextResource vlcVideoAuto();

    @Source("swf-basic.html")
    public ExternalTextResource swfBasic();
    @Source("swf-logger.html")
    public ExternalTextResource swfLogger();
    @Source("swf-video.html")
    public ExternalTextResource swfVideo();
    @Source("swf-video-auto.html")
    public ExternalTextResource swfVideoAuto();
    @Source("swf-playlist.html")
    public ExternalTextResource swfPlaylist();

    @Source("ntv-basic.html")
    public ExternalTextResource nativeBasic();
    @Source("ntv-logger.html")
    public ExternalTextResource nativeLogger();
    @Source("ntv-video.html")
    public ExternalTextResource nativeVideo();
    @Source("ntv-video-auto.html")
    public ExternalTextResource nativeVideoAuto();

    @Source("ntv-basic.html")
    public ExternalTextResource divxBasic();
    @Source("ntv-logger.html")
    public ExternalTextResource divxLogger();
    @Source("ntv-video.html")
    public ExternalTextResource divxVideo();
    @Source("ntv-video-auto.html")
    public ExternalTextResource divxVideoAuto();

    @Source("misc-basic.html")
    public ExternalTextResource miscBasic();
    @Source("misc-flash.html")
    public ExternalTextResource miscFlash();

    @Source("ytube-basic.html")
    public ExternalTextResource ytubeBasic();
    @Source("ytube-chrome.html")
    public ExternalTextResource ytubeChrome();

    @Source("pll-swf.html")
    public ExternalTextResource pllSwf();
    @Source("pll-vlc.html")
    public ExternalTextResource pllVlc();
    @Source("pll-auto.html")
    public ExternalTextResource pllAuto();

    @Source("aud-swf.html")
    public ExternalTextResource audSwf();
    @Source("aud-vlc.html")
    public ExternalTextResource audVlc();
    @Source("aud-wmp.html")
    public ExternalTextResource audWmp();
    @Source("aud-qt.html")
    public ExternalTextResource audQt();
    @Source("aud-ntv.html")
    public ExternalTextResource audNtv();
    @Source("aud-auto.html")
    public ExternalTextResource audAuto();

    @Source("vid-swf.html")
    public ExternalTextResource vidSwf();
    @Source("vid-vlc.html")
    public ExternalTextResource vidVlc();
    @Source("vid-wmp.html")
    public ExternalTextResource vidWmp();
    @Source("vid-qt.html")
    public ExternalTextResource vidQt();
    @Source("vid-ntv.html")
    public ExternalTextResource vidNtv();
    @Source("vid-auto.html")
    public ExternalTextResource vidAuto();

    // Style bundle ...
    @Source("styles.css")
    public ShowcaseStyles styles();
}
