package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

import parser.TinyParser;
import parser.TreeNode;
import scanner.TinyScanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
      /*  ArrayList<TreeNode> nodes = new ArrayList<>();
        for (int i = 0; i < 7; i++)
        	nodes.add(new TreeNode("token" + i, " "));
        nodes.get(3).AddChild(new TreeNode("hi", " "));
        nodes.get(4).AddChild(new TreeNode("hi2", " "));
        nodes.get(4).AddChild(new TreeNode("hi2", " "));
        nodes.get(5).AddChild(new TreeNode("hi3", " "));
        nodes.get(5).AddChild(new TreeNode("hi3", " "));
        nodes.get(5).AddChild(new TreeNode("hi3", " "));
        TreeNode root = new TreeNode("token", " ", nodes);
        */
        
        BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("input.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = "";
		TinyScanner scanner = new TinyScanner();
		try {
			while((line = reader.readLine()) != null)
			{
				scanner.Scan(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TinyParser parser = new TinyParser(scanner.getTokens(), scanner.getTokensTypes());
		parser.RunParser();
		TreeNode root = parser.getRoot();
		DrawTreeNode(gc, 10, 10, root);
		
		//canvasContainer.setHvalue(0.5);
	}
	
	private void DrawTreeNode(GraphicsContext gc, double x, double y, TreeNode node) {
		float width = 100, height = 50;
		String tokenName = node.getTokenName();
		String tokenValue = "(" + node.getTokenValue() + ")";
		boolean isRoot = node.isRoot();
		int childrenCount = node.getChildrenCount();
		
        gc.setFill(new Color(1, 1, 1, 0.8));
        gc.fillRect(x, y, width, height);
        gc.setFill(new Color(0, 0, 0, 1.0));
        gc.strokeRect(x, y, width, height);
        gc.fillText(tokenName, x + width/2 , y + height/2 - 5);
        gc.fillText(tokenValue, x + width/2 , y + height/2 + 15);
    
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
        
        if(node.hasNext())
        {
        	double x1, y1, x2, y2;
        	x1 = x + width;
        	y1 = y + height/2;
        	x2 = x1 + width * (childrenCount + 2);
        	y2 = y1;
        	
        	gc.strokeLine(x1, y1, x2, y2);
        	DrawTreeNode(gc, x2, y2 - height/2, node.getNext());
        }
    }
	
	
}
