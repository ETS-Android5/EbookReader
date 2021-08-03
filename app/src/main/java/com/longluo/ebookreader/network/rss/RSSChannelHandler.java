package com.longluo.ebookreader.network.rss;

import com.longluo.ebookreader.network.NetworkCatalogItem;
import com.longluo.ebookreader.network.NetworkItem;
import com.longluo.ebookreader.network.atom.ATOMId;

public class RSSChannelHandler extends AbstractRSSChannelHandler {
	private final NetworkCatalogItem myCatalog;
	private final String myBaseURL;
	private final RSSCatalogItem.State myData;

	private int myIndex;
	private String mySkipUntilId;
	private boolean myFoundNewIds;

	/**
	 * Creates new RSSChannelHandler instance that can be used to get NetworkItem objects from RSS feeds.
	 *
	 * @param baseURL    string that contains URL of the RSS feed, that will be read using this instance of the reader
	 * @param result     network results buffer. Must be created using RSSNetworkLink corresponding to the RSS feed,
	 *                   that will be read using this instance of the reader.
	 */
	RSSChannelHandler(String baseURL, RSSCatalogItem.State result) {
		myCatalog = result.Loader.Tree.Item;
		myBaseURL = baseURL;
		myData = result;
		mySkipUntilId = myData.LastLoadedId;
		myFoundNewIds = mySkipUntilId != null;
		if (!(result.Link instanceof RSSNetworkLink)) {
			throw new IllegalArgumentException("Parameter `result` has invalid `Link` field value: result.Link must be an instance of OPDSNetworkLink class.");
		}

	}

	@Override
	public void processFeedStart() {
	}

	@Override
	public boolean processFeedMetadata(RSSChannelMetadata feed,
			boolean beforeEntries) {
		return false;
	}

	@Override
	public boolean processFeedEntry(RSSItem entry) {

		if (entry.Id == null) {
			entry.Id = new ATOMId();
			entry.Id.Uri = "id_"+myIndex;
		}

		myData.LastLoadedId = entry.Id.Uri;
		if (!myFoundNewIds && !myData.LoadedIds.contains(entry.Id.Uri)) {
			myFoundNewIds = true;
		}
		myData.LoadedIds.add(entry.Id.Uri);

		NetworkItem item = new RSSBookItem((RSSNetworkLink)myData.Link, entry, myBaseURL, myIndex++);

		if (item != null) {
			myData.Loader.onNewItem(item);
		}
		return false;
	}

	@Override
	public void processFeedEnd() {
	}

}
