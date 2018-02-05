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

package com.raulh82vlc.flickrj.feed.domain.usecase_callback;

import com.raulh82vlc.flickrj.feed.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.feed.domain.mapper.TagsMapper;
import com.raulh82vlc.flickrj.feed.presentation.FeedDetailsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Get feed details callback tests
 * @author Raul Hernandez Lopez.
 */
public class GetFeedDetailsCallbackImplTest {

    @Mock
    private FeedDetailsPresenter.View view;
    @Mock
    private TagsMapper mapper;
    @Mock
    private NullInterceptor interceptor;

    private GetFeedDetailsCallback underTestCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTestCallback = new GetFeedDetailsCallbackImpl(view, mapper, interceptor);
    }

    @Test
    public void onItem() throws Exception {
        when(mapper.map(anyList())).thenReturn("1 2");
        when(interceptor.map(any(FeedItemCacheModel.class))).thenReturn("A");

        underTestCallback.onItem(new FeedItemCacheModel());

        verify(view).showAuthor(anyString());
        verify(view).showDate(anyString());
        verify(view).showDescription(anyString());
        verify(view).showEffect();
        verify(view).showImage(anyString());
        verify(view).showTitle(anyString());
        verify(view).showTags(anyString());
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onError() throws Exception {

        underTestCallback.onError();

        verify(view).closeView();
        verifyNoMoreInteractions(view);
    }
}