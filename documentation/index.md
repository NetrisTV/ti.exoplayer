# ti.exoplayer Module

## Description

A native control for playing videos for Titanium.
Based on Google ExoPlayer, using Titanium.Media.VideoPlayer API.

## Accessing the ti.exoplayer Module

To access this module from JavaScript, you would do the following:

    var ExoPlayer = require("ru.netris.mobile.exoplayer");

The `ExoPlayer` variable is a reference to the Module object.

## Methods

* `ExoPlayer.createVideoPlayer`: [Titanium.Media.createVideoPlayer](http://docs.appcelerator.com/platform/latest/#!/api/Titanium.Media-method-createVideoPlayer)

## Properties
* `ExoPlayer.EXCEPTION_TYPE_RENDERER`: [ExoPlaybackException.TYPE_RENDERER](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_RENDERER)
* `ExoPlayer.EXCEPTION_TYPE_SOURCE`: [ExoPlaybackException.TYPE_SOURCE](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_SOURCE)
* `ExoPlayer.EXCEPTION_TYPE_UNEXPECTED`: [ExoPlaybackException.TYPE_UNEXPECTED](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_UNEXPECTED)
* `ExoPlayer.DRM_WIDEVINE`
* `ExoPlayer.DRM_PLAYREADY`
* `ExoPlayer.DRM_CLEARKEY`
* `ExoPlayer.CONTENT_TYPE_DASH`
* `ExoPlayer.CONTENT_TYPE_HLS`
* `ExoPlayer.CONTENT_TYPE_SS`
* `ExoPlayer.CONTENT_TYPE_OTHER`

## VideoPlayer API Difference
### Methods
* `cancelAllThumbnailImageRequests`: not supported
* `requestThumbnailImagesAtTimes`: not supported
* `thumbnailImageAtTime`: not supported

### Events
* `durationAvailable`: removed
* `error`: removed property `code`, added property `type` (`ExoPlayer.EXCEPTION_TYPE_*`)

### Properties
* `contentType`: one of `ExoPlayer.CONTENT_TYPE_*`
* `drmScheme`: one of `ExoPlayer.DRM_*`
* `drmLicenseUrl`
* `drmKeyRequestProperties`
* `adTagUri`

## Usage

The following code creates a simple video player to play a local video file.

    var ti_exoplayer = require("ru.netris.mobile.exoplayer");
    var vidWin = Titanium.UI.createWindow({
      title : 'Video View Demo',
      backgroundColor : '#fff'
    });

    var videoPlayer = ti_exoplayer.createVideoPlayer({
      top : 2,
      autoplay : true,
      backgroundColor : 'blue',
      height : 300,
      width : 300,
      mediaControlStyle : Titanium.Media.VIDEO_CONTROL_DEFAULT,
      scalingMode : Titanium.Media.VIDEO_SCALING_ASPECT_FIT
    });

    videoPlayer.url = 'movie.mp4';
    vidWin.add(videoPlayer);
    vidWin.open();

## Author

Sergey Volkov <s.volkov@netris.ru>

## License

Apache 2.0, see [LICENSE](LICENSE)
