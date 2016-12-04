package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import extras.Alerts;
import scanner.TinyScanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
		ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        
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
		if(Editor.getText().isEmpty())
		{
			Alerts.createWarningAlert("Editor is empty");
		}
		else
		{
			try {
				Stage stage = (Stage) Editor.getScene().getWindow();
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Tiny Language File");
				ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                
				fileChooser.setInitialFileName("TinyCode");
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
	}
	
	public void ScanTokens(ActionEvent action) {
		if(Editor.getText().isEmpty())
		{
			Alerts.createWarningAlert("Editor is empty");
		}
		else
		{
			String TinyCode = Editor.getText();
			scanner = new TinyScanner();
			scanner.Scan(TinyCode);
			display.setText(scanner.getTokensString());
		}
	}
	
	public void SaveTokens(ActionEvent action) {
		if(Editor.getText().isEmpty())
		{
			Alerts.createWarningAlert("Editor is empty");
		}
		else if(display.getText().isEmpty())
		{
			Alerts.createWarningAlert("The Code is not Scanned", "Press Scan Tokens first");
		}
		else
		{
			try {
				Stage stage = (Stage) display.getScene().getWindow();
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Tokens File");
				ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                
				fileChooser.setInitialFileName("tokens");
				File file = fileChooser.showSaveDialog(stage);
				
				if(file != null)
				{
					FileWriter writer = new FileWriter(file);
					writer.write(display.getText());
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void CreateTree(ActionEvent action) {
		if(scanner == null)
		{
			Alerts.createWarningAlert("The Code is not Scanned", "Press Scan Tokens first");
		}
		else
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
	}
}
