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
package com.bramosystems.oss.player.showcase.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Sikiru
 */
public class HorizontalStackLayoutPanel extends Composite implements HasWidgets,
        RequiresResize, ProvidesResize {

    private class ClickWrapper extends Composite {

        private Widget target;

        public ClickWrapper(Widget target, Widget wrappee) {
            this.target = target;
            initWidget(wrappee);
            sinkEvents(Event.ONCLICK);
        }

        @Override
        public void onBrowserEvent(Event event) {
            if (event.getTypeInt() == Event.ONCLICK) {
                showWidget(target);
            }
        }
    }

    private static class LayoutData {

        public double headerSize;
        public Widget header;
        public Widget widget;

        public LayoutData(Widget widget, Widget header, double headerSize) {
            this.widget = widget;
            this.header = header;
            this.headerSize = headerSize;
        }
    }
    private static final int ANIMATION_TIME = 300;
    private LayoutPanel layoutPanel;
    private Unit unit;
    private ArrayList<LayoutData> layoutData = new ArrayList<LayoutData>();
    private Widget visibleWidget;

    public HorizontalStackLayoutPanel(Unit unit) {
        this.unit = unit;
        initWidget(layoutPanel = new LayoutPanel());
    }

    @Override
    public void add(Widget w) {
        assert false : "Single-argument add() is not supported for this widget";
    }

    /**
     * Adds a child widget to this stack, along with a widget representing the
     * stack header.
     *
     * @param widget the child widget to be added
     * @param header the header widget
     * @param headerSize the size of the header widget
     */
    public void add(final Widget widget, Widget header, double headerSize) {
        ClickWrapper wrapper = new ClickWrapper(widget, header);
        layoutPanel.add(wrapper);
        layoutPanel.add(widget);

        layoutPanel.setWidgetTopBottom(wrapper, 0, Unit.PX, 0, Unit.PX);
        layoutPanel.setWidgetTopBottom(widget, 0, Unit.PX, 0, Unit.PX);

        LayoutData data = new LayoutData(widget, wrapper, headerSize);
        layoutData.add(data);

        if (visibleWidget == null) {
            // If there's no visible widget, display the first one. The layout will
            // be updated onLoad().
            visibleWidget = widget;
        }
    }

    @Override
    public void clear() {
        layoutPanel.clear();
        layoutData.clear();
        visibleWidget = null;
    }

    /**
     * Gets the currently-selected widget.
     *
     * @return the selected widget, or <code>null</code> if none exist
     */
    public Widget getVisibleWidget() {
        return visibleWidget;
    }

    @Override
    public Iterator<Widget> iterator() {
        return new Iterator<Widget>() {

            int i = 0, last = -1;

            @Override
            public boolean hasNext() {
                return i < layoutData.size();
            }

            @Override
            public Widget next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return layoutData.get(last = i++).widget;
            }

            @Override
            public void remove() {
                if (last < 0) {
                    throw new IllegalStateException();
                }

                HorizontalStackLayoutPanel.this.remove(layoutData.get(last).widget);
                i = last;
                last = -1;
            }
        };
    }

    @Override
    public void onResize() {
        layoutPanel.onResize();
    }

    @Override
    public boolean remove(Widget child) {
        if (child.getParent() != layoutPanel) {
            return false;
        }

        // Find the layoutData associated with this widget and remove it.
        for (int i = 0; i < layoutData.size(); ++i) {
            LayoutData data = layoutData.get(i);
            if (data.widget == child) {
                layoutPanel.remove(data.header);
                layoutPanel.remove(child);
                layoutData.remove(i);

                if (visibleWidget == child) {
                    visibleWidget = null;
                    if (layoutData.size() > 0) {
                        showWidget(layoutData.get(0).widget);
                    }
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Shows the specified widget.
     *
     * @param widget the child widget to be shown.
     */
    public void showWidget(Widget widget) {
        showWidget(widget, ANIMATION_TIME);
    }

    @Override
    protected void onLoad() {
        // When the widget becomes attached, update its layout.
        animate(0);
    }

    private void animate(int duration) {
        // Don't try to animate zero widgets.
        if (layoutData.size() == 0) {
            return;
        }

        double top = 0, bottom = 0;
        int i = 0, visibleIndex = -1;
        for (; i < layoutData.size(); ++i) {
            LayoutData data = layoutData.get(i);
            layoutPanel.setWidgetLeftWidth(data.header, top, unit, data.headerSize,
                    unit);

            top += data.headerSize;

            layoutPanel.setWidgetLeftWidth(data.widget, top, unit, 0, unit);

            if (data.widget == visibleWidget) {
                visibleIndex = i;
                break;
            }
        }

        assert visibleIndex != -1;

        for (int j = layoutData.size() - 1; j > i; --j) {
            LayoutData data = layoutData.get(j);
            layoutPanel.setWidgetRightWidth(data.header, bottom, unit,
                    data.headerSize, unit);
            layoutPanel.setWidgetRightWidth(data.widget, bottom, unit, 0, unit);
            bottom += data.headerSize;
        }

        LayoutData data = layoutData.get(visibleIndex);
        layoutPanel.setWidgetLeftRight(data.widget, top, unit, bottom, unit);

        layoutPanel.animate(duration);
    }

    private void showWidget(Widget widget, final int duration) {
        visibleWidget = widget;
        animate(duration);
    }
}
