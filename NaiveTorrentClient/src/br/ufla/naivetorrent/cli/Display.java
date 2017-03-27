/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufla.naivetorrent.cli;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ufla.naivetorrent.cli.console.Console;
import br.ufla.naivetorrent.domain.file.ShareTorrent;

/**
 * 
 * @author luizfps
 */
public class Display implements Runnable {

	private static final String EMPTY_TXT =
			"-----------------------------------------\n"
			+ "O usuário não possui torrents atualmente!\n"
			+ "-----------------------------------------\n"
			+ "# Entre com o comando 'cmd' para entrar na interface de inserção de comandos.\n"
			+ "# Uma vez nesta interface é possível criar e adicionar torrents.\n"
			+ "# O comando 'help' mostra a lista de comandos aceitos pelo NaiveTorrent.\n"
			+ "# O comando 'quit' fecha o NaiveTorrent adequadamente.\n";
	private List<ShareTorrent> shareTorrents;
	private boolean ativa;

	public Display(List<ShareTorrent> shareTorrents) {
		this.shareTorrents = shareTorrents;
		ativa = true;
	}
	
	private void displayData() {
		synchronized (shareTorrents) {
			int n = shareTorrents.size();
			for (int i = 0; i < n; i++) {
				System.out.println(shareTorrents.get(i).toString());
			}
			if (n == 0) {
				System.out.println(EMPTY_TXT);
			}
		}
	}

	@Override
	public void run() {
		Console.CLEAR_HOME.apply();
		while (isAtiva()) {
			try {
				displayData();
				Thread.sleep(4000);
				if (isAtiva()) {
					Console.CLEAR_HOME.apply();
				}
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
				Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	public synchronized boolean isAtiva() {
		return ativa;
	}
	public synchronized void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

}
