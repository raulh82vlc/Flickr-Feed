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

package com.raulh82vlc.flickrj.feed.domain.usecase;

import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.data.repository.FeedRepository;
import com.raulh82vlc.flickrj.common.exceptions.NoNetConnectionException;
import com.raulh82vlc.flickrj.common.threading.TaskThreading;
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.GetFeedCallback;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Get feed Use case implementation of {@link UseCase}
 * @author Raul Hernandez Lopez.
 */
public class GetFeedUseCase implements UseCase<GetFeedCallback> {
    private final FeedRepository repository;
    private final TaskThreading taskThreading;
    private GetFeedCallback callback;
    private Disposable disposable;

    @Inject
    public GetFeedUseCase(FeedRepository repository, TaskThreading taskThreading) {
        this.repository = repository;
        this.taskThreading = taskThreading;
    }

    @Override
    public void execute(GetFeedCallback callback) {
        this.callback = callback;
        disposable = repository.getFeed()
                .subscribeOn(taskThreading.computation())
                .observeOn(taskThreading.ui())
                .subscribe(
                    this::onSuccess,
                    this::onError);
    }

    private void onSuccess(List<FeedItemCacheModel> data) {
        callback.onSuccess(data);
    }

    private void onError(Throwable e) {
        Timber.e(e);
        String message = e.getMessage();
        if (e instanceof NoNetConnectionException) {
            callback.onInternetConnectionProblem(message);
        } else {
            callback.onError(message);
        }
    }

    @Override
    public void dispose() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
        callback = null;
    }
}
