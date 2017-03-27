package br.ufla.naivetorrent.tracker.request;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.peer.Peer;
import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.tracker.protocol.RequestEvent;
import br.ufla.naivetorrent.tracker.protocol.RequestMessage;
import br.ufla.naivetorrent.tracker.protocol.ResponseMessage;
import br.ufla.naivetorrent.util.UtilHex;

public class ManagerTorrentRequest implements Runnable {
	
	private ShareTorrent shareTorrent;
	private RequestMessage requestMessage;
	private ManagerRequest managerRequest;
	private SortedSet<Tracker> trackers;
	private AtomicBoolean stop;
	private AtomicBoolean stopTorrent;
	private Set<Tracker> trackersUpdate;


	public ManagerTorrentRequest(ShareTorrent shareTorrent, ManagerRequest managerRequest) {
		this.shareTorrent = shareTorrent;
		this.managerRequest = managerRequest;
		stop = new AtomicBoolean(false);
		stopTorrent = new AtomicBoolean(false);
		trackersUpdate = new HashSet<>();
		defineTrackers();
		createRequestMessage();
		updateRequestMessage();
	}
	
	private void defineTrackers() {
		trackers = new TreeSet<>();
		List<Tracker> trackersList = shareTorrent.getTrackers();
		for (Tracker tracker : trackersList) {
			trackers.add(tracker);
		}
	}
	
	private void createRequestMessage() {
		requestMessage = new RequestMessage();
		requestMessage.setInfoHash(shareTorrent.getInfoHashHex());
		Peer me = shareTorrent.getMe();
		requestMessage.setIp(me.getIpOrHostName());
		requestMessage.setPort(me.getPort());
		requestMessage.setPeerId(me.getIdHex());
	}
	
	private void updateRequestMessage() {
		requestMessage.setDownloaded(shareTorrent.getDownloaded());
		requestMessage.setLeft(shareTorrent.getLeft());
		requestMessage.setUploaded(shareTorrent.getUploaded());
	}

	private void initialComunication() {
		Peer me = shareTorrent.getMe();
		for (Tracker tracker : trackers) {
			ByteBuffer id = managerRequest.getClientId(tracker);
			if (id == null) {
				id = me.getId();
				managerRequest.putTrackerToClientId(tracker, id);
				requestMessage.setEvent(RequestEvent.STARTED);
			} else {
				requestMessage.setTrackerId(tracker.getIdHex());
			}
			requestMessage.setPeerId(UtilHex.toHexString(id));
			HttpRequest httpRequest = new HttpRequest(tracker, requestMessage, this);
			new Thread(httpRequest).start();
		}
	}
	
	private void sendRequestMessage(Tracker tracker) {
		updateRequestMessage();
		if (shareTorrent.isSeeder()) {
			requestMessage.setEvent(RequestEvent.COMPLETED);
		}
		if (tracker.getId() != null) {
			requestMessage.setTrackerId(tracker.getIdHex());
		}
		requestMessage.setPeerId(UtilHex.toHexString(managerRequest.getClientId(tracker)));
		HttpRequest httpRequest = new HttpRequest(tracker, requestMessage, this);
		new Thread(httpRequest).start();
	}
	
	private void stopRequest() {
		updateRequestMessage();
		for (Tracker tracker : trackers) {
			if (stop.get()) {
				requestMessage.setEvent(RequestEvent.STOPPED);
			} else if (stopTorrent.get()) {
				requestMessage.setEvent(RequestEvent.STOPPED_FILE);
			}
			requestMessage.setTrackerId(tracker.getIdHex());
			requestMessage.setPeerId(UtilHex.toHexString(managerRequest.getClientId(tracker)));
			HttpRequest httpRequest = new HttpRequest(tracker, requestMessage, this);
			new Thread(httpRequest).start();
		}
	}
	
	@Override
	public void run() {
		initialComunication();
		Iterator<Tracker> itTracker;
		while (!stop.get() && !stopTorrent.get()) {
			synchronized (trackersUpdate) {
				synchronized (trackers) {
					for (Tracker tracker : trackersUpdate) {
						trackers.remove(tracker);
						trackers.add(tracker);
					}
				}
				trackersUpdate.clear();
			}
			itTracker = trackers.iterator();
			try {
				Thread.sleep(Tracker.MIN_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int lastInterval = Tracker.MIN_INTERVAL;
			while (itTracker.hasNext()) {
				Tracker tracker = itTracker.next();
				try {
					Thread.sleep(tracker.getInterval() - lastInterval);
					lastInterval = tracker.getInterval();
					sendRequestMessage(tracker);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		stopRequest();
	}
	
	public void updateResponse(ResponseMessage responseMessage, Tracker tracker) {
		ConcurrentHashMap<Tracker, ByteBuffer> trackerToClientId = 
				managerRequest.getTrackerToClientId();
		if (responseMessage.getClientId() != null) {
			ByteBuffer id = UtilHex.toBytes(responseMessage.getClientId());
			ByteBuffer actualId = trackerToClientId.get(tracker);
			if (!id.equals(actualId)) {
				trackerToClientId.put(tracker, id);
			}
		}
		String trackerIdHex = responseMessage.getTrackerId();
		if (trackerIdHex != null) {
			tracker.setIdHex(trackerIdHex);
		}
		Integer interval = responseMessage.getInterval();
		if (interval != null && interval != tracker.getInterval()) {
			tracker.setInterval(interval);
			synchronized (trackersUpdate) {
				trackersUpdate.add(tracker);
			}
		}
		List<Peer> peers = responseMessage.getPeers();
		if (peers == null) {
			return;
		}
		Set<Peer> peersDisconnected = shareTorrent.getPeers();
		Set<Peer> peersConnected = shareTorrent.getPeersConnected();
		for (Peer peer : peers) {
			if (!peersConnected.contains(peer) 
					&& !peersDisconnected.contains(peer) 
					&& !peer.getSocketAddressListening()
						.equals(shareTorrent.getMe().getSocketAddressListening())) {
				peersDisconnected.add(peer);
			}
		}
	}

}
