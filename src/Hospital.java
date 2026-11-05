import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Hospital {
    // Shared counter to track how many patients have finished treatment and left
    public static AtomicInteger patientsFinished = new AtomicInteger(0);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Read user inputs
        System.out.print("Waiting room capacity: ");
        int roomCapacity = scanner.nextInt();

        System.out.print("Number of doctors: ");
        int numDoctors = scanner.nextInt();

        System.out.print("Number of patients: ");
        int numPatients = scanner.nextInt();
        scanner.close();

        System.out.println("\nPatients arriving:");
        StringBuilder patientList = new StringBuilder();
        for (int i = 1; i <= numPatients; i++) {
            patientList.append("P").append(i).append(" ");
        }
        System.out.println(patientList.toString().trim() + "\n");

        // Initialize shared resources
        WaitingRoom waitingRoom = new WaitingRoom(roomCapacity);
        TreatmentRooms treatmentRooms = new TreatmentRooms();

        // 2. Create and start Doctor threads (Consumers)
        Thread[] doctorThreads = new Thread[numDoctors];
        for (int i = 1; i <= numDoctors; i++) {
            Doctor doctor = new Doctor(i, waitingRoom, treatmentRooms);
            doctorThreads[i - 1] = new Thread(doctor);
            doctorThreads[i - 1].start();
        }

        // 3. Create and start Patient threads (Producers)
        Thread[] patientThreads = new Thread[numPatients];
        for (int i = 1; i <= numPatients; i++) {
            Patient patient = new Patient(i, waitingRoom);
            patientThreads[i - 1] = new Thread(patient);
            patientThreads[i - 1].start();
            try {
                Thread.sleep(150); // Slight delay between arrivals for realistic spacing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 4. Wait dynamically until ALL patients have finished treatment and left
        while (patientsFinished.get() < numPatients) {
            try {
                Thread.sleep(100); // Check every 100ms without wasting CPU
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All patients have been treated.");
        System.out.println("Simulation completed.");

        // 5. Shut down doctor threads cleanly
        for (Thread t : doctorThreads) {
            t.interrupt();
        }
        System.exit(0);
    }
}