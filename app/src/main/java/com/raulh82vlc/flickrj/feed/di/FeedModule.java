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

package com.raulh82vlc.flickrj.feed.di;


import com.raulh82vlc.flickrj.common.threading.TaskThreading;
import com.raulh82vlc.flickrj.di.activity.scopes.ActivityScope;
import com.raulh82vlc.flickrj.feed.data.repository.FeedRepository;
import com.raulh82vlc.flickrj.feed.domain.usecase.GetFeedUseCase;
import com.raulh82vlc.flickrj.feed.presentation.FeedPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Module which provides all user required artifacts
 * (presenter, use cases)
 * in order to use them in a decoupled way
 *
 * @author Raul Hernandez Lopez
 */
@Module
public class FeedModule {

    @Provides
    @ActivityScope
    FeedPresenter provideFeedPresenter(GetFeedUseCase getFeedUseCase) {
        return new FeedPresenter(getFeedUseCase);
    }

    @Provides
    @ActivityScope
    GetFeedUseCase provideGetFeedUseCase(FeedRepository feedRepository, TaskThreading taskThreading) {
        return new GetFeedUseCase(feedRepository, taskThreading);
    }
}
