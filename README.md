# Functionality and Requirements
- **Main screen**: Displays a list of to-do items.
- **To-do item UI**: Each to-do item has a title, a timestamp of when it was created, and a delete button to remove it.
- **Bottom sheet**: Users can add and input a title for to-do items.
- **Database implementation**: Use the `android.database.sqlite` library to implement CRUD without any extra dependencies.
- **Scaling consideration**: Implement limit-offset pagination to load chunks of data and use Kotlin coroutines for asynchronous tasks.
- **Performance**: Key optimizations for LazyColumn loading and pagination for large data sets.
- **Testing**: Unit tests for SQLite database operations. Achieve 100% method coverage for the `TodoDatabaseHelper` class (cover all methods).

Testing: 

![WX20240905-171347@2x.png](WX20240905-171347%402x.png)

Video:

[Screen Recording 2024-09-05 at 6.37.14 PM.mov](Screen%20Recording%202024-09-05%20at%206.37.14%E2%80%AFPM.mov)


# How to run
- **Build and run**: Clone the repository and open it in Android Studio.
- **APK**: [MiniTodo.apk](MiniTodo.apk)

# Project Structure
This mini-project follows the MVVM pattern and Clean Architecture. The code is organized into `domain`, `presentation`, and `data` packages. There is also a `di` package for placing object creation code, aiming for separation of concerns.

## Library Dependencies
**Highlight: Minimum library dependencies.**

Since this mini-project demonstrates proficiency with the native Android SDK, I use as few libraries as possible, particularly in the data layer. No third-party libraries are used in this project.

**UI**: Uses official Jetpack Compose and Material 3 for basic UI widgets, a to-do item list implementation, and ViewModel to link the view and data layers.

**Dependency Injection**: Since this is a single-page app, no DI library is used. However, to separate object creation from operation logic, I centralized the dependency creation logic under `di/Injector.kts`. This follows a manual service locator pattern. (For production, using Dagger Hilt or Koin would be more appropriate and necessary, but this is sufficient for the mini-project and helps illustrate the essence of DI: lifespan scope control and separation of creation logic.)

**Data**: The data layer contains only DTO and database-related classes. For the database, I used the relatively low-level `android.database.sqlite` library instead of Room DB. Although Room DB offers more powerful model mapping functionalities and supports cursor-based pagination and coroutines, I implemented limit-offset pagination with the SQLite library to minimize dependencies.

**Async Operations**: Kotlin's built-in coroutines are used to avoid time-consuming operations on the main thread.

# Implementation Details
Here are some challenges and solutions for this mini-project:

**How to ensure good UI performance when displaying a list?**

Similar to RecyclerView in the View-based UI, Compose UI has its equivalent in LazyList (LazyColumn in this case). Using a key (TodoItem.id) to identify each item optimizes performance by letting the runtime know that an item with the same ID hasn’t changed (similar to StableId in RecyclerView).

**How to display large amounts of data, like 2000 items?**

Pagination is the solution for seamless incremental loading.

There are multiple ways to implement pagination. I chose limit-offset pagination, which loads chunks of data based on page size (limit) and offset. Each new set of loaded data is added to memory, preventing the need to load all the data at once.
The reason I didn't use cursor-based pagination is:
1. It’s more complex to implement, requiring tracking of the last loaded item and passing the cursor back and forth.
2. It requires an ordered attribute to sort all items.

While doable, it is not as flexible as limit-offset pagination (for example, if we need to sort items by time or title, more work is required).

**When to trigger the next page load**

To ensure a seamless user experience, I set the trigger condition: if the index of the last item in the view is greater than half of the loaded items, the next page will load. This prevents users from getting stuck at the last item.

# In the end
That's all about my mini homework introduction. Thanks to the team for the opportunity!

\- Tianqi
