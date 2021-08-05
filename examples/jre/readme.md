# Runnable Java/JRE SDK sample

This project is a runnable sample for the fiskaly Java/JRE SDK.

## Build

First of all, download the [fiskaly Client](https://developer.fiskaly.com/downloads) for your platform and copy the library (e.g. `com.fiskaly.client-linux-amd64-v1.2.200.so`) to `src/main/resources/`.

Fetch all dependencies:

```bash
$ ./gradlew build
```

## Run example

First, if you want to test the V1 API,  set environment variables `FISKALY_API_KEY` and `FISKALY_API_SECRET` to your API key and secret:

```bash
export FISKALY_API_KEY=yourAPIkey
 export FISKALY_API_SECRET=yourAPIsecret
 ```
 If you want to test the V2 API, set environment variables `FISKALY_API_KEY_V2` and `FISKALY_API_SECRET_V2` to your API key and secret for V2, in the same way.
 
 If you want to use an existing TSS for V2 instead of creating a new one (e.g. because you have reached the limit of active TSS), also set `FISKALY_TSS_UUID_V2` to the UUID of that TSS. If you do this, you will need to either set `FISKALY_TSS_ADMIN_PIN` to the admin PIN of that TSS, or set `FISKALY_TSS_PUK` to the admin PUK of that TSS. The PUK will only be used if the PIN is empty.
 
 Next, run the sample:

```bash
$ ./gradlew run
```
