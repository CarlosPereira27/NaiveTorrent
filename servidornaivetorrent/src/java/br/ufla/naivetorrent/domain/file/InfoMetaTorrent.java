package br.ufla.naivetorrent.domain.file;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoMetaTorrent implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private Integer piecesLength;
	private List<ByteBuffer> piecesHash;
	private List<MetaFileTorrent> metaFiles;
	
	public InfoMetaTorrent() {
		piecesHash = new ArrayList<>();
		metaFiles = new ArrayList<>();
	}
	
	/**
	 * Adiciona uma hash de um pedaço na lista de hash de pedaços.
	 * @param pieceHash hash de um pedaço
	 */
	public void addPieceHash(ByteBuffer pieceHash) {
		piecesHash.add(pieceHash);
	}
	
	/**
	 * Adiciona o metadado de um arquivo na lista de metadados de arquivos.
	 * @param metaFile metadado de um arquivo
	 */
	public void addMetaFile(MetaFileTorrent metaFile) {
		metaFiles.add(metaFile);
	}
	
	/**
	 * Recupera o tamanho em bytes das hash dos pedaços do arquivo.
	 * @return tamanho em bytes das hash dos pedaços do arquivo.
	 */
	private int getPiecesHashBufferLength() {
		int length = 0;
		for (ByteBuffer pieceHash : piecesHash) {
			length += pieceHash.array().length;
		}
		return length;
	}
	
	/**
	 * Recupera as hash dos pedaços do arquivo em uma string.
	 * @return hash dos pedaços do arquivo em uma string
	 */
	public String getPiecesHashString() {
		int lengthPiecesHash = getPiecesHashBufferLength();
		byte data[] = new byte[lengthPiecesHash];
		int ind = 0;
		for (ByteBuffer pieceHash : piecesHash) {
			byte b[] = pieceHash.array();
			int length = b.length;
			for (int i = 0; i < length; i++) {
				data[ind++] = b[i];
			}
		}
		return new String(data);
	}
	
	// MÉTODOS ACESSORES
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
	public List<MetaFileTorrent> getMetaFiles() {
		return metaFiles;
	}
	public void setMetaFiles(List<MetaFileTorrent> metaFiles) {
		this.metaFiles = metaFiles;
	}
	
	// HASHCODE e EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metaFiles == null) ? 0 : metaFiles.hashCode());
		result = prime * result + ((piecesHash == null) ? 0 : piecesHash.hashCode());
		result = prime * result + ((piecesLength == null) ? 0 : piecesLength.hashCode());
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
		InfoMetaTorrent other = (InfoMetaTorrent) obj;
		if (metaFiles == null) {
			if (other.metaFiles != null)
				return false;
		} else if (!metaFiles.equals(other.metaFiles))
			return false;
		if (piecesHash == null) {
			if (other.piecesHash != null)
				return false;
		} else if (piecesHash.size() != other.piecesHash.size()) {
			return false;
		} else {
			int size = piecesHash.size();
			for (int i = 0; i < size; i++) {
				if (!Arrays.equals(piecesHash.get(i).array(), 
						other.piecesHash.get(i).array()))
					return false;
			}
		}
		if (piecesLength == null) {
			if (other.piecesLength != null)
				return false;
		} else if (!piecesLength.equals(other.piecesLength))
			return false;
		return true;
	}
	
	
	// TOSTRING
	@Override
	public String toString() {
		return "InfoMetaTorrent [piecesLength=" + piecesLength + ", piecesHash=" + piecesHash + ", metaFiles="
				+ metaFiles + "]";
	}

	/**
	 * Representa os atributos de InfoMetaTorrent.
	 * @author carlos
	 *
	 */
	public static class Attributes {
		
		public static final String PIECE_LENGTH = "piece length";
		public static final String PIECES = "pieces";
		public static final String FILES = "files";
		
	}

}
