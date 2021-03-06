# fiskaly SDK for Java/JRE and Java/Android

The fiskaly SDK includes an HTTP client that is needed<sup>[1](#fn1)</sup> for accessing the [kassensichv.io](https://kassensichv.io) API that implements a cloud-based, virtual **CTSS** (Certified Technical Security System) / **TSE** (Technische Sicherheitseinrichtung) as defined by the German **KassenSichV** ([Kassen­sich­er­ungsver­ord­nung](https://www.bundesfinanzministerium.de/Content/DE/Downloads/Gesetze/2017-10-06-KassenSichV.pdf)).

## Features

- [X] Automatic authentication handling (fetch/refresh JWT and re-authenticate upon 401 errors).
- [X] Automatic retries on failures (server errors or network timeouts/issues).
- [ ] Automatic JSON parsing and serialization of request and response bodies.
- [X] Future: [<a name="fn1">1</a>] compliance regarding [BSI CC-PP-0105-2019](https://www.bsi.bund.de/SharedDocs/Downloads/DE/BSI/Zertifizierung/Reporte/ReportePP/pp0105b_pdf.pdf?__blob=publicationFile&v=7) which mandates a locally executed SMA component for creating signed log messages. 
- [ ] Future: Automatic offline-handling (collection and documentation according to [Anwendungserlass zu § 146a AO](https://www.bundesfinanzministerium.de/Content/DE/Downloads/BMF_Schreiben/Weitere_Steuerthemen/Abgabenordnung/AO-Anwendungserlass/2019-06-17-einfuehrung-paragraf-146a-AO-anwendungserlass-zu-paragraf-146a-AO.pdf?__blob=publicationFile&v=1))

## Integration

### Maven / Gradle

The fiskaly Java SDK is available via Maven.

#### JRE (version 1.6+)

Add the following to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.fiskaly.sdk:fiskaly-sdk:1.2.200-jre'
}
```

Additionaly to the SDK, you'll also need the fiskaly client. Follow these steps to integrate it into your project:

1. Go to [https://developer.fiskaly.com/downloads](https://developer.fiskaly.com/downloads)
2. Download the appropriate client build for your platform
3. Move the client into your project output directory or somewhere within the OS search path

#### Android

First of all, download the [fiskaly Client](https://developer.fiskaly.com/downloads) for android (e.g. `com.fiskaly.client-android-all-v1.2.200.aar`) and copy the Android Archive to `app/libs/`

Then, add the following to your `app/build.gradle`:

```groovy
dependencies {
    implementation 'com.fiskaly.sdk:fiskaly-sdk:1.2.200-android'
    implementation files('libs/com.fiskaly.client-android-all-v1.2.200.aar')
}
```

## Usage

### Demo

```java
package com.fiskaly.sdk.demo.jre;

import com.fiskaly.sdk.*;

public class Main {
    public static void main(String[] args) throws Exception {
        final String apiKey = System.getenv("FISKALY_API_KEY");
        final String apiSecret = System.getenv("FISKALY_API_SECRET");
        final FiskalyHttpClient client = new FiskalyHttpClient(apiKey, apiSecret, "https://kassensichv.io/api/v1");
        final FiskalyHttpResponse response = client.request("GET", "/tss");
        System.out.println(response);
    }
}
```

## Proguard Configuration

```
-keep class com.sun.jna.* { *; }
-keepclassmembers class * extends com.sun.jna.* { public *; }
```

### Client Configuration

The SDK is built on the [fiskaly Client](https://developer.fiskaly.com/en/docs/client-documentation) which can be [configured](https://developer.fiskaly.com/en/docs/client-documentation#configuration) through the SDK.

## Related

* [fiskaly.com](https://fiskaly.com)
* [dashboard.fiskaly.com](https://dashboard.fiskaly.com)
* [kassensichv.io](https://kassensichv.io)
* [kassensichv.net](https://kassensichv.net)
