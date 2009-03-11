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
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
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

    public ArrayList<MediaItem> getAvailableItems() {
        ArrayList<MediaItem> items = new ArrayList<MediaItem>();
        if (!isEmpty(albumTitle)) {
            items.add(MediaItem.AlbumTitle);
        }
        if (!isEmpty(artists)) {
            items.add(MediaItem.Artists);
        }
        if (!isEmpty(contentProviders)) {
            items.add(MediaItem.ContentProviders);
        }
        if (!isEmpty(comment)) {
            items.add(MediaItem.Comment);
        }
        if (!isEmpty(genre)) {
            items.add(MediaItem.Genre);
        }
        if (!isEmpty(publisher)) {
            items.add(MediaItem.Publisher);
        }
        if (!isEmpty(internetStationName)) {
            items.add(MediaItem.StationName);
        }
        if (!isEmpty(internetStationOwner)) {
            items.add(MediaItem.StationOwner);
        }
        if (!isEmpty(title)) {
            items.add(MediaItem.Title);
        }
        if (!isEmpty(year)) {
            items.add(MediaItem.Year);
        }
        if (!isEmpty(copyright)) {
            items.add(MediaItem.Copyright);
        }
        if (!isEmpty(hardwareSoftwareRequirements)) {
            items.add(MediaItem.HardwareSoftwareRequirements);
        }
        if (duration > 0) {
            items.add(MediaItem.Duration);
        }

        Collections.sort(items);
        return items;
    }

    public String getItem(MediaItem item) {
        String value = "";
        switch (item) {
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

    public enum MediaItem {

        Title("Title"), Artists("Artists"), AlbumTitle("Album Title"), Genre("Genre"),
        Year("Year"), Publisher("Publisher"),
        ContentProviders("Content Providers"), StationName("Station Name"),
        StationOwner("Station Owner"), Comment("Comment"),
        Duration("Duration"), HardwareSoftwareRequirements("Hardware/Software Requirements"),
        Copyright("Copyright");
        private String itemName;

        MediaItem(String itemName) {
            this.itemName = itemName;
        }

        public String getItemName() {
            return itemName;
        }
    }
}
