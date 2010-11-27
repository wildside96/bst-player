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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author Sikiru
 */
public class MimePoolGenerator extends Generator {

    private final String DEFAULT_MIME_TYPES_FILE = "default-mime-types.properties";
    private HashMap<String, String> mimeMap = new HashMap<String, String>();
    private EnumMap<Plugin, String> pluginMap = new EnumMap<Plugin, String>(Plugin.class);
    private EnumMap<Plugin, String> protoMap = new EnumMap<Plugin, String>(Plugin.class);
    private String className = null;
    private String packageName = null;
    private TreeLogger logger;

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
            buildPluginTypes();

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
            Properties p = new Properties();
            p.load(MimePoolGenerator.class.getResourceAsStream(mimePropertyFile));
            Iterator<String> types = p.stringPropertyNames().iterator();
            while (types.hasNext()) {
                String key = types.next();
                if (key.toLowerCase().startsWith("audio") || key.toLowerCase().startsWith("video")) {
                    // mime types ...
                    mimeMap.put(key, p.getProperty(key));
                } else if (key.toLowerCase().startsWith("plugin")) {
                    String _plugin = key.substring(key.indexOf(".") + 1);
                    try {
                        pluginMap.put(Plugin.valueOf(_plugin), p.getProperty(key));
                    } catch (Exception e) {
                        logger.log(TreeLogger.Type.WARN, "Invalid plugin type - '" + key + "'", e);
                    }
                } else if (key.toLowerCase().startsWith("protocols")) {
                    String _plugin = key.substring(key.indexOf(".") + 1);
                    try {
                        protoMap.put(Plugin.valueOf(_plugin), p.getProperty(key));
                    } catch (Exception e) {
                        logger.log(TreeLogger.Type.WARN, "Invalid plugin type - '" + key + "'", e);
                    }
                }
            }
        } catch (IOException ex) {
            throw new UnableToCompleteException();
        } catch (Exception e) {
            throw new UnableToCompleteException();
        }
    }

    private void buildPluginTypes() throws UnableToCompleteException {
        try {
            Iterator<Plugin> plugins = pluginMap.keySet().iterator();
            while (plugins.hasNext()) {
                Plugin key = plugins.next();
                String[] mimes = pluginMap.get(key).split(",");
                String exts = null;
                for (String mime : mimes) {
                    if (exts == null) {
                        exts = mimeMap.get(mime.trim());
                    } else {
                        exts += "," + mimeMap.get(mime.trim());
                    }
                }
                pluginMap.put(key, exts);
            }
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
        composer.setSuperclass("com.bramosystems.oss.player.core.client.impl.MimePoolBase");
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

        // implement init pools ...
        sourceWriter.println("@Override");
        sourceWriter.println("protected void initPools() {");
        sourceWriter.indent();
        sourceWriter.println("addPluginExtensions(Plugin.DivXPlayer, \"" + pluginMap.get(Plugin.DivXPlayer) + "\");");
        sourceWriter.println("addPluginExtensions(Plugin.FlashPlayer, \"" + pluginMap.get(Plugin.FlashPlayer) + "\");");
        sourceWriter.println("addPluginExtensions(Plugin.QuickTimePlayer, \"" + pluginMap.get(Plugin.QuickTimePlayer) + "\");");
        sourceWriter.println("addPluginExtensions(Plugin.VLCPlayer, \"" + pluginMap.get(Plugin.VLCPlayer) + "\");");
        sourceWriter.println("addPluginExtensions(Plugin.WinMediaPlayer, \"" + pluginMap.get(Plugin.WinMediaPlayer) + "\");");
        sourceWriter.println("addPluginProtocols(Plugin.DivXPlayer, \"" + protoMap.get(Plugin.DivXPlayer) + "\");");
        sourceWriter.println("addPluginProtocols(Plugin.FlashPlayer, \"" + protoMap.get(Plugin.FlashPlayer) + "\");");
        sourceWriter.println("addPluginProtocols(Plugin.QuickTimePlayer, \"" + protoMap.get(Plugin.QuickTimePlayer) + "\");");
        sourceWriter.println("addPluginProtocols(Plugin.VLCPlayer, \"" + protoMap.get(Plugin.VLCPlayer) + "\");");
        sourceWriter.println("addPluginProtocols(Plugin.WinMediaPlayer, \"" + protoMap.get(Plugin.WinMediaPlayer) + "\");");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // close generated class
        sourceWriter.outdent();
        sourceWriter.println("}");

        // commit generated class
        context.commit(logger, printWriter);
    }
}
