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

import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;

import java.util.List;

import io.reactivex.Single;

/**
 * Repository contract
 *
 * @author Raul Hernandez Lopez
 */
public interface FeedRepository {

    /**
     * Gets a list of feed
     **/
    Single<List<FeedItemCacheModel>> getFeed();

    /**
     * Get an item based on its title and authorId
     * @param title title
     * @param authorId author identifier
     * @return {@link FeedItemCacheModel}
     */
    FeedItemCacheModel getItemFromFeed(String title, String authorId);
}
