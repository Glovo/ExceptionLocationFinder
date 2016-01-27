package settings.blacklist;

public class BlackClass {
    public void throwBlack() {
        throw new BlackException("black");
    }
}