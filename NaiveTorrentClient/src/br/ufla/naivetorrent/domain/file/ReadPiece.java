package br.ufla.naivetorrent.domain.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

public class ReadPiece {
	
	public static final String TORRENT_PART_FILE = ".part";
	
	private ShareTorrent shareTorrent;
	private BlockOfPiece blockOfPiece;

	public ReadPiece(ShareTorrent shareTorrent, BlockOfPiece blockOfPiece) {
		this.shareTorrent = shareTorrent;
		this.blockOfPiece = blockOfPiece;
	}

	private int readFile(MetaFileTorrent metaFile, 
			long desloc, int length, byte data[], int index) 
					throws IOException {
		String pathFile = shareTorrent.getSharePathString();
		if (shareTorrent.getSharePath().isDirectory()) {
			pathFile += "/";
		}
		pathFile += metaFile.getPathFile();
		if (!shareTorrent.isCompleted(metaFile)) {
			pathFile += TORRENT_PART_FILE;
		}
		RandomAccessFile randomAccessFile = 
				new RandomAccessFile(new File(pathFile), "r");
		randomAccessFile.seek(desloc);
		randomAccessFile.read(data, index, length);
		randomAccessFile.close();
		return index + length;
	}

	public byte[] read() throws IOException {
		int lengthPiece = shareTorrent.getMetaTorrent().getInfo().getPiecesLength();
		long initPointer = blockOfPiece.getInitPointer(lengthPiece);
		long finalPointer = initPointer + blockOfPiece.getLength();
		byte data[] = new byte[(int) (finalPointer - initPointer)];
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
				index = readFile(metaFile, desloc, length, data, index);
			}
		}
		return data;
	}

}
