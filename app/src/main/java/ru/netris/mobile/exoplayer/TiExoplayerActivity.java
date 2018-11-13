/*
 * Titanium Exoplayer module
 * Copyright (c) 2009-2012 by Appcelerator, Inc. All Rights Reserved.
 * Copyright (c) 2018 by Netris, CJSC. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ru.netris.mobile.exoplayer;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiLifecycle;
import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.view.TiCompositeLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.LayoutInflater;

import com.google.android.exoplayer2.ui.PlayerView;

public class TiExoplayerActivity extends Activity
{

	private static final String TAG = "TiExoplayerActivity";

	protected TiCompositeLayout layout = null;
	private Messenger proxyMessenger = null;
	private TiLifecycle.OnLifecycleEvent lifecycleListener = null;

	public TiExoplayerActivity()
	{
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.i(TAG, "onCreate");

		final Intent intent = getIntent();

		proxyMessenger = intent.getParcelableExtra(TiC.PROPERTY_MESSENGER);

		if (intent.hasExtra(TiC.PROPERTY_BACKGROUND_COLOR)) {
			ColorDrawable d = new ColorDrawable(intent.getIntExtra(TiC.PROPERTY_BACKGROUND_COLOR, Color.RED));
			getWindow().setBackgroundDrawable(d);
		}

		layout = new TiCompositeLayout(this);
		PlayerView videoView = null;
		LayoutInflater inflater = LayoutInflater.from(this);
		String path = "layout.player_view_surface_view";
		if (intent.hasExtra(TiExoplayerModule.PROPERTY_SURFACE_TYPE)) {
			int surfaceType = intent.getIntExtra(TiExoplayerModule.PROPERTY_SURFACE_TYPE,
												 TiExoplayerModule.SURFACE_TYPE_SURFACE_VIEW);
			if (surfaceType == TiExoplayerModule.SURFACE_TYPE_NONE) {
				path = "layout.player_view_none";
			} else if (surfaceType == TiExoplayerModule.SURFACE_TYPE_TEXTURE_VIEW) {
				path = "layout.player_view_texture_view";
			} else if (surfaceType != TiExoplayerModule.SURFACE_TYPE_SURFACE_VIEW) {
				Log.e(TAG, "Wrong \"" + TiExoplayerModule.PROPERTY_SURFACE_TYPE + "\" property value:" + surfaceType
							   + ". Fallback to SurfaceView");
			}
		}

		try {
			int layout_player_view = TiRHelper.getResource(path);
			videoView = (PlayerView) inflater.inflate(layout_player_view, null, false);
		} catch (TiRHelper.ResourceNotFoundException e) {
			//Custom layout is not provided, it is ok.
		}
		if (videoView == null) {
			videoView = new PlayerView(this);
		}
		layout.addView(videoView, new TiCompositeLayout.LayoutParams());

		setContentView(layout);

		if (proxyMessenger != null) {
			Message msg = Message.obtain();
			msg.what = VideoPlayerProxy.CONTROL_MSG_ACTIVITY_AVAILABLE;
			msg.obj = this;
			try {
				proxyMessenger.send(msg);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to send 'activity available' message to proxy", e);
			}
		}

		Log.d(TAG, "exiting onCreate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		sendProxyMessage(VideoPlayerProxy.CONTROL_MSG_CONFIG_CHANGED);
	}

	@Override
	public void onNewIntent(Intent intent)
	{
		Log.d(TAG, "onNewIntent");
		setIntent(intent);
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		if (lifecycleListener != null) {
			lifecycleListener.onStart(this);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		TiApplication.getInstance().setCurrentActivity(this, this);
		if (lifecycleListener != null) {
			lifecycleListener.onResume(this);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		TiApplication.getInstance().setCurrentActivity(this, null);
		if (lifecycleListener != null) {
			lifecycleListener.onPause(this);
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		if (lifecycleListener != null) {
			lifecycleListener.onPause(this);
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		if (lifecycleListener != null) {
			lifecycleListener.onDestroy(this);
		}
	}

	private void sendProxyMessage(final int messageId)
	{
		if (proxyMessenger != null) {
			Message msg = Message.obtain();
			msg.what = messageId;
			try {
				proxyMessenger.send(msg);
			} catch (RemoteException e) {
				Log.w(TAG, "VideoPlayerProxy no longer available: " + e.getMessage());
			}
		}
	}

	public void setOnLifecycleEventListener(TiLifecycle.OnLifecycleEvent listener)
	{
		lifecycleListener = listener;
	}
}
