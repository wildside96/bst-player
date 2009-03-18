/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bramosystems.gwt.player.client.impl;

import com.bramosystems.gwt.player.client.Plugin;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class PlayerUtilImplTest extends GWTTestCase {

    public PlayerUtilImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of suggestPlayer method, of class PlayerUtilImpl.
     */
    @Test
    public void testSuggestPlayer() throws Exception {
        System.out.println("suggestPlayer");
        PlayerUtilImpl instance = GWT.create(PlayerUtilImpl.class);

        Plugin result = instance.suggestPlayer(null, "flv");
        assertEquals(Plugin.FlashVideoPlayer, result);

//        result = instance.suggestPlayer(null, "mov");
//        assertEquals(Plugin.QuickTimePlayer, result);

        result = instance.suggestPlayer(null, "m4a");
        assertEquals(Plugin.QuickTimePlayer, result);

        result = instance.suggestPlayer(null, "wma");
        assertEquals(Plugin.WinMediaPlayer, result);
    }

    @Override
    public String getModuleName() {
        return "com.bramosystems.gwt.player.BSTPlayer";
    }

}