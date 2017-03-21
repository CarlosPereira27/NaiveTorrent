package br.ufla.naivetorrent.domain.file;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class MetaFileTorrent implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private Integer length;
	private ByteBuffer md5sum;
	private String pathFile;
	
	
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public ByteBuffer getMd5sum() {
		return md5sum;
	}
	public void setMd5sum(ByteBuffer md5sum) {
		this.md5sum = md5sum;
	}
	public String getPathFile() {
		return pathFile;
	}
	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

}
