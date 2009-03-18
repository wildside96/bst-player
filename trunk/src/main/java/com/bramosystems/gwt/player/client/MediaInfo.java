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

import java.util.ArrayList;
import java.util.Collections;

/**
 * Wraps media metadata information for a media file (such as ID3 tag entries
 * for MP3 files)
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 * @since 0.6
 */
public class MediaInfo {

    private String title,  albumTitle,  artists,  year,  genre,  comment;
    private String internetStationName,  internetStationOwner,  publisher;
    private String hardwareSoftwareRequirements,  copyright,  contentProviders;
    private double duration;

    @Override
    public String toString() {
        return "Title : " + title +
                ", Artists : " + artists +
                ", AlbumTitle : " + albumTitle +
                ", Genre : " + genre +
                ", Year : " + year +
                ", Duration : " + duration +
                ", Publisher : " + publisher +
                ", Content Providers : " + contentProviders +
                ", Hardware/Software Requirements : " + hardwareSoftwareRequirements +
                ", Station Name : " + internetStationName +
                ", Station Owner : " + internetStationOwner +
                ", Comment : " + comment;
    }

    /**
     * Returns an HTML table containing all {@code MediaInfoKey}s and their
     * values for this object.  Useful during debugging operations.
     *
     * @return all {@code MediaInfoKey}s values as an HTML table.
     */
    public String asHTMLString() {
        return "========== Media Info Details ========" +
                "<style>.info_odd{background-color:#ccc}</style>" +
                "<table border='0' align='center' cellspacing='0' cellpadding='3'><tbody>" +
                "<tr class='info_odd'><td>Title</td><td>" + title + "</td></tr>" +
                "<tr><td>Artists</td><td>" + artists + "</td></tr>" +
                "<tr class='info_odd'><td>AlbumTitle</td><td>" + albumTitle + "</td></tr>" +
                "<tr><td>Genre</td><td>" + genre + "</td></tr>" +
                "<tr class='info_odd'><td>Year</td><td>" + year + "</td></tr>" +
                "<tr><td>Duration</td><td>" + PlayerUtil.formatMediaTime((long) duration) + "</td></tr>" +
                "<tr class='info_odd'><td>Publisher</td><td>" + publisher + "</td></tr>" +
                "<tr><td>Content Providers</td><td>" + contentProviders + "</td></tr>" +
                "<tr class='info_odd'><td>Hardware/Software Requirements</td><td>" + hardwareSoftwareRequirements + "</td></tr>" +
                "<tr><td>Station Name</td><td>" + internetStationName + "</td></tr>" +
                "<tr class='info_odd'><td>Station Owner</td><td>" + internetStationOwner + "</td></tr>" +
                "<tr><td>Comment</td><td>" + comment + "</td></tr>" +
                "</tbody></table>";
    }

    private boolean isEmpty(String value) {
        return (value == null) || (value.length() == 0);
    }

    /**
     * Returns a list of {@link MediaInfoKey}s that have metadata entries in a media
     * file.
     *
     * @return list containing the keys of available media metadata.
     */
    public ArrayList<MediaInfoKey> getAvailableItems() {
        ArrayList<MediaInfoKey> items = new ArrayList<MediaInfoKey>();
        if (!isEmpty(albumTitle)) {
            items.add(MediaInfoKey.AlbumTitle);
        }
        if (!isEmpty(artists)) {
            items.add(MediaInfoKey.Artists);
        }
        if (!isEmpty(contentProviders)) {
            items.add(MediaInfoKey.ContentProviders);
        }
        if (!isEmpty(comment)) {
            items.add(MediaInfoKey.Comment);
        }
        if (!isEmpty(genre)) {
            items.add(MediaInfoKey.Genre);
        }
        if (!isEmpty(publisher)) {
            items.add(MediaInfoKey.Publisher);
        }
        if (!isEmpty(internetStationName)) {
            items.add(MediaInfoKey.StationName);
        }
        if (!isEmpty(internetStationOwner)) {
            items.add(MediaInfoKey.StationOwner);
        }
        if (!isEmpty(title)) {
            items.add(MediaInfoKey.Title);
        }
        if (!isEmpty(year)) {
            items.add(MediaInfoKey.Year);
        }
        if (!isEmpty(copyright)) {
            items.add(MediaInfoKey.Copyright);
        }
        if (!isEmpty(hardwareSoftwareRequirements)) {
            items.add(MediaInfoKey.HardwareSoftwareRequirements);
        }
        if (duration > 0) {
            items.add(MediaInfoKey.Duration);
        }

        Collections.sort(items);
        return items;
    }

    /**
     * Return the value associated with the specified {@code MediaInfoKey}.
     *
     * @param key the metadata key whose value is required.
     * @return the value associated with specified metadata key.
     */
    public String getItem(MediaInfoKey key) {
        String value = "";
        switch (key) {
            case AlbumTitle:
                value = albumTitle;
                break;
            case Artists:
                value = artists;
                break;
            case Comment:
                value = comment;
                break;
            case ContentProviders:
                value = contentProviders;
                break;
            case Copyright:
                value = copyright;
                break;
            case Duration:
                value = String.valueOf(duration);
                break;
            case Genre:
                value = genre;
                break;
            case HardwareSoftwareRequirements:
                value = hardwareSoftwareRequirements;
                break;
            case Publisher:
                value = publisher;
                break;
            case StationName:
                value = internetStationName;
                break;
            case StationOwner:
                value = internetStationOwner;
                break;
            case Title:
                value = title;
                break;
            case Year:
                value = year;
                break;
        }
        return value;
    }

    /**
     * An enum of supported media metadata keys
     */
    public enum MediaInfoKey {

        Title("Title"),
        Artists("Artists"),
        AlbumTitle("Album Title"),
        Genre("Genre"),
        Year("Year"),
        Publisher("Publisher"),
        ContentProviders("Content Providers"),
        StationName("Station Name"),
        StationOwner("Station Owner"),
        Comment("Comment"),
        Duration("Duration"),
        HardwareSoftwareRequirements("Hardware/Software Requirements"),
        Copyright("Copyright");

        private String itemName;

        MediaInfoKey(String itemName) {
            this.itemName = itemName;
        }

        /**
         * Returns a "friendly" name for this metadata key
         *
         * @return "friendly" name for this key object
         */
        @Override
        public String toString() {
            return itemName;
        }
    }
}
