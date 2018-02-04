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

package com.raulh82vlc.flickrj.data.datasource.network;

import com.raulh82vlc.flickrj.data.FileReaderHelper;
import com.raulh82vlc.flickrj.data.datasource.network.connection.ConnectionHandler;
import com.raulh82vlc.flickrj.data.datasource.network.exceptions.NoNetConnectionException;
import com.raulh82vlc.flickrj.data.datasource.network.model.FeedApiModel;
import com.raulh82vlc.flickrj.data.datasource.network.model.FeedItemApiModel;
import com.raulh82vlc.flickrj.data.datasource.network.response.ResponseHandler;
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
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Network data source unit tests
 * @author Raul Hernandez Lopez.
 */
public class NetworkDataSourceImplTest {

    private static final String RAW_FEED_JSON = "raw/feed.json";
    private static final MediaType JSON_UTF = MediaType.parse(FileReaderHelper.APPLICATION_JSON);

    @Mock
    TaskThreading taskThreading;
    @Mock
    FeedApi feedApi;
    @Mock
    ConnectionHandler connectionHandler;
    @Mock
    ResponseHandler responseHandler;

    private NetworkDataSource underTestNetworkDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(taskThreading.computation()).thenReturn(Schedulers.trampoline());
        when(taskThreading.io()).thenReturn(Schedulers.trampoline());
        underTestNetworkDataSource = new NetworkDataSourceImpl(feedApi, connectionHandler, responseHandler,
                taskThreading, "json");
    }

    @After
    public void tearDown() throws Exception {
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }

    @Test
    public void getFeedPassesListIfResponseIsSuccessful() throws Exception {
        when(feedApi.getFeed(anyString())).thenReturn(getMockSingleResult());
        when(connectionHandler.isThereConnection()).thenReturn(true);
        when(responseHandler.hasFeedFormat(anyString())).thenReturn(true);
        when(responseHandler.hasNoApiFailure(anyString())).thenReturn(true);
        when(responseHandler.hasNoErrorResponse(any())).thenReturn(true);
        when(responseHandler.getStringContent(any())).thenReturn("{}");
        when(responseHandler.extractJSONFromResponse(any())).thenReturn("{}");
        when(responseHandler.deserializeFeedJSON(anyString())).thenReturn(new FeedApiModel());
        when(responseHandler.returnListOfItems(any(FeedApiModel.class))).thenReturn(new ArrayList<>());

        TestObserver<List<FeedItemApiModel>> testSubscriber = new TestObserver<>();

        Single<List<FeedItemApiModel>> single = underTestNetworkDataSource.getFeed();

        assertNotNull(single);
        single.subscribe(testSubscriber);
        testSubscriber.assertSubscribed();
        verify(responseHandler).getStringContent(any());
        verify(responseHandler).extractJSONFromResponse(anyString());
        verify(responseHandler).deserializeFeedJSON(anyString());
        verify(responseHandler).hasNoApiFailure(anyString());
        verify(responseHandler).returnListOfItems(any(FeedApiModel.class));
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(single.blockingGet());
        assertEquals(new ArrayList<>(), single.blockingGet());
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void getFeedStopsIfNoConnection() throws Exception {
        when(feedApi.getFeed(anyString())).thenReturn(getMockSingleResult());
        when(connectionHandler.isThereConnection()).thenReturn(false);

        TestObserver<List<FeedItemApiModel>> testSubscriber = new TestObserver<>();

        Single<List<FeedItemApiModel>> single = underTestNetworkDataSource.getFeed();

        assertNotNull(single);
        single.subscribe(testSubscriber);
        testSubscriber.assertSubscribed();
        verify(responseHandler, never()).hasNoErrorResponse(any());
        verify(responseHandler, never()).getStringContent(any());
        testSubscriber.assertError(NoNetConnectionException.class);
        testSubscriber.assertValueCount(0);
    }

    private Single<Result<ResponseBody>> getMockSingleResult() {
        return Single.just(
                Result.response(
                Response.success(
                        ResponseBody.create(JSON_UTF, getMockFeedResponse()))));
    }

    private String getMockFeedResponse() {
        try {
            return FileReaderHelper.readTextFromFile(this.getClass()
                    .getClassLoader().getResourceAsStream(RAW_FEED_JSON));
        } catch (Exception e) {
            return "{}";
        }
    }
}