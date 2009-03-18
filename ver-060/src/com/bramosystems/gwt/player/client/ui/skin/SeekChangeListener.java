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

package com.bramosystems.gwt.player.client.ui.skin;

import java.util.EventListener;

/**
 * Implement this interface to receive seek change updates from a {@code MediaSeekBar}
 * implementation.
 *
 * @see MediaSeekBar
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public interface SeekChangeListener extends EventListener {

    /**
     * Fired when the state of a {@code MediaSeekBar} implementation is changed.
     *
     * @param newValue between {@code 0} (start of media) to {@code 1} (end
     * of media).
     */
    public void onSeekChanged(double newValue);
}
