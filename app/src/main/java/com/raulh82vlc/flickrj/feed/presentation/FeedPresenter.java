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

import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.GetFeedCallbackImpl;
import com.raulh82vlc.flickrj.feed.domain.usecase.GetFeedUseCase;

import java.util.List;

import javax.inject.Inject;

/**
 * Feed presenter
 * @author Raul Hernandez Lopez.
 */
public class FeedPresenter {

    private final GetFeedUseCase feedUseCase;
    private View view;

    public interface View {
        void updateList(List<FeedItemCacheModel> items);
        void showList();
        void hideList();
        void showLoader();
        void hideLoader();
        void showError(String message);
        void hideError();
    }

    @Inject
    public FeedPresenter(GetFeedUseCase feedUseCase) {
        this.feedUseCase = feedUseCase;
    }

    public void getFeed() {
        feedUseCase.execute(new GetFeedCallbackImpl(view));
    }

    public void setView(View view) {
        this.view = view;
    }

    public void removeView() {
        feedUseCase.dispose();
        view = null;
    }
}
