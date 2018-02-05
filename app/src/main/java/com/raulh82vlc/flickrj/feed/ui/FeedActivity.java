
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.raulh82vlc.flickrj.FlickrApp;
import com.raulh82vlc.flickrj.R;
import com.raulh82vlc.flickrj.di.activity.ActivityModule;
import com.raulh82vlc.flickrj.feed.di.DaggerFeedComponent;
import com.raulh82vlc.flickrj.feed.di.FeedComponent;

import butterknife.BindView;

public class FeedActivity extends BaseActivity {

    private FeedComponent feedComponent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed;
    }

    @Override
    protected AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponentInstance().inject(this);
        setToolbarInitialisation();
    }

    protected void setToolbarInitialisation() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        toolbar.setTitle(getString(R.string.app_name));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    public FeedComponent getComponentInstance() {
        if (feedComponent == null) {
            feedComponent = DaggerFeedComponent.builder()
                    .applicationComponent(((FlickrApp) getApplication()).getComponentInstance())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return feedComponent;
    }
}
