# PickPhotoView

A Library help u to pick photos.

Click to download lastest demo ⬇️ or select [Release Version](https://github.com/Werb/PickPhotoSample/releases)

[![download](/app/src/main/res/mipmap-xhdpi/ic_launcher.png)](https://fir.im/hm38)

## Last Update (2017.6.19)
#### [v0.2.1](https://github.com/Werb/PickPhotoSample/releases/tag/v0.2.1)

1. fix bug when single choose picture
2. fix bug when photo dir not exist
3. add english support


## Update (2017.4.27)

#### [v0.1.5](https://github.com/Werb/PickPhotoSample/releases/tag/v0.1.5)

1. remove Provider in lib Manifest

so you must register Manifest this code in your app AndroidManifest
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

## Screenshot
<img src="/screenshots/custom_1.png" alt="screenshot" title="home" width="270" height="160" /> <img src="/screenshots/custom_2.png" alt="screenshot" title="select" width="270" height="160" /><img src="/screenshots/custom_3.png" alt="screenshot" title="select" width="270" height="160" />


<img src="/screenshots/home.png" alt="screenshot" title="home" width="270" height="480" /> <img src="/screenshots/new_home.png" alt="screenshot" title="select" width="270" height="480" /><img src="/screenshots/new_preview.png" alt="screenshot" title="select" width="270" height="480" />

<img src="/screenshots/list.png" alt="screenshot" title="preview" width="270" height="480" /><img src="/screenshots/camera.png" alt="screenshot" title="group" width="270" height="480" /><img src="/screenshots/show.png" alt="screenshot" title="group2" width="270" height="480" />


## Dependency

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://github.com/Werb/PickPhotoSample/blob/master/LICENSE)
[![last-version](https://api.bintray.com/packages/werbhelius/maven/pickphotoview/images/download.svg) ](https://bintray.com/werbhelius/maven/pickphotoview/_latestVersion)

the last-version is [releases-version](https://github.com/Werb/PickPhotoSample/releases)️

#### Gradle

```
  compile 'com.werb.pickphotoview:pickphotoview:0.2.1'  // Last Version
```

some Library already dependency

* Glide
* Recyclerview
* Gson

If you don't want to dependency this Library version , you can replace it just like

```
  compile ('com.werb.pickphotoview:pickphotoview:last-version',{
        exclude group: 'com.google.code.gson'
  })
  compile 'com.google.code.gson:gson:XXXX'
```

#### Maven

```
  <dependency>
    <groupId>com.werb.pickphotoview</groupId>
    <artifactId>pickphotoview</artifactId>
    <version>last-version</version>
    <type>pom</type>
  </dependency>
```
#### Eclipse

Sorry ，You are out !

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

```
  new PickPhotoView.Builder(context)
        .setPickPhotoSize(9)   //select max size
        .setShowCamera(true)   //is show camera
        .setSpanCount(4)       //SpanCount
        .setLightStatusBar(true)  // custom theme
        .setStatusBarColor("#ffffff")   // custom statusBar
        .setToolbarColor("#ffffff")   // custom toolbar
        .setToolbarIconColor("#000000")   // custom toolbar icon
        .start();
```

#### onActivityResult

```
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
