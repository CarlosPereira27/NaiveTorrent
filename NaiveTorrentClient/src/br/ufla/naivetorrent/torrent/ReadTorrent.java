package br.ufla.naivetorrent.torrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ufla.naivetorrent.domain.file.MetaInfoTorrent;
import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.tracker.Tracker;
import gyurix.bencoder.BEncoder;

public class ReadTorrent {
	
	private File torrentFile;
	private MetaTorrent metaTorrent;
	private Map<Object, Object> metaFileDecode;
	
	public ReadTorrent(File torrentFile) 
			throws FileNotFoundException {
		this.torrentFile = torrentFile;
		metaTorrent = new MetaTorrent();
		metaFileDecode = new LinkedHashMap<>();
	}
	
	/**
	 * Realiza a leitura do arquivo e retorna uma string com o seu conteúdo.
	 * @return conteúdo do arquivo (string)
	 * @throws IOException
	 */
	private String readFile() 
			throws IOException {
		FileInputStream torrentReader = 
				new FileInputStream(torrentFile);
		int length = (int) torrentFile.length();
		byte charBuffer[] = new byte[length];
		System.out.println(length);
		System.out.println(torrentReader.read(charBuffer));
		torrentReader.close();
		return new String(charBuffer);
	}
	

	/**
	 * Decodifica o arquivo .torrent.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void decodeFile() 
			throws IOException {
		BEncoder bEncoder = new BEncoder(readFile());
		metaFileDecode = (Map<Object, Object>) bEncoder.read();
	}
	
	/**
	 * Decodifica a lista de metadados dos arquivos compartilhados.
	 * @param metaInfo decodificação dos metadados de informação
	 * @return lista de metadados dos arquivos compartilhados
	 */
	private List<MetaFileTorrent> decodeMetaFiles(Map<Object, Object> metaInfo) {
		List<MetaFileTorrent> metaFiles = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<Object, Object>> metaFilesDic = (List<Map<Object, Object>>) 
				metaInfo.get(MetaInfoTorrent.Attributes.FILES);
		for (Map<Object, Object> metaFileDic : metaFilesDic) {
			MetaFileTorrent metaFile = new MetaFileTorrent();
			metaFile.setLength((Long) metaFileDic.get(
					MetaFileTorrent.Attributes.LENGTH)); 
			metaFile.setMd5sumHex((String) metaFileDic.get(
					MetaFileTorrent.Attributes.MD5SUM));
			metaFile.setPathFile((String) metaFileDic.get(
					MetaFileTorrent.Attributes.PATH));
			metaFiles.add(metaFile);
		}
		return metaFiles;
	}
	
	/**
	 * Decodifica o dicionário info.
	 */
	private void decodeMetaInfo() {
		@SuppressWarnings("unchecked")
		Map<Object, Object> metaInfo = (Map<Object, Object>) 
				metaFileDecode.get(MetaTorrent.Attributes.INFO);
		MetaInfoTorrent infoMetaTor = metaTorrent.getInfo();
		infoMetaTor.setPiecesLength(((Long) metaInfo
				.get(MetaInfoTorrent.Attributes.PIECE_LENGTH)).intValue());
		infoMetaTor.setPiecesHashString((String) 
				metaInfo.get(MetaInfoTorrent.Attributes.PIECES));
		infoMetaTor.setMetaFiles(decodeMetaFiles(metaInfo));
	}
	
	/**
	 * Decodifica a lista de trackers.
	 */
	private void decodeAnnounceList() {
		@SuppressWarnings("unchecked")
		List<String> announceList = (List<String>) metaFileDecode.get(
				MetaTorrent.Attributes.ANNOUNCE_LIST);
		List<Tracker> trackers = new ArrayList<>();
		for (String announce : announceList) {
			Tracker tracker = new Tracker();
			int index = announce.lastIndexOf(':');
			String hostName = announce.substring(0, index);
			Integer port = Integer.parseInt(announce.substring(index + 1, 
					announce.length()));
			tracker.setSocketAddressListening(new InetSocketAddress(hostName, port));
			trackers.add(tracker);
		}
		metaTorrent.setTrackers(trackers);
	}
	
	/**
	 * Realiza a leitura e decodificação do arquivo .torrent retornando um objeto
	 * MetaTorrent equivalente.
	 * @return objeto MetaTorrent equivalente ao arquivo .torrent
	 * @throws IOException
	 */
	public MetaTorrent read() 
			throws IOException {
		decodeFile();
		metaTorrent.setInfoHash((String) metaFileDecode
				.get(MetaTorrent.Attributes.INFO_HASH));
		decodeMetaInfo();
		decodeAnnounceList();
		metaTorrent.setCreationDate(new Date((Long) metaFileDecode
				.get(MetaTorrent.Attributes.CREATION_DATE)));
		if (metaFileDecode.containsKey(MetaTorrent.Attributes.COMMENT))
			metaTorrent.setComment(((String) metaFileDecode
					.get(MetaTorrent.Attributes.COMMENT)));
		if (metaFileDecode.containsKey(MetaTorrent.Attributes.CREATED_BY))
			metaTorrent.setCreatedBy(((String) metaFileDecode
					.get(MetaTorrent.Attributes.CREATED_BY)));
		if (metaFileDecode.containsKey(MetaTorrent.Attributes.ENCODING))
			metaTorrent.setEncoding(((String) metaFileDecode
					.get(MetaTorrent.Attributes.ENCODING)));
		metaFileDecode = new LinkedHashMap<>();
		return metaTorrent;
	}
	

}
