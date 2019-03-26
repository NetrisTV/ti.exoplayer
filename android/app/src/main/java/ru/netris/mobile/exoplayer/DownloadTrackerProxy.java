package ru.netris.mobile.exoplayer;

import android.app.Activity;
import android.net.Uri;

import com.google.android.exoplayer2.offline.DownloadAction;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction;
import com.google.android.exoplayer2.source.dash.offline.DashDownloadAction;
import com.google.android.exoplayer2.source.hls.offline.HlsDownloadAction;
import com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import java.io.File;

@Kroll.proxy
public class DownloadTrackerProxy extends KrollProxy implements DownloadTracker.Listener
{
	private static final String TAG = "DownloadTrackerProxy";
	private String downloadActionFile = "actions";
	private String downloadTrackerActionFile = "tracked_actions";
	private String downloadContentDirectory = "downloads";
	private int maxSimultaneousDownloads = 2;
	private static final DownloadAction.Deserializer[] DOWNLOAD_DESERIALIZERS =
		new DownloadAction.Deserializer[] { DashDownloadAction.DESERIALIZER, HlsDownloadAction.DESERIALIZER,
											SsDownloadAction.DESERIALIZER, ProgressiveDownloadAction.DESERIALIZER };

	private File downloadDirectory;
	private Cache downloadCache;
	private DownloadManager downloadManager;
	private DownloadTracker downloadTracker;
	private String userAgent;

	public DownloadTrackerProxy()
	{
		userAgent = Util.getUserAgent(TiApplication.getAppRootOrCurrentActivity(), TiExoplayerModule.MODULE_NAME);
	}

	/** Returns a {@link HttpDataSource.Factory}. */
	public HttpDataSource.Factory buildHttpDataSourceFactory(TransferListener<? super DataSource> listener)
	{
		return new DefaultHttpDataSourceFactory(userAgent, listener);
	}

	/** Returns a {@link DataSource.Factory}. */
	public DataSource.Factory buildDataSourceFactory(TransferListener<? super DataSource> listener)
	{
		DefaultDataSourceFactory upstreamFactory = new DefaultDataSourceFactory(
			TiApplication.getAppRootOrCurrentActivity(), listener, buildHttpDataSourceFactory(listener));
		return buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache());
	}

	public DownloadManager getDownloadManager()
	{
		initDownloadManager();
		return downloadManager;
	}

	public DownloadTracker getDownloadTracker()
	{
		initDownloadManager();
		return downloadTracker;
	}

	private synchronized void initDownloadManager()
	{
		if (downloadManager == null) {
			DownloaderConstructorHelper downloaderConstructorHelper =
				new DownloaderConstructorHelper(getDownloadCache(), buildHttpDataSourceFactory(/* listener= */ null));
			downloadManager = new DownloadManager(
				downloaderConstructorHelper, maxSimultaneousDownloads, DownloadManager.DEFAULT_MIN_RETRY_COUNT,
				new File(getDownloadDirectory(), downloadActionFile), DOWNLOAD_DESERIALIZERS);
			downloadTracker = new DownloadTracker(
				/* context= */ TiApplication.getInstance(), buildDataSourceFactory(/* listener= */ null),
				new File(getDownloadDirectory(), downloadTrackerActionFile), DOWNLOAD_DESERIALIZERS);
			downloadManager.addListener(downloadTracker);
			downloadTracker.addListener(this);
		}
	}

	private synchronized Cache getDownloadCache()
	{
		if (downloadCache == null) {
			File downloadContentDirectory = new File(getDownloadDirectory(), this.downloadContentDirectory);
			downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor());
		}
		return downloadCache;
	}

	private File getDownloadDirectory()
	{
		if (downloadDirectory == null) {
			Activity activity = TiApplication.getAppRootOrCurrentActivity();
			downloadDirectory = activity.getExternalFilesDir(null);
			if (downloadDirectory == null) {
				downloadDirectory = activity.getFilesDir();
			}
		}
		return downloadDirectory;
	}

	private static CacheDataSourceFactory buildReadOnlyCacheDataSource(DefaultDataSourceFactory upstreamFactory,
																	   Cache cache)
	{
		return new CacheDataSourceFactory(cache, upstreamFactory, new FileDataSourceFactory(),
										  /* cacheWriteDataSinkFactory= */ null,
										  CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
										  /* eventListener= */ null);
	}

	// clang-format off
	@Kroll.method
	@Kroll.getProperty
	public String getDownloadContentDirectory()
	// clang-format on
	{
		return downloadContentDirectory;
	}

	// clang-format off
	@Kroll.method
	@Kroll.setProperty
	public void setDownloadContentDirectory(String value)
	// clang-format on
	{
		downloadContentDirectory = value;
	}

	// clang-format off
	@Kroll.method
	@Kroll.getProperty
	public String getDownloadActionFile()
	// clang-format on
	{
		return downloadActionFile;
	}

	// clang-format off
	@Kroll.method
	@Kroll.setProperty
	public void setDownloadActionFile(String value)
	// clang-format on
	{
		downloadActionFile = value;
	}

	// clang-format off
	@Kroll.method
	@Kroll.getProperty
	public String getDownloadTrackerActionFile()
	// clang-format on
	{
		return downloadTrackerActionFile;
	}

	// clang-format off
	@Kroll.method
	@Kroll.setProperty
	public void setDownloadTrackerActionFile(String value)
	// clang-format on
	{
		downloadTrackerActionFile = value;
	}

	// clang-format off
	@Kroll.method
	@Kroll.getProperty
	public int getMaxSimultaneousDownloads()
	// clang-format on
	{
		return maxSimultaneousDownloads;
	}

	// clang-format off
	@Kroll.method
	@Kroll.setProperty
	public void getMaxSimultaneousDownloads(int value)
	// clang-format on
	{
		maxSimultaneousDownloads = value;
	}

	@Kroll.method
	public void toggleDownload(String name, String uriString, String extension)
	{
		Log.d(TAG, "toggleDownload" + name + " " + uriString + " " + extension);
		getDownloadTracker().toggleDownload(TiApplication.getAppCurrentActivity(), name, Uri.parse(uriString),
											extension);
	}

	@Kroll.method
	public boolean isDownloaded(String uriString)
	{
		return getDownloadTracker().isDownloaded(Uri.parse(uriString));
	}

	@Kroll.method
	public void release()
	{
		if (downloadManager != null) {
			downloadManager.removeListener(downloadTracker);
			downloadManager = null;
		}
		if (downloadTracker != null) {
			downloadTracker.removeListener(this);
			downloadTracker = null;
		}
		downloadCache = null;
		TiExoplayerModule.getInstance().releaseDownloadTrackerProxy();
	}

	@Override
	public void onDownloadsChanged()
	{
		Log.d(TAG, "onDownloadsChanged");
	}
}
