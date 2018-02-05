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

package com.raulh82vlc.flickrj.feed.data.datasource.cache;

import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


/**
 * Cache data source implementation {@link CacheDataSource} where the repo has its data saved
 *
 * @author Raul Hernandez Lopez
 */
public class CacheDataSourceImpl implements CacheDataSource {

    private List<FeedItemCacheModel> listOfItems = new ArrayList<>();
    private Map<String, FeedItemCacheModel> dictionaryOfItems = new HashMap<>();

    @Inject
    public CacheDataSourceImpl() {
    }

    @Override
    public List<FeedItemCacheModel> getFeed() {
        return listOfItems;
    }

    @Override
    public void saveFeed(List<FeedItemCacheModel> itemCacheModels) {
        listOfItems.clear();
        for (FeedItemCacheModel item : itemCacheModels) {
            listOfItems.add(item);
            dictionaryOfItems.put(item.getTitle() + item.getAuthorId(), item);
        }
    }

    @Override
    public boolean isEmpty() {
        return listOfItems.isEmpty();
    }

    @Override
    public FeedItemCacheModel getItemFromFeed(String title, String authorId) {
        return dictionaryOfItems.get(title + authorId);
    }
}
