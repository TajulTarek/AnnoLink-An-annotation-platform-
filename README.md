# AnnoLink — An Annotation Platform

## Overview

AnnoLink is a mobile platform that connects researchers who need data annotation with annotators looking for paid work. Researchers can publish annotation jobs and manage applicants, while annotators discover opportunities, apply, collaborate, and get rated based on their contribution quality.

This repository contains the Android app built with Kotlin and Jetpack Compose, backed by Firebase (Authentication, Firestore, Realtime Database, and Storage).

## Key Features

- Posting and browsing annotation jobs
- Job search and filtering
- Application and selection workflow for research projects
- Project-based group chat
- One-to-one direct messaging
- Ratings for annotators by researchers

## Tech Stack

- Kotlin, Android SDK
- Jetpack Compose, Material 3
- AndroidX Navigation, ViewBinding
- Firebase 
## Project Structure

```
root
├─ settings.gradle.kts                 # rootProject.name = "cmbss"
├─ build.gradle.kts                    # root build config
├─ gradle/                             # wrapper
├─ app/
│  ├─ build.gradle.kts                # Android module config, Firebase deps
│  ├─ google-services.json            # Firebase project config (not committed in public repos)
│  └─ src/
│     ├─ main/
│     │  ├─ AndroidManifest.xml       # Activities and launcher entry
│     │  ├─ java/com/example/cmbss/   # Kotlin sources
│     │  │  ├─ MainActivity.kt        # Auth entry screen and navigation
│     │  │  ├─ ...                    # Screens: sign in/up, posts, profiles, chat
│     │  └─ res/                      # UI resources (layouts, drawables, values)
│     └─ test/                        # Unit tests
```

Notable packages and screens (non-exhaustive):

- `MainActivity.kt`: handles sign-in flow and navigation to `studenthomeActivity`
- Authentication: email/password via `FirebaseAuth`
- Jobs and profiles: `AddPostActivity`, `MyPostsActivity`, `MyProfileActivity`, `OtherProfileActivity`
- Applicants and channels: `AllApplicantsActivity`, `MyChannelsActivity`
- Chat: `ChatScreenActivity`, `SingleChatActivity`, `ChatBoxActivity`

## Screenshots

![AnnoLink screenshot](https://github.com/TajulTarek/AnnoLink-An-annotation-platform-/assets/106928817/1c7c43eb-f953-49a0-befd-8c1433813196)

## Requirements

- Android Studio (Giraffe/Flamingo or newer)
- Android SDK 34 (compileSdk 34), targetSdk 33, minSdk 24
- JDK 8+ (Gradle config uses Java 1.8 compatibility)
- A Firebase project with the following services enabled:
  - Authentication (Email/Password)
  - Cloud Firestore
  - Realtime Database
  - Storage

## Local Setup

1. Clone or download this repository.
2. In the Firebase console, create a project and add an Android app with package name `com.example.cmbss`.
3. Download `google-services.json` and place it at `app/google-services.json`.
4. Enable the required Firebase products (Auth, Firestore, Realtime Database, Storage) and set any rules suitable for your development environment.
5. Open the project in Android Studio and let Gradle sync.

## Build and Run

- From Android Studio: Build > Make Project, then Run on an emulator or device.
- Or via Gradle wrapper from the project root:

```bash
./gradlew assembleDebug    # macOS/Linux
gradlew.bat assembleDebug  # Windows
```

Install the generated APK from `app/build/outputs/apk/debug/` onto your device/emulator.

## Configuration Notes

- Application ID and namespace: `com.example.cmbss`
- Compose is enabled with Material 3; ViewBinding is also turned on
- Firebase dependencies are defined in `app/build.gradle.kts` using the BoM
- Launcher activity: `MainActivity` (navigates to `studenthomeActivity` on authenticated sessions)

## Troubleshooting

- Build fails with Firebase/Google Services errors: ensure `google-services.json` is present under `app/` and the Gradle plugin `com.google.gms.google-services` is applied (already configured in this project).
- Authentication or database runtime errors: verify Firebase services are enabled and rules permit your test operations.
- Version conflicts: let Android Studio update the Gradle plugin and dependencies as needed; then re-sync.


## Contributing

Contributions are welcome! Please open an issue to discuss significant changes before submitting a pull request.

