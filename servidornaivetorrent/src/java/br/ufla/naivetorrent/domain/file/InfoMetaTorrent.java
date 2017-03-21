package br.ufla.naivetorrent.domain.file;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

public class InfoMetaTorrent implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private Integer piecesLength;
	private List<ByteBuffer> piecesHash;
	private List<MetaFileTorrent> files;
	
	public Integer getPiecesLength() {
		return piecesLength;
	}
	public void setPiecesLength(Integer piecesLength) {
		this.piecesLength = piecesLength;
	}
	public List<ByteBuffer> getPiecesHash() {
		return piecesHash;
	}
	public void setPiecesHash(List<ByteBuffer> piecesHash) {
		this.piecesHash = piecesHash;
	}
	public List<MetaFileTorrent> getFiles() {
		return files;
	}
	public void setFiles(List<MetaFileTorrent> files) {
		this.files = files;
	}

}
