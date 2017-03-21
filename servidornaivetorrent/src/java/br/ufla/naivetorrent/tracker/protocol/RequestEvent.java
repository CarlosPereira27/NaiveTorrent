package br.ufla.naivetorrent.tracker.protocol;

import java.io.Serializable;

/**
 * 
 * Representa o tipo de evento ao realizar um requisição ao tracker. Os tipos são:
 * STARTED - a comunicação está sendo iniciada.
 * STOPPED - a comunicação está sendo interrompida pelo cliente
 * COMPLETED - o cliente completou de baixar o arquivo, tornou-se um seeder
 * STOPPED_FILE - o cliente está interrompendo a comunicação para um determinado arquivo
 * STARTED_FILE - o cliente está inicicando a comunicação para  um determinado arquivo.
 * @author carlos
 *
 */
public enum RequestEvent implements Serializable {
	
	STARTED,
	STOPPED,
	COMPLETED,
	STOPPED_FILE,
	STARTED_FILE;
	
	/**
	 * Recupera uma instância de um evento através de seu número ordinal.
	 * @param ordinal número que representa a instância.
	 * @return instância do evento
	 */
	public static RequestEvent getInstance(int ordinal) {
		switch (ordinal) {
		case 0:
			return STARTED;
		case 1:
			return STOPPED;
		case 2:
			return COMPLETED;
		case 3:
			return STOPPED_FILE;
		case 4:
			return STARTED_FILE;
		default:
			throw new RuntimeException("Instância inválida!");
		}
	}

}
