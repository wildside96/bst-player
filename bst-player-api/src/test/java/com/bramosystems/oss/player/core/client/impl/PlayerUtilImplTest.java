/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.Plugin;
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

        assertTrue(instance.canHandleMedia(Plugin.FlashPlayer, null, "flv"));
        assertTrue(instance.canHandleMedia(Plugin.QuickTimePlayer, null, "m4a"));
        assertTrue(instance.canHandleMedia(Plugin.WinMediaPlayer, null, "wma"));
        assertTrue(instance.canHandleMedia(Plugin.VLCPlayer, null, "vob"));
    }

    @Override
    public String getModuleName() {
        return "com.bramosystems.oss.player.core.Core";
    }

}