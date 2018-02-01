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
import com.raulh82vlc.flickrj.data.network.FeedApi;
import com.raulh82vlc.flickrj.data.network.connection.ConnectionHandler;
import com.raulh82vlc.flickrj.data.network.connection.ConnectionHandlerImpl;
import com.raulh82vlc.flickrj.data.network.datasource.NetworkDataSource;
import com.raulh82vlc.flickrj.data.network.datasource.NetworkDataSourceImpl;

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
 * Module which provides activity scope context
 * and satisfies both activity & fragment dependencies
 *
 * @author Raul Hernandez Lopez
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    NetworkDataSource provideNetDataSource(FeedApi api, ConnectionHandler connectionHandler, Gson gson) {
        return new NetworkDataSourceImpl(api, connectionHandler, gson);
    }

    @Provides
    ConnectionHandler provideConnectionHandler(ConnectionHandlerImpl connectionHandler) {
        return connectionHandler;
    }

    @Provides
    public FeedApi provideFeedApi(Retrofit retrofit) {
        return retrofit.create(FeedApi.class);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient(@Named("logging_interceptor") Interceptor interceptor) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(interceptor);
        return okHttpClient.build();
    }

    @Provides
    @Singleton
    public RxJava2CallAdapterFactory providesRxJava2CallAdapter() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit(OkHttpClient okHttpClient,
                                     RxJava2CallAdapterFactory adapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_API)
                .client(okHttpClient)
                .addCallAdapterFactory(adapterFactory)
                .build();
    }

    @Provides
    @Singleton
    @Named("logging_interceptor")
    public Interceptor providesLogsInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());
            if (BuildConfig.DEBUG) {
                String bodyString = response.body().string();
                Request originalRequest = chain.request();
                Timber.d("Request %s with headers %s ", originalRequest.url(), originalRequest.headers());
                Timber.d("HTTP response code %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers());
                response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
            }
            return response;
        };
    }
}