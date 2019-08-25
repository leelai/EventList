# EventList
The EventList is a personal event managment app,  provides users with the ability to add, modify, and delete events, as well as click events to view event details.

# Demo
![add event](/preview/add_event.gif)![re-order](/preview/reorder.gif)![add category](/preview/add_new_category.gif)![remove](/preview/swipe_remove.gif)

# Dependencies
*  Kotlin stdlib
*  kotlin-android plugin

# Implementing the app
* This app is composed of five activities; display events, show detailed events, add/edit event, add category and select date time interval.
* Each activity implements [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) (MVP) architecture

![mvp](/preview/mvp.png)
* The data is stored locally in a SQLite database, using [Room](https://developer.android.com/topic/libraries/architecture/room.html).

# Third party library
*  [ActionSheet](https://github.com/WindSekirun/ActionSheet)
*  [Android-ItemTouchHelper-Demo](https://github.com/iPaulPro/Android-ItemTouchHelper-Demo)
*  [DateTimeWidget](https://github.com/techartist/DateTimeWidget)
