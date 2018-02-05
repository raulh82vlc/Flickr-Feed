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
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.GetFeedDetailsCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Get feed details use case tests
 * @author Raul Hernandez Lopez.
 */
public class GetFeedDetailsUseCaseTest {
    @Mock
    FeedRepository feedRepository;
    @Mock
    GetFeedDetailsCallback callback;

    private GetFeedDetailsUseCase underTestUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTestUseCase = new GetFeedDetailsUseCase(feedRepository);
    }

    @Test
    public void executeOnSuccess() throws Exception {
        when(feedRepository.getItemFromFeed(anyString(), anyString())).thenReturn(new FeedItemCacheModel());

        underTestUseCase.execute(callback, "a", "b");

        verify(callback).onItem(any(FeedItemCacheModel.class));
        verifyNoMoreInteractions(callback);
    }

    @Test
    public void executeOnError() throws Exception {
        when(feedRepository.getItemFromFeed(anyString(), anyString())).thenReturn(null);

        underTestUseCase.execute(callback, "a", "b");

        verify(callback).onError();
        verifyNoMoreInteractions(callback);
    }
}