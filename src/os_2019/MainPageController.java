
package os_2019;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Saleem
 */
public class MainPageController implements Initializable {
    @FXML
    private Button runRRBtn;
    @FXML
    private Button generateFileBtn;
    @FXML
    private Button loadFileBtn;
    @FXML
    private TextField timeQuantumTextField;
    @FXML
    private Button runSRTFBtn;

      ProcessesFile myFile; 
     static ArrayList<Process> processes;
    static int timeQuantum = 4;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void runRR(ActionEvent event) throws IOException{
        
        if(timeQuantumTextField.getText() == null)
        {
            timeQuantum = 4; //deafault
        }else {
            try{
        timeQuantum = Integer.parseInt(timeQuantumTextField.getText());
        } catch (Exception e) {}
        }
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("RR.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void generateFile(ActionEvent event){
        try{
            myFile = new ProcessesFile(); 
        }catch(Exception e){
        }
        
    }

    @FXML
    private void loadFile(ActionEvent event) throws IOException{
        Stage loadStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(loadStage);
        processes = readFile(selectedFile);
    }

    @FXML
    private void runSRTF(ActionEvent event) throws IOException{
         Stage stage = new Stage();
         Parent root = FXMLLoader.load(getClass().getResource("SRTF.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
        public static ArrayList<Process> readFile(File file) {
        int time = 0;
        ArrayList<Process> processes = new ArrayList<>();
//        File file = new File(myFile.getFileName());
        try (Scanner scan = new Scanner(file)) {
            // read first line
            String line = scan.nextLine();
            int i = 0;
            while (scan.hasNextLine()) {
//                ArrayList<Integer> CPUBusts = new ArrayList<>();
//                ArrayList<Integer> IOBusts = new ArrayList<>();
                ArrayList<Integer> bursts = new ArrayList<>();
                line = scan.nextLine();
                String[] proc = line.split("\\s+");

                Process p = new Process(Integer.parseInt(proc[1]));
                p.setDelay(Integer.parseInt(proc[0]));
                for (int j = 2; j < proc.length; j++) {

                    bursts.add(Integer.parseInt(proc[j]));
//                    if (j % 2 == 0) {
//                        CPUBusts.add(Integer.parseInt(proc[j]));
//                    } else {
//                        IOBusts.add(Integer.parseInt(proc[j]));
//                    }
                }
                time += p.getDelay();
                p.arrivalTime += time;
                p.setBursts(bursts);
//                p.setCPUBursts(CPUBusts);
//                p.setIOBursts(IOBusts);
                p.remainingTime = bursts.get(0);

                processes.add(p);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        return processes;
    }
}
