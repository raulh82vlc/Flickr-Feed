# Flickr Feed [![Build Status](https://travis-ci.org/raulh82vlc/FlickrFeed.svg?branch=master)](https://travis-ci.org/raulh82vlc/FlickrFeed)
Shows latest Flickr feed images and metadata.
 This project uses *Clean architecture* by means of *Model-View-Presenter (MVP)* for the presentation layer with repository pattern and
 one network data source and another cache data source as well as `Dagger 2` for *Dependency Injection*, trying to respect *SOLID principles* as much as possible.

## Architecture design overview
The exchange between the different *layers* is as follows:
- **Data layer**: from the *Repository*, which is responsible of the *data logic* and communicating results to the *Interactor*
- **Domain layer**: from an *Use Case*, which is responsible of the *business logic* and communicating results to the *Presenter*
- **Presentation layer**: from the *Presenter*, which orchestrates different interactors when required and also provides the final formatted info to a passive `View` from a UI element (fragments / activities).
Finally, this information would be passed through the UI thread.
- All these 3 layers are tested in-depth. Therefore there is no need for UI testing, all the logic is tested.

### Features
- Shows the Flickr public feed as the latest 20 items uploaded by people
- Shows detail view of every feed item and its main meta data
- The code is lowly coupled and highly cohesive to enable SOLID principles
- TDD approach has been followed, using SOLID as a proper strategy to test small units
- Caches images (Picasso) & content on RAM memory (Cache data source)
- Internet connection handling when no connection

### ToDo
New Task is: Search by tag:
- using a SearchView or EditText with a debounce time to enable look after no key is pressed at the main screen to filter by tag
- the idea would be to index a list of items with the same tags under a dictionary or using a Trie instead if wanted to be predictable
- once a word is typed, the presenter asks to the use case, to pass information from the repository
- the repository asks to the cache data source, where would be parsed once the API request was done
- update at the saveFeed method of the cache data source would be required for it

### Test results to open with a web browser
[Test results](./Test_Results_java_in_app.html)
### SDK support
Support SDKs from **19** to **27**

# Disclosure - Libraries used
- [Dagger 2](http://google.github.io/dagger) for Dependency Injection
- Retrofit 2 with OkHttp for network requests
- RxJava 2 for multithreading and network response asynchronous handling
- Gson for easy JSON parsing
- Timber for console logging on debug mode only
- Picasso for image rendering and cache
- [ButterKnife](http://jakewharton.github.io/butterknife) v8.X.X for Views Injection
# Disclosure - Libraries used for testing
- [Mockito](http://site.mockito.org/) for Mocking artifacts
- [JUnit](http://junit.org/) for Unit tests

# References (special thanks) - those are the same I indicated at my personal blog ([Insights and projects](https://raulh82vlc.github.io/Movies-Finder)): 
- [Uncle Bob: The Clean Architecture](https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html) by Uncle Bob
- [The Repository pattern](https://msdn.microsoft.com/en-us/library/ff649690.aspx) by Microsoft
- [Effective Android UI](https://github.com/pedrovgs/EffectiveAndroidUI) by Pedro Gomez
- [Android Clean Architecture](https://github.com/android10/Android-CleanArchitecture) by Fernando Cejas

## About the author
**Raul Hernandez Lopez**,
- [Insights and projects (Personal projects blog)](https://raulh82vlc.github.io)
- [@RaulHernandezL (Twitter)](https://twitter.com/RaulHernandezL)
- [raul.h82@gmail.com](mailto:raul.h82@gmail.com)

# License
```
Copyright (C) 2018 Raul Hernandez Lopez

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
