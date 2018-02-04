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

package com.raulh82vlc.flickrj.feed.data.di;

import com.raulh82vlc.flickrj.feed.data.datasource.cache.CacheDataSource;
import com.raulh82vlc.flickrj.feed.data.datasource.cache.CacheDataSourceImpl;
import com.raulh82vlc.flickrj.feed.data.datasource.network.NetworkDataSource;
import com.raulh82vlc.flickrj.feed.data.repository.FeedRepository;
import com.raulh82vlc.flickrj.feed.data.repository.FeedRepositoryImpl;
import com.raulh82vlc.flickrj.feed.data.repository.mapper.NetworkToCacheMapper;
import com.raulh82vlc.flickrj.common.threading.TaskThreading;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module which provides Repository artifacts necessary to handle Feed data
 *
 * @author Raul Hernandez Lopez
 */
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    CacheDataSource provideCacheDataSource(CacheDataSourceImpl cacheNetworkDataSource) {
        return cacheNetworkDataSource;
    }

    @Provides
    @Singleton
    FeedRepository provideRepo(TaskThreading taskThreading,
                               NetworkDataSource networkDataSource,
                               CacheDataSource cacheDataSource,
                               NetworkToCacheMapper networkToCacheMapper) {
        return new FeedRepositoryImpl(
                taskThreading,
                networkDataSource,
                cacheDataSource,
                networkToCacheMapper);
    }
}
