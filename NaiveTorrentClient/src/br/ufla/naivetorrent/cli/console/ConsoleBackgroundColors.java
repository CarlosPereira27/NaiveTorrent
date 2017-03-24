package br.ufla.naivetorrent.cli.console;

public enum ConsoleBackgroundColors {
	
	DEFAULT ("\033[1;49m"),
	BLACK ("\033[1;40m"),
	RED ("\033[1;41m"),
	GREEN ("\033[1;42m"),
	YELLOW ("\033[1;43m"),
	BLUE ("\033[1;44m"),
	MAGENTA_PURPLE ("\033[1;45m"),
	CYAN ("\033[1;46m"),
	LIGHT_GRAY ("\033[1;47m"),
	DARK_GRAY ("\033[1;100m"),
	LIGHT_RED ("\033[1;101m"),
	LIGHT_GREEN ("\033[1;102m"),
	LIGHT_YELLOW ("\033[1;103m"),
	LIGHT_BLUE ("\033[1;104m"),
	LIGHT_MAGENTA_PINK ("\033[1;105m"),
	LIGHT_CYAN ("\033[1;106m"),
	WHITE ("\033[1;107m");
	
	private String value;
	
	private ConsoleBackgroundColors(String value) {
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
