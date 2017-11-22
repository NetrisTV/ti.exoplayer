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
* `ExoPlayer.DRM_WIDEVINE`
* `ExoPlayer.DRM_PLAYREADY`
* `ExoPlayer.DRM_CLEARKEY`
* `ExoPlayer.EXCEPTION_TYPE_RENDERER`: [ExoPlaybackException.TYPE_RENDERER](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_RENDERER)
* `ExoPlayer.EXCEPTION_TYPE_SOURCE`: [ExoPlaybackException.TYPE_SOURCE](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_SOURCE)
* `ExoPlayer.EXCEPTION_TYPE_UNEXPECTED`: [ExoPlaybackException.TYPE_UNEXPECTED](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_UNEXPECTED)
* `ExoPlayer.CONTENT_TYPE_DASH`: [C.CONTENT_TYPE_DASH](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.CONTENT_TYPE_HLS`: [C.CONTENT_TYPE_HLS](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.CONTENT_TYPE_SS`: [C.CONTENT_TYPE_SS](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.CONTENT_TYPE_OTHER`: [C.CONTENT_TYPE_OTHER](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_UNKNOWN`: [C.TRACK_TYPE_UNKNOWN](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_DEFAULT`: [C.TRACK_TYPE_DEFAULT](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_AUDIO`: [C.TRACK_TYPE_AUDIO](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_VIDEO`: [C.TRACK_TYPE_VIDEO](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_TEXT`: [C.TRACK_TYPE_TEXT](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_METADATA`: [C.TRACK_TYPE_METADATA](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_CUSTOM_BASE`: [C.TRACK_TYPE_CUSTOM_BASE](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.FORMAT_HANDLED`: [RendererCapabilities.FORMAT_HANDLED](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.FORMAT_EXCEEDS_CAPABILITIES`: [RendererCapabilities.FORMAT_EXCEEDS_CAPABILITIES](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.FORMAT_UNSUPPORTED_DRM`: [RendererCapabilities.FORMAT_UNSUPPORTED_DRM](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.FORMAT_UNSUPPORTED_SUBTYPE`: [RendererCapabilities.FORMAT_UNSUPPORTED_SUBTYPE](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.FORMAT_UNSUPPORTED_TYPE`: [RendererCapabilities.FORMAT_UNSUPPORTED_TYPE](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.ADAPTIVE_SEAMLESS`: [RendererCapabilities.ADAPTIVE_SEAMLESS](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.ADAPTIVE_NOT_SEAMLESS`: [RendererCapabilities.ADAPTIVE_NOT_SEAMLESS](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.ADAPTIVE_NOT_SUPPORTED`: [RendererCapabilities.ADAPTIVE_NOT_SUPPORTED](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)

## VideoPlayer API Difference
### Methods
* `cancelAllThumbnailImageRequests`: not supported
* `requestThumbnailImagesAtTimes`: not supported
* `thumbnailImageAtTime`: not supported

### Events
* `durationAvailable`: removed (was deprecated, use `durationavailable`)
* `playbackState`: removed (was deprecated, use `playbackstate`)
* `error`: removed property `code`, added property `type` (`ExoPlayer.EXCEPTION_TYPE_*`)
* `metadata`: new event, property `metadata`
* `tracksChanged`: new event, property `trackInfo`

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

Apache 2.0, see [LICENSE](../LICENSE)
