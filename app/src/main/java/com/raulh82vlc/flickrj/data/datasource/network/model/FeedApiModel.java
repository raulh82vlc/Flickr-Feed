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

package com.raulh82vlc.flickrj.data.datasource.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Feed Api Model coming from backend
 * @author Raul Hernandez Lopez.
 */
public class FeedApiModel {
    @SerializedName("stat")
    private String statusOfCall;
    @SerializedName("title")
    private String title;
    @SerializedName("link")
    private String link;
    @SerializedName("description")
    private String description;
    @SerializedName("modified")
    private String modified;
    @SerializedName("generator")
    private String generator;
    @SerializedName("items")
    private List<FeedItemApiModel> feedItems;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getModified() {
        return modified;
    }

    public String getGenerator() {
        return generator;
    }

    public List<FeedItemApiModel> getFeedItems() {
        return feedItems;
    }

    public String getStatusOfCall() {
        return statusOfCall == null || statusOfCall.isEmpty() ? "ok" : statusOfCall;
    }
}
