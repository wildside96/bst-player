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
package com.bramosystems.oss.player.core.rebind;

import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.Player;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Sikiru
 */
public class DetectionEngineGenerator extends Generator {

    private ArrayList<String> playerMap = new ArrayList<String>();
    private String className = null, packageName = null;
    private TreeLogger logger;

    public DetectionEngineGenerator() {
        // init plugin props ...
    }

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName)
            throws UnableToCompleteException {
        this.logger = logger;
        TypeOracle typeOracle = context.getTypeOracle();

        try {
            // get classType and save instance variables
            JClassType classType = typeOracle.getType(typeName);
            packageName = classType.getPackage().getName();
            className = classType.getSimpleSourceName() + "Impl";

            // Generate class source code
            generateClass(logger, context);
        } catch (Exception e) {
            logger.log(TreeLogger.ERROR, "Unable to build Media Types!", e);
        }

        // return the fully qualifed name of the class generated
        return packageName + "." + className;
    }

    private void collatePlayers(TypeOracle typeOracle) {
        logger.log(TreeLogger.Type.INFO, "Searching for Player widgets");
        JClassType a[] = typeOracle.getTypes();
        for (int i = 0; i < a.length; i++) {
            if (a[i].isAnnotationPresent(Player.class)) {
                playerMap.add(a[i].getQualifiedSourceName());
            }
        }
        logger.log(TreeLogger.Type.INFO, "Processing Player widgets : " + playerMap);
    }

    /**
     * @param logger Logger object
     * @param context Generator context
     */
    private void generateClass(TreeLogger logger, GeneratorContext context) throws NotFoundException {
        // get print writer that receives the source code
        PrintWriter printWriter = context.tryCreate(logger, packageName, className);

        // print writer if null, source code has ALREADY been generated,  return
        if (printWriter == null) {
            return;
        }

        collatePlayers(context.getTypeOracle());

        // init composer, set class properties, create source writer
        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);
        composer.setSuperclass("DetectionEngine");
        composer.addImport("com.google.gwt.core.client.GWT");
        composer.addImport("com.bramosystems.oss.player.dev.client.spi.PlayerWidgetFactory");
        composer.addImport("com.bramosystems.oss.player.core.client.Plugin");
        composer.addImport("java.util.*");

        SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

        sourceWriter.println("private HashSet<String> pnames = new HashSet<String>();");

        // collate widget factories & create static holders ...
        HashSet<Class> factories = new HashSet<Class>();
        for (String player : playerMap) {
            JClassType jt = context.getTypeOracle().getType(player);
            Player p = jt.getAnnotation(Player.class);
            factories.add(p.widgetFactory());
        }
        for (Class fact : factories) {
            sourceWriter.println("private static PlayerWidgetFactory pwf_" + fact.getSimpleName() + ";");
        }

        // generate constructor source code
        sourceWriter.println("public " + className + "() { ");
        sourceWriter.indent();
        for (String player : playerMap) {
            JClassType jt = context.getTypeOracle().getType(player);
            Player p = jt.getAnnotation(Player.class);
            sourceWriter.println("pnames.add(\"" + p.name() + "\");");

            JClassType ints[] = jt.getImplementedInterfaces();
            for (int j = 0; j < ints.length; j++) {
                if (ints[j].getQualifiedSourceName().equals(MatrixSupport.class.getName())) {
                    sourceWriter.println("addPlayerSupports(Plugin.MatrixSupport, \"" + p.name() + "\");");
                } else if (ints[j].getQualifiedSourceName().equals(PlaylistSupport.class.getName())) {
                    sourceWriter.println("addPlayerSupports(Plugin.PlaylistSupport, \"" + p.name() + "\");");
                }
            }
        }
        sourceWriter.outdent();
        sourceWriter.println("}");   // end constructor source generation

        sourceWriter.println();

        // implement get player names ...
        sourceWriter.println("@Override");
        sourceWriter.println("public Set<String> getRegisteredPlayerNames() {");
        sourceWriter.indent();
        sourceWriter.println("return pnames;");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // implement get widget factory with defered binding on demand ....
        sourceWriter.println("@Override");
        sourceWriter.println("public PlayerWidgetFactory getWidgetFactory(String playerName) {");
        sourceWriter.indent();
        sourceWriter.println("PlayerWidgetFactory wf = null;");

        boolean firstRun = true;
        for (String player : playerMap) {
            JClassType jt = context.getTypeOracle().getType(player);
            Player p = jt.getAnnotation(Player.class);
            if (firstRun) {
                sourceWriter.println("if(\"" + p.name() + "\".equals(playerName)) {");
            } else {
                sourceWriter.println("else if(\"" + p.name() + "\".equals(playerName)) {");
            }
            sourceWriter.indent();
            sourceWriter.println("if(pwf_" + p.widgetFactory().getSimpleName() + " == null) {");
            sourceWriter.indent();
            sourceWriter.println("pwf_" + p.widgetFactory().getSimpleName() + " = GWT.create(" + p.widgetFactory().getName() + ".class);");
            sourceWriter.outdent();
            sourceWriter.println("}");
            sourceWriter.println("wf = pwf_" + p.widgetFactory().getSimpleName() + ";");
            sourceWriter.outdent();
            sourceWriter.println("}");
            firstRun = false;
        }
        sourceWriter.println("return wf;");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // implement get required version ....
        sourceWriter.println("@Override");
        sourceWriter.println("protected String getRequiredPluginVersionImpl(String playerName) {");
        sourceWriter.indent();
        sourceWriter.println("String v = \"\";");

        firstRun = true;
        for (String player : playerMap) {
            JClassType jt = context.getTypeOracle().getType(player);
            Player p = jt.getAnnotation(Player.class);
            if (firstRun) {
                sourceWriter.println("if(\"" + p.name() + "\".equals(playerName)) {");
            } else {
                sourceWriter.println("else if(\"" + p.name() + "\".equals(playerName)) {");
            }
            sourceWriter.indent();
            sourceWriter.println("v = \"" + p.minPluginVersion() + "\";");
            sourceWriter.outdent();
            sourceWriter.println("}");
            firstRun = false;
        }
        sourceWriter.println("return v;");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // close generated class
        sourceWriter.outdent();
        sourceWriter.println("}");

        // commit generated class
        context.commit(logger, printWriter);
    }

    public static class PluginPropsCollection {

        private HashMap<PluginVersion, PluginProps> props;

        public PluginPropsCollection() {
            props = new HashMap<PluginVersion, PluginProps>();
        }

        public PluginProps getProps(PluginVersion pv) {
            if (props.containsKey(pv)) {
                return props.get(pv);
            } else {
                PluginProps pp = new PluginProps();
                props.put(pv, pp);
                return pp;
            }
        }

        public HashMap<PluginVersion, PluginProps> getProps() {
            return props;
        }

        @Override
        public String toString() {
            return "PluginPropsCollection{" + "props=" + props + '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PluginPropsCollection other = (PluginPropsCollection) obj;
            if (this.props != other.props && (this.props == null || !this.props.equals(other.props))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 17 * hash + (this.props != null ? this.props.hashCode() : 0);
            return hash;
        }
    }

    public static class PluginProps {

        private String mimes, protos;

        public PluginProps() {
            mimes = "";
            protos = "";
        }

        public String getMimes() {
            return mimes;
        }

        public void setMimes(String mimes) {
            this.mimes = mimes;
        }

        public String getProtos() {
            return protos;
        }

        public void setProtos(String protos) {
            this.protos = protos;
        }

        @Override
        public String toString() {
            return "PluginProps{" + "mimes=" + mimes + ", protos=" + protos + '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PluginProps other = (PluginProps) obj;
            if ((this.mimes == null) ? (other.mimes != null) : !this.mimes.equals(other.mimes)) {
                return false;
            }
            if ((this.protos == null) ? (other.protos != null) : !this.protos.equals(other.protos)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + (this.mimes != null ? this.mimes.hashCode() : 0);
            hash = 47 * hash + (this.protos != null ? this.protos.hashCode() : 0);
            return hash;
        }
    }
}
