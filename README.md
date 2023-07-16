# Final Notes

## Design Choices

The architecture that I have used has been **MVVM** with **LiveData** for dispatching events to the View as it is lifecycle aware and for the communication between the ViewModel and the UseCases I used **Flow**.

I have tried to keep the package structure as if it was for a bigger project, dividing the packages by functionality.
That is, each feature would have had its data, domain, UI and DI module package.

I have decided to use **Hilt** for the dependency injection as it is powerful and easy to use with the ViewModels.

For the http requests, I used **Retrofit** and **Moshi** to parse the responses as I used them previously and think they are reliable and well maintained.

Even if it was not necessary I added **Navigation Component** because it would be easy to expand the app in the future.
Also I added **Ktlint** and **Detekt** plugins to check the code style.

~~I decided to not to use Compose as I wanted to focus more on data/domain layers due to the Timescale for the project.~~
I created a branch named `feature/compose` with the compose version of the app due to it was the weekend and I had some free time.


## Can be improved

- I added a constants file to keep some parameters accessible. In a real app, I would put the API_KEY in a safer place, like in local.properties file to avoid include the key in the repository, or in an external service like some CI/CD have a place to keep the secrets.

- Also I added the BASE_URL on the constants file, but I would be better to create a file in `debug/java/com...` and `release/java/com...` to change the BASE_URL depending on the build variant

- The UI on landscape mode is not responsive and on this mode it shows too much space between the elements, to fix this I would add another XML for landscape with more columns instead of managing this in the ViewModel.

## Future

- A big improvement for the project would be to add Cache, having only one request with no filters, it would be possible to add a table that contains the pages and the repository would manage if request the next page to the DB or Network

- On Github we can add Actions to execute lint checks on every Pull Request to check the code style.
  Also other actions as testing or publishing the app.


## Libraries

- **Moshi** – Json parser
- **Retrofit** – Http client
- **Timber** – Logging
- **Coil** – Image loader and cache
- **Navigation component** – Navigation between views
- **Mockito** – Test mocking
- **Hilt** – Dependency injection
- **Ktlint** – Code linter
- **Detekt** – Code analysis
