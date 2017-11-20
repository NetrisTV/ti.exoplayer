Requirements
---------------
- Titanium Mobile SDK 6.3.0.GA or later

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
`appc run -p android --build-only` from the `android` directory

Author
---------------
Sergey Volkov <s.volkov@netris.ru>

License
---------------
Apache 2.0
