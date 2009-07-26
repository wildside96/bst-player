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
package com.bramosystems.oss.player.showcase.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class IPage extends Composite {

    private HTML body;
    private String url;

    public IPage(String url) {
        this.url = url;

        body = new HTML();
        initWidget(body);
    }

    @Override
    protected void onLoad() {
        getPage(url);
    }

    private void getPage(String url) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            builder.sendRequest(null, new RequestCallback() {

                public void onResponseReceived(Request req, Response res) {
                    body.setHTML(res.getText());
                }

                public void onError(Request req, Throwable t) {
                    body.setHTML(t.getMessage());
                }
            });
        } catch (RequestException ex) {
            body.setHTML(ex.getMessage());
        }
    }
}
