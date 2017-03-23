package br.ufla.naivetorrent;


import br.ufla.naivetorrent.cli.CommandLine;

public class Main {
	
	
	public static void main(String[] args) {
		new Thread(new CommandLine()).start();
	}

}
