package br.ufla.naivetorrent.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;

import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;

public class CreateShareTorrent {

	private ShareTorrent share;
	private byte lastPiece[];
	private byte writyePeace[];
	public static final String PART_EXTENSION = ".part";
	private int sizeMetaInfo;

	public CreateShareTorrent(ShareTorrent share) {
		this.share = share;
		if (this.share.getMyBitfield() == null) {
			this.share.setMyBitfield(new BitSet());
		}
		if (this.share.getMyBitfield().isEmpty()) {
			this.share.setMyBitfield(new BitSet());
		}
	}

	private boolean createSingleFile(File fileCreate) {
		String path = fileCreate.getPath();
		File dir = new File(path.substring(0, path.lastIndexOf('/')));
		// File fileCreate = new File(path + PART_EXTENSION);
		// System.out.println(path);
		// System.out.println(dir.getPath());
		// fileCreate = new File(fileCreate.getPath());
		try {
			dir.mkdirs();
			return fileCreate.createNewFile();

		} catch (Exception e) {
			System.out.println("Falha na criação de :" + path);
			e.printStackTrace();
		}
		return false;
	}

	public boolean createFiles() {
		String directory = this.share.getSharePath().getPath();
		BitSet myBitSet = null;
		int size = 0;
		boolean exist = true;
		// System.out.println(directory);
		this.sizeMetaInfo = share.getMetaTorrent().getInfo().getPiecesLength();
		ArrayList<MetaFileTorrent> fields = (ArrayList<MetaFileTorrent>) share.getMetaTorrent().getInfo()
				.getMetaFiles();
		// System.out.println(fields.size());
		int i = 0;
		for (MetaFileTorrent mt : fields) {
			// System.out.println(mt.getPathFile());
			String path = new String(directory + mt.getPathFile());
			File fileFull = new File(path);

			if (fileFull.exists()) {
				fileFull.renameTo(new File(path + PART_EXTENSION));
			}

			File fileCreate = new File(path + PART_EXTENSION);

			if (!fileCreate.exists()) {
				exist = false;
				// System.out.println(path);
				createSingleFile(fileCreate);
				size += mt.getLength().intValue();
				byte[] blankByte = new byte[mt.getLength().intValue()];
				mt.setMd5sum(ByteBuffer.wrap(blankByte));
				writeFile(path, mt.getLength().intValue());
				// writePeace(path + PART_EXTENSION, blankByte);
			} else {
				readFile(fileCreate, i);
			}
			i++;
		}
		if (!exist) {
			myBitSet = new BitSet(size);
			share.setMyBitfield(myBitSet);
			System.out.println(share.getMyBitfield().size());
		}
		return true;
	}

	private void writeFile(String path, long sizeFile) {
		long i;
		for (i = 0l; i < sizeFile; i += sizeMetaInfo) {
			writePeace(path, new byte[sizeMetaInfo]);
		}
		writePeace(path, new byte[(int) (sizeFile - i - sizeMetaInfo)]);
	}

	private boolean writePeace(String path, byte[] peaceByteArray) {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(path, true);
			output.write(peaceByteArray);
			output.flush();
			output.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void readFile(File file, long indexFile) {
		readPeace(file, indexFile, sizeMetaInfo);
	}

	private void readPeace(File file, long desloc, int lengthRead) {
		long length = file.length();
		if (lengthRead > (length - desloc)) {
			lengthRead = (int) (length - desloc);
		}
		try {
			RandomAccessFile raFile = new RandomAccessFile(file, "r");
			raFile.seek(desloc);
			lastPiece = new byte[lengthRead];
			raFile.read(lastPiece);
			raFile.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// Comparar o pedaco com HashPeace e setar op bitSet do pedaco
	}
}
