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

import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.HasMediaMessageHandlers;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.bramosystems.oss.player.resources.sources.ResourceBundle;
import com.bramosystems.oss.player.showcase.client.event.PlayerOptionsChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlayerOptionsChangeHandler;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeHandler;
import com.bramosystems.oss.player.showcase.client.event.PluginChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PluginChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.resources.client.ResourceCallback;
import com.google.gwt.resources.client.ResourceException;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 *
 * @author Sikiru
 */
public class StageController extends Composite implements ValueChangeHandler<String>, PluginChangeHandler,
        PlayerOptionsChangeHandler, PlaylistChangeHandler, HasMediaMessageHandlers {

    private final String LOG_SEPARATOR =  "------------------------------";
    private ArrayList<MRL> playlist;
    private PlayerOptions playerOptions;
    private AbstractMediaPlayer player;
    private BrowserInfo info;
    private HTML docPane;
    private AppOptions loadedOption;
    private Plugin plugin;
    private MatrixStagePane matrix;

    public StageController() {
        initWidget(sb.createAndBindUi(this));
        info = new BrowserInfo();
        docPane = new HTML();
        playlist = new ArrayList<MRL>();
        loadedOption = AppOptions.home;
    }

    @Override
    public HandlerRegistration addDebugHandler(DebugHandler handler) {
        return addHandler(handler, DebugEvent.TYPE);
    }

    @Override
    public HandlerRegistration addMediaInfoHandler(MediaInfoHandler handler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onPluginChanged(PluginChangeEvent pevt) {
        plugin = pevt.getPlugin();
        switch (loadedOption) {
            case core: // load player again ...
            case capsule:
            case flat:
                loadPlayer(loadedOption);
                break;
            case matrix:
                loadMatrix();
        }
    }

    @Override
    public void onPlayerOptionsChanged(PlayerOptionsChangeEvent pevt) {
        playerOptions = pevt.getPlayerOptions();
        switch (loadedOption) {
            case core:
            case capsule:
            case flat:
                player.setControllerVisible(playerOptions.isShowControls());
                player.setResizeToVideoSize(playerOptions.isResizeToVideo());
                player.showLogger(playerOptions.isShowLogger());
        }
    }

    @Override
    public void onPlaylistChanged(PlaylistChangeEvent pevt) {
        if (pevt.isAdded()) {
            playlist.add(pevt.getPlaylistItem());
            switch (loadedOption) {
                case core: // load player again ...
                case capsule:
                case flat:
                    if (player instanceof PlaylistSupport) {
                        ((PlaylistSupport) player).addToPlaylist(pevt.getPlaylistItem().get(0));
                    }
            }
        } else {
            playlist.remove(pevt.getPlaylistItem());
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        loadedOption = AppOptions.home;
        try {
            loadedOption = AppOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }

        switch (loadedOption) {
            case home:
                loadDoc(ResourceBundle.bundle.home());
                break;
            case plugins:
            case pool:
                loadInfo(loadedOption);
                break;
            case core:
            case capsule:
            case flat:
                loadPlayer(loadedOption);
                break;
            case matrix:
                loadMatrix();
        }
        title.setText(loadedOption.toString());
    }

    private void loadPlayer(AppOptions options) {
        try {
            panel.setWidget(null);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, "------------------------------");
            switch (options) {
                case capsule:
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading Capsule with : " + plugin);
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "------------------------------");
                    player = new Capsule(plugin, playlist.get(0).get(0), false);
                    player.setWidth("70%");
                    break;
                case core:
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading Core player : " + plugin);
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "------------------------------");
                    player = PlayerUtil.getPlayer(plugin, playlist.get(0).get(0), false, "250px", "70%");
                    break;
                case flat:
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading FlatVideoPlayer : " + plugin);
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "------------------------------");
                    player = new FlatVideoPlayer(plugin, playlist.get(0).get(0), false, "250px", "70%");
            }
            player.setControllerVisible(playerOptions.isShowControls());
            player.setResizeToVideoSize(playerOptions.isResizeToVideo());
            player.showLogger(playerOptions.isShowLogger());
            player.addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    fireEvent(event);
                }
            });

            if (playlist.size() > 1) {
                for (int i = 1; i < playlist.size(); i++) {
                    switch (plugin) {
                        case Native:
                            ((NativePlayer) player).addToPlaylist(playlist.get(i).toArray(new String[0]));
                            break;
                        default:
                            ((PlaylistSupport) player).addToPlaylist(playlist.get(i).get(0));
                    }
                }
            }

            panel.setWidget(player);
        } catch (LoadException ex) {
        } catch (PluginVersionException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
        } catch (PluginNotFoundException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        }
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

    private void loadInfo(AppOptions options) {
        panel.setWidget(info);
        info.update(options);
    }

    private void loadMatrix() {
        try {
            panel.setWidget(null);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading MatrixSupport : " + plugin);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
            matrix = new MatrixStagePane(plugin, "90%", "250px");
            matrix.addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    fireEvent(event);
                }
            });
            panel.setWidget(matrix);
        } catch (LoadException ex) {
        } catch (PluginVersionException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
        } catch (PluginNotFoundException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        }
    }
    
    @UiField Label title;
    @UiField SimplePanel panel;

    StageBinder sb = GWT.create(StageBinder.class);
    @UiTemplate("xml/Stage.ui.xml")
    interface StageBinder extends UiBinder<Widget, StageController>{}
}
