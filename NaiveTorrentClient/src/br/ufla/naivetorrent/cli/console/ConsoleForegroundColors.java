package br.ufla.naivetorrent.cli.console;

public enum ConsoleForegroundColors {
	
	DEFAULT ("\033[1;39m"),
	BLACK ("\033[1;30m"),
	RED ("\033[1;31m"),
	GREEN ("\033[1;32m"),
	YELLOW ("\033[1;33m"),
	BLUE ("\033[1;34m"),
	MAGENTA_PURPLE ("\033[1;35m"),
	CYAN ("\033[1;36m"),
	LIGHT_GRAY ("\033[1;37m"),
	DARK_GRAY ("\033[1;90m"),
	LIGHT_RED ("\033[1;91m"),
	LIGHT_GREEN ("\033[1;92m"),
	LIGHT_YELLOW ("\033[1;93m"),
	LIGHT_BLUE ("\033[1;94m"),
	LIGHT_MAGENTA_PINK ("\033[1;95m"),
	LIGHT_CYAN ("\033[1;96m"),
	WHITE ("\033[1;97m");
	
	private String value;
	
	private ConsoleForegroundColors(String value) {
		this.value = value;
	}
	
	public void apply() {
		System.out.print(value);
		System.out.flush();
	}
	
	public String getValue() {
		return value;
	}
	

}
