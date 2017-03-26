package br.ufla.naivetorrent.cli.console;

public enum Console {
	
	DEFAULT ("\033[1;0m"),
	BOLD ("\033[1;1m"),
	REMOVE_BOLD ("\033[1;21m"),
	UNDERLINED ("\033[1;4m"),
	REMOVE_UNDERLINED ("\033[1;24m"),
	CLEAR ("\u001b[2J"),
	HOME ("\u001b[H"),
	CLEAR_HOME ("\u001b[2J\u001b[H");
	
	private String value;
	
	private Console(String value) {
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
