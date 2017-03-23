package br.ufla.naivetorrent.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;

public class CreateShareTorrent {

	private ShareTorrent share;
	public static final String PART_EXTENSION = ".part";

	public CreateShareTorrent(ShareTorrent share) {
		this.share = share;
	}

	private boolean createSingleFile(String path) {
		File dir = new File(path.substring(0, path.lastIndexOf('/')));
		File fileCreate = new File(path + PART_EXTENSION);
		//System.out.println(path);
		//System.out.println(dir.getPath());
		//fileCreate = new File(fileCreate.getPath());
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
		//System.out.println(directory);
		ArrayList<MetaFileTorrent> fields = (ArrayList<MetaFileTorrent>) share.getMetaTorrent().getInfo()
				.getMetaFiles();
		//System.out.println(fields.size());
		for (MetaFileTorrent mt : fields) {
			//System.out.println(mt.getPathFile());
			String path = new String(directory + mt.getPathFile());
			//System.out.println(path);
			createSingleFile(path);
		}
		return true;
	}

	private boolean writePeace(String path, byte[] peaceByteArray) {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(path, true);
			output.write(peaceByteArray);
			output.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * if(){ //gerar a hash SH1 dos pedacos, e marca no shareTorrent // cada
	 * pedaco no bitfield
	 * 
	 * } else{
	 * 
	 * 
	 * 
	 * // Senao cria o arquivo com bit 0, seta o bitfield como 0
	 * 
	 * }
	 */

	private void piecesHashGenerate() {

	}
}
