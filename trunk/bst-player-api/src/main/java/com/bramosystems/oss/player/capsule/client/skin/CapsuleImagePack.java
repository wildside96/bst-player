package com.bramosystems.oss.player.capsule.client.skin;

import com.bramosystems.oss.player.core.client.skin.ImagePack;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Simple Image Pack with flat blue images.
 */
public interface CapsuleImagePack extends ImagePack, ClientBundle {
    public ImageResource pause();

    public ImageResource pauseHover();

    public ImageResource play();

    public ImageResource playHover();

    public ImageResource playDisabled();

    public ImageResource stop();

    public ImageResource stopDisabled();

    public ImageResource stopHover();

    public ImageResource spk();

    public ImageResource prev();

    public ImageResource prevDisabled();

    public ImageResource prevHover();

    public ImageResource next();

    public ImageResource nextDisabled();

    public ImageResource nextHover();

    public ImageResource lEdge();

    public ImageResource rEdge();
}
