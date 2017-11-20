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

	public TiExoplayerModule()
	{
		super();
	}

}

