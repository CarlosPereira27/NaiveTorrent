package br.ufla.naivetorrent.domain.file;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.util.UtilHex;

public class MetaTorrent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ByteBuffer infoHash;
	private MetaInfoTorrent info;
	private List<Tracker> trackers;
	private Date creationDate;
	private String comment;
	private String createdBy;
	private String encoding;
	
	public MetaTorrent() {
		trackers = new ArrayList<>();
		creationDate = new Date();
		info = new MetaInfoTorrent();
	}
	
	/**
	 * Recupera tamanho do torrent.
	 * @return tamanho do torrent
	 */
	public long getLenghtTorrent() {
		return info.getLenghtTorrent();
	}
	
	public Integer getPiecesLength() {
		return info.getPiecesLength();
	}

	/**
	 * Recupera a lista dos endereços dos trackers.
	 * @return lista dos endereços dos trackers
	 */
	public List<String> getAnnounceList() {
		List<String> announceList = new ArrayList<>();
		for (Tracker tracker : trackers) {
			announceList.add(tracker.getAddress());
		}
		return announceList;
	}
	
	/**
	 * Recupera o tempo da criação do torrent
	 * @return tempo da criação do torrent, se não foi definido null
	 */
	public Long getCreationDateTime() {
		if (creationDate == null)
			return null;
		return creationDate.getTime();
	}
	
	/**
	 * Define a data de criação em relação ao tempo da data.
	 * @param creationDateTime tempo da data de criação
	 */
	public void setCreationDateTime(Long creationDateTime) {
		if (creationDateTime != null)
			creationDate = new Date(creationDateTime);
	}

	
	public ByteBuffer getInfoHash() {
		return infoHash;
	}
	public String getInfoHashHex() {
		return UtilHex.toHexString(infoHash);
	}
	public void setInfoHash(ByteBuffer infoHash) {
		this.infoHash = infoHash;
	}
	public void setInfoHash(String infoHashHex) {
		this.infoHash = UtilHex.toBytes(infoHashHex);
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public MetaInfoTorrent getInfo() {
		return info;
	}
	public void setInfo(MetaInfoTorrent info) {
		this.info = info;
	}
	public List<Tracker> getTrackers() {
		return trackers;
	}
	public void setTrackers(List<Tracker> trackers) {
		this.trackers = trackers;
	}
	
	// HASHCODE e EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((infoHash == null) ? 0 : infoHash.hashCode());
		result = prime * result + ((trackers == null) ? 0 : trackers.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaTorrent other = (MetaTorrent) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (encoding == null) {
			if (other.encoding != null)
				return false;
		} else if (!encoding.equals(other.encoding))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (infoHash == null) {
			if (other.infoHash != null)
				return false;
		} else if (!Arrays.equals(infoHash.array(), 
				other.infoHash.array()))
			return false;
		if (trackers == null) {
			if (other.trackers != null)
				return false;
		} else if (!trackers.equals(other.trackers))
			return false;
		return true;
	}
	
	//TOSTRING
	@Override
	public String toString() {
		return "MetaTorrent [infoHash=" + infoHash + ", info=" + info + ", trackers=" + trackers + ", creationDate="
				+ creationDate + ", comment=" + comment + ", createdBy=" + createdBy + ", encoding=" + encoding + "]";
	}

	/**
	 * Representa os atributos de MetaTorrent.
	 * @author carlos
	 *
	 */
	public static class Attributes {
		
		public static final String INFO = "info";
		public static final String INFO_HASH = "info_hash";
		public static final String ANNOUNCE_LIST = "announce-list";
		public static final String CREATION_DATE = "creation date";
		public static final String COMMENT = "comment";
		public static final String CREATED_BY = "created by";
		public static final String ENCODING = "encoding";
		
	}

}
