/*
 * Copyright (C) 2018 Raul Hernandez Lopez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.raulh82vlc.flickrj.feed.data.datasource.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Raul Hernandez Lopez.
 */

public class FeedItemApiModel {
    @SerializedName("title")
    private String title;
    @SerializedName("link")
    private String link;
    @SerializedName("media")
    private MediaItem media;
    @SerializedName("description")
    private String description;
    @SerializedName("date_taken")
    private String dateTaken;
    @SerializedName("published")
    private String published;
    @SerializedName("author")
    private String author;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("tags")
    private String tags;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public MediaItem getMedia() {
        return media;
    }

    public String getDescription() {
        return description;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public String getPublished() {
        return published;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getTags() {
        return tags;
    }

    public static class MediaItem {
        @SerializedName("m")
        private String url;

        public String getUrl() {
            return url;
        }
    }
}
