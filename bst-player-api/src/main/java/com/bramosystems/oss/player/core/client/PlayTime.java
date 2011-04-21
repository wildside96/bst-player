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
package com.bramosystems.oss.player.core.client;

import com.bramosystems.oss.player.util.client.RegExp;
import com.bramosystems.oss.player.util.client.RegExp.RegexException;

/**
 *
 * @author Administrator
 */
public final class PlayTime implements Comparable<PlayTime> {

    private static RegExp regexp;
    private double ms;

    public PlayTime() {
    }

    public PlayTime(int hour, int minute, int second, int fract) {
        ms = fract + (second * 1000) + (minute * 60000) + (hour * 3600000);
    }

    public PlayTime(double milliseconds) {
        ms = milliseconds;
    }

    public PlayTime(String time) {
        try {
            regexp = RegExp.getRegExp("((\\d+):)?(\\d\\d):(\\d\\d)(\\.(\\d+))?", "");
            RegExp.RegexResult m = regexp.exec(time);
            for (int i = 1; i <= 6; i++) {
                String val = m.getMatch(i);
                if (val != null) {
                    switch (i) {
                        case 2:
                            setHour(Integer.parseInt(val));
                            break;
                        case 3:
                            setMinute(Integer.parseInt(val));
                            break;
                        case 4:
                            setSecond(Integer.parseInt(val));
                            break;
                        case 6:
                            setFract(Integer.parseInt(val));
                            break;
                    }
                }
            }
        } catch (RegexException ex) {
        }
    }

    public PlayTime add(int milliseconds) {
        ms += milliseconds;
        return this;
    }

    public PlayTime reduce(int milliseconds) {
        ms -= milliseconds;
        return this;
    }

    public double getTime() {
        return ms;
    }

    public int getHour() {
        return (int) ((ms / 3600000) % 60);
    }

    public void setHour(int hour) {
        ms = getFract() + (getSecond() * 1000) + (getMinute() * 60000) + (hour * 3600000);
    }

    public int getMinute() {
        return (int) ((ms / 60000) % 60);
    }

    public void setMinute(int minute) {
        ms = getFract() + (getSecond() * 1000) + (minute * 60000) + (getHour() * 3600000);
    }

    public int getSecond() {
        return (int) ((ms / 1000) % 60);
    }

    public void setSecond(int second) {
        ms = getFract() + (second * 1000) + (getMinute() * 60000) + (getHour() * 3600000);
    }

    public int getFract() {
        return (int) (ms % 1000);
    }

    public void setFract(int fract) {
        ms = fract + (getSecond() * 1000) + (getMinute() * 60000) + (getHour() * 3600000);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean fullFormat) {
        int hr = getHour();
        if (fullFormat) {
            return pad(hr) + ":" + pad(getMinute()) + ":" + pad(getSecond()) + "." + getFract();
        } else {
            return (hr > 0 ? hr + ":" : "") + pad(getMinute()) + ":" + pad(getSecond());
        }
    }

    private String pad(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return String.valueOf(value);
    }

    @Override
    public int compareTo(PlayTime o) {
        return Double.compare(ms, o.ms);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayTime other = (PlayTime) obj;
        if (this.ms != other.ms) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 41 * 7 + (int) (this.ms * 32);
    }
}
