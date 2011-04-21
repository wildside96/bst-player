/*
 * Copyright 2011 Administrator.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bramosystems.oss.player.dev.client.playlist.impl.asx;

import com.bramosystems.oss.player.core.client.PlayTime;

/**
 *
 * @author Administrator
 */
public class Ref {
    private String href;
    private PlayTime duration, startTime;

    public Ref() {
    }

    public PlayTime getDuration() {
        return duration;
    }

    public void setDuration(PlayTime duration) {
        this.duration = duration;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public PlayTime getStartTime() {
        return startTime;
    }

    public void setStartTime(PlayTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Ref{" + "href=" + href + ", duration=" + duration + ", startTime=" + startTime + '}';
    }

}
