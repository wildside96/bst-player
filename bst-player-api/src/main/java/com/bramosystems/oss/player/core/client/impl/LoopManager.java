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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.PlayException;

/**
 * Provides programmatic loop count management for players without native support
 *
 * @author Sikiru
 * @since 1.2
 */
public class LoopManager {

    private int loopCount, _count;
    private LoopCallback callback;
    private boolean handleLooping, looping;

    /**
     *
     * @param handleLooping true if manager should handle continous looping or false
     * if player has support for it
     * @param callback the callback
     */
    public LoopManager(boolean handleLooping, LoopCallback callback) {
        this.callback = callback;
        this.handleLooping = handleLooping;
        loopCount = 1;
        _count = loopCount;
    }

    /**
     * Returns loop count
     *
     * @return
     */
    public int getLoopCount() {
        if (looping) {
            return -1;
        }
        return loopCount;
    }

    /**
     * Set loop count
     *
     * @param loopCount
     */
    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
        _count = loopCount;
        boolean toLoop = loopCount < 0;
        if (looping && !toLoop) {
            // we're looping already & we wanna loop no more ...
            looping = false;
            if (!handleLooping) { // tell player to loopForever no more
                callback.loopForever(false);
            }
        } else if (toLoop) { // we wanna loop now...
            looping = true;
            if (!handleLooping) {   // tell player to loopForever
                callback.loopForever(true);
            }
        }
    }

    /**
     * notifies this manager that the player just finished current loop
     * TODO: handle playlist with looping in place...
     */
    public void notifyPlayFinished() {
        // one item playback finished, try another item ...
        try {
            callback.playNextItem();
        } catch (PlayException ex) {
            checkLoop();
        }
    }

    private void checkLoop() {
        if(handleLooping && (_count < 0)) {
            // loop continously, player has no support for it! initiate here...
            callback.playNextLoop();
            return;
        }

        _count--;
        if (_count <= 0) {
            _count = loopCount;
            callback.onLoopFinished();
        } else {
            callback.playNextLoop();
        }
    }

    // TODO: check repeat mode ...
    public static interface LoopCallback {

        /**
         * all loops finished
         */
        public void onLoopFinished();

        /**
         * put player in looping mode
         * @param loop
         */
        public void loopForever(boolean loop);

        /**
         * One loop finished, start another ...
         */
        public void playNextLoop();

        /**
         * Play next playlist item
         * @throws PlayException if playlist has no more entries
         */
        public void playNextItem() throws PlayException;
    }
}
