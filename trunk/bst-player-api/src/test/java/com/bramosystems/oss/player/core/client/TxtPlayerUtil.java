/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bramosystems.oss.player.core.client;

import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.junit.client.GWTTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class TxtPlayerUtil extends GWTTestCase {

    public TxtPlayerUtil() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testFormatMediaTime() {
        System.out.println("formatMediaTime");
        assertEquals("00:00", PlayerUtil.formatMediaTime(0));
        assertEquals("1:00:00", PlayerUtil.formatMediaTime(3600000));
    }

    @Test
    public void testGetFlashPlayerVersion() throws Exception {
        System.out.println("getFlashPlayerVersion");
        PluginVersion expResult = new PluginVersion(9, 0, 0);
        PluginVersion result = PlayerUtil.getFlashPlayerVersion();
        assertTrue(result.compareTo(expResult) >= 0);
    }

    @Test
    public void testGetQuickTimePluginVersion() throws Exception {
        System.out.println("getQuickTimePluginVersion");
        PluginVersion expResult = new PluginVersion(7, 2, 1);
        PluginVersion result = PlayerUtil.getQuickTimePluginVersion();
        assertTrue(result.compareTo(expResult) >= 0);
    }

    @Test
    public void testGetWindowsMediaPlayerPluginVersion() throws Exception {
        System.out.println("getWindowsMediaPlayerPluginVersion");
        PluginVersion expResult = new PluginVersion(11, 0, 0);
        PluginVersion result = PlayerUtil.getWindowsMediaPlayerPluginVersion();
        assertTrue(result.compareTo(expResult) >= 0);
    }

    @Test
    public void testGetPlayer() throws Exception {
        System.out.println("getPlayer: MP3 format");
        AbstractMediaPlayer result = PlayerUtil.getPlayer("foo.mp3", false, "0", "0");
        assertTrue((result instanceof QuickTimePlayer) ||
                (result instanceof WinMediaPlayer) ||
                (result instanceof FlashMediaPlayer) ||
                (result instanceof VLCPlayer));

        System.out.println("getPlayer: MOV format");
        result = PlayerUtil.getPlayer("foo.mov", false, "0", "0");
        assertTrue((result instanceof QuickTimePlayer) ||
                (result instanceof VLCPlayer));

        System.out.println("getPlayer: WMA format");
        result = PlayerUtil.getPlayer("foo.wma", false, "0", "0");
        assertTrue((result instanceof WinMediaPlayer) ||
                (result instanceof VLCPlayer));

        System.out.println("getPlayer: VOB format");
        result = PlayerUtil.getPlayer("foo.vob", false, "0", "0");
        assertTrue(result instanceof VLCPlayer);

    }

    @Override
    public String getModuleName() {
        return "com.bramosystems.oss.player.core.Core";
    }
}