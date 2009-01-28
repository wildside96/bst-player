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
package com.bramosystems.gwt.player.client.ui.skin;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

/**
 * Abstract base class for seek bar implementations.
 *
 * <p>Provides means of controlling <b>loading</b> and <b>playing</b> progress
 * indicators during media loading and playback respectively.
 *
 * @author Sikirulai Braheem
 */
public abstract class MediaSeekBar extends Composite implements MouseListener {

    private Widget playing,  loading;
    protected AbsolutePanel seekTrack;
    private ArrayList<SeekChangeListener> seekListeners;

    /**
     * Constructs <code>MediaSeekBar</code> of the specified height.
     *
     * @param height the height of the seek bar in pixels
     */
    public MediaSeekBar(int height) {
        seekTrack = new AbsolutePanel();
        seekTrack.setSize("100%", String.valueOf(height));
        super.initWidget(seekTrack);

        seekListeners = new ArrayList<SeekChangeListener>();
    }

    /**
     * Overridden to prevent subclasses from changing the wrapped widget.
     * Subclass should call <code>initSeekBar</code> instead.
     *
     * @see #initSeekBar(com.google.gwt.user.client.ui.Widget, com.google.gwt.user.client.ui.Widget) 
     */
    @Override
    protected void initWidget(Widget widget) {
    }

    /**
     * Initialize the seek bar with the widgets that will be used to indicate
     * media loading and playback progress respectively.
     *
     * <p>Subclasses should call this method before calling any Widget methods
     * on this object.
     *
     * @param loadingWidget loading progress indicator widget
     * @param playingWidget playback progress indicator widget
     */
    protected final void initSeekBar(Widget loadingWidget, Widget playingWidget) {
        loading = loadingWidget;
        playing = playingWidget;

        seekTrack.add(this.loading, 0, 0);
        seekTrack.add(this.playing, 0, 0);

        setLoadingProgress(0);
        setPlayingProgress(0);
    }

    /**
     * Set the progress of the media loading operation.
     *
     * @param loadingProgress progress should be between {@code 0} (the minimum)
     * and {@code 1} (the maximum). Any value outside the range will be ignored.
     */
    public final void setLoadingProgress(double loadingProgress) {
        if ((loadingProgress >= 0) && (loadingProgress <= 1.0)) {
            loadingProgress *= 100;
            loading.setWidth(loadingProgress + "%");
        }
    }

    /**
     * Set the progress of the media loading operation.
     *
     * @param playingProgress progress should be between {@code 0} (the minimum)
     * and {@code 1} (the maximum). Any value outside the range will be ignored.
     */
    public final void setPlayingProgress(double playingProgress) {
        if ((playingProgress >= 0) && (playingProgress <= 1.0)) {
            playingProgress *= 100;
            playing.setWidth(playingProgress + "%");
        }
    }

    public final void onMouseEnter(Widget sender) {
    }

    public final void onMouseLeave(Widget sender) {
    }

    public final void onMouseMove(Widget sender, int x, int y) {
    }

    public final void onMouseDown(Widget sender, int x, int y) {
    }

    public final void onMouseUp(Widget sender, int x, int y) {
        fireSeekChanged(x / (double) getOffsetWidth());
    }

    /**
     * Adds {@code SeekChangeListener} objects.  The listener
     * is notified whenever the state of the seek bar changes.
     *
     * @param listener {@code SeekChangeListener} object to add to
     * the list of {@code SeekChangeListener}s.
     */
    public void addSeekChangeListener(SeekChangeListener listener) {
        seekListeners.add(listener);
    }

    /**
     * Removes the specified {@code SeekChangeListener} object from the list of
     * registered listeners.
     *
     * @param listener {@code SeekChangeListener} object to remove from the
     * list of {@code SeekChangeListener}s.
     */
    public void removeSeekChangeListener(SeekChangeListener listener) {
        seekListeners.remove(listener);
    }

    private void fireSeekChanged(double newValue) {
        for (int i = 0; i < seekListeners.size(); i++) {
            seekListeners.get(i).onSeekChanged(newValue);
        }
    }
}
