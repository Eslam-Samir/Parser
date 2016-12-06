package scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TinyScanner {
	
	private static TinyScanner ScannerSingleton = new TinyScanner();
	
	private static final List<String> RESERVED_WORDS = Arrays.asList(
		"if", "then", "else", "end", "repeat", "until", "read", "write"
		);
	
	private State CurrentState;
	private State NextState;
	private ArrayList<String> Tokens;
	private ArrayList<String> TokensTypes;
	
	private TinyScanner() {
		NextState = State.START;
		Tokens = new ArrayList<>();
		TokensTypes = new ArrayList<>();
	}
	
	public static TinyScanner getScannerInstance() {
	    return ScannerSingleton;
	}

	public void Scan(String line)
	{
		String token = "";
		String type = "";
		char CurrentChar = 0;
		int i = 0;
		while(i < line.length() || NextState == State.DONE)
		{	
			if(NextState != State.DONE)
			{
				CurrentChar = line.charAt(i);
			}
			CurrentState = NextState;
			switch(CurrentState)
			{
			case START:
				if(CurrentChar == ' ')
				{
					NextState = State.START;
				}
				else if(CurrentChar == '{')
				{
					NextState = State.IN_COMMENT;
				}
				else if(CurrentChar == ':')
				{
					token += CurrentChar;
					type = "assign";
					NextState = State.IN_ASSIGN;
				}
				else if(Character.isLetter(CurrentChar))
				{
					token += CurrentChar;
					type = "identifier";
					NextState = State.IN_IDENTIFIER;
				}
				else if(Character.isDigit(CurrentChar))
				{
					token += CurrentChar;
					type = "number";
					NextState = State.IN_NUMBER;
				}
				else if(CurrentChar == '+' || CurrentChar == '-' || CurrentChar == '*' ||
						CurrentChar == '/' || CurrentChar == '(' || CurrentChar == ')' ||
						CurrentChar == '<' || CurrentChar == ';' || 
						CurrentChar == '=')
				{
					if(CurrentChar == ';')
						type = "semi";
					else if(CurrentChar == '(' || CurrentChar == ')')
						type = String.valueOf(CurrentChar);
					else
						type = "op";
					token += CurrentChar;
					NextState = State.DONE;
				}
				else
				{
					NextState = State.DONE;
				}
				i++;
				if(i >= line.length())
					NextState = State.DONE;
				break;
				
			case IN_COMMENT:
				if(CurrentChar == '}')
				{
					NextState = State.START;
				}
				else
				{
					NextState = State.IN_COMMENT;
				}
				i++;
				break;
				
			case IN_NUMBER:
				if(Character.isDigit(CurrentChar))
				{
					token += CurrentChar;
					i++;	
					if(i == line.length())
						NextState = State.DONE;
					else
						NextState = State.IN_NUMBER;
				}
				else
				{
					NextState = State.DONE;
				}
				break;
				
			case IN_IDENTIFIER:
				if(Character.isLetter(CurrentChar))
				{
					token += CurrentChar;
					NextState = State.IN_IDENTIFIER;
					i++;
					if(i == line.length())
						NextState = State.DONE;
					else
						NextState = State.IN_IDENTIFIER;
				}
				else
				{
					NextState = State.DONE;
				}
				break;
			
			case IN_ASSIGN:
				if(CurrentChar == '=')
				{
					token += CurrentChar;
					i++;
				}
				NextState = State.DONE;
				break;
				
			case DONE:
				NextState = State.START;
				if(!token.isEmpty())
				{
					TokensTypes.add(type);
					Tokens.add(token);
					token = "";
				}
				break;
			}
		}		
	}
	
	public String getTokensString()
	{
		String TokensString = "";
		for(int i = 0; i < Tokens.size(); i++)
		{
			TokensString += String.format("%-30.30s  %-30.30s%n", Tokens.get(i), TokensTypes.get(i));
		}
		return TokensString;
	}
	
	public void readTokensFromFile(File file)
	{
		if(file != null)
		{
			Scanner scan; // line scanner
			try {
				scan = new Scanner(file);
				
				while (scan.hasNext()) {
					String line = scan.nextLine();
					String arr[] = line.split("\\s+"); // split on any whitespace character
					if(arr.length == 2)
					{
						Tokens.add(arr[0]);
						TokensTypes.add(arr[1]);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public ArrayList<String> getTokens()
	{
		return Tokens;
	}
	
	public ArrayList<String> getTokensTypes()
	{
		for(int i = 0; i < Tokens.size(); i++)
		{
			if(RESERVED_WORDS.contains(Tokens.get(i)))
			{
				TokensTypes.remove(i); //replace identifier with reserved word
				TokensTypes.add(i, Tokens.get(i));
			}
		}
		return TokensTypes;
	}
	
	public boolean isEmpty() {
		if(Tokens.isEmpty() || TokensTypes.isEmpty())
		{
			return true;
		}
		return false;
	}
	
	public void clear() {
		Tokens.clear();
		TokensTypes.clear();
	}
	
}
