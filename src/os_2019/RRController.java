/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os_2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

       

/**
 * FXML Controller class
 *
 * @author Saleem
 */
public class RRController implements Initializable {
    @FXML
    private Label resultLabel;
    @FXML
    private TextArea CPUSimulationText;
    @FXML
    private TextArea IOSimulationText;
    @FXML
    private TextArea resultText;
    @FXML
    private Button backBtn;
    @FXML
    private Button exitBtn;

    
     static ArrayList<Process> processes = MainPageController.processes;
       AtomicBoolean adding = new AtomicBoolean(false);
        AtomicBoolean addingIO = new AtomicBoolean(false);
        AtomicBoolean IODone = new AtomicBoolean(false);
        AtomicBoolean CPUDone = new AtomicBoolean(false);
        ReadyQueue CPUReadyQueue = new ReadyQueue(processes.size());
        ReadyQueue IOReadyQueue = new ReadyQueue(processes.size());
        ArrayList<Process> finish = new ArrayList<Process>(processes.size());
        AtomicInteger remainingNoOfProcesses = new AtomicInteger(processes.size());
        AtomicInteger time = new AtomicInteger();
        AtomicInteger finishTime = new AtomicInteger();
        static int TQ = MainPageController.timeQuantum;

        
        
/************************************************************************************************************************************************************************/
        /**Thread one: adds new processes the ready queue as they arrive**/
        Thread RR_addToReadyQueue = new Thread(() -> {

            time.set(0);
            for (Process process : processes) {
                if (process.getDelay() != 0) {
                    int delay = process.getDelay();
                    while (delay != 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        delay--;
                        time.getAndIncrement();
                    }
                }
                process.arrivalTime = time.get();
                while (adding.get()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                adding.set(true);
                CPUReadyQueue.enqueue(process);
                adding.set(false);


            }
        });
        /**Thread two: the scheduler moving processes to running state and back to ready*/
        Thread RR_CPU_Scheduler = new Thread(() -> {
             try{
                    CPUSimulationText.appendText(" CPU is working... \n Simulation is printed to the console... ");
                }catch(Exception e){}
//            int TQ = 4; /////////// read from GUI
            Process currentRunning;
//            int CPUEmptyCounter = 0;
            while (remainingNoOfProcesses.get() != 0) {
                if (CPUReadyQueue.isEmpty()) {
                    time.getAndIncrement();
                    System.out.print(" {Waiting_CPU} ");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    currentRunning = CPUReadyQueue.dequeue();
                    System.out.println(" { CPU context switch} ");
                    time.getAndIncrement(); // for context switch
                    if (!currentRunning.started) {
                        currentRunning.started = true;
                        currentRunning.startTime = time.get();
                    }
                    int counter = 0;
                    while (counter < TQ && currentRunning.remainingTime != 0) {
                        currentRunning.remainingTime--;
                        System.out.print(" {CPU " + currentRunning.getPid() + "} ");
                        time.getAndIncrement(); //running
                        currentRunning.CPUBursts++;
                        counter++;
                    }
                    if (currentRunning.remainingTime == 0) {
                        currentRunning.getBursts().remove(0);
                        if (currentRunning.getBursts().isEmpty()) { // terminated
                            currentRunning.finishTime = time.get();
                            remainingNoOfProcesses.getAndDecrement();
                            finish.add(currentRunning);
                        } else {
                            currentRunning.remainingTime = currentRunning.getBursts().get(0);
                            while (addingIO.get()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            addingIO.set(true);
                            IOReadyQueue.enqueue(currentRunning);
                            addingIO.set(false);
                        }
                    } else {
                        while (adding.get()) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        adding.set(true);
                        CPUReadyQueue.enqueue(currentRunning);
                        adding.set(false);
                    }

                }
            }
            if (remainingNoOfProcesses.get() == 0) {
                CPUDone.set(true);
                  try{
                      CPUSimulationText.clear();
                    CPUSimulationText.appendText("CPU Finished working ");
                }catch(Exception e){}
                while(!IODone.get() && !CPUDone.get())
                    { }
                    System.out.println("All processes done CPU");
//                    CPUDone.set(false);
                    finishTime.set(time.get());
                

            }
        });
        
        
       
   
        /**Thread three: the scheduler moving processes to running state and back to ready*/
        Thread RR_IO_Scheduler = new Thread(() -> {
            try{
                      IOSimulationText.clear();
                    IOSimulationText.appendText(" Working on IO Bursts... \n Simulation is printed to the console... ");
                }catch(Exception e){}
//            int TQ = 4; /////////// read from GUI
            Process IOcurrentRunning;
            while (remainingNoOfProcesses.get() != 0) {
                if (IOReadyQueue.isEmpty()) {
                    System.out.print(" {Waiting_IO} ");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    IOcurrentRunning = IOReadyQueue.dequeue();
                    System.out.println(" { IO context switch} ");
                    int counter = 0;
                    while (counter < TQ && IOcurrentRunning.remainingTime != 0) {
                        IOcurrentRunning.remainingTime--;
                        System.out.print(" {IO " + IOcurrentRunning.getPid() + "} ");
                        counter++;
                    }
                    if (IOcurrentRunning.remainingTime == 0) {
                        IOcurrentRunning.getBursts().remove(0);
                        if (IOcurrentRunning.getBursts().isEmpty()) { // terminated
                            IOcurrentRunning.finishTime = time.get();
                            remainingNoOfProcesses.getAndDecrement();
                            finish.add(IOcurrentRunning);
                        } else {
                            IOcurrentRunning.remainingTime = IOcurrentRunning.getBursts().get(0);
                            while (adding.get()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            adding.set(true);
                            CPUReadyQueue.enqueue(IOcurrentRunning);
                            adding.set(false);
                        }
                    } else {
                        while (addingIO.get()) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        addingIO.set(true);
                        IOReadyQueue.enqueue(IOcurrentRunning);
                        addingIO.set(false);
                    }
                }
            }
            if (remainingNoOfProcesses.get() == 0) {
                try{
                      IOSimulationText.clear();
                    IOSimulationText.appendText(" Finished All IO Bursts ");
                }catch(Exception e){}
                IODone.set(true);
                  while(!IODone.get() && !CPUDone.get())
                    { }
                    System.out.println("All processes done IO");
//                    IODone.set(false);
                    finishTime.set(time.get());
                }
            
        });

        
           Thread finishQueue = new Thread(() -> {
            double utilization = 0.0;
            double avgResponseTime = 0.0;
            double throughput = 0.0;
            int index = 0;
            int allCPUBursts = 0;
            int allResponseTime = 0;
            while (index < processes.size()) {
                if (finish.isEmpty() || index >= finish.size()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Process p = finish.get(index++);
                    p.turnaround = p.finishTime - p.arrivalTime;
                    p.responseTime = p.startTime - p.arrivalTime;
                    p.waitingTime = p.turnaround - p.CPUBursts;
                    allResponseTime += p.responseTime;
                    allCPUBursts += p.CPUBursts;
                }
            }
            while(!IODone.get() && !CPUDone.get())
            {
                
            }
            int numberOfProcesses = processes.size();
            int TIME = Integer.parseInt(finishTime.toString());
            utilization = (double) allCPUBursts / TIME * 100;
            avgResponseTime = (double) allResponseTime / numberOfProcesses;
            throughput = (double) numberOfProcesses / TIME;

            resultText.setText("Finish time is: "+TIME+"\n");
            resultText.appendText("Utilization is: "+utilization+"\n");
            resultText.appendText("Average response time is: "+avgResponseTime+"\n");
            resultText.appendText("Throughput is: "+throughput+" processes/time unit \n");
            
//            System.out.println("Finish time is: "+TIME);
//            System.out.printf("Utilization is: %4.2f %% \n",utilization);
//            System.out.printf("Average response time is: %4.2f \n",avgResponseTime);
//            System.out.printf("Throughput is: %4.2f  processes/time unit.\n",throughput);


        });
        
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RR_addToReadyQueue.start();
        RR_CPU_Scheduler.start();
        RR_IO_Scheduler.start();
        finishQueue.start();
    }    

    @FXML
    private void back(ActionEvent event) {
    }

    @FXML
    private void exit(ActionEvent event) {
    }
    
}
