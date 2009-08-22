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

package com.bramosystems.oss.player.youtube.client;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class PlayerParameters {
    private int loadRelatedVideos = 1, autoplay, loop, enableJsApi, disableKeyboardControls,
            egm, showBorder, fullScreen, highDef, showSearch = 1, showInfo = 1,
            ivLoadPolicy = 1, closedCaptionPolicy, startTime;
    private String color1 = "", color2 = "", playerAPIId = "";

    public PlayerParameters() {
    }

    public boolean isAutoplay() {
        return autoplay == 1;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay ? 1 : 0;
    }

//   User Preference ...
//    public boolean isClosedCaptionPolicy() {
//        return closedCaptionPolicy;
//    }

    public void showClosedCaptions(boolean show) {
        this.closedCaptionPolicy = show ? 1 : 0;
    }

    public String getPrimaryBorderColor() {
        return color1;
    }

    public void setPrimaryBorderColor(String color) {
        this.color1 = color;
    }

    public String getSecondaryBorderColor() {
        return color2;
    }

    public void setSecondaryBorderColor(String color) {
        this.color2 = color;
    }

    public boolean isKeyboardControlsEnabled() {
        return disableKeyboardControls == 0;
    }

    public void setKeyboardControlsEnabled(boolean enable) {
        this.disableKeyboardControls = enable ? 0 : 1;
    }

    public boolean isEnhancedGenieMenuEnabled() {
        return egm == 1;
    }

    public void setEnhancedGenieMenuEnabled(boolean enable) {
        this.egm = enable ? 1 : 0;
    }

    public boolean isJSApiEnabled() {
        return enableJsApi == 1;
    }

    public void setJSApiEnabled(boolean enable) {
        this.enableJsApi = enable ? 1 : 0;
    }

    public boolean isFullScreenEnabled() {
        return fullScreen == 1;
    }

    public void setFullScreenEnabled(boolean enable) {
        this.fullScreen = enable ? 1 : 0;
    }

    public boolean isHDEnabled() {
        return highDef == 1;
    }

    public void setHDEnabled(boolean enable) {
        this.highDef = enable ? 1 : 0;
    }

    public boolean isShowVideoAnnotations() {
        return ivLoadPolicy == 1;
    }

    public void showVideoAnnotations(boolean show) {
        this.ivLoadPolicy = show ? 1 : 3;
    }

    public boolean isLoadRelatedVideos() {
        return loadRelatedVideos == 1;
    }

    public void setLoadRelatedVideos(boolean loadRelatedVideos) {
        this.loadRelatedVideos = loadRelatedVideos ? 1 : 0;
    }

    public boolean isLoopEnabled() {
        return loop == 1;
    }

    public void setLoopEnabled(boolean loop) {
        this.loop = loop ? 1 : 0;
    }

    public boolean isShowBorder() {
        return showBorder == 1;
    }

    public void showBorder(boolean show) {
        this.showBorder = show ? 1 : 0;
    }

    public boolean isShowVideoInformation() {
        return showInfo == 1;
    }

    public void showVideoInformation(boolean show) {
        this.showInfo = show ? 1 : 0;
    }

    /**
     * Returns the display status of the search box.
     *
     * @return <code>true</code> if the search box is enabled, <code>false</code> otherwise.
     */
    public boolean isShowSearchBox() {
        return showSearch == 1;
    }

    /**
     * Shows or hides the search box from displaying when the video is minimized.
     *
     * <p>Note that if {@linkplain #setLoadRelatedVideos(boolean)} is false, then the search box will
     * also be disabled.
     *
     * @param show <code>true</code> to show the search box, <code>false</code> otherwise.
     */
    public void showSearchBox(boolean show) {
        this.showSearch = show ? 1 : 0;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getPlayerAPIId() {
        return playerAPIId;
    }

    public void setPlayerAPIId(String playerAPIId) {
        this.playerAPIId = playerAPIId;
    }

}
