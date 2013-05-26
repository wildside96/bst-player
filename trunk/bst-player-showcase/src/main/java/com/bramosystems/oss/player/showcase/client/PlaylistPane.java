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

import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.playlist.client.ParseException;
import com.bramosystems.oss.player.playlist.client.PlaylistFactory;
import com.bramosystems.oss.player.playlist.client.spf.SPFPlaylist;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 *
 * @author Sikiru
 */
public class PlaylistPane extends Composite implements ValueChangeHandler<String> {

    public static PlaylistPane singleton = new PlaylistPane();
    private ArrayList<MRL> entries;

    private PlaylistPane() {
        initWidget(bb.createAndBindUi(this));
    }

    private void loadList(String provider) {
        String spf = GWT.getHostPageBaseURL() + "media/jspf-core.json";
        if (provider.equals("bst.vimeo")) {
            spf = GWT.getHostPageBaseURL() + "media/jspf-vimeo.json";
        } else if (provider.equals("bst.youtube")) {
            spf = GWT.getHostPageBaseURL() + "media/jspf-youtube.json";
        }

        try {
            RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, spf);
            rb.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        SPFPlaylist spf = PlaylistFactory.parseJspfPlaylist(response.getText());
                        entries = spf.toPlaylist();
                        refreshView();
                    } catch (ParseException ex) {
                        GWT.log("Parse Exception", ex);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                }
            });
        } catch (RequestException ex) {
            GWT.log("Request Exception", ex);
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        loadList(event.getValue());
    }

    public void addChangeHandler(PlaylistChangeHandler handler) {
        addHandler(handler, PlaylistChangeEvent.TYPE);
    }

    public final void addEntry(String... urls) {
        entries.add(new MRL(urls));
        if (isAttached()) {
            refreshView();
        }
    }

    public MRL removeEntry(int index) {
        MRL m = entries.remove(index);
        if (isAttached()) {
            refreshView();
        }
        return m;
    }

    public ArrayList<MRL> getEntries() {
        return entries;
    }

    private void refreshView() {
        list.clear();
        ArrayList<MRL> es = entries;
        for (MRL entry : es) {
            list.addItem(entry.toString());
        }
    }

    @UiHandler("addButton")
    public void onAddClicked(ClickEvent event) {
        String res = Window.prompt("Type the URL of the media to add to the playlist. Separate multiple URLs of the same media with a comma", "");
        if (res != null && !res.isEmpty()) {
            String[] r = res.split(",");
            for (String x : r) {
                x.trim();
            }
            addEntry(r);
            fireEvent(new PlaylistChangeEvent(new MRL(r), list.getItemCount(), true));
        }
    }

    @UiHandler("delButton")
    public void onDelClicked(ClickEvent event) {
        if (Window.confirm("Do you really want to remove the selected media from the playlist?")) {
            int index = list.getSelectedIndex();
            MRL m = removeEntry(index);
            delButton.setEnabled(false);
            fireEvent(new PlaylistChangeEvent(m, index, false));
        }
    }

    @UiHandler("list")
    public void onListChange(ChangeEvent event) {
        delButton.setEnabled(true);
    }
    @UiField ListBox list;
    @UiField Button addButton, delButton;

    @UiTemplate("xml/PlaylistPane.ui.xml")
    interface Binder extends UiBinder<Widget, PlaylistPane> {
    }
    Binder bb = GWT.create(Binder.class);
}
