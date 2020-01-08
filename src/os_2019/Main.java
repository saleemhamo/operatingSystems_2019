package os_2019;

import java.io.*;
import java.security.interfaces.ECPublicKey;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {


    public static void main(String[] args) throws IOException {

        AtomicBoolean adding = new AtomicBoolean(false);
        AtomicBoolean addingIO = new AtomicBoolean(false);
        AtomicBoolean IODone = new AtomicBoolean(false);
        AtomicBoolean CPUDone = new AtomicBoolean(false);
        ProcessesFile myFile = new ProcessesFile();
        ArrayList<Process> processes = readFile(myFile);
        ReadyQueue CPUReadyQueue = new ReadyQueue(processes.size());
        ReadyQueue IOReadyQueue = new ReadyQueue(processes.size());
        ArrayList<Process> finish = new ArrayList<Process>(processes.size());
        AtomicInteger remainingNoOfProcesses = new AtomicInteger(processes.size());
        AtomicInteger time = new AtomicInteger();
        AtomicInteger finishTime = new AtomicInteger();

/**********************************************************************************************************************************************************************/

        /**Thread one: adds new processes the ready queue as they arrive**/
        Thread SRTF_addToReadyQueue = new Thread(() -> {
            time.set(0);
            for (Process process : processes) {
                if (process.getDelay() != 0) {
                    int delay = process.getDelay();
                    while (delay != 0) {
                        try {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            delay--;
                            time.getAndIncrement();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                CPUReadyQueue.addSRTF(process);
                adding.set(false);

            }

        });

        /** Thread two: the scheduler moving processes to running state and back to ready*/

        Thread SRTF_CPU_Scheduler = new Thread(() -> {
            Process currentRunning;
            while (remainingNoOfProcesses.get() != 0) {
                if (CPUReadyQueue.isEmpty()) {
                    time.getAndIncrement(); // waiting
                    System.out.print(" {Waiting} ");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
//                CPUEmptyCounter = 0;
                    currentRunning = CPUReadyQueue.dequeue();
                    System.out.println(" { CPU context switch} ");
                    time.getAndIncrement(); // for context switch
                    if (!currentRunning.started) {
                        currentRunning.started = true;
                        currentRunning.startTime = time.get();
                    }
                    if (!CPUReadyQueue.isEmpty()) {
                        while (currentRunning.remainingTime <= CPUReadyQueue.peek().remainingTime && currentRunning.remainingTime != 0) {
                            currentRunning.remainingTime--;
                            System.out.print(" {CPU " + currentRunning.getPid() + "} ");
                            time.getAndIncrement(); //running
                            currentRunning.CPUBursts++;
                        }
                        if (currentRunning.remainingTime > CPUReadyQueue.peek().remainingTime && currentRunning.remainingTime != 0) {
                            while (adding.get()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            adding.set(true);
                            CPUReadyQueue.addSRTF(currentRunning);
                            adding.set(false);
                        } else if (currentRunning.remainingTime == 0) {
                            currentRunning.getBursts().remove(0);
                            if (!currentRunning.getBursts().isEmpty()) {
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


                            } else if (currentRunning.getBursts().isEmpty()) { // terminated
                                currentRunning.finishTime = time.get();
                                remainingNoOfProcesses.getAndDecrement();
                                finish.add(currentRunning);
                            }
                        }
                    } else {
                        while (currentRunning.remainingTime != 0) {
                            currentRunning.remainingTime--;
                            System.out.print(" {CPU " + currentRunning.getPid() + "} ");
                            time.getAndIncrement(); //running
                            currentRunning.CPUBursts++;
                        }
                        currentRunning.getBursts().remove(0);
                        if (!currentRunning.getBursts().isEmpty()) {
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


                        } else if (currentRunning.getBursts().isEmpty()) { /// process is done
                            currentRunning.finishTime = time.get();
                            remainingNoOfProcesses.getAndDecrement();
                            finish.add(currentRunning);
                        }

                    }
                }
            }
            if (remainingNoOfProcesses.get() == 0) {
                CPUDone.set(true);
                System.out.println("All processes done CPU");
                if (IODone.get() && CPUDone.get()) {
                    CPUDone.set(false);
                    finishTime.set(time.get());
                }

            }

        });

        /**Thread two: the scheduler moving processes to running state and back to ready*/
        Thread SRTF_IO_Scheduler = new Thread(() -> {
            Process IOcurrentRunning;
            while (remainingNoOfProcesses.get() != 0) {
                if (IOReadyQueue.isEmpty()) {
                    System.out.print(" {IO_Waiting} ");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    IOcurrentRunning = IOReadyQueue.dequeue();
                    System.out.println(" { IO context switch} ");
                    if (!IOReadyQueue.isEmpty()) {
                        while (IOcurrentRunning.remainingTime <= IOReadyQueue.peek().remainingTime && IOcurrentRunning.remainingTime != 0) {
                            IOcurrentRunning.remainingTime--;
                            System.out.print(" {IO " + IOcurrentRunning.getPid() + "} ");
                        }
                        if (IOcurrentRunning.remainingTime > IOReadyQueue.peek().remainingTime && IOcurrentRunning.remainingTime != 0) {
                            IOReadyQueue.enqueue(IOcurrentRunning);
                        } else if (IOcurrentRunning.remainingTime == 0) {
                            IOcurrentRunning.getBursts().remove(0);
                            if (!IOcurrentRunning.getBursts().isEmpty()) {
                                IOcurrentRunning.remainingTime = IOcurrentRunning.getBursts().get(0);
                                CPUReadyQueue.addSRTF(IOcurrentRunning);
                            } else if (IOcurrentRunning.getBursts().isEmpty()) {
                                IOcurrentRunning.finishTime = time.get();
                                remainingNoOfProcesses.getAndDecrement();
                                finish.add(IOcurrentRunning);
                            }
                        }
                    } else {
                        while (IOcurrentRunning.remainingTime != 0) {
                            IOcurrentRunning.remainingTime--;
                            System.out.print(" {IO " + IOcurrentRunning.getPid() + "} ");
                            time.getAndIncrement(); //running
                        }
                        IOcurrentRunning.getBursts().remove(0);
                        if (!IOcurrentRunning.getBursts().isEmpty()) {
                            IOcurrentRunning.remainingTime = IOcurrentRunning.getBursts().get(0);
                            CPUReadyQueue.addSRTF(IOcurrentRunning);
                        } else if (IOcurrentRunning.getBursts().isEmpty()) {
                            IOcurrentRunning.finishTime = time.get();
                            remainingNoOfProcesses.getAndDecrement();
                            finish.add(IOcurrentRunning);
                        }

                    }
                }
            }
            if (remainingNoOfProcesses.get() == 0) {
                IODone.set(true);
                System.out.println("All processes done IO");
                if (IODone.get() && CPUDone.get()) {
                    IODone.set(false);
                    finishTime.set(time.get());
                }
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
            int numberOfProcesses = processes.size();
            int TIME = Integer.parseInt(finishTime.toString());
            utilization = (double) allCPUBursts / TIME * 100;
            avgResponseTime = (double) allResponseTime / numberOfProcesses;
            throughput = (double) numberOfProcesses / TIME;

            System.out.println("Finish time is: "+TIME);
            System.out.printf("Utilization is: %4.2f %% \n",utilization);
            System.out.printf("Average response time is: %4.2f \n",avgResponseTime);
            System.out.printf("Throughput is: %4.2f  processes/time unit.\n",throughput);


        });

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
            int TQ = 4; /////////// read from GUI
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
                if (IODone.get() && CPUDone.get()) {
                    System.out.println("All processes done CPU");
                    CPUDone.set(false);
                    finishTime.set(time.get());
                }

            }
        });

        /**Thread three: the scheduler moving processes to running state and back to ready*/
        Thread RR_IO_Scheduler = new Thread(() -> {
            int TQ = 4; /////////// read from GUI
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
                IODone.set(true);
                if (IODone.get() && CPUDone.get()) {
                    System.out.println("All processes done IO");
                    IODone.set(false);
                    finishTime.set(time.get());
                }
            }
        });


        SRTF_addToReadyQueue.start();
        SRTF_CPU_Scheduler.start();
        SRTF_IO_Scheduler.start();
        finishQueue.start();
//
//        RR_addToReadyQueue.start();
//        RR_CPU_Scheduler.start();
//        RR_IO_Scheduler.start();

    }

    /************************ء**********************************************************************************************************************************************/


    public static ArrayList<Process> readFile(ProcessesFile myFile) {
        int time = 0;
        ArrayList<Process> processes = new ArrayList<>();
        File file = new File(myFile.getFileName());
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

