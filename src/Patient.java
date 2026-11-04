public class Patient implements Runnable {

    private int id;
    private WaitingRoom waitingRoom;
    public Patient(){
        System.out.println("P" + id + " arrived.");
    }

    @Override
    public void run(){
        waitingRoom.enterRoom(this);
    }

    public String getId(){
        return "P" + id;
    }

}
