[![Android CI](https://github.com/rajsingha/Bookshelf-App/actions/workflows/android.yml/badge.svg)](https://github.com/rajsingha/Bookshelf-App/actions/workflows/android.yml)
# Bookshelf-App

## Tech Stacks

The application is built using the following technologies and libraries:

- **Hilt**: Hilt is used for dependency injection, simplifying the setup and management of the application's dependencies.

- **Kotlin Flow**: Kotlin Flow is utilized to work with asynchronous data streams, enabling efficient handling of data flows, particularly in the context of coroutines.

- **Coil**: Coil is used as the image loading library to efficiently load and cache images in the app.

- **Coroutines**: Coroutines are employed for managing asynchronous and concurrent operations, allowing for responsive and non-blocking code execution.

- **Android Studio**: Android Studio serves as the development environment for creating, testing, and debugging the Android application.

- **Room Database**: Room Database is used for local data storage, providing an efficient and convenient way to manage the application's data.

## Architecture

The project follows a structured architecture combining the principles of MVVM and Clean Architecture. Key architectural components include:

- **MVVM (Model-View-ViewModel)**: The application follows the MVVM architectural pattern, separating the concerns of data (Model), presentation logic (ViewModel), and UI (View).

- **Clean Architecture**: The codebase is organized following Clean Architecture principles, separating the application into layers like Domain, Data, and Presentation. This architecture promotes modularity and testability.

- **Modularization**: The application is modularized to divide it into separate feature modules, allowing for better code organization and reusability. Each module is self-contained and can be developed and tested independently.

## Getting Started

To run the project locally and explore the book management and reading platform, follow these steps:

### Prerequisites

- Android Studio with Kotlin support
- Android Emulator or a physical Android device

### Installation

1. Clone the repository:

   ```bash[
   git clone https://github.com/rajsingha/bookshelf-app.git
   cd bookshelf-app
