import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {
    private Queue<Patient> queue = new LinkedList<>();
    private Semaphore emptySeats;
    private Semaphore waitingPatients = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);

    public WaitingRoom(int capacity) {
        emptySeats = new Semaphore(capacity);
    }

    public void confirmSeatVacated() {
        emptySeats.V();
    }

    // Called by Doctor if treatment room acquisition times out
    public void returnPatient(Patient p, Doctor d) {
        mutex.P();
        queue.add(p); 
        System.out.println(d.getName() + " is sending " + p.getId() + " back to the waiting room.");
        mutex.V();
        waitingPatients.V(); // Signal that the patient is back in the queue
    }

    // Producer: called by the patient thread
    public void enterRoom(Patient p) {
       if (emptySeats.getValue() <= 0) {
        System.out.println("The waiting room is full.");
        System.out.println(p.getId() + " is waiting outside for an available seat.");
        }
        emptySeats.P();
        mutex.P();
        queue.add(p);
        System.out.println(p.getId() + " entered the waiting room.");
        mutex.V();
        waitingPatients.V();
    }

    // Consumer: called by the doctor thread
    public Patient takePatient(Doctor d) {
        waitingPatients.P(); // doctor waits idle until a patient exists
        mutex.P();
        Patient p = queue.poll();
        mutex.V();
        emptySeats.V();
        return p;
    }
}