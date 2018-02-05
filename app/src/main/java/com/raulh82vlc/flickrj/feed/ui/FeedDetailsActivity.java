
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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raulh82vlc.flickrj.FlickrApp;
import com.raulh82vlc.flickrj.R;
import com.raulh82vlc.flickrj.di.activity.ActivityModule;
import com.raulh82vlc.flickrj.feed.di.DaggerFeedDetailsComponent;
import com.raulh82vlc.flickrj.feed.di.FeedDetailsComponent;
import com.raulh82vlc.flickrj.feed.presentation.FeedDetailsPresenter;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;

public class FeedDetailsActivity extends BaseActivity implements FeedDetailsPresenter.View {

    private static final String KEY_TITLE = "DETAILS_TITLE";
    private static final String KEY_AUTHORID = "AUTHORID";
    private FeedDetailsComponent feedComponent;

    private final static String IMG_TRANSITION_TAG = "activity_compat_transition_by_img";

    // UI Injections
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.title_txt)
    TextView title;
    @BindView(R.id.description_txt)
    TextView description;
    @BindView(R.id.author_txt)
    TextView author;
    @BindView(R.id.date_txt)
    TextView date;
    @BindView(R.id.tags_txt)
    TextView tags;
    @BindView(R.id.main_image)
    ImageView mainImage;

    @Inject
    FeedDetailsPresenter presenter;

    /**
     * Navigate To details of the comic using different animations depending on the Android version
     */
    public static void navigateToDetailsActivity(AppCompatActivity activity,
                                                 String title,
                                                 String authorId,
                                                 View view) {
        Intent intent = new Intent(activity, FeedDetailsActivity.class);
        Bundle data = new Bundle();
        data.putString(KEY_TITLE, title);
        data.putString(KEY_AUTHORID, authorId);
        intent.putExtras(data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    view, IMG_TRANSITION_TAG);
            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.postponeEnterTransition(this);
        }
        getComponentInstance().inject(this);
        setToolbarInitialisation();
        presenter.setView(this);
        if (getIntent() != null) {
            String title = getIntent().getStringExtra(KEY_TITLE);
            String authorId = getIntent().getStringExtra(KEY_AUTHORID);
            setInitialTitle(title);
            presenter.getFeedItem(title, authorId);
        }
    }

    private void setToolbarInitialisation() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    protected void setInitialTitle(String title) {
        toolbar.setTitle(title);
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        collapsingToolbar.setTitle(title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details_feed;
    }

    @Override
    protected AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        presenter.removeView();
        super.onDestroy();
    }

    public FeedDetailsComponent getComponentInstance() {
        if (feedComponent == null) {
            feedComponent = DaggerFeedDetailsComponent.builder()
                    .applicationComponent(((FlickrApp) getApplication()).getComponentInstance())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return feedComponent;
    }

    @Override
    public void closeView() {
        Toast.makeText(this, getString(R.string.feed_item_not_known), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void showDescription(String description) {
        this.description.setText(
                Html.fromHtml(description));
    }

    @Override
    public void showAuthor(String author) {
        this.author.setText(author);
    }

    @Override
    public void showTags(String formattedTags) {
        tags.setText(formattedTags);
    }

    @Override
    public void showImage(String url) {
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.view_holder)
                .into(mainImage);
    }

    @Override
    public void showDate(String dateTaken) {
        date.setText(dateTaken);
    }

    @Override
    public void showEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(mainImage, IMG_TRANSITION_TAG);
            ActivityCompat.startPostponedEnterTransition(this);
        }
    }
}
