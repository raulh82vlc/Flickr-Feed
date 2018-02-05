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

package com.raulh82vlc.flickrj.feed.data.repository.mapper;

import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.data.datasource.network.model.FeedItemApiModel;
import com.raulh82vlc.flickrj.common.mapping.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper from network to cache
 * @author Raul Hernandez Lopez.
 */

public class NetworkToCacheMapper implements Mapper<List<FeedItemApiModel>, List<FeedItemCacheModel>> {

    private static final String REGEX_WHITESPACE = "\\s+";

    @Inject
    public NetworkToCacheMapper() {

    }

    @Override
    public List<FeedItemCacheModel> map(List<FeedItemApiModel> apiInput) {
        List<FeedItemCacheModel> cacheOutput = new ArrayList<>();
        for (FeedItemApiModel apiItem : apiInput) {
            cacheOutput.add(
                new FeedItemCacheModel(
                    apiItem.getTitle(),
                    apiItem.getLink(),
                    new FeedItemCacheModel.MediaItem(apiItem.getMedia().getUrl()),
                    apiItem.getDescription(),
                    apiItem.getDateTaken(),
                    apiItem.getPublished(),
                    apiItem.getAuthor(),
                    apiItem.getAuthorId(),
                    mapTags(apiItem.getTags()))
                );
        }
        return cacheOutput;
    }

    public List<String> mapTags(String tags) {
        String[] strings = tags.split(REGEX_WHITESPACE);
        // this avoids empty space passed as a value
        if (strings.length == 1 && strings[0].isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(strings);
    }
}
