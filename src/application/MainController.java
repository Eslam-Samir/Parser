package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

import parser.TinyParser;
import scanner.TinyScanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
	
	BufferedReader reader = null;
	TinyScanner scanner = null;
	
	@FXML
	private Button scan;
	
	@FXML
	private TextArea Editor;
	
	@FXML
	private TextArea display;
	
	public void LoadFile(ActionEvent action) {
		Stage stage = (Stage) Editor.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Tiny Language File");
		File file = fileChooser.showOpenDialog(stage);
		if(file != null)
		{
			Editor.setText("");
			Scanner scan;
			try {
				scan = new Scanner(file);
				
				while (scan.hasNext()) {
					String content = scan.nextLine();
					Editor.setText(Editor.getText() + content + "\n");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 	
		}
	}
	
	public void SaveFile(ActionEvent action) {
		if(!Editor.getText().isEmpty())
		{
			try {
				Stage stage = (Stage) Editor.getScene().getWindow();
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Tiny Language File");
				File file = fileChooser.showSaveDialog(stage);
				if(file != null)
				{
					FileWriter writer = new FileWriter(file);
					writer.write(Editor.getText());
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			// TODO Alert editor is empty
		}
	}
	
	public void ScanTokens(ActionEvent action) {
		if(!Editor.getText().isEmpty())
		{
			display.setText("");
			String TinyCode = Editor.getText();
			scanner = new TinyScanner();
			scanner.Scan(TinyCode);
			ArrayList<String> tokens = scanner.getTokens(), types = scanner.getTokensTypes();
			for(int i = 0; i < tokens.size(); i++)
			{
				String token = tokens.get(i), type = types.get(i);
				if(token.equals("<") || token.equals("=") || token.equals("+") ||
						token.equals("-") || token.equals("*") || token.equals("/"))
					type = "op";
				else if(token.equals(";"))
					type = "semi";
				display.setText(display.getText() + 
						String.format("%-30.30s  %-30.30s%n", token, type));
			}
			try {
				scanner.PrintTokens("output.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		else
		{
			// TODO Alert enter code
		}
	}
	public void CreateTree(ActionEvent action) {
		// Switch to tree scene
		if(scanner != null)
		{
			try {
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Tree.fxml"));
				Parent root = loader.load();
		        TreeController controller = loader.getController();
		        controller.setTreeTokens(scanner.getTokens(), scanner.getTokensTypes());
		     
		        Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setMaximized(true);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			// Alert scanner not created
		}
	}
}
