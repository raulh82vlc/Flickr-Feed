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

package com.raulh82vlc.flickrj.feed.ui.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raulh82vlc.flickrj.R;
import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.ui.viewholders.FeedViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Feed list
 */
public class FeedListAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private OnItemClickListener onItemClickListener;

    private List<FeedItemCacheModel> feedList = new ArrayList<>();

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (onItemClickListener != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onItemClickListener.onItemFromListClick((FeedItemCacheModel) view.getTag(), view);
                    }
                }, 200);
            }
        }
    };

    public FeedListAdapter() {
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemListView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_list, parent, false);
        FeedViewHolder feedViewHolder = new FeedViewHolder(itemListView);
        feedViewHolder.onSetClickListener(onClickListener);
        return feedViewHolder;
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder feedViewHolder, int position) {
        feedViewHolder.onBindView(feedList.get(position));
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void setOnItemClickFromList(OnItemClickListener onItemClickFromList) {
        onItemClickListener = onItemClickFromList;
    }

    public interface OnItemClickListener {
        void onItemFromListClick(FeedItemCacheModel feedItemCacheModel, View view);
    }

    public void updateFeed(List<FeedItemCacheModel> feedListFresh) {
        feedList.clear();
        feedList.addAll(feedListFresh);
        notifyDataSetChanged();
    }
}
