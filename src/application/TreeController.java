package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

import javax.imageio.ImageIO;

import parser.TinyParser;
import parser.TreeNode;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TreeController implements Initializable{
	
	private static final double NODE_WIDTH = 100, NODE_HEIGHT = 50;
	private static final int CANVAS_NODE_WIDTH = 3000, CANVAS_NODE_HEIGHT = 3000;
	private static final int INITIAL_X = 100, INITIAL_Y = 10;
	
	private double maxX = 0, maxY = 0;
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
		DrawTreeNode(gc, INITIAL_X, INITIAL_Y, root);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		canvas.setWidth(CANVAS_NODE_WIDTH);
		canvas.setHeight(CANVAS_NODE_HEIGHT);
	}
	
	public void SaveCanvas(ActionEvent action) {
		Stage stage = (Stage) canvas.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Tree Image");
		ExtensionFilter pngFilter = 
                new FileChooser.ExtensionFilter("Image files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(pngFilter);
		fileChooser.setInitialFileName("SyntaxTree");
		File file = fileChooser.showSaveDialog(stage);
		
		int width = (int)(INITIAL_X + maxX + 2*NODE_WIDTH);
		int height = (int)(INITIAL_Y + maxY + NODE_HEIGHT);
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableImage image = new WritableImage(width, height);
		SnapshotParameters param = new SnapshotParameters();
		canvas.snapshot(param, image);
		try {
			String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
			ImageIO.write(SwingFXUtils.fromFXImage(image, bufferedImage), extension, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
        	
        	x1 = x + NODE_WIDTH/2;
        	y1 = y + NODE_HEIGHT;
        	if(childrenCount == 1)
        	{
        		theta = Math.toRadians(90);
        		r = NODE_HEIGHT/Math.sin(theta);
        		x2 = x1 + r * Math.cos(theta);
        		y2 = y1 + r * Math.sin(theta);
        		if(x2 > maxX)
        			maxX = x2;
        		if(y2 > maxY)
        			maxY = y2;
        		gc.strokeLine(x1, y1, x2, y2);
        		DrawTreeNode(gc, x2 - NODE_WIDTH/2, y2, node.getChild(0));
        	}
        	else
        	{
	        	for(int i = 0; i < childrenCount; i++)
	        	{
	        		theta = -(initial_theta + i*((180 - 2*initial_theta) / (childrenCount-1)));
	        		theta = Math.toRadians(theta);
	        		r = NODE_HEIGHT/Math.sin(theta);
	        		r *= (childrenCount/1); // scale r depending on number of children
	        		x2 = x1 + r * Math.cos(theta) + padding;
	        		y2 = y1 + r * Math.sin(theta);
	        		
	        		if(x2 > maxX)
	        			maxX = x2;
	        		if(y2 > maxY)
	        			maxY = y2;
	        		
	        		gc.strokeLine(x1, y1, x2, y2);
	        		DrawTreeNode(gc, x2 - NODE_WIDTH/2, y2, node.getChild(i));
	        		
	        		TreeNode next = node.getChild(i).getNext();
	        		while(next != null)
	        		{
	        			padding += NODE_WIDTH * (childrenCount + 1);
	        			next = next.getNext();
	        		}
	        	}
        	}
        }
        
        if(node.hasNext())
        {
        	double x1, y1, x2, y2;
        	x1 = x + NODE_WIDTH;
        	y1 = y + NODE_HEIGHT/2;

        	x2 = x1 + NODE_WIDTH * (childrenCount + 1);
        	y2 = y1;
    		if(x2 > maxX)
    			maxX = x2;
    		if(y2 > maxY)
    			maxY = y2;
        	gc.strokeLine(x1, y1, x2, y2);
        	DrawTreeNode(gc, x2, y2 - NODE_HEIGHT/2, node.getNext());
        }
    }
	
	private void DrawTerminalNode(GraphicsContext gc, double x, double y, String name, String value)
	{
		gc.setFill(new Color(1, 1, 1, 0.8));
        gc.fillOval(x, y, NODE_WIDTH, NODE_HEIGHT);
        gc.setFill(new Color(0, 0, 0, 1.0));
        gc.strokeOval(x, y, NODE_WIDTH, NODE_HEIGHT);
        gc.fillText(name, x + NODE_WIDTH/2 , y + NODE_HEIGHT/2 - 5);
	    gc.fillText(value, x + NODE_WIDTH/2 , y + NODE_HEIGHT/2 + 15);
	}
	
	private void DrawNonTerminalNode(GraphicsContext gc, double x, double y, String name, String value)
	{
		gc.setFill(new Color(1, 1, 1, 0.8));
        gc.fillRect(x, y, NODE_WIDTH, NODE_HEIGHT);
        gc.setFill(new Color(0, 0, 0, 1.0));
        gc.strokeRect(x, y, NODE_WIDTH, NODE_HEIGHT);
        
        if(name.equals("write") || name.equals("repeat") || name.equals("if"))
        {
	        gc.fillText(name, x + NODE_WIDTH/2 , y + NODE_HEIGHT/2 + 5);
        }
        else
        {
        	gc.fillText(name, x + NODE_WIDTH/2 , y + NODE_HEIGHT/2 - 5);
	        gc.fillText(value, x + NODE_WIDTH/2 , y + NODE_HEIGHT/2 + 15);
        }
	}
}
