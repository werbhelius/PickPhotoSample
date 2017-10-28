# PickPhotoView

A Library help u to pick photos.

Click to download lastest demo ⬇️ or select [Release Version](https://github.com/Werb/PickPhotoSample/releases)

[![download](/app/src/main/res/mipmap-xhdpi/ic_launcher.png)](https://fir.im/hm38)

## Last Update (2017.10.8)
#### [v0.3.6](https://github.com/Werb/PickPhotoSample/releases/tag/v0.3.6)
1. Refactor with kotlin
2. Optimized image load logic
3. Bug fixed

## Screenshot
![one](./screenshots/one.png)
![two](./screenshots/two.png)

## Dependency

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://github.com/Werb/PickPhotoSample/blob/master/LICENSE)
[![last-version](https://api.bintray.com/packages/werbhelius/maven/pickphotoview/images/download.svg) ](https://bintray.com/werbhelius/maven/pickphotoview/_latestVersion)

the last-version is [releases-version](https://github.com/Werb/PickPhotoSample/releases)️

#### Gradle

```
  compile 'com.werb.pickphotoview:pickphotoview:0.3.6'  // Last Version
```

some Library already dependency

* Glide
* Recyclerview
* Gson
* [Moretype - new method to build data in RecyclerView with Kotlin! ](https://github.com/Werb/MoreType)
* [EventBusKotlin - A Simple EventBus](https://github.com/Werb/EventBusKotlin)

If you don't want to dependency this Library version , you can replace it just like

```
  compile ('com.werb.pickphotoview:pickphotoview:last-version',{
        exclude group: 'com.google.code.gson'
  })
  compile 'com.google.code.gson:gson:XXXX'
```

New Version 0.3.6+ build with Kotlin new feature `LayoutContainer` , so you must add this command in your **module build.gradle**
```gradle
apply plugin: 'kotlin-android-extensions'
```
and config this command in `android{}`
```gradle
androidExtensions {
    experimental = true
}
```
**when new feature published in kotlin release version it will built-in code and no need for config**

## Usage

Make sure you have permissions about CAMERA and WRITE／READ_EXTERNAL_STORAGE before use

[PermissionsChecker : A Library help u to check permissions on Android M.](https://github.com/Werb/PermissionsCheckerSample)

#### Register Provider in your app AndroidManifest.xml

```
    <provider
        android:name="com.werb.pickphotoview.provider.PickProvider"
        android:authorities="${applicationId}.provider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/pick_file_paths"/>
    </provider>
```

#### Initialize PickPhotoView

```java
 new PickPhotoView.Builder(MainActivity.this)
    .setPickPhotoSize(1)                  // select image size
    .setClickSelectable(true)             // click one image immediately close and return image
    .setShowCamera(true)                  // is show camera
    .setSpanCount(3)                      // span count
    .setLightStatusBar(true)              // lightStatusBar used in Android M or higher
    .setStatusBarColor(R.color.white)     // statusBar color
    .setToolbarColor(R.color.white)       // toolbar color
    .setToolbarTextColor(R.color.black)   // toolbar text color
    .setSelectIconColor(R.color.pink)     // select icon color
    .setShowGif(false)                    // is show gif
    .start();
```

#### onActivityResult

```java
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            return;
        }
        if(data == null){
            return;
        }
        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            ArrayList<String> selectPaths = (ArrayList<String>) data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);
            // do something u want
        }
    }
```

## License

[Apache2.0](https://github.com/Werb/PickPhotoSample/blob/master/LICENSE)
