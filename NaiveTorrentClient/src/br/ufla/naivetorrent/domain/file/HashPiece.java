package br.ufla.naivetorrent.domain.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HashPiece {
	
	private ShareTorrent shareTorrent;
	private int indexPiece;

	public HashPiece(ShareTorrent shareTorrent, int indexPiece) {
		this.shareTorrent = shareTorrent;
		this.indexPiece = indexPiece;
	}

	private void updateDigestFile(MetaFileTorrent metaFile, 
			long desloc, int length, 
			MessageDigest messageDigest) 
					throws IOException {
		String pathFile  = shareTorrent.getSharePathString() 
				+ metaFile.getPathFile();
		if (!shareTorrent.isCompleted(metaFile)) {
			pathFile += ReadPiece.TORRENT_PART_FILE;
		}
		RandomAccessFile randomAccessFile = 
				new RandomAccessFile(new File(pathFile), "r");
		randomAccessFile.seek(desloc);
		byte data[] = new byte[length];
		randomAccessFile.read(data);
		randomAccessFile.close();
		messageDigest.update(data);
	}

	public boolean check() throws IOException {
		int lengthPiece = shareTorrent.getMetaTorrent().getInfo().getPiecesLength();
		long initPointer = ((long) indexPiece * (long) lengthPiece);
		long finalPointer = initPointer + lengthPiece;
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Map<MetaFileTorrent, FileLimits> fileToLimits = shareTorrent.getFileToLimits();
		for (Map.Entry<MetaFileTorrent, FileLimits> entry : fileToLimits.entrySet()) {
			FileLimits fileLimits = entry.getValue();
			MetaFileTorrent metaFile = entry.getKey();
			if (fileLimits.limitSup > initPointer) {
				long desloc = 0;
				if (fileLimits.limitInf <= initPointer) {
					desloc = initPointer - fileLimits.limitInf;
				}
				int length = (int) (Math.min(fileLimits.limitSup, finalPointer)
						- fileLimits.limitInf);
				updateDigestFile(metaFile, desloc, length, messageDigest);
			}
		}
		ByteBuffer sha1Generated = ByteBuffer.wrap(messageDigest.digest());
		ByteBuffer sha1 = shareTorrent.getMetaTorrent().getInfo()
				.getPiecesHash().get(indexPiece);
		return sha1Generated.equals(sha1);
	}

}
