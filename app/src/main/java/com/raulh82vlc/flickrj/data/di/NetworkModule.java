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

package com.raulh82vlc.flickrj.data.di;

import com.google.gson.Gson;
import com.raulh82vlc.flickrj.BuildConfig;
import com.raulh82vlc.flickrj.data.datasource.network.FeedApi;
import com.raulh82vlc.flickrj.data.datasource.network.NetworkDataSource;
import com.raulh82vlc.flickrj.data.datasource.network.NetworkDataSourceImpl;
import com.raulh82vlc.flickrj.data.datasource.network.connection.ConnectionHandler;
import com.raulh82vlc.flickrj.data.datasource.network.connection.ConnectionHandlerImpl;
import com.raulh82vlc.flickrj.data.datasource.network.response.ResponseHandler;
import com.raulh82vlc.flickrj.threading.TaskThreading;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import timber.log.Timber;

/**
 * Module which provides network artifacts necessary to make requests & handle responses
 *
 * @author Raul Hernandez Lopez
 */
@Module
public class NetworkModule {

    private static final String LOGGING_INTERCEPTOR = "logging_interceptor";

    @Provides
    @Singleton
    NetworkDataSource provideNetDataSource(FeedApi api, ConnectionHandler connectionHandler,
                                           ResponseHandler responseHandler, TaskThreading taskThreading) {
        return new NetworkDataSourceImpl(api, connectionHandler, responseHandler, taskThreading,
                BuildConfig.JSON_FORMAT);
    }

    @Provides
    ConnectionHandler provideConnectionHandler(ConnectionHandlerImpl connectionHandler) {
        return connectionHandler;
    }

    @Provides
    ResponseHandler provideResponseHandler(Gson gson) {
        return new ResponseHandler(gson);
    }

    @Provides
    FeedApi provideFeedApi(Retrofit retrofit) {
        return retrofit.create(FeedApi.class);
    }

    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(@Named(LOGGING_INTERCEPTOR) Interceptor interceptor) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(interceptor);
        return okHttpClient.build();
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory providesRxJava2CallAdapter() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient okHttpClient,
                              RxJava2CallAdapterFactory adapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_API)
                .client(okHttpClient)
                .addCallAdapterFactory(adapterFactory)
                .build();
    }

    @Provides
    @Singleton
    @Named(LOGGING_INTERCEPTOR)
    Interceptor providesLogsInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());
            if (BuildConfig.DEBUG) {
                String bodyString = response.body().string();
                Request originalRequest = chain.request();
                Timber.d("Request %s with headers %s ", originalRequest.url(),
                        originalRequest.headers());
                Timber.d("HTTP response code %s %s \n\n with body %s \n\n with headers %s ",
                        response.code(), response.message(), bodyString, response.headers());
                response = response.newBuilder().body(ResponseBody.create(response.body().contentType(),
                        bodyString)).build();
            }
            return response;
        };
    }
}
