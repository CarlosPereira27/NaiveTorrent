package br.ufla.naivetorrent.torrent;

import java.io.File;

public class MakeTorrent {
	
	private File shareFile;
	private File torrentFile;
	
	public MakeTorrent(File shareFile, File torrentFile) {
		this.shareFile = shareFile;
		this.torrentFile = torrentFile;
	}
	
	private void makeDir(File dir) {
		
	}
	
	public void make() {
		if (shareFile.isDirectory()) {
		}
	}
	
	

}
