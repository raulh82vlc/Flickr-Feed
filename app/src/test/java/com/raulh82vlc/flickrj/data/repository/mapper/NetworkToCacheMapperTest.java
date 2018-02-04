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

package com.raulh82vlc.flickrj.data.repository.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raulh82vlc.flickrj.data.FileReaderHelper;
import com.raulh82vlc.flickrj.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.data.datasource.network.model.FeedItemApiModel;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Mapper validation from Network to Cache data sources at data layer
 * @author Raul Hernandez Lopez.
 */
public class NetworkToCacheMapperTest {
    private static final String RAW_FEED_GOOD_JSON = "raw/feed_well_formatted.json";
    private NetworkToCacheMapper networkToCacheMapper;

    @Before
    public void setUp() throws Exception {
        networkToCacheMapper = new NetworkToCacheMapper();
    }

    @Test
    public void map() throws Exception {
        String json = getMockFeedResponse();
        Type listType = new TypeToken<List<FeedItemApiModel>>() {
            }.getType();

        List<FeedItemApiModel> itemsFromAPI = new Gson().fromJson(json, listType);

        List<FeedItemCacheModel> listOfCachedItems = networkToCacheMapper.map(itemsFromAPI);

        assertEquals("Bookshelf", listOfCachedItems.get(0).getTitle());
        assertEquals("45737848@N07", listOfCachedItems.get(0).getAuthorId());
        assertEquals("ifttt", listOfCachedItems.get(0).getTags().get(0));
        assertEquals("Emperor @ Tons Of Rock 2017-54.jpg", listOfCachedItems.get(1).getTitle());
        assertEquals("126885790@N07", listOfCachedItems.get(1).getAuthorId());
        assertEquals("norway", listOfCachedItems.get(1).getTags().get(0));
    }

    @Test
    public void mapTags() throws Exception {
        List<String> tags = networkToCacheMapper.mapTags("Brazil Spain Mexico");
        assertEquals("Brazil", tags.get(0));
        assertEquals("Spain", tags.get(1));
        assertEquals("Mexico", tags.get(2));
    }

    private String getMockFeedResponse() {
        try {
            return FileReaderHelper.readTextFromFile(this.getClass()
                    .getClassLoader().getResourceAsStream(RAW_FEED_GOOD_JSON));
        } catch (Exception e) {
            return "[]";
        }
    }
}