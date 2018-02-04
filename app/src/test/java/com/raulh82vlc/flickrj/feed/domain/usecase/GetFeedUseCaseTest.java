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

import com.raulh82vlc.flickrj.common.threading.TaskThreading;
import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.data.repository.FeedRepository;
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.GetFeedCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Get feed use case tests
 * @author Raul Hernandez Lopez.
 */
public class GetFeedUseCaseTest {

    @Mock
    FeedRepository feedRepository;
    @Mock
    TaskThreading taskThreading;
    @Mock
    GetFeedCallback callback;

    private GetFeedUseCase underTestUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(taskThreading.computation()).thenReturn(Schedulers.trampoline());
        when(taskThreading.ui()).thenReturn(Schedulers.trampoline());
        underTestUseCase = new GetFeedUseCase(feedRepository, taskThreading);
    }

    @Test
    public void executeOnSuccess() throws Exception {
        when(feedRepository.getFeed()).thenReturn(getMockSingleData());

        underTestUseCase.execute(callback);

        verify(callback).onSuccess(anyList());
        verifyNoMoreInteractions(callback);
    }

    @Test(expected = RuntimeException.class)
    public void executeOnError() throws Exception {
        when(feedRepository.getFeed()).thenThrow(getMockErrorData());

        underTestUseCase.execute(callback);

        verify(callback).onInternetConnectionProblem(anyString());
        verifyNoMoreInteractions(callback);
    }

    private RuntimeException getMockErrorData() {
        return new RuntimeException();
    }

    private Single<List<FeedItemCacheModel>> getMockSingleData() {
        return Single.just(new ArrayList<>());
    }
}