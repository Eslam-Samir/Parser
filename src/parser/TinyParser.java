package parser;

import java.util.ArrayList;

public class TinyParser {
	private ArrayList<String> Tokens;
	private ArrayList<String> TokensTypes;
	private TreeNode root;
	
	private String CurrentToken;
	private int pointer;
	
	public TinyParser(ArrayList<String> tokens, ArrayList<String> types) { 
		Tokens = new ArrayList<>();
		TokensTypes = new ArrayList<>();
		Tokens.addAll(tokens);
		TokensTypes.addAll(types);
		pointer = 0;
		CurrentToken = TokensTypes.get(0);
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
		while(CurrentToken.equals(";"))
		{
			match(";");
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
		else if(CurrentToken.equals("id"))
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
		String name = match("id").getTokenValue();
		TreeNode parent = match("assign");
		parent.setValue(name);
		parent.AddChild(exp());
		return parent;
	}
	
	private TreeNode read_stmt() {
		TreeNode parent = match("read");
		String name = match("id").getTokenValue();
		parent.setValue(name);
		return parent;
	}

	private TreeNode write_stmt() {
		TreeNode parent = match("write");
		parent.AddChild(exp());
		return parent;
	}
	
	private TreeNode exp() {
		TreeNode parent = new TreeNode();

		TreeNode left = simple_exp();
		TreeNode temp = parent;
		while(CurrentToken.equals("<") || CurrentToken.equals("="))
		{
			temp.setName("op");
			temp.setValue(CurrentToken);
			match(CurrentToken);
			TreeNode right = simple_exp();
			temp.AddChild(left);
			temp.AddChild(right);
			temp = left;
		}
		if(parent.isEmpty())
			return left;
		else 
			return parent;
	}
	
	private TreeNode simple_exp() {
		TreeNode parent = new TreeNode();

		TreeNode left = term();
		TreeNode temp = parent;
		while(CurrentToken.equals("+") || CurrentToken.equals("-"))
		{
			temp.setName("op");
			temp.setValue(CurrentToken);
			match(CurrentToken);
			TreeNode right = term();
			temp.AddChild(left);
			temp.AddChild(right);
			temp = left;
		}
		if(parent.isEmpty())
			return left;
		else 
			return parent;
	}
	
	private TreeNode term() {
		TreeNode parent = new TreeNode();

		TreeNode left = factor();
		TreeNode temp = parent;
		while(CurrentToken.equals("*") || CurrentToken.equals("/"))
		{
			temp.setName("op");
			temp.setValue(CurrentToken);
			match(CurrentToken);
			TreeNode right = factor();
			temp.AddChild(left);
			temp.AddChild(right);
			temp = left;
		}
		if(parent.isEmpty())
			return left;
		else 
			return parent;
	}
	
	private TreeNode factor() {
		if(CurrentToken.equals("("))
		{
			TreeNode parent = new TreeNode();
			parent.AddChild(match("("));
			parent.AddChild(exp());
			parent.AddChild(match(")"));
			return parent;
		}
		else if(CurrentToken.equals("const") || CurrentToken.equals("id"))
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
			TreeNode node = new TreeNode(CurrentToken, Tokens.get(pointer));
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
		for(int i = 0; i < TokensTypes.size(); i++)
			System.out.println(TokensTypes.get(i));
		System.out.println("Error At Token: " +CurrentToken + ", TinyParser Line: " + String.valueOf(line));
		System.out.println("Pointer Value: " + String.valueOf(pointer));
		System.exit(1);
	}
}
