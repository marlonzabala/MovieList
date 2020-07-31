# Top US Movies

Shows the top US Movie using itunes api

JSON API endpoints used on app:

# Test Branching

- https://rss.itunes.apple.com/api/v1/us/movies/top-movies/all/100/explicit.json
- https://itunes.apple.com/search?term=search

More info about this api here:
https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/

You can also search all movies on the database

Clicking on an item will direct you to the Details Activity, giving a long description about the selected item.

Retrofit library is used to Read and parse JSON connections

# Persistence
The app stores data on a database using Room Library.

All data is added to database and will be used when device is offline

# MVVM architecture
The app uses MVVM architecture in Kotlin

MVVM was chosen because it is recommended by Google.

With the use of live data, components are less coupled which results in less buggy code.

## Download APK here:

https://github.com/marlonzabala/MovieList/raw/master/MovieList.apk
