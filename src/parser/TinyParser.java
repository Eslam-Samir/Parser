package parser;

import java.util.ArrayList;

import extras.Alerts;

public class TinyParser {
	
	private static TinyParser ParserSingleton = new TinyParser();
	
	private ArrayList<String> Tokens;
	private ArrayList<String> TokensTypes;
	private TreeNode root;
	
	private String CurrentToken;
	private int pointer;
	
	private TinyParser() {
		Tokens = new ArrayList<>();
		TokensTypes = new ArrayList<>();
	}
	
	public void setTokens(ArrayList<String> tokens, ArrayList<String> types) {
		Tokens.clear();
		TokensTypes.clear();
		Tokens.addAll(tokens);
		TokensTypes.addAll(types);
		pointer = 0;
		CurrentToken = TokensTypes.get(0);
	}
	
	public static TinyParser getParserInstance() {
		return ParserSingleton;
	}
	
	public TreeNode getRoot()
	{
		return root;
	}
	
	public void RunParser() {
		root = stmt_sequence();
	}
	
	private TreeNode stmt_sequence() {
		TreeNode parent = statment();
		TreeNode temp = parent; 
		while(CurrentToken.equals("semi"))
		{
			match("semi");
			TreeNode next = statment();
			temp.setNextNode(next);
			temp = next;
		}
		return parent;
	}
	
	private TreeNode statment() {
		if(CurrentToken.equals("if"))
		{
			return if_stmt();
		}
		else if(CurrentToken.equals("repeat"))
		{
			return repeat_stmt();
		}
		else if(CurrentToken.equals("identifier"))
		{
			return assign_stmt();
		}
		else if(CurrentToken.equals("read"))
		{
			return read_stmt();
		}
		else if(CurrentToken.equals("write"))
		{
			return write_stmt();
		}
		else
		{
			error(Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
		return null;
	}
	
	private TreeNode if_stmt() {
		TreeNode parent = match("if");
		parent.AddChild(exp());
		match("then");
		parent.AddChild(stmt_sequence());
		
		if(CurrentToken.equals("else"))
		{
			match("else");
			parent.AddChild(stmt_sequence());
		}
		
		match("end");
		return parent;
	}
	
	private TreeNode repeat_stmt() {
		TreeNode parent = match("repeat");
		parent.AddChild(stmt_sequence());
		match("until");
		parent.AddChild(exp());
		return parent;
	}
	
	private TreeNode assign_stmt() {
		String name = match("identifier").getTokenValue();
		TreeNode parent = match("assign");
		parent.setValue(name);
		parent.AddChild(exp());
		return parent;
	}
	
	private TreeNode read_stmt() {
		TreeNode parent = match("read");
		String name = match("identifier").getTokenValue();
		parent.setValue(name);
		return parent;
	}

	private TreeNode write_stmt() {
		TreeNode parent = match("write");
		parent.AddChild(exp());
		return parent;
	}
	
	private TreeNode exp() {
		TreeNode left = simple_exp();
		while(CurrentToken.equals("op") && (Tokens.get(pointer).equals("<") || Tokens.get(pointer).equals("=")))
		{
			TreeNode newLeft = match(CurrentToken);
			newLeft.AddChild(left);
			TreeNode right = simple_exp();
			newLeft.AddChild(right);
			left = newLeft;
		}
		return left;
	}
	
	private TreeNode simple_exp() {
		TreeNode left = term();
		while(CurrentToken.equals("op") && (Tokens.get(pointer).equals("+") || Tokens.get(pointer).equals("-")))
		{
				TreeNode newLeft = match(CurrentToken);
				newLeft.AddChild(left);
				TreeNode right = term();
				newLeft.AddChild(right);
				left = newLeft;
		}
		return left;
	}
	
	private TreeNode term() {
		TreeNode left = factor();
		while(CurrentToken.equals("op") && (Tokens.get(pointer).equals("*") || Tokens.get(pointer).equals("/")))
		{
			TreeNode newLeft = match(CurrentToken);
			newLeft.AddChild(left);
			TreeNode right = factor();
			newLeft.AddChild(right);
			left = newLeft;
		}
		return left;
	}
	
	private TreeNode factor() {
		if(CurrentToken.equals("("))
		{
			match("(");
			TreeNode node = exp();
			match(")");
			return node;
		}
		else if(CurrentToken.equals("number") || CurrentToken.equals("identifier"))
		{
			return match(CurrentToken);
		}
		else
		{
			error(Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
		return null;
	}

	private TreeNode match(String token) {
		System.out.println("matching " + CurrentToken + " With " + token + " " + String.valueOf(pointer)); 
		if(CurrentToken.equals(token))
		{
			TreeNode node;
			if(CurrentToken.equals("op") || CurrentToken.equals("identifier") || CurrentToken.equals("number"))
				node = new TreeNode(CurrentToken, Tokens.get(pointer), true);
			else
				node = new TreeNode(CurrentToken, Tokens.get(pointer), false);
			System.out.println("matched " + CurrentToken + " " + String.valueOf(pointer));
			pointer++;
			if(pointer < TokensTypes.size())
				CurrentToken = TokensTypes.get(pointer);
			return node;
		}
		else
		{
			error(Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
		return null;
	}
	
	private void error(int line) {
		String error = "Error At Token: " + CurrentToken;
		String content = "TinyParser Line: " + String.valueOf(line) + "\nToken Number: " + String.valueOf(pointer);
		Alerts.createErrorAlert(error, content);
	}
}
