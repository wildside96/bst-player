package com.bramosystems.oss.player.showcase.client.matrix;

/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.showcase.client.AbstractCase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class MatrixShowcase extends AbstractCase {

    public static String[] caseNames = {"Matrix Support"};
    public static String[] caseLinks = {"matrix/basic"};

    public MatrixShowcase() {
    }

    public String getSummary() {
        return "Matrix Support";
    }

    public void init(String token) {
        Widget mp = null, mp1 = null;
        try {
            mp = new XMatrixStage(GWT.getHostPageBaseURL() + "media/traffic.mp4", "100%", "400px");
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            mp = PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            mp = PlayerUtil.getMissingPluginNotice(ex.getPlugin());
        }
        addCase("Transformation Matrices", "Using matrix transformation for graphics manipulation",
                mp, null);

        try {
            mp1 = new XMatrixStage(GWT.getHostPageBaseURL() + "media/traffic.flv", "100%", "400px");
        } catch (LoadException ex) {
            Window.alert("Load exp");
        } catch (PluginVersionException ex) {
            mp1 = PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion());
        } catch (PluginNotFoundException ex) {
            mp1 = PlayerUtil.getMissingPluginNotice(ex.getPlugin());
        }
        addCase("Transformation Matrices", "Using matrix transformation for graphics manipulation",
                mp1, null);
    }
}
