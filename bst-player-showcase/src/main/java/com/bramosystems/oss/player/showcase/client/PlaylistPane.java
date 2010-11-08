/*
 *  Copyright 2010 Sikiru.
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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeHandler;
import com.bramosystems.oss.player.showcase.client.images.Bundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import java.util.*;

/**
 *
 * @author Sikiru
 */
public class PlaylistPane extends Composite {

    public static String baseURL = "http://localhost:8080/local-video/";
    private ArrayList<MRL> entries;

    public PlaylistPane() {
        initWidget(bb.createAndBindUi(this));

        entries = new ArrayList<MRL>();

        addEntry(baseURL + "thunder.mp3");
        addEntry(baseURL + "traffic.flv");
//        addEntry(baseURL + "traffic.mp4");
        addEntry(baseURL + "applause.mp3");
        addEntry("http://bst-player.googlecode.com/svn/tags/showcase/media/islamic-jihad.wmv");
        addEntry(baseURL + "big-buck-bunny.mp4", baseURL + "big-buck-bunny.ogv");
    }

    public void addChangeHandler(PlaylistChangeHandler handler){
        addHandler(handler, PlaylistChangeEvent.TYPE);
    }

    public final void addEntry(String... urls) {
        entries.add(new MRL(urls));
        if (isAttached()) {
            refreshView();
        }
    }

    public void removeEntry(int index) {
        entries.remove(index);
        if (isAttached()) {
            refreshView();
        }
    }

    public ArrayList<MRL> getEntries() {
        return entries;
    }

    private void refreshView() {
        list.clear();
        for (MRL entry : entries) {
            StringBuilder sb = new StringBuilder();
            for (String e : entry) {
                sb.append(e).append("; ");
            }
            sb.deleteCharAt(sb.lastIndexOf("; "));

            Label l = new Label(sb.toString(), true);
            l.setStyleName(Bundle.bundle.css().playlistItem());
            list.add(l);
        }
    }

    @Override
    protected void onLoad() {
        for(MRL entry : entries) {
            fireEvent(new PlaylistChangeEvent(entry, true));
        }
        refreshView();
    }


    @UiField FlowPanel list;

    @UiTemplate("xml/PlaylistPane.ui.xml")
    interface Binder extends UiBinder<Widget, PlaylistPane>{}
    Binder bb = GWT.create(Binder.class);
}
