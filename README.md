# Yip
This app aims to replicate features of the Yelp app, allowing users to search up businesses, view business details, and view business reviews.

## Key Features:
- Click on Search to search for businesses using search term and Location
- Enable Location Services to get current location (In Progress)
- It uses retrieves data from Yelp Fusion API
- Can view results in Search view, or Map view
- Click on business in resulting list to display Business Details
- Can view various business details by clicking on them in Business Details

# Dependencies:
- Google Play Services Maps
- Google Play Services Location
- Android Volley standard library to make API calls
- Glide v4 for fast and efficient image loading (https://bumptech.github.io/glide/)

# To Run:
- Clone the repo
- Register for a Yelp API Key: https://www.yelp.com/developers/documentation/v3/authentication
- Go to /app/src/main/res/values/yelp_fusion_api.xml file
- Replace `MY_YELP_API` with your Yelp API Key as such: `<string name="yelp_api_key">MY_YELP_API_KEY</string>`
-  Run it on Android Studio emulator or Android device

# API
- Min API 23
- Target API 24

# Screenshots
| | | |
-------------------------|-------------------------|-------------------------
|<img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot1.jpg?raw=true" width="150"> |<img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot2.jpg?raw=true" width="150">| <img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot3.jpg?raw=true" width="150"> |
|<img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot4.jpg?raw=true" width="150">| <img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot5.jpg?raw=true" width="150"> | <img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot6.jpg?raw=true" width="150">|
|<img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot7.jpg?raw=true" width="150"> | <img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot8.jpg?raw=true" width="150">| <img src="https://github.com/TriDangContact/Yip/blob/master/assets/screenshots/Screenshot9.jpg?raw=true" width="150"> | 
