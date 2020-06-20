# PixApp

PixApp is an example of simple application which fetches and displays the list of images from Pixabay API. The motivation of this project is to create a clean architecture by separating business logic from activities and fragments. I have made this possible by implementing MVVM using repository pattern. MVVM separates your view (i.e. Activitys and Fragments) from your business logic. LiveData is used to further eliminate the boilerplate code and extra code required to update the values on UI. Moreover, LiveData is lifecycle aware so instead of updating the UI every time the app data changes, your observer can update the UI every time there's a change. No memory leaks. Observers are bound to Lifecycle objects and clean up after themselves when their associated lifecycle is destroyed. No crashes due to stopped activities. 

MVVM with Clean Architecture is pretty good. It goes one step further in separating the responsibilities of your code base. It clearly abstracts the logic of the actions that can be performed in your app.

# Architecture Pattern

- **MVVM using Repository Pattern**

# Language Used

- **Kotlin**

# Libraries Used

- **Retrofit** - For networking
- **Coroutines** - For getting of the Main thread
- **Moshi** - For parsing the JSON API response to kotlin data objects
- **Room** - For persisting pictures data
- **Glide** - For images rendering

# Project Structure

- .databse - Contains all the code related to setting up database and db entities
- .domain - Contails doman models for the application. In our case they are PixAppPicture and PixAppUser
- .network - Contains the code related to networking. Setting up Retrofit application's network services
- .repositories - Contains the repositories of project. In our case PicturesRepository and UserRepository
- .ui - Contains activities
- .utils - Contains utilities
- .viewmodels - Contains the viewmodels of our application. Each activity/Fragment has its own viewmodel which handles its business logic

# Advantages of Using Clean Architecture

- Code is even more easily testable than with plain MVVM
- Code is further decoupled
- Package structure is even easier to navigate
- Project is easier to maintain
- Team can add new features even more quickly

## Authors

* **Abdullah Nasim**

