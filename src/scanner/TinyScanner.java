package scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TinyScanner {
	private static final List<String> RESERVED_WORDS = Arrays.asList(
		"if", "then", "else", "end", "repeat", "until", "read", "write"
		);
	
	private State CurrentState;
	private State NextState;
	private ArrayList<String> Tokens;
	private ArrayList<String> TokensTypes;
	
	public TinyScanner() {
		NextState = State.START;
		Tokens = new ArrayList<>();
		TokensTypes = new ArrayList<>();
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
					type = "id";
					NextState = State.IN_IDENTIFIER;
				}
				else if(Character.isDigit(CurrentChar))
				{
					token += CurrentChar;
					type = "const";
					NextState = State.IN_NUMBER;
				}
				else if(CurrentChar == '+' || CurrentChar == '-' || CurrentChar == '*' ||
						CurrentChar == '/' || CurrentChar == '(' || CurrentChar == ')' ||
						CurrentChar == '<' || CurrentChar == ';' || 
						CurrentChar == '=')
				{
					type = String.valueOf(CurrentChar);
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
			String token = Tokens.get(i), type = TokensTypes.get(i);
			if(token.equals("<") || token.equals("=") || token.equals("+") ||
					token.equals("-") || token.equals("*") || token.equals("/"))
				type = "op";
			else if(token.equals(";"))
				type = "semi";
			TokensString += String.format("%-30.30s  %-30.30s%n", token, type);
		}
		return TokensString;
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
				TokensTypes.remove(i);
				TokensTypes.add(i, Tokens.get(i));
			}
		}
		return TokensTypes;
	}
	
}
