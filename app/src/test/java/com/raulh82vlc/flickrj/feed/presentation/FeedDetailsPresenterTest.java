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

package com.raulh82vlc.flickrj.feed.presentation;

import com.raulh82vlc.flickrj.feed.domain.usecase.GetFeedDetailsUseCase;
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.GetFeedDetailsCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Feed details presenter
 * @author Raul Hernandez Lopez.
 */
public class FeedDetailsPresenterTest {

    @Mock
    private GetFeedDetailsUseCase useCase;
    @Mock
    private FeedDetailsPresenter.View view;

    private FeedDetailsPresenter underTestPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTestPresenter = new FeedDetailsPresenter(useCase);
        underTestPresenter.setView(view);
    }
    @Test
    public void getFeedItem() throws Exception {
        underTestPresenter.getFeedItem("a", "b");

        verify(useCase).execute(any(GetFeedDetailsCallback.class), anyString(), anyString());
    }
}