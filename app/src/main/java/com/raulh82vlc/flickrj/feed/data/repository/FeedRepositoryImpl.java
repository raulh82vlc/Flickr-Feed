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

package com.raulh82vlc.flickrj.feed.data.repository;

import com.raulh82vlc.flickrj.feed.data.datasource.cache.CacheDataSource;
import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.data.datasource.network.NetworkDataSource;
import com.raulh82vlc.flickrj.feed.data.repository.mapper.NetworkToCacheMapper;
import com.raulh82vlc.flickrj.common.threading.TaskThreading;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;


/**
 * Repository for the feed containing network, cache data sources and mapper into the repo cache model
 *
 * @author Raul Hernandez Lopez
 */
public class FeedRepositoryImpl implements FeedRepository {

    private final TaskThreading taskThreading;
    private final NetworkDataSource networkDataSource;
    private final CacheDataSource cacheDataSource;
    private final NetworkToCacheMapper networkToCacheMapper;

    @Inject
    public FeedRepositoryImpl(TaskThreading taskThreading,
                              NetworkDataSource networkDataSource,
                              CacheDataSource cacheDataSource,
                              NetworkToCacheMapper networkToCacheMapper) {
        this.taskThreading = taskThreading;
        this.networkDataSource = networkDataSource;
        this.cacheDataSource = cacheDataSource;
        this.networkToCacheMapper = networkToCacheMapper;
    }

    @Override
    public Single<List<FeedItemCacheModel>> getFeed() {
        return networkDataSource.getFeed()
                .subscribeOn(taskThreading.io())
                .observeOn(taskThreading.computation())
                .map(networkToCacheMapper::map)
                .doOnSuccess(
                        data -> {
                            cacheDataSource.saveFeed(data);
                            cacheDataSource.getFeed();
                        }
                );
    }

    @Override
    public FeedItemCacheModel getItemFromFeed(String title, String authorId) {
        return cacheDataSource.getItemFromFeed(title, authorId);
    }
}
