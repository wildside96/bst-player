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
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.google.gwt.core.ext.*;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sikiru
 */
public class PlayerManagerGenerator extends Generator {

    private final String DEFAULT_MIME_TYPES_FILE = "default-mime-types.properties";
    private String className = null, packageName = null;
    private TreeLogger logger;
    private HashMap<String, _provider> pMap = new HashMap<String, _provider>();
    private HashMap<String, String> mimeMap = new HashMap<String, String>();

    public PlayerManagerGenerator() {
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
            logger.log(TreeLogger.ERROR, "Unable to build Player Widgets!", e);
        }

        // return the fully qualifed name of the class generated
        return packageName + "." + className;
    }

    /**
     * @param logger Logger object
     * @param context Generator context
     */
    private void generateClass(TreeLogger logger, GeneratorContext context) throws
            NotFoundException, BadPropertyValueException, UnableToCompleteException, IOException {
        // get print writer that receives the source code
        PrintWriter printWriter = context.tryCreate(logger, packageName, className);

        // print writer if null, source code has ALREADY been generated,  return
        if (printWriter == null) {
            return;
        }

        // build plugin mime types ...
        ConfigurationProperty mimeFile =
                context.getPropertyOracle().getConfigurationProperty("bstplayer.media.mimeTypes");
        String val = mimeFile.getValues().get(0);
        if (val == null) {
            logger.log(TreeLogger.Type.INFO, "'" + mimeFile.getName() + "' configuration property not set! Using defaults");
            parseMimeFile(DEFAULT_MIME_TYPES_FILE);
        } else {
            logger.log(TreeLogger.Type.INFO, "'" + mimeFile.getName() + "' set! Using '" + val + "'");
            parseMimeFile(val);
        }

        collatePlayers(context.getTypeOracle());

        // init composer, set class properties, create source writer
        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);
        composer.setSuperclass("PlayerManager");
        composer.addImport("com.google.gwt.core.client.GWT");
        composer.addImport("com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory");
        composer.addImport("com.bramosystems.oss.player.core.client.impl.CallbackUtility");
        composer.addImport("com.bramosystems.oss.player.core.client.spi.ConfigurationContext");
        composer.addImport("com.bramosystems.oss.player.core.client.*");
        composer.addImport("java.util.*");

        SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

        sourceWriter.println("private HashMap<String, HashMap<String, PlayerInfo>> pInfos = new HashMap<String, HashMap<String, PlayerInfo>>();");

        // collate widget factories & create static holders ...
        Iterator<String> fact = pMap.keySet().iterator();
        while (fact.hasNext()) {
            String provClass = fact.next();
            sourceWriter.println("private static PlayerProviderFactory pwf_" + escapeProviderName(pMap.get(provClass).name)
                    + " = GWT.create(" + provClass + ".class);");
        }
        sourceWriter.println();

        Pattern ptrn = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");

        // generate constructor source code
        sourceWriter.println("public " + className + "() { ");
        sourceWriter.indent();

        // init widget factories ...
        fact = pMap.keySet().iterator();
        while (fact.hasNext()) {
            String provClass = fact.next();
            String provName = escapeProviderName(pMap.get(provClass).name);
            sourceWriter.println("pwf_" + provName + ".init(new ConfigurationContext(CallbackUtility.initCallbackHandlers(\""
                    + provName + "\"), \"bstplayer.handlers." + provName + "\"));");
        }

        sourceWriter.println();

        // init providers ...
        fact = pMap.keySet().iterator();
        while (fact.hasNext()) {
            _provider pvd = pMap.get(fact.next());
            sourceWriter.println("pInfos.put(\"" + pvd.name + "\", new HashMap<String, PlayerInfo>());");

            Iterator<_player> pns = pvd.players.iterator();
            while (pns.hasNext()) {
                _player ply = pns.next();
                boolean ps = false, ms = false;

                JClassType ints[] = ply.interfaces;
                for (int j = 0; j < ints.length; j++) {
                    if (ints[j].getQualifiedSourceName().equals(MatrixSupport.class.getName())) {
                        ms = true;
                    } else if (ints[j].getQualifiedSourceName().equals(PlaylistSupport.class.getName())) {
                        ps = true;
                    }
                }
                Matcher m = ptrn.matcher(ply.minPluginVer);
                if (m.matches()) {
                    sourceWriter.println("pInfos.get(\"" + pvd.name + "\").put(\"" + ply.name
                            + "\", new PlayerInfo(\"" + pvd.name + "\",\"" + ply.name + "\"," + "PluginVersion.get("
                            + Integer.parseInt(m.group(1)) + "," + Integer.parseInt(m.group(2)) + "," + Integer.parseInt(m.group(3)) + "),"
                            + ps + "," + ms + "));");
                } else {
                    logger.log(TreeLogger.Type.WARN, "Min");
                }
            }
        }
        sourceWriter.outdent();
        sourceWriter.println("}");   // end constructor source generation

        sourceWriter.println();

        // implement get provider names ...
        sourceWriter.println("@Override");
        sourceWriter.println("public Set<String> getProviders(){");
        sourceWriter.indent();
        sourceWriter.println("return new TreeSet<String>(pInfos.keySet());");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // implement get player names by provider ...
        sourceWriter.println("@Override");
        sourceWriter.println("public Set<String> getPlayerNames(String providerName) {");
        sourceWriter.indent();
        sourceWriter.println("if(!pInfos.containsKey(providerName))");
        sourceWriter.println("throw new IllegalArgumentException(\"Unknown player provider - \" + providerName);");
        sourceWriter.println("return new TreeSet<String>(pInfos.get(providerName).keySet());");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // implement get player infos ...
        sourceWriter.println("@Override");
        sourceWriter.println("public PlayerInfo getPlayerInfo(String providerName, String playerName) {");
        sourceWriter.indent();
        sourceWriter.println("if(!pInfos.containsKey(providerName))");
        sourceWriter.println("throw new IllegalArgumentException(\"Unknown player provider - \" + providerName);");
        sourceWriter.println("if(!pInfos.get(providerName).containsKey(playerName))");
        sourceWriter.println("throw new IllegalArgumentException(\"Unknown player name - \" + playerName);");
        sourceWriter.println("return pInfos.get(providerName).get(playerName);");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // implement get widget factory with defered binding on demand ....
        sourceWriter.println("@Override");
        sourceWriter.println("public PlayerProviderFactory getProviderFactory(String provider) {");
        sourceWriter.indent();
        sourceWriter.println("PlayerProviderFactory wf = null;");

        boolean firstRun = true;
        Iterator<_provider> provs = pMap.values().iterator();
        while (provs.hasNext()) {
            _provider prov = provs.next();
            if (firstRun) {
                sourceWriter.println("if(\"" + prov.name + "\".equals(provider)) {");
            } else {
                sourceWriter.println("else if(\"" + prov.name + "\".equals(provider)) {");
            }
            sourceWriter.indent();
            sourceWriter.println("wf = pwf_" + escapeProviderName(prov.name) + ";");
            sourceWriter.outdent();
            sourceWriter.println("}");
            firstRun = false;
        }
        sourceWriter.println("return wf;");
        sourceWriter.outdent();
        sourceWriter.println("}");
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

        // close generated class
        sourceWriter.outdent();
        sourceWriter.println("}");

        // commit generated class
        context.commit(logger, printWriter);
    }

    private void collatePlayers(TypeOracle typeOracle) {
        TreeLogger tl = logger.branch(TreeLogger.Type.INFO, "Searching for Player Providers");
        JClassType a[] = typeOracle.getTypes();
        for (int i = 0; i < a.length; i++) {
            if (a[i].isAnnotationPresent(PlayerProvider.class)) {
                String pName = a[i].getAnnotation(PlayerProvider.class).value();
                tl.log(TreeLogger.Type.INFO, "Processing Player Provider : " + pName);
                pMap.put(a[i].getQualifiedSourceName(), new _provider(pName));
            }
        }

        tl = logger.branch(TreeLogger.Type.INFO, "Searching for Player widgets");
        for (int i = 0; i < a.length; i++) {
            if (a[i].isAnnotationPresent(Player.class)) {
                Player p = a[i].getAnnotation(Player.class);
                String pName = p.providerFactory().getName();
                if (pMap.containsKey(pName)) {
                    tl.log(TreeLogger.Type.INFO, "Processing Player widget : " + a[i].getQualifiedSourceName());
                    _player _py = new _player(p.name(), p.minPluginVersion(), a[i].getQualifiedSourceName());
                    _py.interfaces = a[i].getImplementedInterfaces();
                    pMap.get(pName).players.add(_py);
                } else {
                    logger.log(TreeLogger.Type.ERROR, "WidgetFactory '" + pName + "' should be annotated with @PlayerProvider");
                }
            }
        }
    }

    private void parseMimeFile(String mimePropertyFile) throws IOException {
        // build props ...            
        Properties p = new Properties();
        p.load(getClass().getResourceAsStream(mimePropertyFile));
        Iterator<String> types = p.stringPropertyNames().iterator();
        while (types.hasNext()) {
            String key = types.next();
            if (key.toLowerCase().startsWith("audio") || key.toLowerCase().startsWith("video")) {
                // mime types ...
                mimeMap.put(key, p.getProperty(key));
            }
        }
    }

    // replace chars [.] with escaped strings ...
    private String escapeProviderName(String provName) {
        return provName.replace(".", "$");
    }

    private class _provider {

        String name;
        HashSet<_player> players = new HashSet<_player>();

        public _provider(String name) {
            this.name = name;
        }
    }

    private class _player {

        String name;
        String minPluginVer;
        String implClass;
        JClassType[] interfaces;

        public _player(String name, String minPluginVer, String implClass) {
            this.name = name;
            this.minPluginVer = minPluginVer;
            this.implClass = implClass;
        }
    }
}
