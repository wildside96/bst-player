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
package com.bramosystems.gwt.player.client.ui.images.capsule;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * ImageBundle definition for the bundled Capsule skin player.
 *
 * @author Sikirulai Braheem
 */
public interface CapsuleImages extends ImageBundle {

    public AbstractImagePrototype lEdge();

    public AbstractImagePrototype rEdge();

    public AbstractImagePrototype pause();

    public AbstractImagePrototype pauseDisabled();

    public AbstractImagePrototype pauseHover();

    public AbstractImagePrototype play();

    public AbstractImagePrototype playDisabled();

    public AbstractImagePrototype playHover();

    public AbstractImagePrototype stop();

    public AbstractImagePrototype stopDisabled();

    public AbstractImagePrototype stopHover();

    public AbstractImagePrototype spk();
}
