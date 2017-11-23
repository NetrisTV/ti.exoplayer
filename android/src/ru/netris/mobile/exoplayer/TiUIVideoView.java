/**
 * Titanium Exoplayer module
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Copyright (c) 2017 by Netris, CJSC. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ru.netris.mobile.exoplayer;

import java.util.UUID;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiUIView;

import org.json.JSONException;
import org.json.JSONObject;

import ti.modules.titanium.media.TiPlaybackListener;
import ti.modules.titanium.media.MediaModule;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.MediaController;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TiUIVideoView extends TiUIView implements EventListener,
		PlaybackControlView.VisibilityListener, MetadataRenderer.Output

{
	private static final String TAG = "TiUIVideoView";

	private Activity activity;
	private boolean inErrorState;
	private TrackGroupArray lastSeenTrackGroupArray;
	private boolean readyFired = false;
	private boolean shouldAutoPlay;
	private int resumeWindow;
	private long resumePosition;
	private EventLogger eventLogger;
	private SimpleExoPlayerView videoView;
	private MediaController mediaController;

	private DataSource.Factory mediaDataSourceFactory;
	private SimpleExoPlayer player;
	private DefaultTrackSelector trackSelector;

	private Handler mainHandler;

	private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

	// Fields used only for ad playback. The ads loader is loaded via reflection.

	private Object imaAdsLoader; // com.google.android.exoplayer2.ext.ima.ImaAdsLoader
	private Uri loadedAdTagUri;
	private ViewGroup adOverlayViewGroup;
	private Messenger proxyMessenger = null;

	public TiUIVideoView(TiViewProxy proxy)
	{
		super(proxy);
		TiCompositeLayout.LayoutParams params = getLayoutParams();
		params.autoFillsHeight = true;
		params.autoFillsWidth = true;
	}

	/**
	 * Used when setting video view to one created by our fullscreen TiVideoActivity, in which
	 * case we shouldn't create one of our own in this class.
	 * @param vv instance of SimpleExoPlayerView created by TiExoplayerActivity
	 */
	public void setVideoViewFromActivityLayout(TiExoplayerActivity activity)
	{
		this.activity = activity;
		TiCompositeLayout layout = activity.layout;
		setNativeView(layout);
		for (int i = 0; i < layout.getChildCount(); i++) {
			View child = layout.getChildAt(i);
			if (child instanceof SimpleExoPlayerView) {
				videoView = (SimpleExoPlayerView) child;
				break;
			}
		}
		initView();
	}

	private void initView()
	{
		if (nativeView == null) {
			TiCompositeLayout layout = new TiCompositeLayout(videoView.getContext(), proxy);
			layout.addView(videoView, new TiCompositeLayout.LayoutParams());
			setNativeView(layout);
		}

		if (mainHandler == null) {
			mainHandler = new Handler();
		}

		if (mediaDataSourceFactory == null) {
			mediaDataSourceFactory = buildDataSourceFactory(true);
		}

		videoView.setControllerVisibilityListener(this);
		initializePlayer();
	}

	@Override
	public void processProperties(KrollDict d)
	{
		if (videoView == null) {
			activity = proxy.getActivity();
			videoView = new SimpleExoPlayerView(activity);
			initView();
		}
		super.processProperties(d);

		if (videoView == null || player == null) {
			return;
		}

		getPlayerProxy().fireLoadState(MediaModule.VIDEO_LOAD_STATE_UNKNOWN);

		// Proxy holds the scaling mode directly.
		setScalingMode(getPlayerProxy().getScalingMode());
		// Proxy holds the media control style directly.
		setMediaControlStyle(getPlayerProxy().getMediaControlStyle());
		// Proxy holds the repeat mode directly.
		setRepeatMode(getPlayerProxy().getRepeatMode());

		if (d.containsKey(TiC.PROPERTY_VOLUME)) {
			player.setVolume(TiConvert.toFloat(d, TiC.PROPERTY_VOLUME, 1.0f));
		}
		if (d.containsKey(TiC.PROPERTY_AUTOPLAY)) {
			shouldAutoPlay = TiConvert.toBoolean(d, TiC.PROPERTY_AUTOPLAY);
			player.setPlayWhenReady(shouldAutoPlay);
		}
	}

	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy)
	{
		if (videoView == null) {
			return;
		}

		if (key.equals(TiC.PROPERTY_URL) || key.equals(TiC.PROPERTY_CONTENT_URL)) {
			if (newValue != null) {
				getPlayerProxy().fireLoadState(MediaModule.VIDEO_LOAD_STATE_UNKNOWN);
				initializePlayer();
			} else {
				player.stop();
			}
		} else if (key.equals(TiC.PROPERTY_SCALING_MODE)) {
			setScalingMode(TiConvert.toInt(newValue));
		} else if (key.equals(TiC.PROPERTY_VOLUME)) {
			player.setVolume(TiConvert.toFloat(newValue));
		} else if (key.equals(TiC.PROPERTY_REPEAT_MODE)) {
			setRepeatMode(TiConvert.toInt(newValue));
		} else if (key.equals(TiC.PROPERTY_AUTOPLAY)) {
			shouldAutoPlay = TiConvert.toBoolean(newValue);
			if (player != null) {
				player.setPlayWhenReady(shouldAutoPlay);
			}
		} else {
			super.propertyChanged(key, oldValue, newValue, proxy);
		}
	}

	public SimpleExoPlayer getPlayer()
	{
		return player;
	}


	// PlaybackControlView.VisibilityListener implementation
	@Override
	public void onVisibilityChange(int visibility)
	{
		Log.d(TAG, "onVisibilityChange");
	}

	public boolean isPlaying()
	{
		if (player == null) {
			return false;
		}

		int state = player.getPlaybackState();

		if (state == Player.STATE_READY && player.getPlayWhenReady()) {
			return true;
		}
		return false;
	}

	public void setScalingMode(int mode)
	{
		if (player == null) {
			return;
		}

		if (mode == MediaModule.VIDEO_SCALING_ASPECT_FILL) {
			videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
			player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
		} else if (mode == MediaModule.VIDEO_SCALING_ASPECT_FIT) {
			videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
			player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
		} else if (mode == MediaModule.VIDEO_SCALING_MODE_FILL) {
			videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
			player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
		} else if (mode == MediaModule.VIDEO_SCALING_NONE) {
			Log.w(TAG, "unsupported scaling mode VIDEO_SCALING_NONE");
			videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
			player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
		}
	}

	public void setRepeatMode(int mode)
	{
		if (player == null) {
			return;
		}

		if (mode == MediaModule.VIDEO_REPEAT_MODE_NONE) {
			player.setRepeatMode(Player.REPEAT_MODE_OFF);
		} else if (mode == MediaModule.VIDEO_REPEAT_MODE_ONE) {
			player.setRepeatMode(Player.REPEAT_MODE_ONE);
		}
	}

	public void setMediaControlStyle(int style)
	{
		if (videoView == null) {
			return;
		}

		boolean showController = true;

		switch (style) {
			case MediaModule.VIDEO_CONTROL_DEFAULT:
			case MediaModule.VIDEO_CONTROL_EMBEDDED:
			case MediaModule.VIDEO_CONTROL_FULLSCREEN:
				showController = true;
				break;
			case MediaModule.VIDEO_CONTROL_HIDDEN:
			case MediaModule.VIDEO_CONTROL_NONE:
				showController = false;
				break;
		}

		videoView.setUseController(showController);
	}

	public void hideMediaController()
	{
		if (mediaController != null && mediaController.isShowing()) {
			mediaController.hide();
		}
	}

	public void play()
	{
		if (player == null) {
			return;
		}
		int state = player.getPlaybackState();
		boolean playWhenReady = player.getPlayWhenReady();

		if (state == Player.STATE_READY || state == Player.STATE_BUFFERING) {
			if (!playWhenReady) {
				player.setPlayWhenReady(true);
				return;
			}
			Log.w(TAG, "play() ignored, already playing");
			return;
		}

		if (state == Player.STATE_IDLE) {
			// Url not loaded yet. Do that first.
			Object urlObj = proxy.getProperty(TiC.PROPERTY_URL);
			if (urlObj == null) {
				Log.w(TAG, "play() ignored, no url set.");
				return;
			}
			getPlayerProxy().fireLoadState(MediaModule.VIDEO_LOAD_STATE_UNKNOWN);
			initializePlayer();
		}
		player.setPlayWhenReady(true);
	}

	public void stop()
	{
		VideoPlayerProxy proxy = getPlayerProxy();
		if (player == null || proxy == null) {
			return;
		}
		player.stop();
		proxy.onPlaybackStopped();
	}

	public void pause()
	{
		if (player == null) {
			return;
		}
		player.setPlayWhenReady(false);
	}

	public int getCurrentPlaybackTime()
	{
		if (player == null) {
			return 0;
		}
		return (int) player.getCurrentPosition();
	}

	public void seek(int milliseconds)
	{
		if (player == null) {
			return;
		}
		player.seekTo((long) milliseconds);
	}

	public void releasePlayer()
	{
		if (player == null) {
			return;
		}
		try {
			player.release();
			player = null;
		} catch (Exception e) {
			Log.e(TAG, "Exception while releasing player", e);
		}
	}

	@Override
	public void release()
	{
		super.release();
		releasePlayer();
		releaseAdsLoader();
		videoView = null;
		mediaController = null;
		trackSelector = null;
		eventLogger = null;
	}

	// Player.EventListener implementation
	@Override
	public void onLoadingChanged(boolean isLoading)
	{
		Log.d(TAG, "onLoadingChanged " + isLoading);
	}

	@Override
	public void onPlaybackParametersChanged(PlaybackParameters playbackParameters)
	{
		Log.d(TAG, "onPlaybackParametersChanged");
	}

	@Override
	public void onPlayerError(ExoPlaybackException e)
	{
		Log.e(TAG, "onPlayerError");
		String errorString = null;
		if (e.type == ExoPlaybackException.TYPE_RENDERER) {
			Exception cause = e.getRendererException();
			if (cause instanceof DecoderInitializationException) {
				// Special case for decoder initialization failures.
				DecoderInitializationException decoderInitializationException =
						(DecoderInitializationException) cause;
				if (decoderInitializationException.decoderName == null) {
					if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
						errorString = activity.getString(R.string.error_querying_decoders);
					} else if (decoderInitializationException.secureDecoderRequired) {
						errorString = activity.getString(R.string.error_no_secure_decoder,
								decoderInitializationException.mimeType);
					} else {
						errorString = activity.getString(R.string.error_no_decoder,
								decoderInitializationException.mimeType);
					}
				} else {
					errorString = activity.getString(R.string.error_instantiating_decoder,
							decoderInitializationException.decoderName);
				}
			}
		}
		if (errorString == null) {
			try {
				errorString = e.getCause().getMessage();
			} catch (Exception ex) {
			}
		}
		VideoPlayerProxy proxy = getPlayerProxy();
		if (proxy != null) {
			proxy.onPlaybackError(e.type, errorString);
		}

