package br.ufla.naivetorrent.domain.file;

public class BlockOfPiece {
	
	private int index;
	private int desloc;
	private int length;
	
	public BlockOfPiece(int index, int desloc, int length) {
		this.index = index;
		this.desloc = desloc;
		this.length = length;
	}
	
	public long getInitPointer(long pieceLength) {
		return (index * pieceLength) + desloc;
	}

	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getDesloc() {
		return desloc;
	}
	public void setDesloc(int desloc) {
		this.desloc = desloc;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}

}
