package br.ufla.naivetorrent.domain.file;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

import br.ufla.naivetorrent.domain.tracker.Tracker;

public class MetaTorrent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ByteBuffer hashInfo;
	private Date creationDate;
	private String createBy;
	private String encoding;
	private InfoMetaTorrent info;
	private List<Tracker> trackers;
	
	
	public ByteBuffer getHashInfo() {
		return hashInfo;
	}
	public void setHashInfo(ByteBuffer hashInfo) {
		this.hashInfo = hashInfo;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public InfoMetaTorrent getInfo() {
		return info;
	}
	public void setInfo(InfoMetaTorrent info) {
		this.info = info;
	}
	public List<Tracker> getTrackers() {
		return trackers;
	}
	public void setTrackers(List<Tracker> trackers) {
		this.trackers = trackers;
	}

}
