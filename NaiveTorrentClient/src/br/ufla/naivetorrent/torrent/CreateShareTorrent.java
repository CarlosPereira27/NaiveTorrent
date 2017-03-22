package br.ufla.naivetorrent.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;

public class CreateShareTorrent {

	private ShareTorrent share;

	public CreateShareTorrent(ShareTorrent share) {
		this.share = share;
	}

	private boolean createSingleFile(String path) {
		File dir = new File(path.substring(0, path.lastIndexOf('/')));
		File fileCreate = new File(path);
		try {
			if (dir.mkdirs() && fileCreate.createNewFile()) {
				return true;
			}

		} catch (Exception e) {
			System.out.println("Falha na criação de :" + path);
			e.printStackTrace();
		}
		return false;
	}

	private boolean createFiles() {
		String directory = this.share.getSharePath().getPath();
		ArrayList<MetaFileTorrent> fields = (ArrayList<MetaFileTorrent>) share.getMetaTorrent().getInfo()
				.getMetaFiles();
		for (MetaFileTorrent mt : fields) {
			String path = directory + mt.getPathFile();
			return createSingleFile(path);
		}
		return false;
	}

	private boolean writePeace(String path,byte[] peaceByteArray) {
		try {
			FileOutputStream output = new FileOutputStream(path, true);
			output.write(peaceByteArray);
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
