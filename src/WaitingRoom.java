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
        emptySeats.V(); // Now a new patient from outside can take the seat
    }

    // 3. Called by Doctor if treatment room acquisition times out
    public void returnPatient(Patient p, Doctor d) {
        // No emptySeats.P() needed because we never gave up their seat!
        mutex.P();
        queue.add(p); // Or ((LinkedList<Patient>) queue).addFirst(p) to prioritize them
        System.out.println(d.getName() + " is sending " + p.getName() + " back to the waiting room.");
        mutex.V();
        waitingPatients.V(); // Signal that the patient is back in the queue
    }

    // Producer: called by the patient thread
    public void enterRoom(Patient p) {
        if (emptySeats.getValue() <= 0) // only used for the log message
            System.out.println(p.getName() + " is waiting for an available seat.");
        emptySeats.P(); // blocks here, no recursion needed
        mutex.P();
        queue.add(p);
        System.out.println(p.getName() + " entered the waiting room.");
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