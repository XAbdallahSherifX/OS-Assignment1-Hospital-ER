public class TreatmentRooms {
    private Semaphore availableRooms = new Semaphore(2);

    // Returns true if the doctor got a room, false if timed out
    public boolean acquireRoom(Patient p, Doctor d) {
        System.out.println(d.getName() + " is trying to acquire a treatment room.");
        boolean acquired = availableRooms.P(3000);

        if (!acquired) {
            System.out.println(d.getName() + " could not acquire a treatment room.");
            return false;
        }

        System.out.println(d.getName() + " acquired a treatment room.");
        System.out.println(d.getName() + " begins treating " + p.getName() + ".");
        return true;
    }

    public void releaseRoom(Patient p, Doctor d) {
        System.out.println(d.getName() + " finished treating " + p.getName() + ".");
        System.out.println(d.getName() + " released the treatment room.");
        availableRooms.V();
    }
}