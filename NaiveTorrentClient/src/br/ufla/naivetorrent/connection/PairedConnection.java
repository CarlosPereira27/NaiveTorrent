package br.ufla.naivetorrent.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import br.ufla.naivetorrent.domain.file.BlockOfPiece;
import br.ufla.naivetorrent.domain.file.HashPiece;
import br.ufla.naivetorrent.domain.file.ReadPiece;
import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.file.WritePiece;
import br.ufla.naivetorrent.domain.peer.Peer;

public class PairedConnection implements Runnable {
	
	private ShareTorrent shareTorrent;
	private Peer peer;
	@SuppressWarnings("unused")
	private Socket mySocket;
	private OutputStream myOutputStream;
	private InputStream mySocketInput;
	private byte read[];
	private static int MAX_MESSAGE = 17;
	private Queue<Message> sendQueue;
	
	// CONSTRUCTOR
	public PairedConnection(ShareTorrent shareTorrent, Peer peer, 
			Socket mySocket) 
					throws IOException {
		this.shareTorrent = shareTorrent;
		this.peer = peer;
		this.mySocket = mySocket;
		sendQueue = new LinkedList<>();
		mySocketInput = mySocket.getInputStream();
		myOutputStream = mySocket.getOutputStream();
	}
	
	public void addSendMessage(Message message) {
		synchronized (sendQueue) {
			sendQueue.offer(message);
		}
	}
	
	public boolean haveSendMessage() {
		synchronized (sendQueue) {
			return !sendQueue.isEmpty();
		}
	}
	
	public Message nextMessage() {
		synchronized (sendQueue) {
			return sendQueue.poll();
		}
	}
	
	public Peer getPeer() {
		return peer;
	}
	
	public void requestPiece(Message message) {
		requestPiece(message.index, message.begin, message.length);
	}
	
