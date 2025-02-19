# Explanation of the GitHub Actions file `ci.yml`:

`on`: This section specifies the GitHub event triggers. The pipeline is triggered on every push to main and any feature/\* branch, and on every pull request targeting main.

`jobs`: The build job runs on ubuntu-latest and includes several steps:

- **Check out code**: It checks out the code from the repository.
- **Set up JDK 17**: Ensures that Java 17 is set up for your Spring Boot project.
- **Set up Gradle**: Configures the Gradle wrapper to use the specified Gradle version.
- **Build with Gradle**: Runs the build process using Gradle.
- **Run tests (placeholder)**: A placeholder step for running tests (you’ll replace this with real tests in later steps).
