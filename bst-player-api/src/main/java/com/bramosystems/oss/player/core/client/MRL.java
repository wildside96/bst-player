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
package com.bramosystems.oss.player.core.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Media Resource Locator.  Holds multiple alternative URLs of the same media resource 
 * (probably in different formats).
 * 
 * @author Sikiru Braheem
 * @since 1.3
 */
public class MRL {

    private int _index = -1;
    private ArrayList<String> _urls;

    /**
     * Constructs MRL object
     */
    public MRL() {
        _urls = new ArrayList<String>();
    }

    /**
     * Constructs MRL object with the specified URLs
     * 
     * @param urls URLs of the media
     */
    public MRL(String... urls) {
        this();
        _urls.addAll(Arrays.asList(urls));        
    }
    
    /**
     * Constructs MRL object with the specified URLs
     * 
     * @param urls URLs of the media
     */
    public MRL(List<String> urls) {
        this();
        _urls.addAll(urls);
    }
    
    /**
     * Adds the specified URLs to this locator
     * 
     * @param urls URLs of the media
     */
    public void addURL(String... urls) {
        _urls.addAll(Arrays.asList(urls));        
    }

    /**
     * Returns the next alternative URL of the media
     * 
     * @param roll <code>true</code> if an end-of-list should roll over to the first URL
     * 
     * @return the next alternative URL of the media
     * @throws IndexOutOfBoundsException if end-of-list is reached and <code>roll</code> is false
     */
    public String getNextResource(boolean roll) {
        _index++;
        _index = (roll && (_index == _urls.size())) ? 0 : _index;
        return _urls.get(_index);
    }

    /**
     * Return the current URL.  The current URL is that returned by the last {@link #getNextResource(boolean)} 
     * method call
     * 
     * @return the current URL
     */
    public String getCurrentResource() {
        return _urls.get(_index);
    }
    
    /**
     * Returns the number of URLs in this locator
     * 
     * @return the number of URLs in this locator
     */
    public int getResourceCount() {
        return _urls.size();
    }
    
    /**
     * Returns the resource URL at the specified <code>index</code>
     * 
     * @param index the index of the required resource URL
     * 
     * @return the resource URL
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public String getResource(int index) {
        return _urls.get(index);
    }

    @Override
    public String toString() {
        return _urls.toString();
    }
    
    
}
