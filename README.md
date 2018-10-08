A native control for playing videos for Titanium. Based on Google ExoPlayer, using Titanium.Media.VideoPlayer API.

Documentation
---------------
[Module API documentation](android/documentation/index.md)

Requirements
---------------
- Titanium Mobile SDK 7.0.0.GA or later

Example
---------------
Add the module as a dependency to your application by adding a `<module>` item to the `<modules>` element of your `tiapp.xml` file:
```XML
<ti:app>
  ...
  <modules>
    <module platform="android">ru.netris.mobile.exoplayer</module>
  </modules>
  ...
</ti:app>
```

Use `require()` to access the module from JavaScript:
```JS
    var ExoPlayer = require('ru.netris.mobile.exoplayer');
```

The `ExoPlayer` variable is a reference to the module. Make API calls using this reference:
```JS
    var exoplayer = ExoPlayer.createVideoPlayer();
```

Or include the module as a dependency to a native module by adding a `<module>` item to the `<modules>` element of your `timodule.xml` file:
```XML
<ti:module>
  ...
  <modules>
    <module platform="android">ru.netris.mobile.exoplayer</module>
  </modules>
  ...
</ti:module>
```

Build
---------------
Run  from the project root directory
>`./gradlew tiBuild`

**Important:** module dependencies are not included in this repository. Command above will download them for you.

If for some reasons you want to build this module using only `appc` or `titanium` cli commands, you should download exoplayer libraries (or build them locally) and put into `app/libs` (or `android/lib` which is symlink). After that you should run:

>`appc run -p android --build-only`

or

>`ti build -p android --build-only`

from the `android` directory

Known issues
------------
Application build could stuck on `"Running dexer"` step. To avoid this, increase max memory size for dexer with command:

```ti config android.dx.maxMemory 2048M```

Author
---------------
Sergey Volkov <s.volkov@netris.ru>

License
---------------
Apache 2.0
