package br.ufla.naivetorrent.tracker.request;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

import br.ufla.naivetorrent.domain.tracker.Tracker;

public class ManagerRequest {
	
	private ConcurrentHashMap<Tracker, ByteBuffer> trackerToClientId;
	
	public ManagerRequest() {
		trackerToClientId = new ConcurrentHashMap<>();
	}
	
	public ConcurrentHashMap<Tracker, ByteBuffer> getTrackerToClientId() {
		return trackerToClientId;
	}
	
	public ByteBuffer getClientId(Tracker tracker) {
		return trackerToClientId.get(tracker);
	}
	
	public void putTrackerToClientId(Tracker tracker, ByteBuffer clientId) {
		trackerToClientId.put(tracker, clientId);
	}

}
