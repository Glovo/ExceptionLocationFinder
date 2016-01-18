package base;

public interface SuperInterface {
    void throwSuperInterface();

    default void ignoredDefault() {
        throw new CustomUncheckedException("Default interface unchecked");
    }

    default void usedDefault() {
        throw new CustomUncheckedException("hehe");
    }


}
