package com.bramosystems.oss.player.apt;

/*
 * Copyright 2012 Sikirulai Braheem.
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
import com.bramosystems.oss.player.core.client.spi.PlayerModule;
import com.bramosystems.oss.player.core.client.spi.PlayerModules;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 *
 * @author sbraheem
 */
@SupportedAnnotationTypes({"com.bramosystems.oss.player.core.client.spi.PlayerModule",
    "com.bramosystems.oss.player.core.client.spi.PlayerModules"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class InjectionProcessor extends AbstractProcessor {

    private StringBuilder collated;

    public InjectionProcessor() {
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        collated = new StringBuilder("<module>\n");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Iterator<? extends Element> pms = roundEnv.getElementsAnnotatedWith(PlayerModule.class).iterator();
        while (pms.hasNext()) {
            Element e = pms.next();
            addEntry(e.toString(), e.getAnnotation(PlayerModule.class).value());
            
            System.out.println("[Annotated] " + e.toString());
        }

        pms = roundEnv.getElementsAnnotatedWith(PlayerModules.class).iterator();
        while (pms.hasNext()) {
            Element e = pms.next();
            PlayerModule[] pm = e.getAnnotation(PlayerModules.class).value();
            for (int i = 0; i < pm.length; i++) {
                addEntry(e.toString(), pm[i].value());
            }
        }

        if (roundEnv.processingOver()) {
            try {
                collated.append("</module>");
                FileObject f = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT,
                        "com.bramosystems.oss.player.core", "AutoInjected.gwt.xml");
                f.openWriter().append(collated).close();
            } catch (IOException ex) {
                Logger.getLogger(InjectionProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    private void addEntry(String pkgName, String moduleName) {
        collated.append("\t").append("<inherits name='").append(pkgName).append(".");
        collated.append(moduleName).append("' />").append("\n");
    }
}
