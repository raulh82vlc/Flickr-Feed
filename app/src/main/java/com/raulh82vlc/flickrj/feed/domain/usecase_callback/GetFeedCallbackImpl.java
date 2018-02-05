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
import com.raulh82vlc.flickrj.feed.presentation.FeedPresenter;

import java.util.List;

/**
 * Implementation of the {@link GetFeedCallback} to be able to be tested
 * @author Raul Hernandez Lopez.
 */
public class GetFeedCallbackImpl implements GetFeedCallback {

    private FeedPresenter.View view;

    public GetFeedCallbackImpl(FeedPresenter.View view) {
        this.view = view;
        this.view.showLoader();
    }

    @Override
    public void onSuccess(List<FeedItemCacheModel> feedItems) {
        view.hideError();
        view.hideLoader();
        view.showList();
        view.updateList(feedItems);
        view.showAnimation();
    }

    @Override
    public void onError(String message) {
        view.hideLoader();
        view.hideList();
        view.showError(message);
    }

    @Override
    public void onInternetConnectionProblem(String message) {
        view.hideLoader();
        view.hideList();
        view.showError(message);
    }
}
