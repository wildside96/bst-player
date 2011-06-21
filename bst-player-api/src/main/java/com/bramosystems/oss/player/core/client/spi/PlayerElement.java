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
package com.bramosystems.oss.player.core.client.spi;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ParamElement;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class PlayerElement {

    public static enum Type {

        EmbedElement, ObjectElement, ObjectElementIE, VideoElement
    }
    private Document _doc = Document.get();
    private Element e;
    private Type type;

    public PlayerElement(Type type, String id, String typeOrClassId) {
        this.type = type;
        switch (type) {
            case EmbedElement:
                e = _doc.createElement("embed");
                e.setAttribute("type", typeOrClassId);
                break;
            case ObjectElement:
                e = _doc.createObjectElement();
                e.setPropertyString("type", typeOrClassId);
                break;
            case ObjectElementIE:
                e = _doc.createObjectElement();
                e.setPropertyString("classid", typeOrClassId);
                break;
            case VideoElement:
                e = _doc.createElement("video");
        }
        e.setId(id);
    }

    public final void addParam(String name, String value) {
        switch (type) {
            case ObjectElement:
            case ObjectElementIE:
                ParamElement param = _doc.createParamElement();
                param.setName(name);
                param.setValue(value);
                e.appendChild(param);
                break;
            case EmbedElement:
            case VideoElement:
                e.setAttribute(name, value);
        }
    }

    public Element getElement() {
        return e;
    }
}
