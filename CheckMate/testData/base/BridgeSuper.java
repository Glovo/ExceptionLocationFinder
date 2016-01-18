package base;

public class BridgeSuper implements SuperInterface {
    @Override
    public void throwSuperInterface() {
        throw new CustomUncheckedException("hehe");
    }
}
