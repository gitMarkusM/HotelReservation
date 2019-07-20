package varausjarjestelma;

public class RoomIdGen {

    public static int id = 1000;

    public static int getNewId() {
        id++;
        return id;
    }
}
