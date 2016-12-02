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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TreeController implements Initializable{
	
	@FXML
	private VBox parent;
	
	@FXML
	private StackPane canvasContainer ;
	
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
        for (int i = 0; i < 2; i++)
        	nodes.add(new TreeNode("token" + i));
        TreeNode node = new TreeNode("token", nodes); 
		DrawTreeNode(gc, 200, 10, node);
	}
	
	private void DrawTreeNode(GraphicsContext gc, double x, double y, TreeNode node) {
		float width = 150, height = 75;
		String tokenName = node.getTokenName();
		boolean isRoot = node.isRoot();
		int childrenCount = node.getChildrenCount();
		
        gc.strokeRect(x, y, width, height);
        gc.fillText(tokenName, x + width/2 , y + height/2 + 5);
    
        if(!isRoot)
        {
        	double x1, y1, x2, y2, theta, r, initial_theta;
        	initial_theta = 30;
        	x1 = x + width/2;
        	y1 = y + height;
        	for(int i = 0; i < childrenCount; i++)
        	{
        		theta = Math.toRadians((initial_theta + i*(120 / childrenCount)));
        		r = 2*height/Math.sin(theta);
        		x2 = x + r * Math.cos(theta);
        		y2 = y + r * Math.sin(theta);
        		System.out.println(r);
        		System.out.println(Math.toDegrees(theta));
        		gc.strokeLine(x1, y1, x2, y2);
        		DrawTreeNode(gc, x2 - width/2, y2, node.getChild(i));
        	}
        }
    }
	
	
}
