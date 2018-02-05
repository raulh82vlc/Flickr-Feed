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

package com.raulh82vlc.flickrj.feed.domain.usecase;

import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.data.repository.FeedRepository;
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.GetFeedDetailsCallback;

import javax.inject.Inject;

/**
 * Get feed details Use case implementation of {@link UseCaseWithParams}
 * @author Raul Hernandez Lopez.
 */
public class GetFeedDetailsUseCase implements UseCaseWithParams<GetFeedDetailsCallback> {
    private final FeedRepository repository;
    private GetFeedDetailsCallback callback;

    @Inject
    public GetFeedDetailsUseCase(FeedRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(GetFeedDetailsCallback callback, String title, String authorId) {
        this.callback = callback;
        FeedItemCacheModel item = repository.getItemFromFeed(title, authorId);
        if (item == null) {
            this.callback.onError();
        } else {
            this.callback.onItem(item);
        }
    }

    @Override
    public void dispose() {
        callback = null;
    }
}
