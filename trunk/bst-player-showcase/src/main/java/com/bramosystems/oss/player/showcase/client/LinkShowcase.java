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
package com.bramosystems.oss.player.showcase.client;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class LinkShowcase extends AbstractCase {

    public static String[] caseNames = {"Playable Audio Link"};
    public static String[] caseLinks = {"link/audio"};

    public LinkShowcase() {
    }

    public String getSummary() {
        return "Using playable links";
    }

/*
 @Override
    public void init(String token) {
        clearCases();
        Widget v = null;
        int index = getTokenLinkIndex(caseLinks, token);
        switch (index) {
            case 0:
                try {
                    v = new AudioLink(GWT.getHostPageBaseURL() + "media/o-na-som.mp3",
                            "O na som", false);
                } catch (LoadException ex) {
                    Window.alert("Load exp");
                } catch (PluginVersionException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.Auto, ex.getRequiredVersion());
                } catch (PluginNotFoundException ex) {
                    v = PlayerUtil.getMissingPluginNotice(Plugin.Auto);
                }
                addCase("Playing sound automatically", null, v, "sources/custom-audio/wmp.html");
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
 *
 */
}
