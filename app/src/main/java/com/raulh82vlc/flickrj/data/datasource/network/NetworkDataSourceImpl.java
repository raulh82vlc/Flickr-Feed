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

import com.raulh82vlc.flickrj.data.datasource.network.connection.ConnectionHandler;
import com.raulh82vlc.flickrj.data.datasource.network.exceptions.NoNetConnectionException;
import com.raulh82vlc.flickrj.data.datasource.network.model.FeedItemApiModel;
import com.raulh82vlc.flickrj.data.datasource.network.response.ResponseHandler;
import com.raulh82vlc.flickrj.threading.TaskThreading;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;


/**
 * Declared Network data source for the API
 *
 * @author Raul Hernandez Lopez
 */
public class NetworkDataSourceImpl implements NetworkDataSource {

    private final FeedApi feedApi;
    private final ConnectionHandler connectionHandler;
    private final ResponseHandler responseHandler;
    private final TaskThreading taskThreading;
    private final String jsonFormat;

    @Inject
    public NetworkDataSourceImpl(FeedApi feedApi, ConnectionHandler connectionHandler,
                                 ResponseHandler responseHandler, TaskThreading taskThreading,
                                 final String jsonFormat) {
        this.feedApi = feedApi;
        this.connectionHandler = connectionHandler;
        this.responseHandler = responseHandler;
        this.taskThreading = taskThreading;
        this.jsonFormat = jsonFormat;
    }

    @Override
    public Single<List<FeedItemApiModel>> getFeed() {
        return feedApi.getFeed(jsonFormat)
                .observeOn(taskThreading.computation())
                .subscribeOn(taskThreading.io())
                .compose(body -> connectionHandler.isThereConnection()
                        ? body
                        : Single.error(new NoNetConnectionException("No Internet connection")))
                .filter(responseHandler::hasNoErrorResponse)
                .map(responseHandler::getStringContent)
                .filter(responseHandler::hasFeedFormat)
                .map(responseHandler::extractJSONFromResponse)
                .map(responseHandler::deserializeFeedJSON)
                .filter(apiModelSingle -> responseHandler.hasNoApiFailure(apiModelSingle.getStatusOfCall()))
                .map(responseHandler::returnListOfItems)
                .flatMapSingle(Single::just);
    }
}
