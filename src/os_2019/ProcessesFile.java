package os_2019;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Saleem
 */
public class ProcessesFile{

    private static int fileIndex = 0;
    private String fileName;
    private int NoOfProcesses;
    private int NoOfBursts;

    public static int getFileIndex() {
        return fileIndex;
    }

    public static void setFileIndex(int fileIndex) {
        ProcessesFile.fileIndex = fileIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNoOfProcesses() {
        return NoOfProcesses;
    }

    public void setNoOfProcesses(int noOfProcesses) {
        NoOfProcesses = noOfProcesses;
    }

    public int getNoOfBursts() {
        return NoOfBursts;
    }

    public void setNoOfBursts(int noOfBursts) {
        NoOfBursts = noOfBursts;
    }

    public ProcessesFile() throws IOException {
       this.fileName  = "scheduler"+ ++fileIndex +".txt";
//        this.NoOfProcesses = 10;
//        this.NoOfBursts = 5;
       this.NoOfProcesses = (int) (Math.random() * 80 + 20);
       this.NoOfBursts = (int) (Math.random() * 15 + 5);
     createFile();
    }


    public void createFile() throws IOException {

        FileWriter fileWriter = new FileWriter("scheduler"+ fileIndex+".txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        //Number of Processes 20-100
//        int NoOfProcesses = (int) (Math.random() * 80 + 20);
//          int NoOfProcesses = 10;
        //Number of Bursts
//        int NoOfBursts = (int) (Math.random() * 15 + 5);

        //Header
        printWriter.print("Delay    PID");
        for (int i = 0 ; i < NoOfBursts ; i++){
            if(i%2==0)
                printWriter.print("    CPUBurst");
            else
                printWriter.print("    IOBurst");
        }
        printWriter.println(" ");

        //Data
        for (int i = 0; i < this.NoOfProcesses; i++) {
            int delay = (int) (Math.random() * 5 );////////////
            printWriter.print(delay+ "          ");
            printWriter.print(i);
            for (int j = 0 ; j < NoOfBursts ; j++){
                int burst = (int) (Math.random() * 118 + 2);
//                int burst = (int) (Math.random() * 10 + 2);
                printWriter.printf("%11d",burst);
            }
            printWriter.println(" ");

        }

        printWriter.close();

    }

}
