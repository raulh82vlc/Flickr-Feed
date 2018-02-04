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
package com.raulh82vlc.flickrj.feed.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raulh82vlc.flickrj.R;
import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.presentation.FeedPresenter;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Fragment with Content of the feed
 */
public class FeedFragment extends BaseFragment implements FeedPresenter.View {

    @Inject
    FeedPresenter presenter;

    private FeedActivity activity;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onDestroy() {
        presenter.removeView();
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FeedActivity) context;
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.getComponentInstance().inject(this);
        presenter.setView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getFeed();
    }

    @Override
    public void updateList(List<FeedItemCacheModel> items) {
        Timber.d("int " + items.size());
        for (FeedItemCacheModel item : items) {
            for (String tag : item.getTags()) {
                Timber.d(tag);
            }
        }
    }

    @Override
    public void showList() {

    }

    @Override
    public void hideList() {

    }

    @Override
    public void showLoader() {
        activity.showLoaderWithTitleAndDescription(getString(R.string.feed_loader),
                getString(R.string.loading_feed));
    }

    @Override
    public void hideLoader() {
        activity.hideLoader();
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void hideError() {

    }
}
