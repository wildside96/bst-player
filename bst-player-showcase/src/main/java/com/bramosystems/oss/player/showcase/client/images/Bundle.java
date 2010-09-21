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
package com.bramosystems.oss.player.showcase.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public interface Bundle extends ClientBundle {

    Bundle bundle = GWT.create(Bundle.class);

    @Source("nav-right.png")
    public ImageResource navRight();

    @Source("nav-right-disabled.png")
    public ImageResource navRightDisabled();

    @Source("nav-right-hover.png")
    public ImageResource navRightHover();

    @Source("nav-left.png")
    public ImageResource navLeft();

    @Source("nav-left-disabled.png")
    public ImageResource navLeftDisabled();

    @Source("nav-left-hover.png")
    public ImageResource navLeftHover();

    @Source("styles.css")
    public Styles css();

    public interface Styles extends CssResource {

        public String navLeft();
        public String navLeftDisabled();

        public String navRight();
       public String navRightDisabled();
    }
}
