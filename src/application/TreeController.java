package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.lang.Math;

import parser.TinyParser;
import parser.TreeNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;

public class TreeController implements Initializable{
	
	private static final float width = 100, height = 50;
	private static final int CANVAS_WIDTH = 3000, CANVAS_HEIGHT = 3000;
	
	@FXML
	private VBox parent;
	
	@FXML
	private ScrollPane canvasContainer ;
	
	@FXML
	private Canvas canvas;

	public void setTreeTokens(ArrayList<String> tokens, ArrayList<String> types) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setLineWidth(2);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(18));
        TinyParser parser = new TinyParser(tokens, types);
        parser.RunParser();
		TreeNode root = parser.getRoot();
		DrawTreeNode(gc, 100, 10, root);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		canvas.setWidth(CANVAS_WIDTH);
		canvas.setHeight(CANVAS_HEIGHT);
		//canvasContainer.setHvalue(0.5);
	}
	
	private void DrawTreeNode(GraphicsContext gc, double x, double y, TreeNode node) {
		String tokenName = node.getTokenName();
		String tokenValue = "(" + node.getTokenValue() + ")";
		boolean isRoot = node.isRoot();
		int childrenCount = node.getChildrenCount();
		int padding = 0;
		
		if(tokenName.equals("op") || tokenName.equals("id") || tokenName.equals("const"))
			DrawTerminalNode(gc, x, y, tokenName, tokenValue);
		else
			DrawNonTerminalNode(gc, x, y, tokenName, tokenValue);
    
        if(!isRoot)
        {
        	double x1, y1, x2, y2, theta, r, initial_theta;
        	if((initial_theta = 70 - 10*childrenCount) <= 0)
        		initial_theta = 15;
        	
        	x1 = x + width/2;
        	y1 = y + height;
        	if(childrenCount == 1)
        	{
        		theta = Math.toRadians(90);
        		r = height/Math.sin(theta);
        		x2 = x1 + r * Math.cos(theta);
        		y2 = y1 + r * Math.sin(theta);
        		gc.strokeLine(x1, y1, x2, y2);
        		DrawTreeNode(gc, x2 - width/2, y2, node.getChild(0));
        	}
        	else
        	{
	        	for(int i = 0; i < childrenCount; i++)
	        	{
	        		theta = -(initial_theta + i*((180 - 2*initial_theta) / (childrenCount-1)));
	        		theta = Math.toRadians(theta);
	        		r = height/Math.sin(theta);
	        		r *= (childrenCount/1); // scale r depending on number of children
	        		x2 = x1 + r * Math.cos(theta) + padding;
	        		y2 = y1 + r * Math.sin(theta);
	        		
	        		gc.strokeLine(x1, y1, x2, y2);
	        		DrawTreeNode(gc, x2 - width/2, y2, node.getChild(i));
	        		
	        		TreeNode next = node.getChild(i).getNext();
	        		while(next != null)
	        		{
	        			padding += width * (childrenCount + 1);
	        			next = next.getNext();
	        		}
	        	}
        	}
        }
        
        if(node.hasNext())
        {
        	double x1, y1, x2, y2;
        	x1 = x + width;
        	y1 = y + height/2;
        	x2 = x1 + width * (childrenCount + 1);
        	y2 = y1;
        	
        	gc.strokeLine(x1, y1, x2, y2);
        	DrawTreeNode(gc, x2, y2 - height/2, node.getNext());
        }
    }
	
	private void DrawTerminalNode(GraphicsContext gc, double x, double y, String name, String value)
	{
		gc.setFill(new Color(1, 1, 1, 0.8));
        gc.fillOval(x, y, width, height);
        gc.setFill(new Color(0, 0, 0, 1.0));
        gc.strokeOval(x, y, width, height);
        gc.fillText(name, x + width/2 , y + height/2 - 5);
	    gc.fillText(value, x + width/2 , y + height/2 + 15);
	}
	
	private void DrawNonTerminalNode(GraphicsContext gc, double x, double y, String name, String value)
	{
		gc.setFill(new Color(1, 1, 1, 0.8));
        gc.fillRect(x, y, width, height);
        gc.setFill(new Color(0, 0, 0, 1.0));
        gc.strokeRect(x, y, width, height);
        
        if(name.equals("write") || name.equals("repeat") || name.equals("if"))
        {
	        gc.fillText(name, x + width/2 , y + height/2 + 5);
        }
        else
        {
        	gc.fillText(name, x + width/2 , y + height/2 - 5);
	        gc.fillText(value, x + width/2 , y + height/2 + 15);
        }
	}
}
