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

package com.raulh82vlc.flickrj.feed.presentation;

import com.raulh82vlc.flickrj.feed.domain.mapper.TagsMapper;
import com.raulh82vlc.flickrj.feed.domain.usecase.GetFeedDetailsUseCase;
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.GetFeedDetailsCallbackImpl;
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.NullInterceptor;

import javax.inject.Inject;

/**
 * Feed details presenter
 * @author Raul Hernandez Lopez.
 */
public class FeedDetailsPresenter {

    private final GetFeedDetailsUseCase feedDetailsUseCase;
    private View view;

    public interface View {
        void closeView();

        void showTitle(String title);

        void showDescription(String description);

        void showAuthor(String author);

        void showTags(String formattedTags);

        void showImage(String url);

        void showDate(String dateTaken);

        void showEffect();
    }

    @Inject
    public FeedDetailsPresenter(GetFeedDetailsUseCase feedDetailsUseCase) {
        this.feedDetailsUseCase = feedDetailsUseCase;
    }

    public void getFeedItem(String title, String authorId) {
        feedDetailsUseCase.execute(new GetFeedDetailsCallbackImpl(
                view, new TagsMapper(), new NullInterceptor()), title, authorId);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void removeView() {
        feedDetailsUseCase.dispose();
        view = null;
    }
}
