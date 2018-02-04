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

package com.raulh82vlc.flickrj.data.repository;

import com.raulh82vlc.flickrj.data.datasource.cache.CacheDataSource;
import com.raulh82vlc.flickrj.data.datasource.cache.model.FeedItemCacheModel;
import com.raulh82vlc.flickrj.data.datasource.network.NetworkDataSource;
import com.raulh82vlc.flickrj.data.datasource.network.model.FeedItemApiModel;
import com.raulh82vlc.flickrj.data.repository.mapper.NetworkToCacheMapper;
import com.raulh82vlc.flickrj.threading.TaskThreading;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Feed repository unit tests to check interaction with all data sources and mappers when needed
 * @author Raul Hernandez Lopez.
 */
public class FeedRepositoryImplTest {

    @Mock
    TaskThreading taskThreading;
    @Mock
    CacheDataSource cacheDataSource;
    @Mock
    NetworkDataSource networkDataSource;
    @Mock
    NetworkToCacheMapper networkToCacheMapper;

    private FeedRepository underTestRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(taskThreading.computation()).thenReturn(Schedulers.trampoline());
        when(taskThreading.io()).thenReturn(Schedulers.trampoline());
        underTestRepository = new FeedRepositoryImpl(taskThreading, networkDataSource, cacheDataSource,
                networkToCacheMapper);
    }

    @After
    public void tearDown() throws Exception {
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }

    @Test
    public void getFeed() throws Exception {
        when(networkDataSource.getFeed()).thenReturn(getMockSingleResult());
        TestObserver<List<FeedItemCacheModel>> testSubscriber = new TestObserver<>();

        Single<List<FeedItemCacheModel>> single = underTestRepository.getFeed();

        assertNotNull(single);
        single.subscribe(testSubscriber);
        testSubscriber.assertSubscribed();
        verify(networkToCacheMapper).map(anyList());
        verify(cacheDataSource).saveFeed(anyList());
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(single.blockingGet());
        testSubscriber.assertValueCount(1);
    }

    public Single<List<FeedItemApiModel>> getMockSingleResult() {
        return Single.just(new ArrayList<>());
    }
}