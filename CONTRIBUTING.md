# Contributing to AddressAlarm

Thanks for your interest in improving AddressAlarm! This guide outlines how to report issues, propose changes, and submit code.

## Code of conduct
Treat everyone with respect. Harassment, discrimination, or abusive behavior will not be tolerated. Violations may result in removal from project spaces.

## Getting started
1. Fork the repository and clone your fork:
   ```bash
   git clone https://github.com/<your-username>/AAT.git
   cd AAT
   ```
2. Install prerequisites:
   - Android Studio (latest stable)
   - JDK 17+
3. Open the project in Android Studio and let Gradle sync.
4. Run the app on an emulator or device to verify your environment.

## Development workflow
1. Create a feature branch: `git checkout -b feature/my-change`.
2. Make your updates following the style guidelines below.
3. Run `./gradlew lint` and `./gradlew test` before committing.
4. Commit with descriptive messages and push to your fork.
5. Open a pull request against `main` describing the motivation, changes, and testing performed.

## Style guidelines
- Follow Android and Kotlin best practices (Kotlin style guide, Jetpack Compose conventions if applicable).
- Keep functions small and focused; prefer dependency injection for testability.
- Write unit tests for new logic and update existing tests as necessary.
- Document public classes and complex methods with KDoc comments.

## Documentation updates
- Update the README and relevant docs when you introduce new features or behavior changes.
- Include screenshots for UI changes where helpful.
- Add an entry to `CHANGELOG.md` under the “Unreleased” section for notable updates.

## Issue triage
- Label new issues appropriately (bug, enhancement, documentation, question).
- Provide reproduction steps or sample data when reporting bugs.
- Help review open pull requests if you are comfortable doing so.

## Questions
If you’re unsure about the process or need feedback before building a feature, open an issue or start a discussion. We’re happy to collaborate!
