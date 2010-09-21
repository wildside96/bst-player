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
package com.bramosystems.oss.player.showcase.client.widgets;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 *
 * @author Sikiru
 */
public class ThumbnailPanel extends FlowPanel {

    private final int DURATION = 250, OFFSET = 120;
    private Animator animator;
    private int index, _top;

    public ThumbnailPanel() {
        animator = new Animator();
    }

    public boolean hasMore(boolean next) {
        int scrollHeight = getElement().getScrollHeight();
        int diff = scrollHeight - _top;
        return next ? diff >= getOffsetHeight() : diff  < scrollHeight;
    }

    public void scrollTo(int index) {
        if(index < getWidgetCount()) {
            _top = OFFSET * index;
            animator.setTop(_top);
            this.index = index;
        }
    }

    public void scrollToNext() {
        if(hasMore(true)) {
            _top += OFFSET; //getWidget(index).getOffsetHeight();
//            _top += getWidget(index).getOffsetHeight();
            animator.setTop(_top);
            index++;
        }
    }

    public void scrollToPrev() {
        if(hasMore(false)) {
            _top -= OFFSET; //getWidget(index).getOffsetHeight();
//            _top -= getWidget(index).getOffsetHeight();
            animator.setTop(_top);
            index--;
        }
    }

    public void addThumbnail(String text, final String link) {
        Label thumb = thumbBinder.createAndBindUi(null);
        thumb.setText(text);
        thumb.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                History.newItem(link);
            }
        });
        add(thumb);
    }

    private class Animator extends Animation {

        private int nTop, cTop;

        @Override
        protected void onUpdate(double progress) {
            getElement().setScrollTop(cTop + (int) (progress * (nTop - cTop)));
        }

        public void setTop(int top) {
            nTop = top;
            cTop = getElement().getScrollTop();
            run(DURATION);
        }
    }

    @UiTemplate("Thumbnail.ui.xml")
    interface ThumbnailBinder extends UiBinder<Label, Void> {}
    private static ThumbnailBinder thumbBinder = GWT.create(ThumbnailBinder.class);
}
