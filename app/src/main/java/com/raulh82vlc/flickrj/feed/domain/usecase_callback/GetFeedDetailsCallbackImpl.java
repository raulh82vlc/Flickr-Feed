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

package com.raulh82vlc.flickrj.feed.domain.usecase_callback;

import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.domain.mapper.TagsMapper;
import com.raulh82vlc.flickrj.feed.presentation.FeedDetailsPresenter;

import java.util.List;

/**
 * Implementation of the {@link GetFeedDetailsCallback} to be able to be tested
 * @author Raul Hernandez Lopez.
 */
public class GetFeedDetailsCallbackImpl implements GetFeedDetailsCallback {

    private FeedDetailsPresenter.View view;
    private final TagsMapper tagsMapper;
    private final NullInterceptor interceptor;

    public GetFeedDetailsCallbackImpl(FeedDetailsPresenter.View view, TagsMapper tagsMapper,
                                      NullInterceptor interceptor) {
        this.view = view;
        this.tagsMapper = tagsMapper;
        this.interceptor = interceptor;
    }

    @Override
    public void onItem(FeedItemCacheModel feedItem) {
        view.showEffect();
        view.showTitle(feedItem.getTitle());
        view.showDescription(feedItem.getDescription());
        view.showAuthor(feedItem.getAuthor());
        view.showTags(getFormattedTags(feedItem.getTags()));
        view.showImage(interceptor.map(feedItem));
        view.showDate(feedItem.getDateTaken());
    }

    @Override
    public String getFormattedTags(List<String> tags) {
        return tagsMapper.map(tags);
    }

    @Override
    public void onError() {
        view.closeView();
    }
}
