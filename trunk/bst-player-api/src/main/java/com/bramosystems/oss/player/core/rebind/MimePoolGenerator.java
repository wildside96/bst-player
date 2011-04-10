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

import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sikiru
 */
public class MimePoolGenerator extends Generator {

    private final String DEFAULT_MIME_TYPES_FILE = "default-mime-types.properties";
    private HashMap<String, String> mimeMap = new HashMap<String, String>();
    private EnumMap<Plugin, PluginPropsCollection> pluginMap = new EnumMap<Plugin, PluginPropsCollection>(Plugin.class);
    private String className = null, packageName = null;
    private TreeLogger logger;
    private final Pattern PLUGIN_REGEX = Pattern.compile("plugin\\.([a-zA-Z]+)(\\.(\\d+)_(\\d+)_(\\d+))?");
    private final Pattern PROTO_REGEX = Pattern.compile("protocols\\.([a-zA-Z]+)(\\.(\\d+)_(\\d+)_(\\d+))?");
    private final ArrayList<Plugin> pl = new ArrayList<Plugin>(Arrays.asList(Plugin.values()));

    public MimePoolGenerator() {
        // init plugin props ...
        pl.remove(Plugin.Auto);
        pl.remove(Plugin.MatrixSupport);
        pl.remove(Plugin.PlaylistSupport);
        pl.remove(Plugin.Native);
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

            // build plugin mime types ...
            ConfigurationProperty mimeFile =
                    context.getPropertyOracle().getConfigurationProperty("bstplayer.media.mimeTypes");
            String val = mimeFile.getValues().get(0);
            if (val == null) {
                logger.log(TreeLogger.Type.INFO, "'" + mimeFile.getName() + "' configuration property not set! Using defaults");
                parsePropertyFile(DEFAULT_MIME_TYPES_FILE);
            } else {
                logger.log(TreeLogger.Type.INFO, "'" + mimeFile.getName() + "' set! Using '" + val + "'");
                parsePropertyFile(val);
            }

            // Generate class source code
            generateClass(logger, context);
        } catch (Exception e) {
            logger.log(TreeLogger.ERROR, "Unable to build Media Types!", e);
        }

        // return the fully qualifed name of the class generated
        return packageName + "." + className;
    }

    private void parsePropertyFile(String mimePropertyFile) throws UnableToCompleteException {
        try {
            for (Plugin p : pl) {
                pluginMap.put(p, new PluginPropsCollection());
            }

            // build props ...            
            Properties p = new Properties();
            p.load(MimePoolGenerator.class.getResourceAsStream(mimePropertyFile));
            Iterator<String> types = p.stringPropertyNames().iterator();
            while (types.hasNext()) {
                String key = types.next();
                if (key.toLowerCase().startsWith("audio") || key.toLowerCase().startsWith("video")) {
                    // mime types ...
                    mimeMap.put(key, p.getProperty(key));
                }
            }

            types = p.stringPropertyNames().iterator();
            while (types.hasNext()) {
                String key = types.next();
                Matcher m = PLUGIN_REGEX.matcher(key);
                if (m.matches()) {
                    try {
                        PluginPropsCollection ppc = pluginMap.get(Plugin.valueOf(m.group(1)));
                        PluginVersion pv = m.group(2) != null
                                ? PluginVersion.get(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)))
                                : new PluginVersion();
                        String[] mimes = p.getProperty(key).split(",");
                        String exts = null;
                        for (String mime : mimes) {
                            if (exts == null) {
                                exts = mimeMap.get(mime.trim());
                            } else {
                                exts += "," + mimeMap.get(mime.trim());
                            }
                        }
                        ppc.getProps(pv).setMimes(exts);
                    } catch (Exception e) {
                        logger.log(TreeLogger.WARN, "Invalid plugin type - '" + key + "'", e);
                    }
                    continue;
                }

                m = PROTO_REGEX.matcher(key);
                if (m.matches()) {
                    try {
                        PluginPropsCollection ppc = pluginMap.get(Plugin.valueOf(m.group(1)));
                        PluginVersion pv = m.group(2) != null
                                ? PluginVersion.get(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)))
                                : new PluginVersion();
                        ppc.getProps(pv).setProtos(p.getProperty(key));
                   } catch (Exception e) {
                        logger.log(TreeLogger.WARN, "Invalid plugin type - '" + key + "'", e);
                    }
                }
            }
        } catch (IOException ex) {
            throw new UnableToCompleteException();
        } catch (Exception e) {
            throw new UnableToCompleteException();
        }
    }

    /**
     * @param logger Logger object
     * @param context Generator context
     */
    private void generateClass(TreeLogger logger, GeneratorContext context) {
        // get print writer that receives the source code
        PrintWriter printWriter = context.tryCreate(logger, packageName, className);

        // print writer if null, source code has ALREADY been generated,  return
        if (printWriter == null) {
            return;
        }

        // init composer, set class properties, create source writer
        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);
        composer.setSuperclass("com.bramosystems.oss.player.core.client.impl.plugin.MimeParserBase");
        composer.addImport("com.bramosystems.oss.player.core.client.Plugin");
        composer.addImport("java.util.HashMap");
        composer.addImport("java.util.Iterator");

        SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

        // generate constructor source code
        sourceWriter.println("public " + className + "() { ");
        sourceWriter.indent();
        sourceWriter.println("super();");
        sourceWriter.outdent();
        sourceWriter.println("}");   // end constructor source generation

        sourceWriter.println();

        // implement mimeTypes ....
        sourceWriter.println("@Override");
        sourceWriter.println("protected void initMimeTypes(HashMap<String, String> mimeTypes) {");
        sourceWriter.indent();

        Iterator<String> mimeKeys = mimeMap.keySet().iterator();
        while (mimeKeys.hasNext()) {
            String mime = mimeKeys.next();
            sourceWriter.println("mimeTypes.put(\"" + mime + "\",\"" + mimeMap.get(mime) + "\");");
        }
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // process mime pools ...
        sourceWriter.println("@Override");
        sourceWriter.println("protected void processPlugin(Plugin plugin) {");
        sourceWriter.indent();
        sourceWriter.println("switch(plugin) {");
        for (Plugin plug : pl) {
            sourceWriter.println("case " + plug.name() + ":");
            PluginPropsCollection ppc = pluginMap.get(plug);
            HashMap<PluginVersion, PluginProps> ppm = ppc.getProps();
            Iterator<PluginVersion> it = ppm.keySet().iterator();
            while (it.hasNext()) {
                PluginVersion pv = it.next();
                PluginProps pp = ppm.get(pv);
                sourceWriter.println("addExtensions(" + pv.getMajor() + ", " + pv.getMinor() + "," + pv.getRevision() + ", \"" + pp.getMimes() + "\");");
                sourceWriter.println("addProtocols(" + pv.getMajor() + ", " + pv.getMinor() + "," + pv.getRevision() + ", \"" + pp.getProtos() + "\");");
            }
            sourceWriter.println("break;");
        }
        sourceWriter.println("}");
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
