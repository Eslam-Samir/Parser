package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.lang.Math;

import parser.TreeNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TreeController implements Initializable{
	
	@FXML
	private VBox parent;
	
	@FXML
	private ScrollPane canvasContainer ;
	
	@FXML
	private Canvas canvas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setLineWidth(2);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(18));
        
        /* testing */
        ArrayList<TreeNode> nodes = new ArrayList<>();
        for (int i = 0; i < 7; i++)
        	nodes.add(new TreeNode("token" + i));
        nodes.get(3).AddChild(new TreeNode("hi"));
        nodes.get(4).AddChild(new TreeNode("hi2"));
        nodes.get(4).AddChild(new TreeNode("hi2"));
        nodes.get(5).AddChild(new TreeNode("hi3"));
        nodes.get(5).AddChild(new TreeNode("hi3"));
        nodes.get(5).AddChild(new TreeNode("hi3"));
        TreeNode node = new TreeNode("token", nodes); 
		DrawTreeNode(gc, canvas.getWidth()/2, 10, node);
		canvasContainer.setHvalue(0.5);
	}
	
	private void DrawTreeNode(GraphicsContext gc, double x, double y, TreeNode node) {
		float width = 100, height = 50;
		String tokenName = node.getTokenName();
		boolean isRoot = node.isRoot();
		int childrenCount = node.getChildrenCount();
		
        gc.strokeRect(x, y, width, height);
        gc.fillText(tokenName, x + width/2 , y + height/2 + 5);
    
        if(!isRoot)
        {
        	double x1, y1, x2, y2, theta, r, initial_theta;
        	initial_theta = 20;
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
	        		r *= (childrenCount/1.5); // scale r depending on number of children
	        		x2 = x1 + r * Math.cos(theta);
	        		y2 = y1 + r * Math.sin(theta);
	        		System.out.println(r);
	        		System.out.println(Math.toDegrees(theta));
	        		gc.strokeLine(x1, y1, x2, y2);
	        		DrawTreeNode(gc, x2 - width/2, y2, node.getChild(i));
	        	}
        	}
        }
    }
	
	
}
