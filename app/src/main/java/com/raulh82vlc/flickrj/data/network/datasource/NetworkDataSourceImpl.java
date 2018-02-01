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

package com.raulh82vlc.flickrj.data.network.datasource;

import com.raulh82vlc.flickrj.data.network.FeedApi;
import com.raulh82vlc.flickrj.data.network.connection.ConnectionHandler;
import com.raulh82vlc.flickrj.data.network.exceptions.NoNetConnectionException;
import com.raulh82vlc.flickrj.data.network.model.FeedApiModel;
import com.raulh82vlc.flickrj.data.network.model.FeedItemApiModel;
import com.raulh82vlc.flickrj.data.network.response.ResponseHandler;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Declared read operations from the Network API interface
 *
 * @author Raul Hernandez Lopez
 */
public class NetworkDataSourceImpl implements NetworkDataSource<FeedItemApiModel> {

    private final FeedApi feedApi;
    private final ConnectionHandler connectionHandler;
    private final ResponseHandler responseHandler;

    @Inject
    public NetworkDataSourceImpl(FeedApi feedApi, ConnectionHandler connectionHandler,
                                 ResponseHandler responseHandler) {
        this.feedApi = feedApi;
        this.connectionHandler = connectionHandler;
        this.responseHandler = responseHandler;
        //TODO "json" parameter at request into gradle variable
    }

    //TODO datasource passes this list of API model and Repo transforms it into a cache model
    //TODO repo does a zip of both cache & API fresh results and returns it back
    //TODO inject subscriber to be able to mock it
    @Override
    public Single<List<FeedItemApiModel>> getFeed() {
        return feedApi.getFeed("json")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(body -> connectionHandler.isThereConnection()
                        ? body
                        : Single.error(new NoNetConnectionException("No Internet")))
                .filter(responseBodyResult ->
                        !responseBodyResult.isError() && responseBodyResult.response() != null)
                .map(responseBodyResult -> responseBodyResult.response().body().string())
                .filter(responseHandler::hasFeedFormat)
                .map(responseHandler::extractJSONFromResponse)
                .map(responseHandler::deserializeFeedJSON)
                .filter(apiModelSingle -> responseHandler.hasNoApiFailure(apiModelSingle.getStatusOfCall()))
                .map(FeedApiModel::getFeedItems)
                .flatMapSingle(Single::just);
    }
}
