package os_2019;

import java.util.ArrayList;

public class Process {
    private int pid;
    private int delay;
//    private ArrayList<Integer> CPUBursts = new ArrayList<>();
//    private ArrayList<Integer> IOBursts =
    private ArrayList<Integer> bursts = new ArrayList<>();
    int deadline;
    int remainingTime; // For SRTF
    int arrivalTime;
    int startTime; // if -1 Process didnt start
    int turnaround;
    int finishTime; // if -1 Process hasn't finished yet
    int waitingTime;
    int CPUBursts;
    boolean started = false;
    int burstsSum;
    int responseTime;



    public Process() {
    }
    public Process(int pid) {
        this.pid = pid;
        started = false;
    }

    @Override
    public String toString() {
        return "Process{" +
                "pid=" + pid + "delay=" + delay +
                '}';
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public ArrayList<Integer> getBursts() {
        return bursts;
    }

    public void setBursts(ArrayList<Integer> bursts) {
        this.bursts = bursts;
    }

    //    public ArrayList<Integer> getCPUBursts() {
//        return CPUBursts;
//    }
//
//
//    public void setCPUBursts(ArrayList<Integer> CPUBursts) {
//        this.CPUBursts = CPUBursts;
//    }
//
//    public ArrayList<Integer> getIOBursts() {
//        return IOBursts;
//    }
//
//    public void setIOBursts(ArrayList<Integer> IOBursts) {
//        this.IOBursts = IOBursts;
//    }


//    public void resetProcess() { // function to reset a process (re-initialize it)
//        this.startTime = -1;
//        this.finishTime = -1;
//        this.remainingTime = this.getCPUBursts().get(0) ;
//        this.turnaround = -1 ;
//        this.waitingTime = -1 ;
//
//    }

}
