/*
 * Copyright 2010 Sikirulai Braheem <sbraheem at bramosystems.com>.
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
package com.bramosystems.oss.player.dev.client;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class MRL {

    private int _index = -1;
    private ArrayList<String> _urls;

    public MRL() {
        _urls = new ArrayList<String>();
    }

    public MRL(String... urls) {
        addURL(urls);
    }
    
    public void addURL(String... urls) {
        _urls.addAll(Arrays.asList(urls));        
    }

    public String getNextResource(boolean roll) {
        _index++;
        _index = (roll && (_index == _urls.size())) ? 0 : _index;
        return _urls.get(_index);
    }

    public String getCurrentResource() {
        return _urls.get(_index);
    }
}
