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

package com.bramosystems.oss.player.core.client.ui;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayException;

/**
 *
 * @author Sikiru
 */
public class DivXPlayer extends AbstractMediaPlayer {

    private DivXPlayer(String mediaURL, boolean autoplay, String height, String width)
    //        throws LoadException, PluginNotFoundException, PluginVersionException
    {
    }



    @Override
    public void loadMedia(String mediaURL) throws LoadException {
    }

    @Override
    public void playMedia() throws PlayException {
    }

    @Override
    public void stopMedia() {
    }

    @Override
    public void pauseMedia() {
    }

    @Override
    public long getMediaDuration() {
        return 0;
    }

    @Override
    public double getPlayPosition() {
        return 0;
    }

    @Override
    public void setPlayPosition(double position) {
    }

    @Override
    public double getVolume() {
        return 0;
    }

    @Override
    public void setVolume(double volume) {
    }

    public static enum SeekMethod {
        DOWN, UP, DRAG
    }

}
