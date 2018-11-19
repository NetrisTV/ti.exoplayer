# ti.exoplayer Module

## Description

A native control for playing videos for Titanium.
Based on Google ExoPlayer, using Titanium.Media.VideoPlayer API.

## Accessing the ti.exoplayer Module

To access this module from JavaScript, you would do the following:

    var ExoPlayer = require("ru.netris.mobile.exoplayer");

The `ExoPlayer` variable is a reference to the Module object.

## Methods

* `ExoPlayer.createVideoPlayer`: [`Titanium.Media.createVideoPlayer`](http://docs.appcelerator.com/platform/latest/#!/api/Titanium.Media-method-createVideoPlayer)

## Properties
* `ExoPlayer.DEFAULT_PLAYBACK_PARAMETERS`: [PlaybackParameters.DEFAULT](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/PlaybackParameters.html#DEFAULT), see `VideoPlayer.playbackParameters` properry
* `ExoPlayer.DRM_WIDEVINE`
* `ExoPlayer.DRM_PLAYREADY`
* `ExoPlayer.DRM_CLEARKEY`
* `ExoPlayer.EXCEPTION_TYPE_RENDERER`: [`ExoPlaybackException.TYPE_RENDERER`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_RENDERER)
* `ExoPlayer.EXCEPTION_TYPE_SOURCE`: [`ExoPlaybackException.TYPE_SOURCE`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_SOURCE)
* `ExoPlayer.EXCEPTION_TYPE_UNEXPECTED`: [`ExoPlaybackException.TYPE_UNEXPECTED`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/ExoPlaybackException.html#TYPE_UNEXPECTED)
* `ExoPlayer.CONTENT_TYPE_DASH`: [`C.CONTENT_TYPE_DASH`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.CONTENT_TYPE_HLS`: [`C.CONTENT_TYPE_HLS`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.CONTENT_TYPE_SS`: [`C.CONTENT_TYPE_SS`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.CONTENT_TYPE_OTHER`: [`C.CONTENT_TYPE_OTHER`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_UNKNOWN`: [`C.TRACK_TYPE_UNKNOWN`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_DEFAULT`: [`C.TRACK_TYPE_DEFAULT`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_AUDIO`: [`C.TRACK_TYPE_AUDIO`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_VIDEO`: [`C.TRACK_TYPE_VIDEO`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_TEXT`: [`C.TRACK_TYPE_TEXT`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_METADATA`: [`C.TRACK_TYPE_METADATA`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.TRACK_TYPE_CUSTOM_BASE`: [`C.TRACK_TYPE_CUSTOM_BASE`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/C.html)
* `ExoPlayer.FORMAT_HANDLED`: [`RendererCapabilities.FORMAT_HANDLED`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.FORMAT_EXCEEDS_CAPABILITIES`: [`RendererCapabilities.FORMAT_EXCEEDS_CAPABILITIES`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.FORMAT_UNSUPPORTED_DRM`: [`RendererCapabilities.FORMAT_UNSUPPORTED_DRM`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.FORMAT_UNSUPPORTED_SUBTYPE`: [`RendererCapabilities.FORMAT_UNSUPPORTED_SUBTYPE`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.FORMAT_UNSUPPORTED_TYPE`: [`RendererCapabilities.FORMAT_UNSUPPORTED_TYPE`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.ADAPTIVE_SEAMLESS`: [`RendererCapabilities.ADAPTIVE_SEAMLESS`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.ADAPTIVE_NOT_SEAMLESS`: [`RendererCapabilities.ADAPTIVE_NOT_SEAMLESS`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.ADAPTIVE_NOT_SUPPORTED`: [`RendererCapabilities.ADAPTIVE_NOT_SUPPORTED`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/RendererCapabilities.html)
* `ExoPlayer.SURFACE_TYPE_NONE`
* `ExoPlayer.SURFACE_TYPE_SURFACE_VIEW`
* `ExoPlayer.SURFACE_TYPE_TEXTURE_VIEW`

## VideoPlayer
For full VideoPlayer API see documentation for original [Ti.Media.VideoPlayer](https://docs.appcelerator.com/platform/latest/#!/api/Titanium.Media.VideoPlayer).

 Documentation below describes only additional or removed methods, properties and events.
### Methods
* `cancelAllThumbnailImageRequests`: not supported
* `requestThumbnailImagesAtTimes`: not supported
* `thumbnailImageAtTime`: not supported
* `setTrackSelectionOverride`: new method [`MappingTrackSelector.setSelectionOverride`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/trackselection/MappingTrackSelector.html#setSelectionOverride-int-com.google.android.exoplayer2.source.TrackGroupArray-com.google.android.exoplayer2.trackselection.MappingTrackSelector.SelectionOverride-)
* `clearTrackSelectionOverrides`: new method [`MappingTrackSelector.clearSelectionOverrides`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/trackselection/MappingTrackSelector.html#clearSelectionOverrides-int-)
* `setRendererDisabled`: new method [`MappingTrackSelector.setRendererDisabled`](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/trackselection/MappingTrackSelector.html#setRendererDisabled-int-boolean-)
* `setVolume`: new method. Sets the value of the `volume` property.
* `getVolume`: new method. Gets the value of the `volume` property.
* `setLinearGain`: new method. Sets the value of the `linearGain` property.
* `getLinearGain`: new method. Gets the value of the `linearGain` property.

### Events
* ~~`durationAvailable`~~: removed (was deprecated, use `durationavailable`)
* ~~`playbackState`~~: removed (was deprecated, use `playbackstate`)
* `error`: changed property `code` value (one of `ExoPlayer.EXCEPTION_TYPE_*`)
* `metadata`: new event, property `metadata`
* `naturalsizeavailable`: new event, property `naturalSize`
 (see [VideoRendererEventListener.onVideoSizeChanged](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/video/VideoRendererEventListener.html#onVideoSizeChanged-int-int-int-float-)).
 Fired before a frame is rendered for the first time since setting the surface, and each time there's a change in the size, rotation or pixel aspect ratio of the video being rendered.
* **Deprecated** (use `trackschange`) `tracksChanged`: new event, property `trackInfo`
* `trackschange`: new event, property `trackInfo`
* `volumechange`: new event, property `volume`. Fired when the volume on [`AudioManager.STREAM_MUSIC`](https://developer.android.com/reference/android/media/AudioManager.html#STREAM_MUSIC) changes.

### Properties
* `adTagUri`
* `contentExtension`: type `string`, when defined content type will be detected based on this property
* `contentType`: one of `ExoPlayer.CONTENT_TYPE_*`
* `drmLicenseUrl`
* **Deprecated** (use `drmMultiSession`) `drm_multi_session`: type `boolean`, specify whether multiple key session support is enabled. Default value `false`
* `drmMultiSession`: type `boolean`, specify whether multiple key session support is enabled. Default value `false`
* `drmScheme`: one of `ExoPlayer.DRM_*`
* `drmKeyRequestProperties`
* `linearGain`: Ajusts player [volume](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/SimpleExoPlayer.html#setVolume-float-), with 0 being silence and 1 being unity gain. Default value: 1
* `naturalSize`: Returns the natural size of the movie.
 Returns a dictionary with properties `width`, `height`, `rotation` and `pixelRatio`
 (see [Format](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/Format.html)).
 Returns `0` for all properties if not known or applicable.
 The `naturalsizeavailable` event is fired when the natural size is known.
* `playbackParameters`: [PlaybackParameters](http://google.github.io/ExoPlayer/doc/reference/com/google/android/exoplayer2/PlaybackParameters.html), Object with properties:
  * `pitch`: type `number`, The factor by which the audio pitch will be scaled.
  * `skipSilence`: type `boolean`, Whether to skip silence in the input.
  * `speed`: type `number`, The factor by which playback will be sped up.
* `showsControls`: [`videoPlayer.showsControls`](https://docs.appcelerator.com/platform/latest/#!/api/Titanium.Media.VideoPlayer-property-showsControls)
* `surfaceType`: The type of surface view used for video playbacks. Valid values are `ExoPlayer.SURFACE_TYPE_*`.
* `volume`: Ajusts volume of [`AudioManager.STREAM_MUSIC`](https://developer.android.com/reference/android/media/AudioManager.html#STREAM_MUSIC)


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
