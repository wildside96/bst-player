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

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.bramosystems.oss.player.core.client.ui.CoreConfigParameter;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer.Scale;
import com.bramosystems.oss.player.core.event.client.*;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeHandler;
import com.bramosystems.oss.player.showcase.client.res.Bundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.resources.client.ResourceCallback;
import com.google.gwt.resources.client.ResourceException;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 *
 * @author Sikiru
 */
public class StageController extends Composite implements ValueChangeHandler<String> { // PluginChangeHandler, PlayerOptionsChangeHandler

    private final String LOG_SEPARATOR = "---------------------------------", PLAYER_WIDTH = "100%", PLAYER_HEIGHT = "300px";
    private BrowserInfo info;
    private HTML docPane;
    private AppOptions loadedOption;
    PlayerStage playerStage;

    @SuppressWarnings("LeakingThisInConstructor")
    public StageController() {
        initWidget(sb.createAndBindUi(this));
        info = new BrowserInfo();
        docPane = new HTML();
        loadedOption = AppOptions.home;
        playerStage = new PlayerStage();
    }

    /*
     @Override
     public void onPlaylistChanged(PlaylistChangeEvent pevt) {
     switch (loadedOption) {
     case widget:
     if (player != null) {
     if (pevt.isAdded()) {
     if (player instanceof PlaylistSupport) {
     ((PlaylistSupport) player).addToPlaylist(pevt.getPlaylistItem());
     }
     } else {
     if (player instanceof PlaylistSupport) {
     ((PlaylistSupport) player).removeFromPlaylist(pevt.getIndex());
     }
     }
     }
     }
     }
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        loadedOption = AppOptions.home;
        try {
            loadedOption = AppOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }

        switch (loadedOption) {
            case home:
                loadDoc(Bundle.bundle.home());
                break;
            case notices:
                loadDoc(Bundle.bundle.notices());
                break;
            case plugins:
            case mimes:
                loadInfo();
                break;
            case widget:
                playerStage.doPlayerWidget(true);
                panel.setWidget(playerStage);
                break;
            case matrix:
                playerStage.doPlayerWidget(false);
                panel.setWidget(playerStage);
        }
        title.setText(loadedOption.toString());
    }

    private void loadDoc(ExternalTextResource res) {
        panel.setWidget(docPane);
        try {
            res.getText(new ResourceCallback<TextResource>() {
                @Override
                public void onError(ResourceException e) {
                    docPane.setHTML("<h2>Resource loading failed!</h2><br/>"
                            + e.getMessage());
                }

                @Override
                public void onSuccess(TextResource resource) {
                    docPane.setHTML(resource.getText());
                }
            });
        } catch (ResourceException ex) {
            docPane.setHTML("<h2>Resource loading failed!</h2><br/>"
                    + ex.getMessage());
        }
    }

    private void loadInfo() {
        panel.setWidget(info);
        info.update(loadedOption);
    }

    @UiTemplate("xml/Stage.ui.xml")
    interface StageBinder extends UiBinder<Widget, StageController> {
    }

    @UiTemplate("xml/PlayerStage.ui.xml")
    interface PlayerStageBinder extends UiBinder<Widget, PlayerStage> {
    }

    private enum _Option {

        Scale, Translate, Rotate, Skew;
    }
    StageBinder sb = GWT.create(StageBinder.class);
    PlayerStageBinder psb = GWT.create(PlayerStageBinder.class);
    @UiField SimplePanel panel;
    @UiField Label title;

    class PlayerStage extends Composite implements HasMediaMessageHandlers, HasValueChangeHandlers<String> {

        private AbstractMediaPlayer player;
        private TransformationMatrix matrixCache;

        public PlayerStage() {
            initWidget(psb.createAndBindUi(this));
            controls.setValue(true, false);
            repeatOff.setValue(true, false);
            fillXformBoxes(_Option.Rotate);
            fillXformBoxes(_Option.Scale);
            fillXformBoxes(_Option.Skew);
            fillXformBoxes(_Option.Translate);

            providers.addItem(" ");
            for (String nms : PlayerUtil.getPlayerProviderNames()) {
                providers.addItem(nms);
            }
            providers.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    String prov = providers.getValue(providers.getSelectedIndex());
                    players.clear();
                    if (!prov.isEmpty()) {
                        for (String pp : PlayerUtil.getPlayerNames(prov)) {
                            if (loadedOption.equals(AppOptions.matrix)) {
                                PlayerInfo pi = PlayerUtil.getPlayerInfo(prov, pp);
                                if (pi.isHasMatrixSupport()) {
                                    players.addItem(pp);
                                }
                            } else {
                                players.addItem(pp);
                            }
                        }
                        ValueChangeEvent.fire(PlayerStage.this, prov);
                    }
                }
            });
        }

        @Override
        public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
            return addHandler(handler, ValueChangeEvent.getType());
        }

        @Override
        public HandlerRegistration addDebugHandler(DebugHandler handler) {
            return addHandler(handler, DebugEvent.TYPE);
        }

        @Override
        public HandlerRegistration addMediaInfoHandler(MediaInfoHandler handler) {
            throw new UnsupportedOperationException();
        }

        void doPlayerWidget(boolean pw) {
            providers.setItemSelected(0, true);
            players.clear();
            xforms.setVisible(!pw);
            _panel.setWidget(null);
            controlPanel.setWidget(null);
        }

        @UiHandler("clearForms")
        void onClearTransforms(ClickEvent event) {
            if (player != null && player instanceof MatrixSupport) {
                ((MatrixSupport) player).setMatrix(matrixCache);
            }
            resetXformBoxes();
        }

        private void resetXformBoxes() {
            ListBox box = null;
            for (_Option option : _Option.values()) {
                switch (option) {
                    case Scale:
                        box = scale;
                        box.setSelectedIndex(1);
                        break;
                    case Rotate:
                        box = rotate;
                        box.setSelectedIndex(5);
                        break;
                    case Skew:
                        box = skew;
                        box.setSelectedIndex(2);
                        break;
                    case Translate:
                        box = xlate;
                        box.setSelectedIndex(3);
                        break;
                }
            }
        }

        private void fillXformBoxes(final _Option option) {
            ListBox box = null;

            switch (option) {
                case Scale:
                    box = scale;
                    box.addItem("0.5x", "0.5");
                    box.addItem("1.0x", "1.0");
                    box.addItem("1.5x", "1.5");
                    box.addItem("2.0x", "2.0");
                    box.setSelectedIndex(1);
                    break;
                case Rotate:
                    box = rotate;
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
                    box = skew;
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
                    box = xlate;
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
            box.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    ListBox b = (ListBox) event.getSource();
                    doTransform(option, Double.parseDouble(b.getValue(b.getSelectedIndex())));
                }
            });
        }

        private void doTransform(_Option option, double value) {
            if (player != null && player instanceof MatrixSupport) {
                TransformationMatrix matrix = ((MatrixSupport) player).getMatrix();

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
                ((MatrixSupport) player).setMatrix(matrix);
            }
        }

        @UiHandler("loadButton")
        void loadPlayer(ClickEvent ce) {
            try {
                _panel.setWidget(null);
                DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);

                PlayerInfo pi = PlayerUtil.getPlayerInfo(providers.getValue(providers.getSelectedIndex()),
                        players.getValue(players.getSelectedIndex()));

                ArrayList<MRL> playlist = PlaylistPane.singleton.getEntries();
                DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading player : " + pi.getPlayerName());
                DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
                player = PlayerUtil.getPlayer(pi, playlist.get(0).getNextResource(true), autoplay.getValue(), PLAYER_HEIGHT, PLAYER_WIDTH);
                //                   player.setWidth(PLAYER_WIDTH);
                player.setControllerVisible(controls.getValue());
                player.setResizeToVideoSize(resizeToVideo.getValue());
                if (player instanceof PlaylistSupport) {
                    ((PlaylistSupport) player).setShuffleEnabled(shuffle.getValue());
                }

//            player.setConfigParameter(DefaultConfigParameter.BackgroundColor, "#ffffff");
                player.setConfigParameter(CoreConfigParameter.QTScale, QuickTimePlayer.Scale.ToFit);
//            player.setRepeatMode(playerOptions.getRepeatMode());

                player.addDebugHandler(new DebugHandler() {
                    @Override
                    public void onDebug(DebugEvent event) {
                        fireEvent(event);
                    }
                });
                player.addMediaInfoHandler(new MediaInfoHandler() {
                    @Override
                    public void onMediaInfoAvailable(MediaInfoEvent event) {
                        if (player instanceof MatrixSupport) {
                            matrixCache = ((MatrixSupport) player).getMatrix();
                        }
                    }
                });

                if ((playlist.size() > 1) && (player instanceof PlaylistSupport)) {
                    for (int i = 1; i < playlist.size(); i++) {
                        ((PlaylistSupport) player).addToPlaylist(playlist.get(i));
                    }
                }

                title.setText(loadedOption.toString() + " - " + pi.toString());
                _panel.setWidget(player);

                if (custom.getValue()) {
                    CustomPlayerControl cc = new CustomPlayerControl(player);
                    controlPanel.setWidget(cc);
                }
                controlPanel.setVisible(custom.getValue());
            } catch (PluginVersionException ex) {
                _panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
            } catch (PluginNotFoundException ex) {
                _panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
            }
        }

        @UiHandler("controls")
        void onControlsChange(ValueChangeEvent<Boolean> event) {
            if (player != null) {
                player.setControllerVisible(event.getValue());
            }
        }

        @UiHandler("resizeToVideo")
        void onResizeChange(ValueChangeEvent<Boolean> event) {
            if (player != null) {
                player.setResizeToVideoSize(event.getValue());
            }
        }

        @UiHandler("shuffle")
        void onShuffle(ValueChangeEvent<Boolean> event) {
            if (player != null && player instanceof PlaylistSupport) {
                ((PlaylistSupport) player).setShuffleEnabled(event.getValue());
            }
        }

        @UiHandler("repeatOff")
        void onRepeatOff(ValueChangeEvent<Boolean> event) {
            if (player != null) {
                player.setRepeatMode(RepeatMode.REPEAT_OFF);
            }
        }

        @UiHandler("repeatOne")
        void onRepeatOne(ValueChangeEvent<Boolean> event) {
            if (player != null) {
                player.setRepeatMode(RepeatMode.REPEAT_ONE);
            }
        }

        @UiHandler("repeatAll")
        void onRepeatAll(ValueChangeEvent<Boolean> event) {
            if (player != null) {
                player.setRepeatMode(RepeatMode.REPEAT_ALL);
            }
        }
        @UiField CheckBox controls, resizeToVideo, shuffle, autoplay, custom;
        @UiField RadioButton repeatOff, repeatOne, repeatAll;
        @UiField ListBox providers, players, xlate, rotate, skew, scale;
        @UiField Button loadButton, clearForms;
        @UiField Widget control, xforms;
        @UiField SimplePanel _panel, controlPanel;
    }
}
