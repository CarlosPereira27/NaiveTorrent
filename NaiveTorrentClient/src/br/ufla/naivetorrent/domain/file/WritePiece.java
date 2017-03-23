package br.ufla.naivetorrent.domain.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

public class WritePiece {
	
	private ShareTorrent shareTorrent;
	private BlockOfPiece blockOfPiece;
	private byte data[];

	public WritePiece(ShareTorrent shareTorrent, 
			BlockOfPiece blockOfPiece, byte data[]) {
		this.shareTorrent = shareTorrent;
		this.blockOfPiece = blockOfPiece;
		this.data = data;
	}

	private int writeFile(MetaFileTorrent metaFile, 
			long desloc, int length, byte data[], int index) 
					throws IOException {
		String pathFile = shareTorrent.getSharePathString() 
				+ metaFile.getPathFile() + ReadPiece.TORRENT_PART_FILE;
		RandomAccessFile randomAccessFile = 
				new RandomAccessFile(new File(pathFile), "w");
		randomAccessFile.seek(desloc);
		randomAccessFile.write(data, index, length);
		randomAccessFile.close();
		return index + length;
	}

	public boolean write() throws IOException {
		int lengthPiece = shareTorrent.getMetaTorrent().getInfo().getPiecesLength();
		long initPointer = blockOfPiece.getInitPointer(lengthPiece);
		long finalPointer = initPointer + blockOfPiece.getLength();
		int index = 0;
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
				index = writeFile(metaFile, desloc, length, data, index);
			}
		}
		return true;
	}

}
