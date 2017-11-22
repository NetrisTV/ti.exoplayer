/**
 * Titanium Exoplayer module
 * Copyright (c) 2017 by Netris, CJSC. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ru.netris.mobile.exoplayer;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;


@Kroll.module(name = "TiExoplayer", id = "ru.netris.mobile.exoplayer")
public class TiExoplayerModule extends KrollModule
{

	private static final String LCAT = "TiExoplayerModule";
	private static final boolean DBG = TiConfig.LOGD;
	public static final String MODULE_NAME = "TiExoplayerModule";
	public static final String DRM_SCHEME_UUID_EXTRA = "drmScheme";
	public static final String DRM_LICENSE_URL = "drmLicenseUrl";
	public static final String DRM_KEY_REQUEST_PROPERTIES = "drmKeyRequestProperties";
	public static final String PREFER_EXTENSION_DECODERS = "preferExtensionDecoders";
	public static final String CONTENT_TYPE = "contentType";
	public static final String AD_TAG_URI_EXTRA = "adTagUri";

	public static final String EVENT_TRACKS_CHANGED = "tracksChanged";
	public static final String EVENT_METADATA = "metadata";

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


	public TiExoplayerModule()
	{
		super();
	}

}

