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
package com.bramosystems.oss.player.showcase.client.matrix;

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class XMatrixStage extends Composite {

    private FlatVideoPlayer player;
    private TransformationMatrix matrixCache;

    public XMatrixStage(String url, String width, String height) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        player = new FlatVideoPlayer(Plugin.MatrixSupport, url, false, height, width);
        player.showLogger(true);
        player.addPlayerStateHandler(new PlayerStateHandler() {

            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case Ready:
                        matrixCache = player.getMatrix();
                }
            }
        });

        FlexTable table = new FlexTable();
        table.setCellSpacing(5);
        table.setCellPadding(5);
        table.setWidth("100%");

        final _Option[] options = _Option.values();

        for (int i = 0; i < options.length; i++) {
            table.setText(i, 0, options[i].name());
            table.setWidget(i, 1, getListBox(options[i]));
        }

        int row = options.length;
        /*
        table.setWidget(row, 0, new Button("Apply Transforms", new ClickHandler() {

        public void onClick(ClickEvent event) {
        doTransforms(options);
        }
        }));
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        table.getFlexCellFormatter().setHorizontalAlignment(row++, 0, DockPanel.ALIGN_CENTER);
         */
        table.setWidget(row, 0, new Button("Clear Transforms", new ClickHandler() {

            public void onClick(ClickEvent event) {
                clearTransforms();
            }
        }));
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        table.getFlexCellFormatter().setHorizontalAlignment(row, 0, DockPanel.ALIGN_CENTER);

        DockPanel dp = new DockPanel();
        dp.setSize("100%", "100%");
        dp.add(table, DockPanel.EAST);
        dp.add(player, DockPanel.CENTER);
        initWidget(dp);
    }

    private ListBox getListBox(final _Option option) {
        final ListBox box = new ListBox();
        box.setName(option.name());
        option.setBox(box);
        box.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                doTransform(option,
                        Double.parseDouble(box.getValue(box.getSelectedIndex())));
            }
        });

        switch (option) {
            case Scale:
                box.addItem("0.5x", "0.5");
                box.addItem("1.0x", "1.0");
                box.addItem("1.5x", "1.5");
                box.addItem("2.0x", "2.0");
                box.setSelectedIndex(1);
                break;
            case Rotate:
                box.addItem("-90 deg", "-90");
                box.addItem("-45 deg", "-45");
                box.addItem("-30 deg", "-30");
                box.addItem("-15 deg", "-15");
                box.addItem("-10 deg", "-10");
                box.addItem("0 deg", "0");
                box.addItem("10 deg", "10");
                box.addItem("15 deg", "15");
                box.addItem("30 deg", "30");
                box.addItem("45 deg", "45");
                box.addItem("90 deg", "90");
                box.setSelectedIndex(5);
                break;
            case Skew:
                box.addItem("-10 deg", "-10");
                box.addItem("-5 deg", "-5");
                box.addItem("0 deg", "0");
                box.addItem("5 deg", "5");
                box.addItem("10 deg", "10");
//                box.addItem("15 deg", "15");
//                box.addItem("25 deg", "25");
//                box.addItem("30 deg", "30");
//                box.addItem("45 deg", "45");
                box.setSelectedIndex(2);
                break;
            case Translate:
                box.addItem("-50, -50", "-50");
                box.addItem("-30, -30", "-30");
                box.addItem("-20, -20", "-20");
                box.addItem("0, 0", "0");
                box.addItem("20, 20", "20");
                box.addItem("30, 30", "30");
                box.addItem("50, 50", "50");
                box.setSelectedIndex(3);
                break;
        }
        return box;
    }

    private void doTransform(_Option option, double value) {
        TransformationMatrix matrix = player.getMatrix();

        switch (option) {
            case Rotate:
                matrix.rotate(Math.toRadians(value));
                break;
            case Scale:
                matrix.scale(value, value);
                break;
            case Skew:
                matrix.skew(Math.toRadians(value),
                        Math.toRadians(value));
                break;
            case Translate:
                matrix.translate(value, value);
        }
        player.setMatrix(matrix);
    }

    private void clearTransforms() {
//        TransformationMatrix matrix = player.getMatrix();
//        matrix.toIdentity();
//        player.setMatrix(matrix);
        player.setMatrix(matrixCache);
    }

    private enum _Option {

        Scale, Translate, Rotate, Skew;
        private ListBox box;

        private _Option() {
        }

        public void setBox(ListBox box) {
            this.box = box;
        }

        public ListBox getBox() {
            return box;
        }
    }
}
