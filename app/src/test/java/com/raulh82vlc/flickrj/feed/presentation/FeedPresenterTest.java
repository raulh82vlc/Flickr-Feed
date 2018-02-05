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

import com.raulh82vlc.flickrj.feed.domain.usecase.GetFeedUseCase;
import com.raulh82vlc.flickrj.feed.domain.usecase_callback.GetFeedCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Feed presenter tests
 * @author Raul Hernandez Lopez.
 */
public class FeedPresenterTest {

    @Mock
    private GetFeedUseCase useCase;
    @Mock
    private FeedPresenter.View view;

    private FeedPresenter underTestPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTestPresenter = new FeedPresenter(useCase);
        underTestPresenter.setView(view);
    }

    @Test
    public void getFeed() throws Exception {

        underTestPresenter.getFeed();

        verify(useCase).execute(any(GetFeedCallback.class));
    }

    @Test
    public void dispose() throws Exception {

        underTestPresenter.removeView();

        verify(useCase).dispose();
    }
}