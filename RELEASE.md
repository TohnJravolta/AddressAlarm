# AddressAlarm Release Checklist

This document describes how to produce a signed release APK, verify it, and prepare the assets needed for a public launch.

## 1. Prerequisites
- Android Studio (latest stable) with the Android Gradle Plugin installed.
- JDK 17 or newer.
- Access to the keystore used to sign AddressAlarm releases.
- `key.properties` file with the keystore path, alias, and passwords (not committed to VCS).

## 2. Update metadata
1. Confirm the app version and version code in `app/build.gradle.kts` reflect the release number.
2. Update `CHANGELOG.md` with a new entry summarizing changes since the last release.
3. Ensure documentation links (README, docs/index.html) reference the upcoming release version.

## 3. Build the release APK
1. In Android Studio, select **Build → Generate Signed Bundle / APK…**.
2. Choose **APK** and select the `release` variant.
3. Provide the keystore credentials and finish the wizard.
4. Alternatively, build from the command line:
   ```bash
   ./gradlew assembleRelease
   ```
5. The signed artifact will appear at `app/build/outputs/apk/release/app-release.apk`.

## 4. Verify the artifact
1. Rename the APK using the pattern `addressalarm-v<MAJOR.MINOR.PATCH>.apk`.
2. Generate a SHA-256 checksum:
   ```bash
   shasum -a 256 addressalarm-v<MAJOR.MINOR.PATCH>.apk
   ```
3. Record the checksum in the release notes and below for future verification.

| Version | SHA-256 checksum |
|---------|------------------|
| _TBD_   | _Populate after signing_ |

## 5. Publish to GitHub Releases
1. Draft a new release at <https://github.com/TohnJravolta/AAT/releases/new>.
2. Tag the release with `v<MAJOR.MINOR.PATCH>` and point it to the `main` branch commit.
3. Paste the changelog entry into the release notes.
4. Upload the signed APK and any supplemental assets (screenshots, checksum file).
5. Publish the release.

## 6. Update the website
1. Edit `docs/index.html` to point the **Download APK** button to the new release asset URL.
2. Update any version-specific copy.
3. Commit and push changes so GitHub Pages serves the latest content.

## 7. QA device setup instructions
1. Smoke-test the release APK on at least one Google Pixel (Android 14 or 13) and one Samsung Galaxy (One UI 6 or 5).
2. Follow the accessibility enablement instructions in `docs/INSTALLATION.md` to confirm wording and screenshots still match reality.
3. Verify that system dialogs present the correct name so users can find the correct service.
4. If OEMs change menu labels, update the installation guide before publishing the release.

Following this checklist ensures each AddressAlarm release is reproducible, verifiable, and accompanied by the information users need to trust the download.
