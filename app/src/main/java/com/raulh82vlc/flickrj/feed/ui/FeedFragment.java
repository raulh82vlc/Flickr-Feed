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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.raulh82vlc.flickrj.R;
import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.presentation.FeedPresenter;
import com.raulh82vlc.flickrj.feed.ui.adapter.FeedListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Fragment with Content of the feed
 */
public class FeedFragment extends BaseFragment implements FeedPresenter.View,
        FeedListAdapter.OnItemClickListener {
    /**
     * UI injections
     */
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;
    @BindView(R.id.no_results_view)
    public TextView noResultsTextView;

    @Inject
    FeedPresenter presenter;

    private FeedActivity activity;
    private FeedListAdapter adapter;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onDestroyView() {
        presenter.removeView();
        adapter = null;
        super.onDestroyView();
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
        setRecyclerView();
        presenter.getFeed();
    }

    /**
     * <p>Sets the adapter and recyclerview</p>
     **/
    private void setRecyclerView() {
        adapter = new FeedListAdapter();
        adapter.setOnItemClickFromList(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void showAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public void updateList(List<FeedItemCacheModel> items) {
        adapter.updateFeed(items);
    }

    @Override
    public void showList() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        recyclerView.setVisibility(View.GONE);
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
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        noResultsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        noResultsTextView.setVisibility(View.GONE);
    }

    @Override
    public void onItemFromListClick(FeedItemCacheModel feedItemCacheModel, View view) {
        /** go to the detail screen */
        FeedDetailsActivity.navigateToDetailsActivity(activity,
                feedItemCacheModel.getTitle(),
                feedItemCacheModel.getAuthorId(),
                view.findViewById(R.id.iv_image));
    }
}
