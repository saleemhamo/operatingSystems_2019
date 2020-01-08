//import java.util.ArrayList;
//
//public class Scheduler {
//
//    public static ArrayList<Process> Processes = new ArrayList<Process>();; // this will be the arraylist that the
//    // Processes will be Randomized in
//    public static ArrayList<Process> ReadyQueue; // This ArrayList will act as the ReadyQueue of the CPU
//
//    public static int TimeQuantum;
//
//
//    // First Scheduling Algorithm we will Implement will be the Shortest Remaining
//    // Time First
//    // This Mehthod will Return An AraayList That represents the Guantss Chart
//    // we will need a method that calculates the Remaining Time for each process and
//    // selects the shortest remaining one
//    public static Process FindLeastRemainingTimeProcess() { // function to find the process that has minimum remaining
//        // running time
//        int minRemainingTime = Integer.MAX_VALUE; // initilize minBurstTime to a very large number
//        Process leastRemainingTimeProcess = null;
//        for (Process p : ReadyQueue) {
//            if (p.remainingTime < minRemainingTime) { // if process p has less remaining running time than
//                // leastRemainingTimeProcess
//                leastRemainingTimeProcess = p; // make p the leastRemainingTimeProcess
//                minRemainingTime = p.remainingTime;
//            }
//        }
//        return leastRemainingTimeProcess; // return the leastRemainingTimeProcess
//    }
//
////    public static Process findHighestPriorityProcess(int time) { // function to find the process with highest priority
////        // running time
////        int highestpriority = -1; // initialize priority to a very small number
////        Process highestpriorityprocess = null;
////        for (Process p : ReadyQueue) {
////            if (p.priority > highestpriority) { //
////                // leastRemainingTimeProcess
////                highestpriorityprocess = p; // make p the highest priority process
////                highestpriority = p.priority;
////            }
////            if ((time - p.arrivalTime) >= 3 * p.CPUBurst)
////                p.priority++; // this is used to age a process thats been waiting for so long
////        }
////        return highestpriorityprocess; // return the process with highest priority
////    }
//
//    // this next method will check if a process has just arrived and add it to the
//    // ready queue
//    public static void checkProcessesArrival(int time) { // check if any process has arrived at the given time
//        for (Process p : Processes)
//            if (p.arrivalTime == time) // if a process arrived at the given time
//                ReadyQueue.add(p); // then add it to the readyQueue
//    }
//
//    // this next method will check if all the processes are over
//    public static boolean allProcessesFinished() { // function to check if all process has finished or not
//        for (Process p : Processes)
//            if (p.finishTime == -1) // if any process has finish time = -1, then it hasn't finished yet
//                return false;
//        return true;
//    }
//
//    // Next method will be used to add the process to the GANTT CHART
//    public static void addToGanttChart(ArrayList<Integer> ganttChart, Process currentlyRunning) { // function to add a
//        // process's pid to Gantt
//        // Chart
//        if (currentlyRunning != null) // if there is a process running at the time the function has called
//        {
//            ganttChart.add(currentlyRunning.getPid()); // add this process's pid to Gantt Chart
//
//        } else
//            ganttChart.add(-1); // add -1, indicating no process was running at this time
//    }
//    // ----------------------------------------------------------------------------------------------------
//    // this is the main SRTF method
//
//    public static ArrayList<Integer> SRTF() {
//        int time = 0; // current time counter that starts at 0 ms
//        Process currentlyRunning = null; // the process that is running currently, initially null
////        ReadyQueue = new ArrayList<Process>(); // ready queue to add arrived processes that are ready to run
//
//        //// some stuff has to be changed here
//        ArrayList<Integer> ganttChart = new ArrayList<Integer>(); // Gantt Chart to show processes run-time
//
//        while (!allProcessesFinished()) { // while there are still unfinished processes
//            checkProcessesArrival(time); // check if any process has arriced at current moment
//
//            if (currentlyRunning != null) // if a process is running currently
//                currentlyRunning.remainingTime--; // decrement its remaining running time
//
//            Process leastRemainingTimeProcess = FindLeastRemainingTimeProcess(); // find the process that has minimum
//            // remaining running time
//            currentlyRunning = leastRemainingTimeProcess; // and then run it, preemiting the previous running process,if
//            // any
//
//            if (currentlyRunning != null && currentlyRunning.startTime == -1) // if a process has just started to run,
//                currentlyRunning.startTime = time; // then set its start time to current time
//
//            if (currentlyRunning != null && currentlyRunning.remainingTime == 0) { // if remaining running time for a
//                // currently running process is 0,
//                // then this means that the process
//                // has finished
//                currentlyRunning.finishTime = time; // set finish time to current time
//                currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime; // compute
//                // turnaround
//                // time
////                currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.CPUBurst; // compute
//                // waiting
//                // time
//
//                int currentlyRunningIdx = ReadyQueue.indexOf(currentlyRunning); // find the index of the finished
//                // process
//                ReadyQueue.remove(currentlyRunningIdx); // remove the finished process from the readyQueue
//                currentlyRunning = null;
//
//                /*
//                 * starting another process, just at the same time the previous process has
//                 * finished
//                 */
//                leastRemainingTimeProcess = FindLeastRemainingTimeProcess(); // find the process that has minimum
//                // remaining running time
//                currentlyRunning = leastRemainingTimeProcess; // and then run it
//
//                if (currentlyRunning != null && currentlyRunning.startTime == -1) // if a process has just started to
//                    // run,
//                    currentlyRunning.startTime = time; // then set its start time to current time
//
//            }
//
//            addToGanttChart(ganttChart, currentlyRunning); // add currently running process at the current moment to
//            // Gantt Chart
//            time++; // increment time
//        }
//
//        return ganttChart;
//    }
//    // -----------------------------------------------------------------------------------------------------------------
//
//    // this next method Implements the Round Robin Scheduling Algorithm
//
//    public static ArrayList<Integer> RR() {
//        int time = 0; // current time
//        Process currentlyRunning = null; // the process that is running currently, initially null
//        int remainingTimeQuantum;
//        int i = 0; // index for readyQueue
//
//        ReadyQueue = new ArrayList<Process>(); // ready queue to add arrived processes that are ready to run
//        ArrayList<Integer> ganttChart = new ArrayList<Integer>(); // Gantt Chart to show processes run-time
//
//        remainingTimeQuantum = TimeQuantum; // initialize remainingTimeQuantum
//
//        while (!allProcessesFinished()) { // while there are still unfinished processes
//            checkProcessesArrival(time); // check if any process has arriced at current moment
//
//            if (currentlyRunning != null) {
//
//                remainingTimeQuantum--; // decrement remainingTimeQuantum every second
//                currentlyRunning.remainingTime--; // decrement remainingTime for currently running process every second
//            }
//
//            if (currentlyRunning != null && currentlyRunning.remainingTime == 0) { // if remaining running time for a
//                // currently running process is 0,
//                // then this means that the process
//                // has finished
//                currentlyRunning.finishTime = time; // set finish time to current time
//                currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime; // compute
//                // turnaround
//                // time
////                currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.CPUBurst; // compute
//                // waiting time
//
//                int currentlyRunningIdx = ReadyQueue.indexOf(currentlyRunning); // get the index of currently running
//                // process
//                ReadyQueue.remove(currentlyRunningIdx); // remove this process from the readyQueue
//                currentlyRunning = null;
//
//                if (ReadyQueue.size() != 0) { // if there still some processes in readyQueue to run
//                    currentlyRunning = ReadyQueue.get(currentlyRunningIdx % ReadyQueue
//                            .size()); /*
//                     * get the process that is just after the last process finished (FCFS). note
//                     * that, this process's index = currentlyRunningIdx, because we removed an
//                     * element from the arrayList, and indices have been shifted left
//                     */
//                    remainingTimeQuantum = TimeQuantum; // reset remainingTimeQuantum
//
//                    if (currentlyRunning != null && currentlyRunning.startTime == -1) // if the process has just started
//                        // to run,
//                        currentlyRunning.startTime = time; // then set its start time to current time
//                }
//            }
//
//            if ((currentlyRunning == null || remainingTimeQuantum == 0) && ReadyQueue.size() != 0) { // if there is no
//                // process
//                // running
//                // currently or
//                // a process has
//                // finished its
//                // timeQuantum
//                i = (i + 1) % ReadyQueue.size();
//                currentlyRunning = ReadyQueue.get(i); // get the next process in readyQueue
//                /*
//                 * note: this line instead of the previous two made a very big mistake
//                 * currentlyRunning = readyQueue.get( (i++) % readyQueue.size() );
//                 */
//                remainingTimeQuantum = TimeQuantum; // reset remainingTimeQuantum
//
//                if (currentlyRunning != null && currentlyRunning.startTime == -1)// if the process has just started to
//                    // run,
//                    currentlyRunning.startTime = time; // then set its start time to current time
//            }
//
//            addToGanttChart(ganttChart, currentlyRunning); // add currently running process at the current moment to
//            // Gantt Chart
//            time++; // increment time
//        }
//
//        return ganttChart;
//    }
//
//    // --------------------------------------------------------------------------------------------------------------
//    // Before Implementing SJF , this method is used to find the process with the
//    // least Burst Time ;
//
//    public static Process findLeastBurstTimeProcess() { // function to find the process that has minimum burst time
//        int minBurstTime = Integer.MAX_VALUE; // initilize minBurstTime to a very large number
//        Process leastBurstTimeProcess = null;
//        for (Process p : ReadyQueue) {
////            if (p.CPUBurst < minBurstTime) { // if process p has less burst time than leastBurstTimeProcess
////                leastBurstTimeProcess = p; // make p the leastBurstTimeProcess
////                minBurstTime = p.CPUBurst;
////            }
////        }
////        return leastBurstTimeProcess; // return the leastBurstTimeProcess
////    }
////
//     shortest job first Implementation
////    public static ArrayList<Integer> SJF() {
////        int time = 0; // current time
////        Process currentlyRunning = null; // the process that is running currently, initially null
////        ReadyQueue = new ArrayList<Process>(); // ready queue to add arrived processes that are ready to run
////        ArrayList<Integer> ganttChart = new ArrayList<Integer>(); // Gantt Chart to show processes run-time
////
////        while (!allProcessesFinished()) { // while there are still unfinished processes
////
////            checkProcessesArrival(time); // check if any process has arriced at current moment
////
////            if (currentlyRunning == null) { // if no process is running currently
////                Process leastBurstTimeProcess = findLeastBurstTimeProcess(); // find the process with minimum burt time
////                int leastBursTimetProcessIdx = ReadyQueue.indexOf(leastBurstTimeProcess); // find its index in the
////                // readyQueue
////                if (leastBursTimetProcessIdx != -1) // and if it is in the queue
////                    currentlyRunning = ReadyQueue.remove(leastBursTimetProcessIdx); // remove it and run it
////
////                if (currentlyRunning != null && currentlyRunning.startTime == -1) // if there is a process has just
////                    // started to run
////                    currentlyRunning.startTime = time; // set its start time to current time
////            }
////
////            if (currentlyRunning != null && time == currentlyRunning.CPUBurst + currentlyRunning.startTime) { // if
////                // current
////                // time
////                // =
////                // burst
////                // time
////                // +
////                // start
////                // time
////                // for a
////                // currently
////                // running
////                // process,
////                // then
////                // this
////                // means
////                // that
////                // the
////                // process
////                // has
////                // finished
////                currentlyRunning.finishTime = time; // set finish time to current time
////                currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime; // compute
////                // turnaround
////                // time
////                currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.CPUBurst; // compute
////                // waiting time
////                currentlyRunning = null;
////
////                /*
////                 * starting another process, just at the same time the previous process has
////                 * finished
////                 */
////                Process leastBurstTimeProcess = findLeastBurstTimeProcess(); // find the process with minimum burst time
////                int leastBursTimetProcessIdx = ReadyQueue.indexOf(leastBurstTimeProcess); // find its index in the
////                // readyQueue
////                if (leastBursTimetProcessIdx != -1) // and if it is in the queue
////                    currentlyRunning = ReadyQueue.remove(leastBursTimetProcessIdx); // remove it and run it
////
////                if (currentlyRunning != null && currentlyRunning.startTime == -1) // if there is a process has just
////                    // started to run,
////                    currentlyRunning.startTime = time; // then set its start time to current time
////            }
////
////            addToGanttChart(ganttChart, currentlyRunning); // add currently running process at the current moment to
////            // Gantt Chart
////            time++; // increment time
////        }
////
////        return ganttChart;
////    }
////
//     ===============================================================================================================================
//     The Implementation of FCFS
////    public static ArrayList<Integer> FCFS() {
////        int time = 0; // current time
////        Process currentlyRunning = null; // the process that is running currently, initially null
////
////        ReadyQueue = new ArrayList<Process>(); // ready queue to add arrived processes that are ready to run
////        ArrayList<Integer> ganttChart = new ArrayList<Integer>(); // Gantt Chart to show processes run-time //
////        // initialize remainingTimeQuantum
////
////        while (!allProcessesFinished()) { // while there are still unfinished processes
////            checkProcessesArrival(time); // check if any process has arriced at current moment
////
////            if (currentlyRunning == null && (ReadyQueue.size() != 0)) { // if there is no process running currently
////                currentlyRunning = ReadyQueue.get(0); // get the first process in readyQueue
////
////                if (currentlyRunning != null) // if the process has just started to run,
////                    currentlyRunning.startTime = time; // then set its start time to current time
////            }
////
////            if (currentlyRunning != null) {
////
////                // if (time != currentlyRunning.startTime) // decrement remainingTime for
////                // currently running process every second
////                currentlyRunning.remainingTime--; // except at the start second
////
////                if (currentlyRunning != null && currentlyRunning.remainingTime == 0) { // if remaining running time for
////                    // a currently running process
////                    // is 0,
////                    // then this means that the
////                    // process has finished
////                    currentlyRunning.finishTime = time; // set finish time to current time
////                    currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime; // compute
////                    // turnaround
////                    // time
////                    currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.CPUBurst + 1; // compute
////                    // waiting
////                    // time
////
////                    ReadyQueue.remove(0); // remove this process from the readyQueue
////                    currentlyRunning = null;
////
////                    if (ReadyQueue.size() != 0) // if there still some processes in the readyQueue
////                        currentlyRunning = ReadyQueue.get(0); // get the first process in readyQueue
////
////                    if (currentlyRunning != null) // if the process has just started to run,
////                        currentlyRunning.startTime = time; // then set its start time to current time
////                }
////            }
////
////            addToGanttChart(ganttChart, currentlyRunning); // add currently running process at the current moment to
////            // Gantt Chart
////            time++; // increment time
////        }
////
////        return ganttChart;
////    }
////
//     ====================================================================================================]
//     method to display And test if the gannts chart is correct
////    public static void displayGanttChart(ArrayList<Integer> ganttChart) { // function to display Gantt Chart
////
////        for (int i = 0; i < ganttChart.size(); i++) {
////            System.out.printf("%3d  |  ", i);
////
////        }
////
////        System.out.println("");
////
////        for (Integer integer : ganttChart) {
////
////            if (integer.equals(-1))
////                System.out.printf("  NOP  |  ");
////
////            else
////                System.out.printf("  %3d |  ", integer);
////
////        }
////
////    }
////
//    // priority Implementation
//    public static ArrayList<Integer> Priority() {
//        int time = 0; // current time
//        Process currentlyRunning = null; // the process that is running currently, initially null
//        ReadyQueue = new ArrayList<Process>(); // ready queue to add arrived processes that are ready to run
//        ArrayList<Integer> ganttChart = new ArrayList<Integer>(); // Gantt Chart to show processes run-time
//
//        while (!allProcessesFinished()) { // while there are still unfinished processes
//
//            checkProcessesArrival(time); // check if any process has arriced at current moment
//
//            if (currentlyRunning == null) { // if no process is running currently
//                Process highestPriorityProcess = findHighestPriorityProcess(time); // find the process with highest
//                // priority
//                int highestPriorityIndx = ReadyQueue.indexOf(highestPriorityProcess); // find its index in the
//                // readyQueue
//                if (highestPriorityIndx != -1) // and if it is in the queue
//                    currentlyRunning = ReadyQueue.remove(highestPriorityIndx); // remove it and run it
//
//                if (currentlyRunning != null && currentlyRunning.startTime == -1) // if there is a process has just
//                    // started to run
//                    currentlyRunning.startTime = time; // set its start time to current time
//            }
//
//            if (currentlyRunning != null && time == currentlyRunning.CPUBurst + currentlyRunning.startTime) { // if
//                // current
//                // time
//                // =
//                // burst
//                // time
//                // +
//                // start
//                // time
//                // for a
//                // currently
//                // running
//                // process,
//                // then
//                // this
//                // means
//                // that
//                // the
//                // process
//                // has
//                // finished
//                currentlyRunning.finishTime = time; // set finish time to current time
//                currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime; // compute
//                // turnaround
//                // time
//                currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.CPUBurst; // compute
//                // waiting time
//                currentlyRunning = null;
//
//                /*
//                 * starting another process, just at the same time the previous process has
//                 * finished
//                 */
//                Process highestPriorityProcess = findHighestPriorityProcess(time); // find the process with highest
//                // priority
//                int highestPriorityIndx = ReadyQueue.indexOf(highestPriorityProcess); // find its index in the
//                // readyQueue
//                if (highestPriorityIndx != -1) // and if it is in the queue
//                    currentlyRunning = ReadyQueue.remove(highestPriorityIndx); // remove it and run it
//
//                if (currentlyRunning != null && currentlyRunning.startTime == -1) // if there is a process has just
//                    // started to run,
//                    currentlyRunning.startTime = time; // then set its start time to current time
//            }
//
//            addToGanttChart(ganttChart, currentlyRunning); // add currently running process at the current moment to
//            // Gantt Chart
//            time++; // increment time
//        }
//
//        return ganttChart;
//    }
//
//    // ===============================================================================================================================
//
//    // this is the main SRTF method
//
//    public static ArrayList<Integer> PP() {
//        int time = 0; // current time counter that starts at 0 ms
//        Process currentlyRunning = null; // the process that is running currently, initially null
//        ReadyQueue = new ArrayList<Process>(); // ready queue to add arrived processes that are ready to run
//
//        //// some stuff has to be changed here
//        ArrayList<Integer> ganttChart = new ArrayList<Integer>(); // Gantt Chart to show processes run-time
//
//        while (!allProcessesFinished()) { // while there are still unfinished processes
//            checkProcessesArrival(time); // check if any process has arriced at current moment
//
//            if (currentlyRunning != null) // if a process is running currently
//                currentlyRunning.remainingTime--; // decrement its remaining running time
//
//            Process leastRemainingTimeProcess = findHighestPriorityProcess(time); // find the process that has minimum
//            // remaining running time
//            currentlyRunning = leastRemainingTimeProcess; // and then run it, preemiting the previous running process,if
//            // any
//
//            if (currentlyRunning != null && currentlyRunning.startTime == -1) // if a process has just started to run,
//                currentlyRunning.startTime = time; // then set its start time to current time
//
//            if (currentlyRunning != null && currentlyRunning.remainingTime == 0) { // if remaining running time for a
//                // currently running process is 0,
//                // then this means that the process
//                // has finished
//                currentlyRunning.finishTime = time; // set finish time to current time
//                currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime; // compute
//                // turnaround
//                // time
//                currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.CPUBurst; // compute
//                // waiting
//                // time
//
//                int currentlyRunningIdx = ReadyQueue.indexOf(currentlyRunning); // find the index of the finished
//                // process
//                ReadyQueue.remove(currentlyRunningIdx); // remove the finished process from the readyQueue
//                currentlyRunning = null;
//
//                /*
//                 * starting another process, just at the same time the previous process has
//                 * finished
//                 */
//                leastRemainingTimeProcess = findHighestPriorityProcess(time); // find the process that has minimum
//                // remaining running time
//                currentlyRunning = leastRemainingTimeProcess; // and then run it
//
//                if (currentlyRunning != null && currentlyRunning.startTime == -1) // if a process has just started to
//                    // run,
//                    currentlyRunning.startTime = time; // then set its start time to current time
//
//            }
//
//            addToGanttChart(ganttChart, currentlyRunning); // add currently running process at the current moment to
//            // Gantt Chart
//            time++; // increment time
//        }
//
//        return ganttChart;
//    }
//    // -----------------------------------------------------------------------------------------------------------------
//}
