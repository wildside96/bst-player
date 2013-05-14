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
 * Holds the parameters of the YouTube player
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.1
 */
public class PlayerParameters {

    int loadRelatedVideos = 1, autoplay, loop, enableJsApi, disableKeyboardControls,
            fullScreen, showInfo = 1, ivLoadPolicy = 1, closedCaptionPolicy,
            startTime, showControls = 1, modestBranding;
    private AutoHideMode autoHide = AutoHideMode.AUTOHIDE_PROGRESS_BAR;
    private String playerAPIId = "";

    public PlayerParameters() {
    }

    /**
     * Checks if the initial video will autoplay when loaded
     *
     * @return {@code true} if autoplay is active, {@code false} otherwise
     */
    public boolean isAutoplay() {
        return autoplay == 1;
    }

    /**
     * Sets whether or not the initial video will autoplay when the player loads.
     *
     * @param autoplay {@code true} to autoplay, {@code false} otherwise
     */
    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay ? 1 : 0;
    }

//   User Preference ...
//    public boolean isClosedCaptionPolicy() {
//        return closedCaptionPolicy;
//    }
//    public void showClosedCaptions(boolean show) {
//        this.closedCaptionPolicy = show ? 1 : 0;
//    }
 
    /**
     * Checks if keyboard controls are enabled
     *
     * @return {@code true} if enabled, {@code false} otherwise
     */
    public boolean isKeyboardControlsEnabled() {
        return disableKeyboardControls == 0;
    }

    /**
     * Enables or disables keyboard controls
     *
     * @param enable {@code true} to enable keyboard controls, {@code false} otherwise
     */
    public void setKeyboardControlsEnabled(boolean enable) {
        this.disableKeyboardControls = enable ? 0 : 1;
    }

    /**
     * Checks if the Javascript API is enabled
     *
     * @return {@code true} if enabled, {@code false} otherwise
     */
    public boolean isJSApiEnabled() {
        return enableJsApi == 1;
    }

    /**
     * Enables or disables the player's Javascript API
     *
     * @param enable {@code true} to enable the Javascript API, {@code false} otherwise
     */
    public void setJSApiEnabled(boolean enable) {
        this.enableJsApi = enable ? 1 : 0;
    }

    /**
     * Checks if the fullscreen button is enabled
     *
     * @return {@code true} if enabled, {@code false} otherwise
     */
    public boolean isFullScreenEnabled() {
        return fullScreen == 1;
    }

    /**
     * Enables or disables the player's fullscreen button
     *
     * @param enable {@code true} to enable the fullscreen, {@code false} otherwise
     */
    public void setFullScreenEnabled(boolean enable) {
        this.fullScreen = enable ? 1 : 0;
    }
    /**
     * Checks if video annotations are shown by default
     *
     * @return {@code true} if annotations are shown by default, {@code false} otherwise
     */
    public boolean isShowVideoAnnotations() {
        return ivLoadPolicy == 1;
    }

    /**
     * Enables or disables showing video annotations by default.
     *
     * @param show {@code true} to show annotations, {@code false} otherwise
     */
    public void showVideoAnnotations(boolean show) {
        this.ivLoadPolicy = show ? 1 : 3;
    }

    /**
     * Checks if player is set to load related videos
     *
     * @return {@code true} if related videos are loaded, {@code false} otherwise
     */
    public boolean isLoadRelatedVideos() {
        return loadRelatedVideos == 1;
    }

    /**
     * Sets whether the player should load related videos once playback of the
     * initial video starts.
     *
     * @param loadRelatedVideos {@code true} to load related videos, {@code false} otherwise
     */
    public void setLoadRelatedVideos(boolean loadRelatedVideos) {
        this.loadRelatedVideos = loadRelatedVideos ? 1 : 0;
    }

    /**
     * Checks if player is set to repeat playback
     *
     * @return {@code true} if playback is set to repeat, {@code false} otherwise
     */
    public boolean isLoopEnabled() {
        return loop == 1;
    }

    /**
     * Sets player to repeat video playback again and again.
     *
     * @param loop {@code true} to repeat playback, {@code false} otherwise
     */
    public void setLoopEnabled(boolean loop) {
        this.loop = loop ? 1 : 0;
    }

    /**
     * Checks if the display of video information is enabled
     *
     * @return {@code true} if display of video information is enabled, {@code false} otherwise
     */
    public boolean isShowVideoInformation() {
        return showInfo == 1;
    }

    /**
     * Enables or disables the display of information like the video title and rating
     * before the video starts playing.
     *
     * @param show {@code true} if video information should show, {@code false} otherwise
     */
    public void showVideoInformation(boolean show) {
        this.showInfo = show ? 1 : 0;
    }

    /**
     * Gets the time at which the player starts playback
     *
     * @return time (in seconds) at which the player starts playback
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Sets the time at which the player begins video playback.
     *
     * @param startTime time to begin playback (in seconds)
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the player identification value used internally by the YouTube player
     *
     * @return the player ID
     */
    public String getPlayerAPIId() {
        return playerAPIId;
    }

    /**
     * Sets the player identification value used internally by the YouTube player.
     *
     * @param playerAPIId player ID
     */
    public void setPlayerAPIId(String playerAPIId) {
        this.playerAPIId = playerAPIId;
    }

    /**
     * Returns the <code>autohide</code> mode of the player
     * 
     * @return the autohide mode
     * @since 1.3
     */
    public AutoHideMode getAutoHide() {
        return autoHide;
    }

    /**
     * Sets the <code>autohide</code> mode of the player
     * 
     * @param autoHide the autohide mode
     * @since 1.3
     */
    public void setAutoHide(AutoHideMode autoHide) {
        this.autoHide = autoHide;
    }

    /**
     * Returns the status of the video player controls
     * 
     * @return <coode>true</code> if video player controls is visible, <code>false</code> otherwise
     * @since 1.3
     */
    public boolean isShowControls() {
        return showControls == 1;
    }

    /**
     * Shows or hides the video player controls
     * 
     * @param showControls <code>true</code> to show video player controls and <code>false</code> otherwise
     * @since 1.3
     */
    public void setShowControls(boolean showControls) {
        this.showControls = showControls ? 1 : 0;
    }

    /**
     * Returns the status of the YouTube logo on the control bar
     * 
     * @return <coode>true</code> if YouTube logo is not showed on control bar, <code>false</code> otherwise
     * @since 1.3
     */
    public boolean isModestBranding() {
        return modestBranding == 1;
    }

    /**
     * Shows or hides the YouTube logo on the control bar
     * 
     * @param modestBranding <code>true</code> to show video player controls and <code>false</code> otherwise
     * @since 1.3
     */
    public void setModestBranding(boolean modestBranding) {
        this.modestBranding = modestBranding ? 1 : 0;
    }

    /**
     * An enum of autohide parameters for YouTubePlayer
     * 
     * @author Sikiru Braheem
     * @since 1.3
     */
    public static enum AutoHideMode {

        /**
         * Video player controls and video progress bar are visible throughout video playback
         */
        NO_AUTOHIDE,
        /**
         * Video player controls and video progress bar are automatically hidden during video playback
         */
        AUTOHIDE_ALL_CONTROLS,
        /**
         * Video progress bar fades out during video playback while video player controls remain visible
         */
        AUTOHIDE_PROGRESS_BAR
    }
}
