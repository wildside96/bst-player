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
package com.bramosystems.gwt.player.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 * Wraps the <code>major, minor</code> and <code>revision</code> numbers of a browser plugin component.
 *
 * @author Sikirulai Braheem
 */
public class PluginVersion implements IsSerializable, Serializable, Comparable<PluginVersion> {

    private int minor,  major,  revision;

    /**
     * Creates a <code>PluginVersion</code>
     */
    public PluginVersion() {
        major = -1;
        minor = -1;
        revision = -1;
    }

    /**
     * Creates a <code>PluginVersion</code> with the specified minor, major and revision numbers.
     * @param major major version number
     * @param minor minor version number
     * @param revision revision number
     */
    public PluginVersion(int major, int minor, int revision) {
        this.minor = minor;
        this.major = major;
        this.revision = revision;
    }

    /**
     * Static method to create a <code>PluginVersion</code> with the specified minor,
     * major and revision numbers.
     *
     * @param major major version number
     * @param minor minor version number
     * @param revision revision number
     *
     * @return <code>PluginVersion</code> object with specified version numbers.
     */
    public static PluginVersion get(int major, int minor, int revision) {
        return new PluginVersion(major, minor, revision);
    }

    /**
     * Gets minor version number
     * @return minor version number
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Sets minor version number
     * @param minor
     */
    public void setMinor(int minor) {
        this.minor = minor;
    }

    /**
     * Gets major version number.
     * @return major version number
     */
    public int getMajor() {
        return major;
    }

    /**
     * Sets the major version number.
     * @param major
     */
    public void setMajor(int major) {
        this.major = major;
    }

    /**
     * Gets the revsion number
     * @return revision number
     */
    public int getRevision() {
        return revision;
    }

    /**
     * Sets the revision number of this PluginVersion object
     * @param revision
     */
    public void setRevision(int revision) {
        this.revision = revision;
    }

    /**
     * Indicates if some other object is equal to this one.  Two <code>PluginVersion</code>'s
     * are equal if their major, minor and revision numbers are equal.
     *
     * @param obj the other <code>PluginVersion</code> object
     * @return <code>true</code> if and only if <code>obj</code> is same as this object,
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PluginVersion) {
            PluginVersion other = (PluginVersion) obj;
            return (major == other.getMajor()) &&
                    (minor == other.getMinor()) &&
                    (revision == other.getRevision());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Compares this PluginVersion with the specified PluginVersion.
     * Two PluginVersions are the same if they have the same major, minor and revision numbers.
     *
     * @param o the PluginVersion to compare with this one.
     *
     * @return {@code 0} if this PluginVersion is the same as the specified PluginVersion,
     * {@code -1} if this PluginVersion is less than the specified PluginVersion and {@code 1}
     * if this PluginVersion is greater than the specified PluginVersion.
     *
     * @see #compareTo(int, int, int)
     */
    public int compareTo(PluginVersion o) {
        int val = Integer.valueOf(major).compareTo(o.getMajor());
        if(val == 0) {// compare minor numbers...
            val = Integer.valueOf(minor).compareTo(o.getMinor());
            if(val == 0) {
                // same minor, compare revision numbers...
                val = Integer.valueOf(revision).compareTo(o.getRevision());
            }
        }
        return val;
    }

    /**
     * Convinience method to compare this PluginVersion with a PluginVersion with the
     * specified major, minor and revision numbers.
     *
     * @param major major version number
     * @param minor minor version number
     * @param revision revision number
     *
     * @return {@code 0} if this PluginVersion is the same as the specified PluginVersion,
     * {@code -1} if this PluginVersion is less than the specified PluginVersion and {@code 1}
     * if this PluginVersion is greater than the specified PluginVersion.
     *
     * @see #compareTo(PluginVersion o)
     */
    public int compareTo(int major, int minor, int revision) {
        return compareTo(new PluginVersion(minor, major, revision));
    }

    /**
     * Returns a String representation of this PluginVersion in the format
     * <code>major.minor.revision</code>.
     * 
     * @return PluginVersion as String format.
     */
    @Override
    public String toString() {
        return "" + major + "." + minor + "." + revision;
    }

}
