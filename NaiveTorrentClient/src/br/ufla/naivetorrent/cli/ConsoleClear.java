package br.ufla.naivetorrent.cli;

public class ConsoleClear {
	
    public static final String ANSI_CLS = "\u001b[2J";
    public static  final String ANSI_HOME = "\u001b[H";
    
    
    public static void clear() {
		System.out.print(ANSI_CLS + ANSI_HOME);
		System.out.flush();
    }

}
