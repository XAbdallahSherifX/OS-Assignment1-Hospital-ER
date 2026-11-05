import java.util.Random;

public class Doctor implements Runnable {
    private int id;
    private WaitingRoom waitingRoom;
    private TreatmentRooms treatmentRooms;
    private Random random;

    public Doctor(int id, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms) {
        this.id = id;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.random = new Random();
    }

    public String getName() {
        return "Doctor " + id;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                
                //Remain idle until a patient is available, then take them
                Patient p = waitingRoom.takePatient(this);
                System.out.println(getName() + " is assigned to " + p.getId() + ".");

                //Try to acquire a treatment room
                boolean acquired = treatmentRooms.acquireRoom(p, this);

                if (acquired) {
                    //Begin treatment and simulate random duration
                    int treatmentTime = random.nextInt(3000) + 1000;
                    Thread.sleep(treatmentTime);

                    //release treatment room
                    treatmentRooms.releaseRoom(p, this);
                    
                    //Become available for another patient
                    System.out.println(getName() + " is now available.");
                    System.out.println(p.getId() + " left.");
                } else {
                    //Send the patient back to the waiting room if room acquisition timed out
                    waitingRoom.returnPatient(p, this);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}