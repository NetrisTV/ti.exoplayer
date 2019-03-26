/*
 * Titanium Exoplayer module
 * Copyright (c) 2018 by Netris, CJSC. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ru.netris.mobile.exoplayer;

import android.text.TextUtils;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.GeobFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.metadata.id3.UrlLinkFrame;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.MimeTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class ModuleUtil
{

	public static JSONObject buildTrackInfoJSONObject(MappedTrackInfo mappedTrackInfo,
													  TrackSelectionArray trackSelections, SimpleExoPlayer player)
		throws JSONException
	{
		JSONObject trackInfo = new JSONObject();
		JSONArray renderers = new JSONArray();
		trackInfo.put("renderers", renderers);
		if (mappedTrackInfo != null) {
			if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
				== MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
				trackInfo.put("unsupportedVideo", true);
			}
			if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
				== MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
				trackInfo.put("unsupportedAudio", true);
			}

			for (int rendererIndex = 0; rendererIndex < mappedTrackInfo.getRendererCount(); rendererIndex++) {
				TrackGroupArray rendererTrackGroups = mappedTrackInfo.getTrackGroups(rendererIndex);
				TrackSelection trackSelection = trackSelections.get(rendererIndex);
				if (rendererTrackGroups.length > 0) {
					JSONObject renderer = new JSONObject();
					JSONArray groups = new JSONArray();
					renderer.put("index", rendererIndex);
					renderer.put("type", player.getRendererType(rendererIndex));
					for (int groupIndex = 0; groupIndex < rendererTrackGroups.length; groupIndex++) {
						JSONObject group = new JSONObject();
						group.put("index", groupIndex);
						TrackGroup trackGroup = rendererTrackGroups.get(groupIndex);
						if (trackGroup.length < 2) {
							group.put("adaptive", RendererCapabilities.ADAPTIVE_NOT_SUPPORTED);
						} else {
							group.put("adaptive", mappedTrackInfo.getAdaptiveSupport(rendererIndex, groupIndex, false));
						}
						JSONArray tracks = new JSONArray();
						for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
							JSONObject track = new JSONObject();
							boolean status = getTrackStatus(trackSelection, trackGroup, trackIndex);
							track.put("enabled", status);
							track.put("index", trackIndex);
							track.put("format", buildFormatJSONObject(trackGroup.getFormat(trackIndex)));
							track.put("supported",
									  mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, trackIndex));
							tracks.put(track);
						}
						group.put("tracks", tracks);
						groups.put(group);
					}
					renderer.put("groups", groups);
					if (trackSelection != null) {
						for (int selectionIndex = 0; selectionIndex < trackSelection.length(); selectionIndex++) {
							Metadata metadata = trackSelection.getFormat(selectionIndex).metadata;
							if (metadata != null) {
								renderer.put("metadata", buildMetadataJSONObject(metadata));
								break;
							}
						}
					}
					renderers.put(renderer);
				}
			}
			TrackGroupArray unassociatedTrackGroups = mappedTrackInfo.getUnmappedTrackGroups();
			if (unassociatedTrackGroups.length > 0) {
				JSONObject renderer = new JSONObject();
				JSONArray groups = new JSONArray();
				renderer.put("index", -1);
				for (int groupIndex = 0; groupIndex < unassociatedTrackGroups.length; groupIndex++) {
					JSONObject group = new JSONObject();
					group.put("index", groupIndex);
					group.put("adaptive", RendererCapabilities.ADAPTIVE_NOT_SUPPORTED);
					JSONArray tracks = new JSONArray();
					TrackGroup trackGroup = unassociatedTrackGroups.get(groupIndex);
					for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
						JSONObject track = new JSONObject();
						track.put("enabled", false);
						track.put("index", trackIndex);
						track.put("format", buildFormatJSONObject(trackGroup.getFormat(trackIndex)));
						track.put("supported", RendererCapabilities.FORMAT_UNSUPPORTED_TYPE);
						tracks.put(track);
					}
					group.put("tracks", tracks);
					groups.put(group);
				}
				renderer.put("groups", groups);
				renderers.put(renderer);
			}
		}
		return trackInfo;
	}

	private static JSONObject buildFormatJSONObject(Format format) throws JSONException
	{
		JSONObject jsonFormat = new JSONObject();
		jsonFormat.put("mimeType", format.sampleMimeType);
		jsonFormat.put("id", format.id == null ? JSONObject.NULL : format.id);
		jsonFormat.put("bitrate", format.bitrate == Format.NO_VALUE ? JSONObject.NULL : format.bitrate);
		if (MimeTypes.isVideo(format.sampleMimeType)) {
			jsonFormat.put("width", format.width == Format.NO_VALUE ? JSONObject.NULL : format.width);
			jsonFormat.put("height", format.height == Format.NO_VALUE ? JSONObject.NULL : format.height);

		} else if (MimeTypes.isAudio(format.sampleMimeType)) {
			jsonFormat.put("channelCount",
						   format.channelCount == Format.NO_VALUE ? JSONObject.NULL : format.channelCount);
			jsonFormat.put("sampleRate", format.sampleRate == Format.NO_VALUE ? JSONObject.NULL : format.sampleRate);
			jsonFormat.put("language", TextUtils.isEmpty(format.language) ? JSONObject.NULL : format.language);
		} else {
			jsonFormat.put("language", TextUtils.isEmpty(format.language) ? JSONObject.NULL : format.language);
		}
		return jsonFormat;
	}

	public static JSONObject buildMetadataJSONObject(Metadata metadata) throws JSONException
	{
		JSONObject jsonMetadata = new JSONObject();
		for (int i = 0; i < metadata.length(); i++) {
			Metadata.Entry entry = metadata.get(i);
			if (entry instanceof TextInformationFrame) {
				TextInformationFrame textInformationFrame = (TextInformationFrame) entry;
				jsonMetadata.put("id", textInformationFrame.id);
				jsonMetadata.put("value", textInformationFrame.value);
			} else if (entry instanceof UrlLinkFrame) {
				UrlLinkFrame urlLinkFrame = (UrlLinkFrame) entry;
				jsonMetadata.put("id", urlLinkFrame.id);
				jsonMetadata.put("url", urlLinkFrame.url);
			} else if (entry instanceof PrivFrame) {
				PrivFrame privFrame = (PrivFrame) entry;
				jsonMetadata.put("id", privFrame.id);
				jsonMetadata.put("owner", privFrame.owner);
			} else if (entry instanceof GeobFrame) {
				GeobFrame geobFrame = (GeobFrame) entry;
				jsonMetadata.put("id", geobFrame.id);
				jsonMetadata.put("mimeType", geobFrame.mimeType);
				jsonMetadata.put("filename", geobFrame.filename);
				jsonMetadata.put("description", geobFrame.description);
			} else if (entry instanceof ApicFrame) {
				ApicFrame apicFrame = (ApicFrame) entry;
				jsonMetadata.put("id", apicFrame.id);
				jsonMetadata.put("mimeType", apicFrame.mimeType);
				jsonMetadata.put("description", apicFrame.description);
			} else if (entry instanceof CommentFrame) {
				CommentFrame commentFrame = (CommentFrame) entry;
				jsonMetadata.put("id", commentFrame.id);
				jsonMetadata.put("language", commentFrame.language);
				jsonMetadata.put("description", commentFrame.description);
			} else if (entry instanceof Id3Frame) {
				Id3Frame id3Frame = (Id3Frame) entry;
				jsonMetadata.put("id", id3Frame.id);
			} else if (entry instanceof EventMessage) {
				EventMessage eventMessage = (EventMessage) entry;
				jsonMetadata.put("schemeIdUri", eventMessage.schemeIdUri);
				jsonMetadata.put("id", eventMessage.id);
				jsonMetadata.put("value", eventMessage.value);
			}
		}
		return jsonMetadata;
	}

	private static boolean getTrackStatus(TrackSelection selection, TrackGroup group, int trackIndex)
	{
		return selection != null && selection.getTrackGroup() == group
			&& selection.indexOf(trackIndex) != C.INDEX_UNSET;
	}

	private ModuleUtil()
	{
	}
}
