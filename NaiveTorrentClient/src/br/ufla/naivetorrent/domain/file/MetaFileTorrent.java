package br.ufla.naivetorrent.domain.file;

import java.io.Serializable;
import java.nio.ByteBuffer;

import br.ufla.naivetorrent.util.UtilHex;

public class MetaFileTorrent implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private Long length;
	private ByteBuffer md5sum;
	private String pathFile;
	
	
	public Long getLength() {
		return length;
	}
	public void setLength(Long length) {
		this.length = length;
	}
	public ByteBuffer getMd5sum() {
		return md5sum;
	}
	public String getMd5sumHex() {
		return UtilHex.toHexString(md5sum);
	}
	public void setMd5sum(ByteBuffer md5sum) {
		this.md5sum = md5sum;
	}
	public void setMd5sumHex(String md5sumHex) {
		this.md5sum = UtilHex.toBytes(md5sumHex);
	}
	public String getPathFile() {
		return pathFile;
	}
	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}
	
	// HASHCODE e EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((length == null) ? 0 : length.hashCode());
		result = prime * result + ((md5sum == null) ? 0 : md5sum.hashCode());
		result = prime * result + ((pathFile == null) ? 0 : pathFile.hashCode());
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
		MetaFileTorrent other = (MetaFileTorrent) obj;
		if (length == null) {
			if (other.length != null)
				return false;
		} else if (!length.equals(other.length))
			return false;
		if (md5sum == null) {
			if (other.md5sum != null)
				return false;
		} else if (!md5sum.equals(other.md5sum))
			return false;
		if (pathFile == null) {
			if (other.pathFile != null)
				return false;
		} else if (!pathFile.equals(other.pathFile))
			return false;
		return true;
	}
	
	
	// TOSTRING
	@Override
	public String toString() {
		return "MetaFileTorrent [length=" + length + ", md5sum=" + md5sum + ", pathFile=" + pathFile + "]";
	}


	/**
	 * Representa os atributos de MetaFileTorrent.
	 * @author carlos
	 *
	 */
	public static class Attributes {
		
		public static final String LENGTH = "length";
		public static final String MD5SUM = "md5sum";
		public static final String PATH = "path";
		
	}

}
