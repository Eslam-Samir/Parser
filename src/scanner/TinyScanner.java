package scanner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TinyScanner {
	private static final Map<String, String> TOKEN_NAMES = TokensMap();
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
	
	public void PrintTokens(String filename) throws IOException
	{
		BufferedWriter OutputFile = new BufferedWriter(new FileWriter("output.txt"));
		String token;
		OutputFile.write(String.format("%-30.30s  %-30.30s%n", "Token Value", "Token Type"));
		OutputFile.write("\r\n");
		for(int i = 0; i < Tokens.size(); i++)
		{
			token = Tokens.get(i);
			if(TOKEN_NAMES.containsKey(token))
			{
				OutputFile.write(String.format("%-30.30s  %-30.30s%n", token, TOKEN_NAMES.get(token)));
			}
			else if(RESERVED_WORDS.contains(token))
			{
				OutputFile.write(String.format("%-30.30s  %-30.30s%n", token, token.toUpperCase()));
			}
			else
			{
				boolean isNumber = true;
				for(int j = 0; j < token.length(); j++)
				{
					if(!Character.isDigit(token.charAt(j)))
					{
						isNumber = false;
						break;
					}
				}
				if(isNumber)
				{
					OutputFile.write(String.format("%-30.30s  %-30.30s%n", token, "NUMBER"));
				}
				else
				{
					OutputFile.write(String.format("%-30.30s  %-30.30s%n", token, "IDENTIFIER"));
				}
			}
		}
		OutputFile.close();
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
	
	private static Map<String, String> TokensMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(":=", "ASSIGN");
        result.put(";", "SEMI");
        result.put("+", "PLUS");
        result.put("-", "MINUS");
        result.put("/", "DIV");
        result.put("<", "LT");
        result.put("*", "MUL");
        result.put("(", "LB");
        result.put(")", "RB");
        result.put("=", "EQ");
        return result;
    }
}
