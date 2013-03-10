/*
 * Copyright 2013 sbraheem.
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
package com.bramosystems.oss.player.core.client.spi;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Provides the global configuration information for player providers.
 * 
 * <p>As of BST Player 2.0, it provides access to the providers' JavaScript object on
 * the DOM.  The object could be used to bind global functions and/or variables as required.
 *
 * @author Sikiru Braheem
 * @since 2.0
 */
public class ConfigurationContext {
    
    private JavaScriptObject globalJSStack;
    private String globalJSStackName;

    /**
     * Creates the configuration context
     * 
     * @param globalJSStack the global JavaScript object on the DOM
     * @param globalJSStackName the name of the JavaScript object
     */
    public ConfigurationContext(JavaScriptObject globalJSStack, String globalJSStackName) {
        this.globalJSStack = globalJSStack;
        this.globalJSStackName = globalJSStackName;
    }

    /**
     * Returns the global JavaScript object assigned to the player provider
     * 
     * @return the global JavaScript object
     */
    public JavaScriptObject getGlobalJSStack() {
        return globalJSStack;
    }

    /**
     * Returns the name of the global JavaScript object assigned to the player provider
     * 
     * @return the global JavaScript objects' name
     */
    public String getGlobalJSStackName() {
        return globalJSStackName;
    }
}
