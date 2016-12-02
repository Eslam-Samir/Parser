package parser;

import java.util.ArrayList;

public class TreeNode {
	private String Name;
	private ArrayList<TreeNode> Children;
	
	public TreeNode(String name) {
		this.Children = new ArrayList<>();
		this.Name = name;
	}
	
	public TreeNode(String name, TreeNode child) {
		this.Children = new ArrayList<>();
		this.Children.add(child);
		this.Name = name;
	}
	
	public TreeNode(String name, ArrayList<TreeNode> children) {
		this.Children = new ArrayList<>();
		this.Children.addAll(children);
		this.Name = name;
	}
	
	public void AddChildren(ArrayList<TreeNode> children) {
		this.Children.addAll(children);
	}
	
	public void AddChild(TreeNode child) {
		this.Children.add(child);
	}
	
	public boolean isRoot() {
		return this.Children.isEmpty();
	}
	
	public int getChildrenCount() {
		return this.Children.size();
	}
	
	public String getTokenName() {
		return this.Name;
	}
	
	public TreeNode getChild(int i)
	{
		return Children.get(i);
	}
}
