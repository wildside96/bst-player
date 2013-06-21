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
package com.bramosystems.oss.player.core.rebind;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ConfigurationProperty;
import com.google.gwt.core.ext.linker.PropertyProviderGenerator;
import com.google.gwt.user.rebind.StringSourceWriter;
import java.util.Iterator;
import java.util.SortedSet;

/**
 *
 * @author sbraheem
 */
public class PlatformPropertyGenerator implements PropertyProviderGenerator {

    @Override
    public String generate(TreeLogger logger, SortedSet<String> possibleValues, String fallback,
            SortedSet<ConfigurationProperty> configProperties) throws UnableToCompleteException {
        StringSourceWriter sw = new StringSourceWriter();
        sw.println("{");
        sw.indent();
        sw.println("var pltf = navigator.platform.toLowerCase();");
        Iterator<String> it = possibleValues.iterator();
        if (it.hasNext()) {
            fillCode(sw, it.next());
            sw.print("else ");
            while (it.hasNext()) {
                fillCode(sw, it.next());
                sw.print("else ");
            }
            sw.println("{");
            sw.indent();
            sw.print("return 'other';");
            sw.outdent();
            sw.print("}");
        }
        sw.outdent();
        sw.println("}");
        return sw.toString();
    }

    private void fillCode(StringSourceWriter sw, String val) {
        sw.print("if(pltf.indexOf('");
        sw.print(val);
        sw.println("')!=-1){");
        sw.indent();
        sw.print("return '");
        sw.print(val);
        sw.println("';");
        sw.outdent();
        sw.print("} ");
    }
}
