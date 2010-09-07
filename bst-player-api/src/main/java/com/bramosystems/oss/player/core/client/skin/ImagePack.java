package com.bramosystems.oss.player.core.client.skin;

import com.google.gwt.resources.client.ImageResource;

/**
 * ImageBundle definition for the CustomPlayerControl class.
 * <p>
 * extend this class and {@link com.google.gwt.resources.client.ClientBundle} to
 * provide custom ImagePlayerControls.
 * <p>
 * Please note: when you extend this class, you need to copy all the defined
 * methods into your interface again, for the GWT compiler to pick up the
 * images.
 */
public interface ImagePack {
    
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
