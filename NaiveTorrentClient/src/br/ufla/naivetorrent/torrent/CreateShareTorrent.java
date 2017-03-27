package br.ufla.naivetorrent.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufla.naivetorrent.domain.file.FileLimits;
import br.ufla.naivetorrent.domain.file.HashPiece;
import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;

public class CreateShareTorrent {

	public static final String PART_EXTENSION = ".part";
	private ShareTorrent share;
	private int pieceLength;

	public CreateShareTorrent(ShareTorrent share) {
		this.share = share;	
		pieceLength = share.getPiecesLength();
	}

	private boolean createSingleFile(File fileCreate) {
		String path = fileCreate.getPath();
		File dir = new File(path.substring(0, path.lastIndexOf('/')));
		try {
			dir.mkdirs();
			return fileCreate.createNewFile();

		} catch (Exception e) {
			System.out.println("Falha na criação de :" + path);
			e.printStackTrace();
		}
		return true;
	}
	
	private void verifyFile(String directory, MetaFileTorrent mt) {
		File fileFull = new File(directory + mt.getPathFile());
		if (fileFull.exists()) {
			fileFull.renameTo(new File(fileFull.getPath() + PART_EXTENSION));
		}
	}

	public boolean createFiles() throws IOException {
		String directory = this.share.getSharePathString();
		if (share.getSharePath().isDirectory()) {
			directory += "/";
		}
		List<MetaFileTorrent> files = share.getMetaTorrent().getInfo().getMetaFiles();
		List<MetaFileTorrent> haveFiles = new ArrayList<>();
		for (MetaFileTorrent mt : files) {
			verifyFile(directory, mt);
			File fileCreate = new File(directory + mt.getPathFile() + PART_EXTENSION);
			if (!fileCreate.exists()) {
				createSingleFile(fileCreate);
				writeFile(fileCreate, mt.getLength());
			} else {
				haveFiles.add(mt);
				verifyHash(mt);
			}
		}
		for (MetaFileTorrent mt : haveFiles) {
			share.verifyFileCompleted(mt, share.getFileLimits(mt));
		}
		return true;
	}
	
	private void verifyHash(MetaFileTorrent mt) throws IOException {
		FileLimits fileLimits = share.getFileLimits(mt);
		int indexInf = (int) (fileLimits.limitInf / pieceLength);
		if (fileLimits.limitInf % pieceLength == 0 && indexInf != 0) {
			indexInf--;
		}
		int indexSup = (int) (fileLimits.limitSup / pieceLength);
		if (fileLimits.limitSup % pieceLength == 0 && indexSup != 0) {
			indexSup--;
		}
		for (int i = indexInf; i <= indexSup; i++) {
			HashPiece hashPiece = new HashPiece(share, i);
			System.out.print(i);
			if (hashPiece.check()) {
				System.out.println("check");
				share.setSimpleMyBitfieldPiece(i);
			} else {
				System.out.println("not check");
			}
		}
	}

	private void writeFile(File file, long sizeFile) {
		long i;
		for (i = 0l; i + pieceLength < sizeFile; i += pieceLength) {
			writePeace(file, new byte[pieceLength]);
		}
		writePeace(file, new byte[(int) (sizeFile - i)]);
	}

	private boolean writePeace(File file, byte[] pieceByteArray) {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file, true);
			output.write(pieceByteArray);
			output.flush();
			output.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}