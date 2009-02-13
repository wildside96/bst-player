/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bramosystems.gwt.player.showcase.client;

import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public interface Case {

    public String wmpNoPlugin = "Windows Media Player is required to play this file." +
            "<br/><a href='www.microsoft.com/windowsmedia'>Get Player</a>";
    public String wmpPluginVersion = "Windows Media Player 7 or later is required to play this file." +
            "<br/><a href='www.microsoft.com/windowsmedia'>Get Player</a>";

    public String swfNoPlugin = "This media requires Adobe Flash Player." +
            "<br/><a href='http://www.adobe.com/go/getflash' />Get Flash</a>.";
    public String swfPluginVersion = "This media requires Adobe Flash Player 9 or later." +
            "<br/><a href='http://www.adobe.com/go/getflash' />Get Flash</a>.";

    public String qtNoPlugin = "QuickTime Player plugin is required to play this media." +
            "<br/><a href='http://www.apple.com/quicktime/download'>Get QuickTime</a>";
    public String qtPluginVersion = "QuickTime Player 7.2.1 plugin is required to play this media." +
            "<br/><a href='http://www.apple.com/quicktime/download'>Get QuickTime</a>";

    public String getSummary();

    public Widget getContentWidget();

    public void stopAllPlayers();
}
