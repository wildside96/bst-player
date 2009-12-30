package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.bramosystems.oss.player.core.event.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

public abstract class BinderPlayer<T extends AbstractMediaPlayer> extends AbstractMediaPlayer implements MatrixSupport, com.bramosystems.oss.player.core.client.PlaylistSupport {

    private T _engine;
    private final String GWT_HOST_URL_ID = "GWT-HOST::", GWT_MODULE_URL_ID = "GWT-MODULE::";

    protected BinderPlayer(String mediaURL, boolean autoplay, String height, String width) {
        Widget player = null;
        try {
            if (mediaURL.contains(GWT_HOST_URL_ID)) {
                mediaURL = mediaURL.replaceAll(GWT_HOST_URL_ID, GWT.getHostPageBaseURL());
            }
            if (mediaURL.contains(GWT_MODULE_URL_ID)) {
                mediaURL = mediaURL.replaceAll(GWT_MODULE_URL_ID, GWT.getModuleBaseURL());
            }
            Plugin plug = getPlugin();
            switch (plug) {
                case Native:
                    if (mediaURL.contains(",")) {
                        String[] murls = mediaURL.split(",");
                        ArrayList<String> _urls = new ArrayList<String>();
                        for(String url : murls) {
                            _urls.add(url);
                        }
                        _engine = (T) new NativePlayer(_urls, autoplay, height, width);
                    } else {
                        _engine = (T) new NativePlayer(mediaURL, autoplay, height, width);
                    }
                    break;
                default:
                    _engine = (T) PlayerUtil.getPlayer(plug, mediaURL, autoplay, height, width);
            }
            _engine.addDebugHandler(new DebugHandler() {

                public void onDebug(DebugEvent event) {
                    fireEvent(event);
                }
            });
            _engine.addLoadingProgressHandler(new LoadingProgressHandler() {

                public void onLoadingProgress(LoadingProgressEvent event) {
                    fireEvent(event);
                }
            });
            _engine.addMediaInfoHandler(new MediaInfoHandler() {

                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    fireEvent(event);
                }
            });
            _engine.addPlayStateHandler(new PlayStateHandler() {

                public void onPlayStateChanged(PlayStateEvent event) {
                    fireEvent(event);
                }
            });
            _engine.addPlayerStateHandler(new PlayerStateHandler() {

                public void onPlayerStateChanged(PlayerStateEvent event) {
                    fireEvent(event);
                }
            });
            player = _engine;
        } catch (LoadException ex) {
        } catch (PluginNotFoundException ex) {
            player = PlayerUtil.getMissingPluginNotice(ex.getPlugin());
        } catch (PluginVersionException ex) {
            player = PlayerUtil.getMissingPluginNotice(ex.getPlugin());
        }
        initWidget(player);
    }

    public T getEngine() {
        return _engine;
    }

    protected abstract Plugin getPlugin();

    public long getMediaDuration() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getMediaDuration();
    }

    public double getPlayPosition() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getPlayPosition();
    }

    public void setPlayPosition(double position) {
        if (_engine == null) {
            return;
        }
        _engine.setPlayPosition(position);
    }

    public void loadMedia(String mediaURL) throws LoadException {
        if (_engine == null) {
            return;
        }
        _engine.loadMedia(mediaURL);
    }

    public void pauseMedia() {
        if (_engine == null) {
            return;
        }
        _engine.pauseMedia();
    }

    public void playMedia() throws PlayException {
        if (_engine == null) {
            return;
        }
        _engine.playMedia();
    }

    public void stopMedia() {
        if (_engine == null) {
            return;
        }
        _engine.stopMedia();
    }

    public double getVolume() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getVolume();
    }

    public void setVolume(double volume) {
        if (_engine == null) {
            return;
        }
        _engine.setVolume(volume);
    }

    /**
     * Returns the remaining number of times this player loops playback before stopping.
     */
    @Override
    public int getLoopCount() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getLoopCount();
    }

    /**
     * Sets the number of times the current media file should loop playback before stopping.
     */
    @Override
    public void setLoopCount(int loop) {
        if (_engine == null) {
            return;
        }
        _engine.setLoopCount(loop);
    }

    @Override
    public void showLogger(boolean show) {
        if (_engine == null) {
            return;
        }
        _engine.showLogger(show);
    }

    /**
     * Convenience method for UiBinder support. Displays or hides the players' logger widget.
     *
     * @param show <code>true</code> to make the logger widget visible, <code>false</code> otherwise.
     * @see #showLogger(boolean)
     */
    public void setShowLogger(boolean show) {
        if (_engine == null) {
            return;
        }
        _engine.showLogger(show);
    }

    @Override
    public int getVideoHeight() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getVideoHeight();
    }

    @Override
    public int getVideoWidth() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getVideoWidth();
    }

    @Override
    public boolean isControllerVisible() {
        if (_engine == null) {
            return false;
        }
        return _engine.isControllerVisible();
    }

    @Override
    public boolean isResizeToVideoSize() {
        if (_engine == null) {
            return false;
        }
        return _engine.isResizeToVideoSize();
    }

    @Override
    public <T extends ConfigValue> void setConfigParameter(ConfigParameter param, T value) {
        if (_engine == null) {
            return;
        }
        _engine.setConfigParameter(param, value);
    }

    @Override
    public void setControllerVisible(boolean show) {
        if (_engine == null) {
            return;
        }
        _engine.setControllerVisible(show);
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        if (_engine == null) {
            return;
        }
        _engine.setResizeToVideoSize(resize);
    }

    public void addToPlaylist(String mediaURL) {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).addToPlaylist(mediaURL);
        }
    }

    public boolean isShuffleEnabled() {
        if (_engine == null) {
            return false;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            return ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).isShuffleEnabled();
        }
        return false;
    }

    public void removeFromPlaylist(int index) {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).removeFromPlaylist(index);
        }
    }

    public void setShuffleEnabled(boolean enable) {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).setShuffleEnabled(enable);
        }
    }

    public void clearPlaylist() {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).clearPlaylist();
        }
    }

    public int getPlaylistSize() {
        if (_engine == null) {
            return 0;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            return ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).getPlaylistSize();
        }
        return 1;
    }

    public void play(int index) throws IndexOutOfBoundsException {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).play(index);
        }
    }

    public void playNext() throws PlayException {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).playNext();
        }
    }

    public void playPrevious() throws PlayException {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).playPrevious();
        }
    }

    public void setMatrix(TransformationMatrix matrix) {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof MatrixSupport) {
            ((MatrixSupport) _engine).setMatrix(matrix);
        }
    }

    public TransformationMatrix getMatrix() {
        if (_engine == null) {
            return null;
        }
        if (_engine instanceof MatrixSupport) {
            return ((MatrixSupport) _engine).getMatrix();
        }
        return null;
    }
}
