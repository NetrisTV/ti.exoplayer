/*
 * Titanium Exoplayer module
 * Copyright (c) 2018 by Netris, CJSC. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ru.netris.mobile.exoplayer;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.offline.DownloadAction;
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction;
import com.google.android.exoplayer2.source.dash.offline.DashDownloadAction;
import com.google.android.exoplayer2.source.hls.offline.HlsDownloadAction;
import com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction;

@Kroll.module(name = "TiExoplayer", id = "ru.netris.mobile.exoplayer")
public class TiExoplayerModule extends KrollModule
{
	private static final String TAG = "TiExoplayerModule";
	public static final String MODULE_NAME = "TiExoplayerModule";

	public static final String PARAMETERS_PROPERTY_PITCH = "pitch";
	public static final String PARAMETERS_PROPERTY_SKIP_SILENCE = "skipSilence";
	public static final String PARAMETERS_PROPERTY_SPEED = "speed";

	public static final String PROPERTY_AD_TAG_URI_EXTRA = "adTagUri";
	public static final String PROPERTY_CONTENT_EXTENSION = "contentExtension";
	public static final String PROPERTY_CONTENT_TYPE = "contentType";
	public static final String PROPERTY_DRM_KEY_REQUEST_PROPERTIES = "drmKeyRequestProperties";
	public static final String PROPERTY_DRM_LICENSE_URL = "drmLicenseUrl";
	public static final String PROPERTY_DRM_MULTI_SESSION_EXTRA = "drmMultiSession";
	public static final String PROPERTY_DRM_SCHEME_UUID_EXTRA = "drmScheme";
	public static final String PROPERTY_LINEAR_GAIN = "linearGain";
	public static final String PROPERTY_NATURAL_SIZE = "naturalSize";
	public static final String PROPERTY_PLAYBACK_PARAMETERS = "playbackParameters";
	public static final String PROPERTY_PREFER_EXTENSION_DECODERS = "preferExtensionDecoders";
	public static final String PROPERTY_SURFACE_TYPE = "surfaceType";

	public static final String EVENT_METADATA = "metadata";
	public static final String EVENT_NATURAL_SIZE_AVAILABLE = "naturalsizeavailable";

	public static final String EVENT_PROPERTY_PIXEL_RATIO = "pixelRatio";

	public static final String EVENT_TRACKS_CHANGED = "trackschange";
	public static final String EVENT_VOLUME_CHANGE = "volumechange";

	@Kroll.constant
	public static final int EXCEPTION_TYPE_SOURCE = ExoPlaybackException.TYPE_SOURCE;

	@Kroll.constant
	public static final int EXCEPTION_TYPE_RENDERER = ExoPlaybackException.TYPE_RENDERER;

	@Kroll.constant
	public static final int EXCEPTION_TYPE_UNEXPECTED = ExoPlaybackException.TYPE_UNEXPECTED;

	@Kroll.constant
	public static final String DRM_WIDEVINE = "widevine";

	@Kroll.constant
	public static final String DRM_PLAYREADY = "playready";

	@Kroll.constant
	public static final String DRM_CLEARKEY = "cenc";

	@Kroll.constant
	public static final int CONTENT_TYPE_DASH = C.TYPE_DASH;

	@Kroll.constant
	public static final int CONTENT_TYPE_HLS = C.TYPE_HLS;

	@Kroll.constant
	public static final int CONTENT_TYPE_SS = C.TYPE_SS;

	@Kroll.constant
	public static final int CONTENT_TYPE_OTHER = C.TYPE_OTHER;

	@Kroll.constant
	public static final int TRACK_TYPE_UNKNOWN = C.TRACK_TYPE_UNKNOWN;

	@Kroll.constant
	public static final int TRACK_TYPE_DEFAULT = C.TRACK_TYPE_DEFAULT;

	@Kroll.constant
	public static final int TRACK_TYPE_AUDIO = C.TRACK_TYPE_AUDIO;

	@Kroll.constant
	public static final int TRACK_TYPE_VIDEO = C.TRACK_TYPE_VIDEO;

	@Kroll.constant
	public static final int TRACK_TYPE_TEXT = C.TRACK_TYPE_TEXT;

	@Kroll.constant
	public static final int TRACK_TYPE_METADATA = C.TRACK_TYPE_METADATA;

	@Kroll.constant
	public static final int TRACK_TYPE_CUSTOM_BASE = C.TRACK_TYPE_CUSTOM_BASE;

	@Kroll.constant
	public static final int FORMAT_HANDLED = RendererCapabilities.FORMAT_HANDLED;

	@Kroll.constant
	public static final int FORMAT_EXCEEDS_CAPABILITIES = RendererCapabilities.FORMAT_EXCEEDS_CAPABILITIES;

	@Kroll.constant
	public static final int FORMAT_UNSUPPORTED_DRM = RendererCapabilities.FORMAT_UNSUPPORTED_DRM;

	@Kroll.constant
	public static final int FORMAT_UNSUPPORTED_SUBTYPE = RendererCapabilities.FORMAT_UNSUPPORTED_SUBTYPE;

	@Kroll.constant
	public static final int FORMAT_UNSUPPORTED_TYPE = RendererCapabilities.FORMAT_UNSUPPORTED_TYPE;

	@Kroll.constant
	public static final int ADAPTIVE_SEAMLESS = RendererCapabilities.ADAPTIVE_SEAMLESS;

	@Kroll.constant
	public static final int ADAPTIVE_NOT_SEAMLESS = RendererCapabilities.ADAPTIVE_NOT_SEAMLESS;

	@Kroll.constant
	public static final int ADAPTIVE_NOT_SUPPORTED = RendererCapabilities.ADAPTIVE_NOT_SUPPORTED;

	@Kroll.constant
	public static final int SURFACE_TYPE_NONE = 0;

	@Kroll.constant
	public static final int SURFACE_TYPE_SURFACE_VIEW = 1;

	@Kroll.constant
	public static final int SURFACE_TYPE_TEXTURE_VIEW = 2;

	private String downloadActionFile = "actions";
	private String downloadTrackerActionFile = "tracked_actions";
	private String downloadContentDirectory = "downloads";
	private int maxSimultaneousDownloads = 2;
	private static final DownloadAction.Deserializer[] DOWNLOAD_DESERIALIZERS =
		new DownloadAction.Deserializer[] { DashDownloadAction.DESERIALIZER, HlsDownloadAction.DESERIALIZER,
											SsDownloadAction.DESERIALIZER, ProgressiveDownloadAction.DESERIALIZER };

	private static DownloadTrackerProxy dtProxy = null;
	private static TiExoplayerModule self;

	public TiExoplayerModule()
	{
		super();
		self = this;
	}

	public static TiExoplayerModule getInstance()
	{
		return self;
	}

	public void releaseDownloadTrackerProxy()
	{
		dtProxy = null;
	}

	// clang-format off
	@Kroll.method
	@Kroll.getProperty
	public DownloadTrackerProxy getDownloadTrackerProxy()
	// clang-format on
	{
		if (dtProxy == null) {
			dtProxy = new DownloadTrackerProxy();
		}
		return dtProxy;
	}

	@Kroll.getProperty(name = "DEFAULT_PLAYBACK_PARAMETERS")
	public KrollDict getDefaultPlaybackParameters()
	{
		return VideoPlayerProxy.getPlaybackParametersDict(PlaybackParameters.DEFAULT);
	}
}