	/**
	 * Requisita um pedaço
	 * @param index
	 * @param begin
	 * @param length
	 */
	public void requestPiece(int index, int begin, int length) {
		byte data[] = new byte[17];
		int indexDt = 0;
		indexDt = copyArray(data, 13, indexDt);
		data[indexDt++] = 6;
		indexDt = copyArray(data, index, indexDt);
		indexDt = copyArray(data, begin, indexDt);
		indexDt = copyArray(data, length, indexDt);
		try {
			myOutputStream.write(data);
			myOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cancelPiece(Message message) {
		cancelPiece(message.index, message.begin, message.length);
	}
	
	/**
	 * Cancela a requisição de um pedaço
	 * @param index
	 * @param begin
	 * @param length
	 */
	public void cancelPiece(int index, int begin, int length) {
		byte data[] = new byte[17];
		int indexDt = 0;
		indexDt = copyArray(data, 13, indexDt);
		data[indexDt++] = 8;
		indexDt = copyArray(data, index, indexDt);
		indexDt = copyArray(data, begin, indexDt);
		indexDt = copyArray(data, length, indexDt);
		try {
			myOutputStream.write(data);
			myOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Mantém a conexão viva
	 */
	private void keepAlive() {
		try {
			myOutputStream.write(new byte[4]);
			myOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void have(int index) {
		byte data[] = new byte[9];
		int indexDt = 0;
		indexDt = copyArray(data, 5, indexDt);
		data[indexDt++] = 4;
		indexDt = copyArray(data, index, indexDt);
		try {
			myOutputStream.write(data);
			myOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Envia um pedaço do arquivo
	 * @param index
	 * @param begin
	 * @param length
	 */
	private void send(int index, int begin, int length) {
		ReadPiece readPiece = new ReadPiece(shareTorrent, 
				new BlockOfPiece(index, begin, length));
		byte data[] = new byte[13 + length];
		int indexDt = 0;
		indexDt = copyArray(data, 9 + length, indexDt);
		data[indexDt++] = 7;
		indexDt = copyArray(data, index, indexDt);
		indexDt = copyArray(data, begin, indexDt);
		try {
			indexDt = copyArray(data, readPiece.read(), indexDt);
			myOutputStream.write(data);
			myOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Envia o bitfield.
	 */
	public void sendBitfield() {
		int length = shareTorrent.getMyBitfield().toByteArray().length;
		byte data[] = new byte[length + 5];
		int index = 0;
		index += copyArray(data, length + 1, index);
		data[index++] = 5;
		index = copyArray(data, shareTorrent.getMyBitfield().toByteArray(), index);
		try {
			myOutputStream.write(data);
			myOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recebe o bitfield.
	 */
	public void receiveBitfield() {
		int length = shareTorrent.getMyBitfield().toByteArray().length;
		byte data[] = new byte[length + 5];
		int index = 0;
		index += copyArray(data, length + 1, index);
		data[index++] = 5;
		index = copyArray(data, shareTorrent.getMyBitfield().toByteArray(), index);
		try {
			myOutputStream.write(data);
			myOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Escreve um pedaço do arquivo e confere a hash do pedaço
	 * @param index
	 * @param begin
	 * @param length
	 * @param data
	 */
	private void write(int index, int begin, int length, byte data[]) {
		WritePiece writePiece = new WritePiece(shareTorrent, 
				new BlockOfPiece(index, begin, length), data);
		try {
			if (writePiece.write() && begin + length >= shareTorrent.getPiecesLength()) {
				HashPiece hashPiece = new HashPiece(shareTorrent, index);
				if (hashPiece.check()) {
					shareTorrent.setMyBitfieldPiece(index);
				} else {
					shareTorrent.backToDownload(index);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message message) {
		switch(message.id) {
		case -1:
			keepAlive();
			break;
		case 4:
			have(message.index);
			break;
		case 5:
			sendBitfield();
			break;
		case 6:
			requestPiece(message);
			break;
		case 8:
			cancelPiece(message);
			break;
		}
	}

	/**
	 * Escuta o peer da conexão.
	 */
	@Override
	public void run() {
		while (true) {
			read = new byte[MAX_MESSAGE];
			int cont;
			try {
				cont = 0;
				while (mySocketInput.available() == 0) {
					Message message = nextMessage();
					if (message != null) {
						sendMessage(message);
						cont = 0;
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (cont == 50) {
						keepAlive();
					}
					cont++;
				}
				int bytesRead = mySocketInput.read(read);
				if (bytesRead < 4) {
					break;
				}
				int lengthMes = new BigInteger(Arrays.copyOf(read, 4)).intValue();
				// KEEP_ALIVE
				if (lengthMes == 0) {
					continue;
				}
				byte messageId = read[4];
				int index = 0;
				int begin = 0;
				int length = 0;
				if (messageId == 4 || (messageId > 5 && messageId < 9)) {
					index = new BigInteger(
							Arrays.copyOfRange(read, 5, 9)).intValue();
					if (messageId != 4) {
						begin = new BigInteger(
								Arrays.copyOfRange(read, 9, 13)).intValue();
						length = new BigInteger(
								Arrays.copyOfRange(read, 13, 17)).intValue();
					}
				}
				switch (messageId) {
				case 4:
					shareTorrent.setPeerBitfield(peer, index);
					break;
				case 5:
					// não irá acontecer
					break;
				case 6:
					send(index, begin, length);
					break;
				case 7:
					byte piece[] = new byte[length];
					mySocketInput.read(piece);
					write(index, begin, length, piece);
					break;
				case 8:
					break;
				default:
					break;	
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Close connection");
				break;
			}
		}
	}
	
	public static class Message {
		byte id;
		int index;
		int begin;
		int length;
	}
	
	/**
	 * Realiza a copia dos bytes de um array de bytes para um buffer de dados em um 
	 * determinado índice.
	 * @param data
	 * @param val
	 * @param index
	 * @return
	 */
	public static int copyArray(byte data[], byte dataCp[], int index) {
		int n = dataCp.length;
		for (int i = 0; i < n; i++) {
			data[index + i] = dataCp[i];
		}
		return index + n;
	}
	
	/**
	 * Realiza a copia dos bytes de um array de bytes para um buffer de dados em um 
	 * determinado índice.
	 * @param data
	 * @param val
	 * @param index
	 * @return
	 */
	public static int copyArray(byte data[], byte dataCp[], int index, int length) {
		for (int i = 0; i < length; i++) {
			data[index + i] = dataCp[i];
		}
		return index + length;
	}
	
	/**
	 * Realiza a copia dos bytes de um inteiro para um buffer de dados em um 
	 * determinado índice.
	 * @param data
	 * @param val
	 * @param index
	 * @return
	 */
	public static int copyArray(byte data[], int val, int index) {
		byte dataCp[] = BigInteger.valueOf(val).toByteArray();
		int n = dataCp.length;
		for (int i = 0; i < 4 - n; i++) {
			data[index++] = 0;
		}
		return copyArray(data, dataCp, index);
	}

}
