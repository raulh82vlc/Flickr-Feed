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

package com.raulh82vlc.flickrj.di.application;

import android.app.Application;
import android.content.Context;

import com.raulh82vlc.flickrj.FlickrApp;
import com.raulh82vlc.flickrj.data.di.NetworkModule;
import com.raulh82vlc.flickrj.data.network.datasource.NetworkDataSource;

import javax.inject.Singleton;

import dagger.Component;

/**
 * ApplicationComponent is the top level component for this architecture.
 * It provides common dependencies
 * and makes them available to sub-components and other external dependant classes.
 * <p/>
 * Scope {@link Singleton} is used to limit dependency instances across whole execution.
 *
 * @author Raul Hernandez Lopez
 */
@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                NetworkModule.class
        })
public interface ApplicationComponent {

    /**
     * Injections for the dependencies
     */
    void inject(FlickrApp app);

    void inject(Context context);

    /**
     * Used in child components
     */
    Application application();

    NetworkDataSource getNetWorkDataSource();
}
