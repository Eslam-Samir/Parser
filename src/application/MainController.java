package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import scanner.TinyScanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
	
	@FXML
	private Button scan;
	
	public void ScanFile(ActionEvent action) {
		Stage stage = (Stage) scan.getScene().getWindow();
		/*FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Tiny Input File");
		File file = fileChooser.showOpenDialog(stage);
		*/
		BufferedReader reader = null;
		try {
			//reader = new BufferedReader(new FileReader(file)); TODO return after testing
			reader = new BufferedReader(new FileReader("input.txt"));
			String line = "";
			TinyScanner scanner = new TinyScanner();
			while((line = reader.readLine()) != null)
			{
				scanner.Scan(line);
			}
			scanner.PrintTokens("output.txt");
			reader.close();
			
			// Switch to tree scene
			OpenTreeScene();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void OpenTreeScene() throws IOException {
		Stage stage = (Stage) scan.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Tree.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
