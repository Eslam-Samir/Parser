package parser;

import java.util.ArrayList;

public class TreeNode {
	private String Name;
	private String Value;
	private TreeNode NextNode;
	private ArrayList<TreeNode> Children;
	
	public TreeNode() {
		this.Children = new ArrayList<>();
	}
	
	public TreeNode(String name, String value) {
		this.Children = new ArrayList<>();
		this.Name = name;
		this.Value = value;
	}
	
	public TreeNode(String name, String value, TreeNode child) {
		this.Children = new ArrayList<>();
		this.Children.add(child);
		this.Name = name;
		this.Value = value;
	}
	
	public TreeNode(String name, String value, ArrayList<TreeNode> children) {
		this.Children = new ArrayList<>();
		this.Children.addAll(children);
		this.Name = name;
		this.Value = value;
	}
	
	public void AddChildren(ArrayList<TreeNode> children) {
		this.Children.addAll(children);
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
}
