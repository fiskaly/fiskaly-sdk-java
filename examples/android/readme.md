# Runnable Java/Android SDK sample

This project is a runnable sample for the fiskaly Java/Android SDK.

## Build

First of all, download the [fiskaly Client](https://developer.fiskaly.com/downloads) for Android (e.g. `com.fiskaly.client-android-all-v1.2.200.aar`) and copy the Android Archive (.aar) to `app/libs/`.

Fetch all dependencies:

```bash
$ ./gradlew build
```

## Run example

Install `app/build/outputs/apk/debug/app-debug.apk` on an emulator or a real device and start the app.
