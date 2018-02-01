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

package com.raulh82vlc.flickrj.data.network.response;

import com.google.gson.Gson;
import com.raulh82vlc.flickrj.data.network.model.FeedApiModel;

import javax.inject.Inject;

/**
 * Response handler extracts JSON responses as well as validates right format of the responses
 * @author Raul Hernandez Lopez.
 */

public class ResponseHandler {

    private static final String JSON_FLICKR_FEED_KEYWORD = "jsonFlickrFeed\\(";
    private static final String JSON_FLICKR_FEED_LITERAL = "jsonFlickrFeed(";
    private static final String STAT_FAILURE = "fail";

    private final Gson gson;

    @Inject
    public ResponseHandler(Gson gson) {
        this.gson = gson;
    }

    public String extractJSONFromResponse(final String response) {
        String responseOutput = response.replaceFirst(JSON_FLICKR_FEED_KEYWORD, "");
        return responseOutput.substring(0, responseOutput.length() - 1);
    }

    public FeedApiModel deserializeFeedJSON(String responseJson) {
        return gson.fromJson(responseJson, FeedApiModel.class);
    }

    public boolean hasFeedFormat(String responseBodyResult) {
        return responseBodyResult.contains(JSON_FLICKR_FEED_LITERAL);
    }

    public boolean hasNoApiFailure(String jsonResponseResult) {
        return !jsonResponseResult.equals(STAT_FAILURE);
    }
}
