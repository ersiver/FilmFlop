# FilmFlop
<img src="/previews/preview_1.gif" align="right" width="33%"/>

FilmFlop is a demo movie app to browse TMDB's movies by titles with options to save a movie to the list of favourites, sort the list and delete.
The app demonstrates use of MVVM, Kotlin Coroutines and Android Architecture Components. FilmFlop fetches a data from the network with use of Retrofit integrating persisted data in the database providing offline capabilities via NetworkBoundResource. The app demonstrates how to expose network status using a Resource class that encapsulate both the data and its state. FilmFlop provides an eye-catching design built with Material Components for Android.

## Overview

  + Displaying network status while fetching data (circle progress bar when loading, error message and retry button in case of failures).
  + Allow users to mark a movie as a favorite in the details view by tapping a button (heart). The icon will changed appropriately indicating an option to remove the movie.
  + Deleting a movie from the favourites with use of contextual action bar.
  + SnackBar with Undo delete option.
  + Sorting a list of favourites by a title or time-added with a use of SupportSQLiteQuery.
  + Changing a layout from a linear to a grid by pressing icon in the menu. The icon will changed appropriately indicating different layout.
  + Allow users to view and play trailers in the YouTube.
  + Switching day/night mode.
  + Showing movie's rating in form of stars.
  + Displaying loading animation when loading images into the ImageViews.
  + Providing offline data.
  
 
## Tech stack & Open-source libraries
### Android Architecture Components & good practices: </b>
  - DataBinding - the app binds the UI components in the XML layout to data sources using a DataBinding rather than programmatically. The UI calls are done in Binding Adapters reducing boilerplate code in the fragments.
  - Room Persistence - Access app's SQLite database with in-app objects and compile-time checks.
  - NetworkBoundResource - to write the disk and network-bound implementations providing offline capabilities.
  - ViewModel - UI related data holder, lifecycle aware.
  - Lifecycles - Create a UI that automatically responds to lifecycle events.
  - LiveData - Build data objects that notify views when the underlying database changes.
  - Navigation for navigation between different screens, handling up buttons and menu items. 
  - SafeArgs for passing data between fragments.
  - Dagger-Hilt for dependency injection.
  
### Third party:
<img src="/previews/preview_2.gif" align="right" width="33%"/>

  - Glide for image loading.
  - Kotlin Coroutines for managing background threads with simplified code and reducing needs for callbacks
  - Retrofit2 & OkHttp3 - to make REST requests to the web service integrated.
  - Moshi to handle the deserialization of the returned JSON to Kotlin data objects.
  - Timber for logs.
  
### Architecture:
  - MVVM Architecture 
  - Repository pattern
  
## Testing 
###  Device Tests:
 -  <b>App Navigation Test</b> - Navigation between screens is tested using Espresso UI framework and ActivityScenario for lifecycle state. `Hilt` provides test version of Repository and automatically generates a new set of components for each test. This is done with use of a `CustomTestRunner` that uses an Application configured with Hilt. In order to make Espresso aware of data binding`DataBindingIdlingResource` is registered for UI test.
  - <b>Database Testing</b> - The project creates an in memory database for each database test but still runs them on the device. 
### Local Unit Tests:
  - <b>ViewModel Tests</b> - ViewModels are tested using local unit tests with mock Repository implementations.
  - <b>Webservice Tests</b> - The project uses MockWebServer project to test REST api interactions. 
  - <b> Repository Tests</b> - Repository is tested using local unit tests with mock web service and mock database.  

  
## Design
+ FilmFLop is built with Material Components for Android.
+ The app has a navigation drawer, which provides access to destinations and app functionality, such as switching mode.
+ The home screen demonstrates transformation of a top app bar into a contextual action bar to provide delete actions to selected movie. 
+ The detail screen demonstrates prominent top app bar to house a movie's poster and to provide a nice presence.
+ The search screen presents customized outlined text field. The text field has a clear icon to let user clear an entry. It also indicates invalid inputs. 
+ The buttons, menus and progress indicators are customized for colors, shapes and typography.
+ All clickable components behave intuitively changing their appearance when they are pressed.
+ The app has beautiful colors schemes for day and night modes.

## Preview
<img src="/previews/screenshot_1.png" width="33%" /> <img src="/previews/screenshot_2.png" width="33%" />

<img src="/previews/screenshot_3.png" width="33%"/> <img src="/previews/screenshot_4.png" width="33%"/>

<img src="/previews/screenshot_5.png" width="33%"/> <img src="/previews/screenshot_6.png" width="33%"/>  

<img src="/previews/screenshot_7.png" width="33%"/> <img src="/previews/screenshot_8.png" width="33%"/>                                        

## Open API
FilmFlop uses the TMDBApi for constructing RESTful API. Obtain your free [API_KEY:](https://www.themoviedb.org/signup) and paste it to the Constants file to try the app.

## License

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2020 Ersiver

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
