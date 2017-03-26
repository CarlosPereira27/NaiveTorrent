package br.ufla.naivetorrent.cli;

import br.ufla.naivetorrent.cli.console.Console;
import br.ufla.naivetorrent.cli.console.ConsoleForegroundColors;

public class ExtractCommandsIds extends ExtractCommand {

	public ExtractCommandsIds(String[] commandTokens) {
		super(commandTokens);
		// TODO Auto-generated constructor stub
	}
	
	
	public int readParameterint() 
			throws Exception  {
		
		int parameter = -1;
		
		// não possui parâmetro
		if (index > tokens.length) {
			return -1;
		}
		// parâmetro incorreto
		
		try{
			
		 Integer.parseInt(tokens[index]);
		 return parameter;
			
		}
		catch(Exception e){
			throw new Exception(ConsoleForegroundColors.RED.getValue()+"\n o segundo parametro deve ser um número!\n"+Console.DEFAULT.getValue());
		}
	}
	
	
}
