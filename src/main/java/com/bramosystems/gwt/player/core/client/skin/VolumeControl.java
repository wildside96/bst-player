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
package com.bramosystems.gwt.player.core.client.skin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 * Widget to control the volume of a player.  The control is placed on a player
 * as an icon which when clicked upon, opens a popup panel containing the
 * slider widget for the volume.
 *
 * <p>{@code VolumeChangeListener}s are notified whenever the slider is adjusted.
 *
 * @see VolumeChangeListener
 * @author Sikirulai Braheem
 */
public class VolumeControl extends Composite implements MouseUpHandler {

    private Label volume,  track;
    private ArrayList<VolumeChangeListener> seekListeners;
    private PopupPanel volumePanel;
    private AbsolutePanel seekTrack;

    /**
     * Constructs <code>VolumeControl</code>.  The control is displayed as the
     * specified {@code icon}.  An horizontal slider of height {@code sliderHeight} is
     * displayed when the {@code icon} is clicked on.
     *
     * <p>The slider popup panel has a fixed width of 50px.
     *
     * @param icon represents the volume control object.
     * @param sliderHeight the height of the volume slider control.
     */
    public VolumeControl(Image icon, int sliderHeight) {
        icon.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                volumePanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

                    public void setPosition(int offsetWidth, int offsetHeight) {
                        volumePanel.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + getOffsetHeight());
                    }
                });
            }
        });
        super.initWidget(icon);

        seekListeners = new ArrayList<VolumeChangeListener>();

        volume = new Label();
        volume.addMouseUpHandler(this);
        setVolumeIndicatorStyle("cursor", "pointer");
        setVolumeIndicatorStyle("background", "#6600ff");

        track = new Label();
        track.addMouseUpHandler(this);
        setTrackStyle("cursor", "pointer");
        setTrackStyle("background", "#ffff99");

        seekTrack = new AbsolutePanel();

        volumePanel = new PopupPanel(true);
        volumePanel.setStyleName("");
        volumePanel.setWidget(seekTrack);
        DOM.setStyleAttribute(volumePanel.getElement(), "padding", "1px");

        String sizze = String.valueOf(sliderHeight) + "px";
        seekTrack.setSize("50px", sizze);
        track.setSize("50px", sizze);
        volume.setHeight(sizze);
        volumePanel.setWidth("50px");

        seekTrack.add(track, 0, 0);
        seekTrack.add(volume, 0, 0);

        setVolume(0);
    }

    /**
     * Sets the level of the volume slider control.
     *
     * <p><b>Note:</b> {@code VolumeChangeListener}s are not notified by this
     * method.
     *
     * @param volume value between {@code 0} (silent) and {@code 1} (the maximum).
     * Any value outside the range will be ignored.
     */
    public final void setVolume(double volume) {
        if ((volume >= 0) && (volume <= 1.0)) {
            volume *= 100;
            this.volume.setWidth(volume + "%");
        }
    }

    public void onMouseUp(MouseUpEvent event) {
        double vol = 0;
        vol = event.getX() / (double) seekTrack.getOffsetWidth();
        setVolume(vol);
        fireVolumeChanged(vol);
    }

    /**
     * Overridden to prevent subclasses from changing the wrapped widget.
     *
     */
    @Override
    protected void initWidget(Widget widget) {
    }

    /**
     * Adds {@code VolumeChangeListener} object to this control.  The listener
     * is notified whenever the state of the volume slider changes.
     *
     * @param listener {@code VolumeChangeListener} object to add to
     * the list of {@code VolumeChangeListener}s.
     */
    public void addVolumeChangeListener(VolumeChangeListener listener) {
        seekListeners.add(listener);
    }

    /**
     * Removes {@code VolumeChangeListener} object from the list of registered listeners.
     *
     * @param listener {@code VolumeChangeListener} object to remove to remove
     * from the list of {@code VolumeChangeListener}s
     */
    public void removeVolumeChangeListener(VolumeChangeListener listener) {
        seekListeners.remove(listener);
    }

    private void fireVolumeChanged(double newValue) {
        for (int i = 0; i < seekListeners.size(); i++) {
            seekListeners.get(i).onVolumeChanged(newValue);
        }
    }

    /**
     * Assigns a CSS style class name to the volume indicator
     *
     * @param styleName CSS style class name
     */
    public void setVolumeIndicatorStyleName(String styleName) {
        volume.setStyleName(styleName);
    }

    /**
     * Assigns a CSS style class name to the volume track
     *
     * @param styleName CSS style class name
     */
    public void setTrackStyleName(String styleName) {
        track.setStyleName(styleName);
    }

    /**
     * Assigns a CSS style class name to the volume slider popup panel.
     *
     * @param styleName CSS style class name
     */
    public void setPopupStyleName(String styleName) {
        volumePanel.setStyleName(styleName);
    }

    /**
     * Assigns the specified style value to the named style on the
     * volume indicator.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value the style.
     */
    public void setVolumeIndicatorStyle(String name, String value) {
        DOM.setStyleAttribute(volume.getElement(), name, value);
    }

    /**
     * Assigns the specified style value to the named style on the
     * volume track.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value the style.
     */
    public void setTrackStyle(String name, String value) {
        DOM.setStyleAttribute(track.getElement(), name, value);
    }

    /**
     * Assigns the specified style value to the named style on the
     * volume slider popup panel.
     *
     * @param name name of style e.g. {@code background, cursor etc}
     * @param value the style.
     */
    public void setPopupStyle(String name, String value) {
        DOM.setStyleAttribute(volumePanel.getElement(), name, value);
    }
}
