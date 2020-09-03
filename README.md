# FilmFlop
<img src="/previews/preview_1.gif" align="right" width="33%"/>

FilmFlop is a demo movie app to browse TMDB's movies by titles with options to save a movie to the list of favourites, sort the list and delete.
The app demonstrates use of MVVM, Kotlin Coroutines and Android Architecture Components. FilmFlop fetches a data from the network with use of Retrofit integrating persisted data in the database providing offline capabilities via NetworkBoundResource. The app demonstrates how to expose network status using a Resource class that encapsulate both the data and its state. FilmFlop provides an eye-catching design built with Material Components for Android.

## Features
  + Displaying network status while fetching data (circle progress bar when loading, error message and retry button in case of failures).
  + Adding a movie to the list of favourite by pressing the icon in the menu. The icon will changed appropriately indicating an option to remove the movie.
  + Deleting a movie from the favourites with use of contextual action bar.
  + SnackBar with Undo delete option.
  + Sorting a list of favourites by a title or time-added with a use of SupportSQLiteQuery.
  + Changing a layout from a linear to a grid by pressing icon in the menu. The icon will changed appropriately indicating different layout.
  + Opening a movie's trailer on YouTobue.
  + Switching day/night mode.
  + Showing movie's rating in form of stars.
  + Displaying loading animation when loading images into the ImageViews.
  + Providing offline data.
  
 
## Tech stack & Open-source libraries
### Android Architecture Components & good practices </b>
  - DataBinding - the app binds the UI components in the XML layout to data sources using a DataBinding rather than programmatically. The UI calls are done in Binding Adapters reducing boilerplate code in the fragments.
  - Room Persistence - Access app's SQLite database with in-app objects and compile-time checks.
  - NetworkBoundResource - to write the disk and network-bound implementations providing offline capabilities.
  - ViewModel - UI related data holder, lifecycle aware.
  - Saved State module for ViewModel data - that survives background process restart.
  - Lifecycles - Create a UI that automatically responds to lifecycle events.
  - LiveData - Build data objects that notify views when the underlying database changes.
  - Navigation for navigation between different screens, handling up buttons and menu items. 
  - SafeArgs for passing data between fragments.
  - Dagger-Hilt for dependency injection.
  
### Third party
<img src="/previews/preview_2.gif" align="right" width="33%"/>

  - Glide for image loading.
  - Kotlin Coroutines for managing background threads with simplified code and reducing needs for callbacks
  - Retrofit2 & OkHttp3 - to make REST requests to the web service integrated.
  - Moshi to handle the deserialization of the returned JSON to Kotlin data objects.
  - Timber for logs.
  
### Architecture
  - MVVM Architecture 
  - Repository pattern
  
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
FilmFlop uses the [TMDBApi] for constructing RESTful API. Obtain your free API_KEY: https://www.themoviedb.org/signup and paste it to the Constants file to try the app.
