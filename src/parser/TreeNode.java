package parser;

import java.util.ArrayList;

public class TreeNode {
	private String Name;
	private String Value;
	private TreeNode NextNode;
	private ArrayList<TreeNode> Children;
	private boolean terminal;
	
	private double x;
	private double y;
	
	public TreeNode() {
		this.Children = new ArrayList<>();
	}
	
	public TreeNode(String name, String value) {
		this.Children = new ArrayList<>();
		this.Name = name;
		this.Value = value;
		this.terminal = false;
	}
	
	public TreeNode(String name, String value, boolean terminal) {
		this.Children = new ArrayList<>();
		this.Name = name;
		this.Value = value;
		this.terminal = terminal;
	}
	
	public void AddChild(TreeNode child) {
		this.Children.add(child);
	}
	
	public void setNextNode(TreeNode next) {
		this.NextNode = next;
	}
	
	public void setName(String name) {
		this.Name = name;
	}
	
	public void setValue(String value) {
		this.Value = value;
	}
	
	public boolean isRoot() {
		return this.Children.isEmpty();
	}
	
	public boolean hasNext() {
		return this.NextNode != null;
	}
	
	public int getChildrenCount() {
		return this.Children.size();
	}
	
	public String getTokenName() {
		return this.Name;
	}
	
	public String getTokenValue() {
		return this.Value;
	}
	
	public TreeNode getChild(int i)
	{
		return this.Children.get(i);
	}
	
	public TreeNode getNext()
	{
		return this.NextNode;
	}
	
	public boolean isEmpty()
	{
		if(Name == null && Value == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isTerminal() {
		return this.terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
