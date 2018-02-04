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

package com.raulh82vlc.flickrj.data.datasource.cache;

import com.raulh82vlc.flickrj.data.datasource.cache.model.FeedItemCacheModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Cache Network tests
 * @author Raul Hernandez Lopez.
 */
public class CacheDataSourceImplTest {

    private CacheDataSourceImpl cacheNetworkDataSource;
    private List<FeedItemCacheModel> itemsInput;

    @Before
    public void setUp() {
        cacheNetworkDataSource = new CacheDataSourceImpl();
        itemsInput = new ArrayList<>();
        setCollection();
    }

    private void setCollection() {
        itemsInput.add(
                new FeedItemCacheModel("ABC", "http://asd",
                    new FeedItemCacheModel.MediaItem("http://asd"),
                    "interesting", "yesterday", "today", "Raul",
                    "Raul1", new ArrayList<>(Arrays.asList("1", "2", "3"))));
        itemsInput.add(
                new FeedItemCacheModel("CBA", "http://ads",
                        new FeedItemCacheModel.MediaItem("http://ads"),
                        "interesting1", "yesterday", "today", "Raul",
                        "Raul2", new ArrayList<>(Arrays.asList("2", "2", "3"))));
    }

    @Test
    public void cacheDataSourceSavesAndGetsContentStored() throws Exception {
        cacheNetworkDataSource.saveFeed(itemsInput);
        List<FeedItemCacheModel> items = cacheNetworkDataSource.getFeed();
        assertFalse(cacheNetworkDataSource.isEmpty());
        for (int i = 0; i < items.size(); i++) {
            assertEquals(itemsInput.get(i).getTitle(), items.get(i).getTitle());
            assertEquals(itemsInput.get(i).getDescription(), items.get(i).getDescription());
            assertEquals(itemsInput.get(i).getTags().get(0), items.get(i).getTags().get(0));
        }
    }

}