
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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.raulh82vlc.flickrj.FlickrApp;
import com.raulh82vlc.flickrj.R;
import com.raulh82vlc.flickrj.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.data.repository.FeedRepository;
import com.raulh82vlc.flickrj.feed.di.DaggerFeedComponent;
import com.raulh82vlc.flickrj.feed.di.FeedComponent;
import com.raulh82vlc.flickrj.threading.TaskThreading;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class FeedActivity extends AppCompatActivity {

    private FeedComponent feedComponent;

    @Inject
    FeedRepository feedRepository;
    @Inject
    TaskThreading taskThreading;

    private void showError(Throwable e) {
        Timber.e(e.getMessage());
    }

    private void whatever(List<FeedItemCacheModel> modelList) {
        Timber.d("int " + modelList.size());
        for (FeedItemCacheModel item : modelList) {
            for (String tag : item.getTags()) {
                Timber.d(tag);
            }
        }
    }

    public void startRequest() {
        feedRepository.getFeed()
                .subscribeOn(taskThreading.computation())
                .observeOn(taskThreading.ui())
                .subscribe(data -> whatever(data),
                        e -> showError(e));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setSupportActionBar(findViewById(R.id.toolbar));
        getComponentInstance().inject(this);
        startRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public FeedComponent getComponentInstance() {
        if (feedComponent == null) {
            feedComponent = DaggerFeedComponent.builder()
                    .applicationComponent(((FlickrApp) getApplication()).getComponentInstance())
                    .build();
        }
        return feedComponent;
    }
}
