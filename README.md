A native control for playing videos for Titanium. Based on Google ExoPlayer, using Titanium.Media.VideoPlayer API.

Documentation
---------------
[Module API documentation](documentation/index.md)

Requirements
---------------
- Titanium Mobile SDK 9.0.0.GA or later

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


Build
---------------
With [Appcelerator CLI](https://github.com/appcelerator/appc-install):
>`appc run -p android --build-only`

With [Titanium CLI](https://github.com/appcelerator/titanium):
>`ti build -p android --build-only`

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
