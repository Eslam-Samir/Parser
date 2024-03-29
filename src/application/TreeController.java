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
import scanner.TinyScanner;
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
	private int INITIAL_X, INITIAL_Y;
	private ArrayList<TreeNode> nodes;
	private ArrayList<double[]> connections;
	
	private double maxX, maxY, minX, minY;
	@FXML
	private VBox parent;
	
	@FXML
	private ScrollPane canvasContainer ;
	
	@FXML
	private Canvas canvas;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		INITIAL_X = 0;
		INITIAL_Y = 0;
		maxX = 0;
		maxY = 0; 
		minX = 0;
		minY = 0;
		nodes = new ArrayList<>();
		connections = new ArrayList<>();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setLineWidth(2);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(18));
        
        ArrayList<String> tokens = TinyScanner.getScannerInstance().getTokens();
        ArrayList<String> types = TinyScanner.getScannerInstance().getTokensTypes();
        
        TinyParser parser = TinyParser.getParserInstance();
        parser.setTokens(tokens, types);
        parser.RunParser();
		TreeNode root = parser.getRoot();
		setCanvasBoundries(root);
		
		DrawNodes(gc);
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
		
		int width = (int)(maxX + 2*NODE_WIDTH);
		int height = (int)(maxY + 2*NODE_HEIGHT);
		
		WritableImage image = new WritableImage(width, height);
		SnapshotParameters param = new SnapshotParameters();
		canvas.snapshot(param, image);
		try {
			int x = (int)(minX - NODE_WIDTH), y = (int)(minY - NODE_HEIGHT); 
			if(x < 0)
				x = 0;
			if(y < 0)
				y = 0;
			width -= minX;
			height -= minY;
			WritableImage croppedImage = new WritableImage(image.getPixelReader(), 
					x, y, width, height);
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			
			String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
			ImageIO.write(SwingFXUtils.fromFXImage(croppedImage, bufferedImage), extension, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void DrawNodes(GraphicsContext gc)
	{
		for (double[] line : connections) {
			gc.strokeLine(line[0], line[1], line[2], line[3]);
		}
		
		for (TreeNode treeNode : nodes) {
			String tokenName = treeNode.getTokenName();
			String tokenValue = "(" + treeNode.getTokenValue() + ")";
		    double x = treeNode.getX();
		    double y = treeNode.getY();
		    
		    if(treeNode.isTerminal())
				DrawTerminalNode(gc, x, y, tokenName, tokenValue);
			else
				DrawNonTerminalNode(gc, x, y, tokenName, tokenValue);
		}
	}
	
	private void setCanvasBoundries(TreeNode root) {
		calcNodesPositions(INITIAL_X, INITIAL_Y, root);
		maxX = maxX - minX + 2 * NODE_WIDTH;
		maxY = maxY - minY + 2 * NODE_HEIGHT;
		INITIAL_X = (int)(NODE_WIDTH - minX/2);
		INITIAL_Y = (int)(NODE_HEIGHT);
		
		for (double[] line : connections) {
			line[0] += INITIAL_X;
			line[1] += INITIAL_Y;
			line[2] += INITIAL_X;
			line[3] += INITIAL_Y;
		}
		for (TreeNode treeNode : nodes) {
			treeNode.setX(treeNode.getX()+INITIAL_X);
			treeNode.setY(treeNode.getY()+INITIAL_Y);
		}
		
		minX = NODE_WIDTH;
		minY = NODE_HEIGHT;
		canvas.setWidth(maxX);
		canvas.setHeight(maxY);
	}
	
	private void calcNodesPositions(double x, double y, TreeNode node) {
		boolean isRoot = node.isRoot();
		int childrenCount = node.getChildrenCount();
		int padding = 0;
		node.setX(x);
		node.setY(y);
		nodes.add(node);
		if(!isRoot)
        {
        	double x1, y1, x2, y2, theta, r, initial_theta = 90;
        	if(childrenCount == 2)
        		initial_theta = 50;
        	else
        		initial_theta = 40;
        	
        	x1 = x + NODE_WIDTH/2;
        	y1 = y + NODE_HEIGHT;
        	for(int i = 0; i < childrenCount; i++)
        	{
        		if(childrenCount > 1 && node.getChild(0).getChildrenCount() == 2 
        				&& node.getChild(1).getChildrenCount() == 2)
        		{
        			initial_theta = 36;
        		}
        		if(childrenCount == 1)
        			theta = 90;
        		else
        			theta = -(initial_theta + i*((180 - 2*initial_theta) / (childrenCount-1)));
        		
        		theta = Math.toRadians(theta);
        		// scale r depending on number of children
        		r = childrenCount*NODE_HEIGHT/Math.sin(theta); 
        		x2 = x1 + r * Math.cos(theta) + padding;
        		y2 = y1 + r * Math.sin(theta);
        		
        		changeMax(x2 + NODE_WIDTH/2, y2 + NODE_HEIGHT);
        		changeMin(x2 - NODE_WIDTH/2, y2);
        		connections.add(new double[] {x1, y1, x2, y2});
        		calcNodesPositions(x2 - NODE_WIDTH/2, y2, node.getChild(i));
        		
        		TreeNode next = node.getChild(i).getNext();
        		while(next != null)
        		{
        			padding += NODE_WIDTH * (childrenCount + 1);
        			next = next.getNext();
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

        	changeMax(x2, y2 + NODE_HEIGHT);	
        	connections.add(new double[] {x1, y1, x2, y2});
        	calcNodesPositions(x2, y2 - NODE_HEIGHT/2, node.getNext());
        }
	}
	
	private void changeMax(double x, double y)
	{
		if(x > maxX)
			maxX = x;
		if(y > maxY)
			maxY = y;
	}
	private void changeMin(double x, double y)
	{
		if(x < minX)
			minX = x;
		if(y < minY)
			minY = y;
	}
	
	private void DrawTerminalNode(GraphicsContext gc, double x, double y, String name, String value)
	{
		gc.setFill(new Color(1, 1, 1, 0.6));
        gc.fillOval(x, y, NODE_WIDTH, NODE_HEIGHT);
        gc.setFill(new Color(0, 0, 0, 1.0));
        gc.strokeOval(x, y, NODE_WIDTH, NODE_HEIGHT);
        gc.fillText(name, x + NODE_WIDTH/2 , y + NODE_HEIGHT/2 - 5);
	    gc.fillText(value, x + NODE_WIDTH/2 , y + NODE_HEIGHT/2 + 15);
	}
	
	private void DrawNonTerminalNode(GraphicsContext gc, double x, double y, String name, String value)
	{
		gc.setFill(new Color(1, 1, 1, 0.6));
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
