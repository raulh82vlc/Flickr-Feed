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
import com.raulh82vlc.flickrj.feed.presentation.FeedPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Get feed callback tests
 * @author Raul Hernandez Lopez.
 */
public class GetFeedCallbackImplTest {

    @Mock
    private FeedPresenter.View view;
    @Mock
    private List<FeedItemCacheModel> items;
    private GetFeedCallback underTestCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTestCallback = new GetFeedCallbackImpl(view);
    }

    @Test
    public void onSuccess() throws Exception {

        underTestCallback.onSuccess(items);

        verify(view).hideError();
        verify(view).hideLoader();
        verify(view).showList();
        verify(view).updateList(anyList());
    }

    @Test
    public void onError() throws Exception {

        underTestCallback.onError("error");

        verify(view).hideLoader();
        verify(view).hideList();
        verify(view).showError(anyString());
    }

    @Test
    public void onInternetConnectionProblem() throws Exception {

        underTestCallback.onInternetConnectionProblem("error");

        verify(view).hideLoader();
        verify(view).hideList();
        verify(view).showError(anyString());
    }

}