//
//		if (errorString != null) {
//			showToast(errorString);
//		}
//		inErrorState = true;
//		if (isBehindLiveWindow(e)) {
//			clearResumePosition();
//			initializePlayer();
//		} else {
//			updateResumePosition();
////			updateButtonVisibilities();  //debug buttons
////			showControls();              //debug controls
//		}

	}

	@Override
	public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
	{
		Log.d(TAG, "onPlayerStateChanged " + playWhenReady + " " + playbackState);
		VideoPlayerProxy proxy = getPlayerProxy();
		if (proxy == null) {
			return;
		}
		if (playbackState == Player.STATE_ENDED) {
			Log.d(TAG, "onPlayerStateChanged STATE_ENDED");
			proxy.onPlaybackComplete();
		} else if (playbackState == Player.STATE_READY) {
			if (!readyFired) {
				readyFired = true;
				proxy.onPlaybackReady((int) player.getDuration());
			}
			if (!playWhenReady) {
				proxy.onPlaybackPaused();
			} else {
				proxy.onPlaybackStarted();
				proxy.firePlaying();
			}
		} else if (playbackState == Player.STATE_BUFFERING) {
			// Do nothing.
		}
	}

	@Override
	public void onPositionDiscontinuity()
	{
		Log.d(TAG, "onPositionDiscontinuity");
		if (inErrorState) {
			// This will only occur if the user has performed a seek whilst in the error state. Update the
			// resume position so that if the user then retries, playback will resume from the position to
			// which they seeked.
			updateResumePosition();
		}
	}

	@Override
	public void onRepeatModeChanged(int repeatMode)
	{
		Log.d(TAG, "onRepeatModeChanged");
	}

	@Override
	public void onTimelineChanged(Timeline timeline, Object manifest)
	{
		Log.d(TAG, "onTimelineChanged");
	}

	@Override
	@SuppressWarnings("ReferenceEquality")
	public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections)
	{
		Log.d(TAG, "onTracksChanged");
		MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
		try {
			JSONObject trackInfo =
					ModuleUtil.buildTrackInfoJSONObject(mappedTrackInfo, trackSelections, player);
			if (trackGroups != lastSeenTrackGroupArray) {
				if (mappedTrackInfo != null) {
					if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
							== MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
						trackInfo.put("unsupportedVideo", true);
					}
					if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
							== MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
						trackInfo.put("unsupportedAudio", true);
					}
				}
				lastSeenTrackGroupArray = trackGroups;
			}
			getPlayerProxy().onTracksChanged(new KrollDict(trackInfo));
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public void onMetadata(Metadata metadata)
	{
		try {
			getPlayerProxy().onMetadata(new KrollDict(ModuleUtil.buildMetadataJSONObject(metadata)));
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private VideoPlayerProxy getPlayerProxy()
	{
		return ((VideoPlayerProxy) proxy);
	}

	private UUID getDrmUuid(String typeString) throws ParserException
	{
		switch (Util.toLowerInvariant(typeString)) {
			case "widevine":
				return C.WIDEVINE_UUID;
			case "playready":
				return C.PLAYREADY_UUID;
			case "cenc":
				return C.CLEARKEY_UUID;
			default:
				try {
					return UUID.fromString(typeString);
				} catch (RuntimeException e) {
					throw new ParserException("Unsupported drm type: " + typeString);
				}
		}
	}

	public void initializePlayer()
	{
		VideoPlayerProxy proxy = getPlayerProxy();
		readyFired = false;
		boolean needNewPlayer = player == null;
		if (needNewPlayer) {
			TrackSelection.Factory adaptiveTrackSelectionFactory =
					new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
			trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
			lastSeenTrackGroupArray = null;
			eventLogger = new EventLogger(trackSelector);

			UUID drmSchemeUuid = null;
			Object scheme = proxy.getProperty(TiExoplayerModule.DRM_SCHEME_UUID_EXTRA);
			if (scheme != null) {
				try {
					drmSchemeUuid = getDrmUuid(TiConvert.toString(scheme));
				} catch (ParserException e) {
					Log.e(TAG, e.getMessage());
				}
			}
			DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
			if (drmSchemeUuid != null) {
				String drmLicenseUrl =
						TiConvert.toString(proxy.getProperty(TiExoplayerModule.DRM_LICENSE_URL));
				String[] keyRequestPropertiesArray = TiConvert.toStringArray((Object[]) proxy.getProperty(
						TiExoplayerModule.DRM_KEY_REQUEST_PROPERTIES));
				int errorStringId = R.string.error_drm_unknown;
				if (Util.SDK_INT < 18) {
					errorStringId = R.string.error_drm_not_supported;
				} else {
					try {
						drmSessionManager = buildDrmSessionManagerV18(drmSchemeUuid, drmLicenseUrl,
								keyRequestPropertiesArray);
					} catch (UnsupportedDrmException e) {
						errorStringId = e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
								? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
					}
				}
				if (drmSessionManager == null) {
					showToast(errorStringId);
					return;
				}
			}

			boolean preferExtensionDecoders = false;
			if (proxy.hasProperty(TiExoplayerModule.PREFER_EXTENSION_DECODERS)) {
				preferExtensionDecoders = TiConvert.toBoolean(
						proxy.getProperty(TiExoplayerModule.PREFER_EXTENSION_DECODERS), false);
			}
			// All extension renderer modules in lib, so default MODE_ON
			@DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
					preferExtensionDecoders
							? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
							: DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON;
			DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(activity,
					drmSessionManager, extensionRendererMode);

			player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
			player.addListener(this);
			player.addListener(eventLogger);
			player.addMetadataOutput(this);
			player.addMetadataOutput(eventLogger);
			player.setAudioDebugListener(eventLogger);
			player.setVideoDebugListener(eventLogger);

			videoView.setPlayer(player);
			player.setPlayWhenReady(shouldAutoPlay);
			setScalingMode(getPlayerProxy().getScalingMode());
		}
		if (!proxy.hasProperty(TiC.PROPERTY_URL)) {
			return;
		}
		Uri uri = Uri.parse(TiConvert.toString(proxy.getProperty(TiC.PROPERTY_URL)));
		Object contentType = proxy.getProperty(TiExoplayerModule.CONTENT_TYPE);
		MediaSource mediaSource = buildMediaSource(uri, contentType);
		String adTagUriString = TiConvert.toString(proxy.getProperty(TiExoplayerModule.AD_TAG_URI_EXTRA));
		if (adTagUriString != null) {
			Uri adTagUri = Uri.parse(adTagUriString);
			if (!adTagUri.equals(loadedAdTagUri)) {
				releaseAdsLoader();
				loadedAdTagUri = adTagUri;
			}
			try {
				mediaSource = createAdsMediaSource(mediaSource, Uri.parse(adTagUriString));
			} catch (Exception e) {
				showToast(R.string.ima_not_loaded);
			}
		} else {
			releaseAdsLoader();
		}
		boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
		if (haveResumePosition) {
			player.seekTo(resumeWindow, resumePosition);
		} else {
			int seekTo = 0;
			Object initialPlaybackTime = proxy.getProperty(TiC.PROPERTY_INITIAL_PLAYBACK_TIME);
			if (initialPlaybackTime != null) {
				seekTo = TiConvert.toInt(initialPlaybackTime);
			}
			// Resuming from an activity pause?
			Object seekToOnResume = proxy.getProperty(VideoPlayerProxy.PROPERTY_SEEK_TO_ON_RESUME);
			if (seekToOnResume != null) {
				seekTo = TiConvert.toInt(seekToOnResume);
				proxy.setProperty(VideoPlayerProxy.PROPERTY_SEEK_TO_ON_RESUME, 0);
			}
			if (seekTo > 0) {
				player.seekTo(seekTo);
			}
		}

		player.prepare(mediaSource, !haveResumePosition, false);
		inErrorState = false;
//		updateButtonVisibilities();  //debug buttons
	}

	private void showToast(int messageId)
	{
		showToast(activity.getString(messageId));
	}

	private void showToast(String message)
	{
		Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	private static boolean isBehindLiveWindow(ExoPlaybackException e)
	{
		if (e.type != ExoPlaybackException.TYPE_SOURCE) {
			return false;
		}
		Throwable cause = e.getSourceException();
		while (cause != null) {
			if (cause instanceof BehindLiveWindowException) {
				return true;
			}
			cause = cause.getCause();
		}
		return false;
	}

	private void clearResumePosition()
	{
		resumeWindow = C.INDEX_UNSET;
		resumePosition = C.TIME_UNSET;
	}

	public void updateResumePosition()
	{
		resumeWindow = player.getCurrentWindowIndex();
		resumePosition = Math.max(0, player.getContentPosition());
	}

	private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid,
	                                                                          String licenseUrl, String[] keyRequestPropertiesArray) throws UnsupportedDrmException
	{
		HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
				buildHttpDataSourceFactory(false));
		if (keyRequestPropertiesArray != null) {
			for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
				drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
						keyRequestPropertiesArray[i + 1]);
			}
		}
		return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback,
				null, mainHandler, eventLogger);
	}

	private MediaSource buildMediaSource(Uri uri, Object overrideContentType)
	{
		int type = overrideContentType == null ? Util.inferContentType(uri)
				: TiConvert.toInt(overrideContentType);
		switch (type) {
			case C.TYPE_SS:
				return new SsMediaSource(uri, buildDataSourceFactory(false),
						new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
			case C.TYPE_DASH:
				return new DashMediaSource(uri, buildDataSourceFactory(false),
						new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
			case C.TYPE_HLS:
				return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
			case C.TYPE_OTHER:
				return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
						mainHandler, eventLogger);
			default: {
				throw new IllegalStateException("Unsupported type: " + type);
			}
		}
	}

	private void releaseAdsLoader()
	{
		if (imaAdsLoader != null) {
			try {
				Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
				Method releaseMethod = loaderClass.getMethod("release");
				releaseMethod.invoke(imaAdsLoader);
			} catch (Exception e) {
				// Should never happen.
				throw new IllegalStateException(e);
			}
			imaAdsLoader = null;
			loadedAdTagUri = null;
			videoView.getOverlayFrameLayout().removeAllViews();
		}
	}

	/**
	 * Returns an ads media source, reusing the ads loader if one exists.
	 *
	 * @throws Exception Thrown if it was not possible to create an ads media source, for example, due
	 *                   to a missing dependency.
	 */
	private MediaSource createAdsMediaSource(MediaSource mediaSource, Uri adTagUri) throws Exception
	{
		// Load the extension source using reflection so the demo app doesn't have to depend on it.
		// The ads loader is reused for multiple playbacks, so that ad playback can resume.
		Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
		if (imaAdsLoader == null) {
			imaAdsLoader = loaderClass.getConstructor(Context.class, Uri.class)
					.newInstance(activity, adTagUri);
			adOverlayViewGroup = new FrameLayout(activity);
			// The demo app has a non-null overlay frame layout.
			videoView.getOverlayFrameLayout().addView(adOverlayViewGroup);
		}
		Class<?> sourceClass =
				Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsMediaSource");
		Constructor<?> constructor = sourceClass.getConstructor(MediaSource.class,
				DataSource.Factory.class, loaderClass, ViewGroup.class);
		return (MediaSource) constructor.newInstance(mediaSource, mediaDataSourceFactory, imaAdsLoader,
				adOverlayViewGroup);
	}

	/**
	 * Returns a new HttpDataSource factory.
	 *
	 * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
	 *                          DataSource factory.
	 * @return A new HttpDataSource factory.
	 */
	private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter)
	{
		return buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
	}

	private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter)
	{
		return new DefaultHttpDataSourceFactory(
				Util.getUserAgent(activity, TiExoplayerModule.MODULE_NAME), bandwidthMeter);
	}

	/**
	 * Returns a new DataSource factory.
	 *
	 * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
	 *                          DataSource factory.
	 * @return A new DataSource factory.
	 */
	private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter)
	{
		return new DefaultDataSourceFactory(TiApplication.getInstance(),
				useBandwidthMeter ? BANDWIDTH_METER : null,
				buildHttpDataSourceFactory(useBandwidthMeter));
	}
}
