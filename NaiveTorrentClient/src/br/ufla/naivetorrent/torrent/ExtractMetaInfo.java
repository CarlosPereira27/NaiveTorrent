package br.ufla.naivetorrent.torrent;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.tracker.Tracker;

public class ExtractMetaInfo {
	
	/**
	 * Pedaços de 1 MB.
	 */
	public static final int PIECE_LENGTH = 1048576;
	private File shareFile;
	private MetaTorrent metaTorrent;
	private int deslocToRootDirectory;
	private byte lastPiece[];
	
	public ExtractMetaInfo(File shareFile) {
		this.shareFile = shareFile;
		metaTorrent = new MetaTorrent();
	}
	
	/**
	 * Realiza a leitura do próximo pedaço.
	 * @param file arquivo em que realizará a leitura
	 * @param desloc deslocamento para chegar no ínicio do pedaço
	 * @throws IOException
	 */
	private void readNextPiece(File file, long desloc) 
			throws IOException {
		readNextPiece(file, desloc, PIECE_LENGTH);
	}
	
	/**
	 * Realiza a leitura do próximo pedaço.
	 * @param file arquivo em que realizará a leitura
	 * @param desloc deslocamento para chegar no ínicio do pedaço
	 * @param lengthRead quantidade de bytes que deverá ler
	 * @throws IOException
	 */
	private void readNextPiece(File file, long desloc, int lengthRead) 
			throws IOException {
		long length = file.length();
		if (lengthRead > (length - desloc)) {
			lengthRead = (int) (length - desloc);
		}
		RandomAccessFile raFile = new RandomAccessFile(file, "r");
		raFile.seek(desloc);
		lastPiece = new byte[lengthRead];
		raFile.read(lastPiece);
		raFile.close();
	}
	
	/**
	 * Verifica se o último pedaço está incompleto.
	 * @return true se o último pedaço está incompleto, caso contrário false.
	 */
	private boolean isLastPieceIncomplete() {
		return lastPiece != null && lastPiece.length > 0 
				&& lastPiece.length < PIECE_LENGTH;
	}
	
	/**
	 * Gera as chaves hash contidas em um determinado arquivo. Gera tanto a chave 
	 * hash MD5 do arquivo quanto as chaves hash SHA-1 dos pedaços do arquivo.
	 * Salva a chave MD5 nos metadados do arquivo e as chaves SHA-1 nos metadados 
	 * do torrent.
	 * @param file arquivo que será usado para gerar as chaves hash
	 * @param file metadados do arquivo para salvar a chave MD5
	 * @throws IOException
	 */
	private void generateHashKeys(File file, MetaFileTorrent metaFileTorrent) 
			throws IOException {
		long desloc = 0;
		long length = file.length();
		try {
			MessageDigest messageDigestMD5 = MessageDigest.getInstance("MD5");
			MessageDigest messageDigestSHA1 = MessageDigest.getInstance("SHA1");
			if (isLastPieceIncomplete()) {
				byte[] lastPieceCp = lastPiece;
				readNextPiece(file, desloc, PIECE_LENGTH - lastPieceCp.length);
				desloc += lastPiece.length;
				messageDigestMD5.update(lastPiece);
				List<ByteBuffer> pieceHash = metaTorrent.getInfo().getPiecesHash();
				pieceHash.remove(pieceHash.size() - 1);
				messageDigestSHA1.update(lastPieceCp);
				pieceHash.add(ByteBuffer
						.wrap(messageDigestSHA1.digest(lastPiece)));
			}
			while (desloc < length) {
				readNextPiece(file, desloc);
				desloc += lastPiece.length;
				messageDigestMD5.update(lastPiece);
				metaTorrent.getInfo().addPieceHash(ByteBuffer
						.wrap(messageDigestSHA1.digest(lastPiece)));
				
			}
			metaFileTorrent.setMd5sum(ByteBuffer.wrap(messageDigestMD5.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Realiza o processamento de diretórios para encontrar os arquivos e extrair
	 * suas informações.
	 * @param dir diretório que será processado
	 * @throws IOException
	 */
	private void processDir(File dir) 
			throws IOException {
		File files[] = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file);
			} else {
				MetaFileTorrent metaFileTorrent = new MetaFileTorrent();
				metaFileTorrent.setLength(file.length());
				metaFileTorrent.setPathFile(file.getPath()
						.substring(deslocToRootDirectory));
				generateHashKeys(file, metaFileTorrent);
				metaTorrent.getInfo().addMetaFile(metaFileTorrent);
			}
		}
	}
	
	/**
	 * Gera a hash do arquivo torrent.
	 */
	private void generateInfoHash() {
		List<MetaFileTorrent> metaFiles = metaTorrent.getInfo().getMetaFiles();
		try {
			MessageDigest messageDigestMD5 = MessageDigest.getInstance("MD5");
			for (MetaFileTorrent metaFile : metaFiles) {
				messageDigestMD5.update(metaFile.getMd5sum().array());
			}
			metaTorrent.setInfoHash(ByteBuffer.wrap(messageDigestMD5.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gera os metadados do torrent identificado pelo arquivo ou diretório shareFile.
	 * @return metadados do torrent
	 * @throws IOException
	 */
	public MetaTorrent generateMetaTorrent() 
			throws IOException {
		if (shareFile.isDirectory()) {
			deslocToRootDirectory = shareFile.getPath().length() 
					- shareFile.getName().length() - 1;
			processDir(shareFile);
		} else {
			MetaFileTorrent metaFileTorrent = new MetaFileTorrent();
			metaFileTorrent.setLength(shareFile.length());
			metaFileTorrent.setPathFile(shareFile.getName());
			generateHashKeys(shareFile, metaFileTorrent);
			metaTorrent.getInfo().addMetaFile(metaFileTorrent);
		}
		metaTorrent.getInfo().setPiecesLength(PIECE_LENGTH);
		generateInfoHash();
		return metaTorrent;
	}

	public void setComment(String comment) {
		metaTorrent.setComment(comment);
	}
	public void setCreatedBy(String createdBy) {
		metaTorrent.setCreatedBy(createdBy);
	}
	public void setEncoding(String encoding) {
		metaTorrent.setEncoding(encoding);
	}
	public void setTrackers(List<Tracker> trackers) {
		metaTorrent.setTrackers(trackers);
	}
	

}
