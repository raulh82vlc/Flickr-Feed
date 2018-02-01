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

import com.google.gson.Gson;
import com.raulh82vlc.flickrj.data.network.FeedApi;
import com.raulh82vlc.flickrj.data.network.connection.ConnectionHandler;
import com.raulh82vlc.flickrj.data.network.model.FeedApiModel;
import com.raulh82vlc.flickrj.data.network.model.FeedItemApiModel;

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
    private final Gson gson;

    @Inject
    public NetworkDataSourceImpl(FeedApi feedApi, ConnectionHandler connectionHandler, Gson gson) {
        this.feedApi = feedApi;
        this.connectionHandler = connectionHandler;
        this.gson = gson;
        //TODO "json" parameter at request into gradle variable
    }

    //TODO datasource passes this list of API model and Repo transforms it into a cache model
    //TODO repo does a zip of both cache & API fresh results and returns it back
    //TODO use a compose with connection check to pass a custom internet exception
    //TODO inject subscriber to be able to mock it
    //TODO wrap Gson to use a lighter mock for test
    @Override
    public Single<List<FeedItemApiModel>> getFeed() {
        return feedApi.getFeed("json")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(responseBodyResult ->
                        !responseBodyResult.isError() && responseBodyResult.response() != null)
                .map(responseBodyResult -> responseBodyResult.response().body().string())
                .filter(responseBodyResult -> responseBodyResult.contains("jsonFlickrFeed("))
                .map(this::stringTokenizer)
                .map(responseJson -> gson.fromJson(responseJson, FeedApiModel.class))
                .map(FeedApiModel::getFeedItems)
                .flatMapSingle(Single::just);
    }

    //TODO create a delegate to create a parser
    private String stringTokenizer(String response) {
        response = response.replaceFirst("jsonFlickrFeed\\(", "");
        return response.substring(0, response.length() - 1);
    }
}
