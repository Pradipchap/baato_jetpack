
# Map-Based Jetpack Compose App

  

This is a Jetpack Compose-based Android application that utilizes [MapLibre](https://maplibre.org/) and the [Baato API](https://baato.io/) for map rendering, geocoding, and navigation features.

  

## Features

- Display maps using MapLibre with Baato tiles

- Add and display multiple markers on the map

- Search locations using Baato's geocoding API

- Display routes and navigation assistance

- Jetpack Compose UI for modern and intuitive design

  

## Prerequisites

Before running the project, ensure you have:

- Android Studio (latest version recommended)

- API key from [Baato](https://baato.io/)

- Internet connection for fetching map data

  

## Installation

1. Clone the repository:

```sh

git clone https://github.com/Pradipchap/baato_jetpack

cd baato-assessment

```

2. Open the project in Android Studio.

  

3. Add your Baato API key in `local.properties`:

```properties

BAATO_API_KEY=your_api_key_here

```

4. Sync the project and build.

  

5. Run the app on an emulator or physical device.

 

  
  ## Location Sharing
  **To share location and open on same app, we must implement deep linking**
  

 1. **Create Sha256 fingerprint:** In the top right corner of the android studio , clikc on the gradle button and click on "Execute Gradle Task" button. Run "gradle signinReport" . This will display sha256 fingerprint.
    <img width="395" alt="Screenshot 2025-03-19 at 4 38 31 AM" src="https://github.com/user-attachments/assets/65c1060d-2816-43d3-a5d3-66b7b53ba08b" /><img width="1425" alt="Screenshot 2025-03-19 at 4 40 27 AM" src="https://github.com/user-attachments/assets/deded283-e36a-490d-9126-8fe6abd1f948" />


 2. **Deploy digital-assestlinks.json file:** Create a .well-known folder in the root of a web project and inside it create digital-assetlinks.json file with following content
 ```json
 [{
  "relation": ["delegate_permission/common.handle_all_urls"],
  "target": {
    "namespace": "android_app",
    "package_name": <"your_package_name">, 
    "sha256_cert_fingerprints": ["<sha256_fingerprint>"] 

  }
}]
 
  ```

 3. **Deploy the web app:** Now deploy the webapp with the digital assest and you will be ready for deep linking.




## Usage

- **Viewing the map:** The default map view loads on startup.

- **Adding markers:** The app dynamically displays markers based on provided location data.

- **Searching locations:** Users can search locations using Baato’s geocoding API.

- **Share Location:** Share your location between friends and family

  

## Dependencies

Add the following dependencies to your `build.gradle`:

```kotlin

dependencies {

implementation(libs.androidx.material3)

implementation(libs.maplibre.compose)

implementation("com.squareup.retrofit2:retrofit:2.9.0")

implementation("com.squareup.retrofit2:converter-gson:2.9.0")

implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

implementation("com.google.android.gms:play-services-location:21.0.1")

}

```

  
  

## Troubleshooting

- **Markers not visible?** Ensure you are adding them correctly and that the API key is valid.

- **Map not loading?** Check your internet connection and API key setup.

- **Crashes on Android?** Make sure all dependencies are up to date and configured properly.

  

## License

This project is open-source and licensed under the MIT License.

  

## Author

[Pradip Chapagain](https://github.com/your-profile)
