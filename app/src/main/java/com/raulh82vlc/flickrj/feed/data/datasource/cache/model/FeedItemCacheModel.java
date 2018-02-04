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

package com.raulh82vlc.flickrj.feed.data.datasource.cache.model;

import java.util.List;

/**
 * FeedItem for the Cache layer, which is the data common model
 * @author Raul Hernandez Lopez.
 */

public class FeedItemCacheModel {

    private String title;

    private String link;
    private FeedItemCacheModel.MediaItem media;
    private String description;
    private String dateTaken;
    private String published;
    private String author;
    private String authorId;
    private List<String> tags;

    public FeedItemCacheModel(String title, String link, FeedItemCacheModel.MediaItem media,
                              String description, String dateTaken, String published, String author,
                              String authorId, List<String> tags) {
        this.title = title;
        this.link = link;
        this.media = media;
        this.description = description;
        this.dateTaken = dateTaken;
        this.published = published;
        this.author = author;
        this.authorId = authorId;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public FeedItemCacheModel.MediaItem getMedia() {
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

    public List<String> getTags() {
        return tags;
    }

    public static class MediaItem {

        public MediaItem(String url) {
            this.url = url;
        }

        private String url;

        public String getUrl() {
            return url;
        }
    }
}
