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

package com.raulh82vlc.flickrj.feed.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raulh82vlc.flickrj.R;
import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Comic View Holder with onBindView & onSetClickListener functionalities
 *
 * @author Raul Hernandez Lopez
 */

public class FeedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txt_title)
    public TextView title;

    @BindView(R.id.iv_image)
    public ImageView image;

    public FeedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBindView(FeedItemCacheModel feedItemCacheModel) {
        title.setText(feedItemCacheModel.getTitle());
        setImage(feedItemCacheModel.getMedia().getUrl());
        itemView.setTag(feedItemCacheModel);
    }

    private void setImage(String uri) {
        Picasso.with(itemView.getContext())
                .load(uri)
                .placeholder(R.drawable.view_holder)
                .into(image);
    }

    /**
     * Sets the corresponding listener
     * for a click {@link View.OnClickListener} to our {@link FeedViewHolder} item
     *
     * @param onClickListener {@link View.OnClickListener }
     */
    public void onSetClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}