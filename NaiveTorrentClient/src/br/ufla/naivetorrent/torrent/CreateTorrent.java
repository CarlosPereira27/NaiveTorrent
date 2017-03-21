package br.ufla.naivetorrent.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ufla.naivetorrent.domain.file.InfoMetaTorrent;
import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.MetaTorrent;
import gyurix.bencoder.BEncoder;

public class CreateTorrent {
	
	private MetaTorrent metaTorrent;
	private File torrentFile;
	private Map<String, Object> metaInfo;
	
	public CreateTorrent(MetaTorrent metaTorrent, File torrentFile) 
			throws IOException {
		this.metaTorrent = metaTorrent;
		this.torrentFile = torrentFile;
		metaInfo = new LinkedHashMap<>();
	}
	
	/**
	 * Codifica a lista de metadados de arquivos em bencoded.
	 * @return lista de metadados de arquivos codificada em bencoded
	 */
	private List<Map<String, Object>> getMetaFiles() {
		List<Map<String, Object>> metaFilesDict = new ArrayList<>();
		List<MetaFileTorrent> metaFiles = metaTorrent.getInfo().getMetaFiles();
		for (MetaFileTorrent metaFile : metaFiles) {
			Map<String, Object> metaFileDict = new LinkedHashMap<>();
			metaFileDict.put(MetaFileTorrent.Attributes.LENGTH, 
					metaFile.getLength());
			metaFileDict.put(MetaFileTorrent.Attributes.MD5SUM, 
					metaFile.getMd5sumHex());
			metaFileDict.put(MetaFileTorrent.Attributes.PATH, 
					metaFile.getPathFile());
			metaFilesDict.add(metaFileDict);
		}
		return metaFilesDict;
	}
	
	/**
	 * Codifica o dicionário info do arquivo em bencoded.
	 * @return dicionário info do arquivo codificado em bencoded
	 */
	private Map<String, Object> getInfoDictionary() {
		Map<String, Object> infoDictionary = new LinkedHashMap<>();
		InfoMetaTorrent infoMetaTorrent = metaTorrent.getInfo();
		infoDictionary.put(InfoMetaTorrent.Attributes.PIECE_LENGTH, 
				infoMetaTorrent.getPiecesLength());
		infoDictionary.put(InfoMetaTorrent.Attributes.PIECES, 
				infoMetaTorrent.getPiecesHashString());
		infoDictionary.put(InfoMetaTorrent.Attributes.FILES, 
				getMetaFiles());
		return infoDictionary;
	}
	
	/**
	 * Define o dicionário com as informações do torrent.
	 */
	private void defineDictionary() {
		metaInfo.put(MetaTorrent.Attributes.INFO_HASH, 
				metaTorrent.getInfoHashHex());
		metaInfo.put(MetaTorrent.Attributes.INFO, 
				getInfoDictionary());
		metaInfo.put(MetaTorrent.Attributes.ANNOUNCE_LIST, 
				metaTorrent.getAnnounceList());
		metaInfo.put(MetaTorrent.Attributes.CREATION_DATE, 
				metaTorrent.getCreationDate().getTime());
		if (metaTorrent.getComment() != null)
			metaInfo.put(MetaTorrent.Attributes.COMMENT, 
					metaTorrent.getComment());
		if (metaTorrent.getCreatedBy() != null)
			metaInfo.put(MetaTorrent.Attributes.CREATED_BY, 
					metaTorrent.getCreatedBy());
		if (metaTorrent.getEncoding() != null)
			metaInfo.put(MetaTorrent.Attributes.ENCODING, 
					metaTorrent.getEncoding());
	}
	
	/**
	 * Cria o arquivo .torrent com os metadados dos arquivos compartilhados.
	 * @throws IOException
	 */
	public void create() 
			throws IOException {
		defineDictionary();
		BEncoder bEncoder = new BEncoder();
		bEncoder.write(metaInfo);
		metaInfo = null;
		FileOutputStream torrentOut = new FileOutputStream(torrentFile);
		torrentOut.write(bEncoder.toString().getBytes());
		torrentOut.close();
	}


}
