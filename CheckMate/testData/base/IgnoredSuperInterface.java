package base;

public interface IgnoredSuperInterface {
    /**
     * @throws base.other_package.OtherCustomUncheckedException ;lasdkfja;sdlfkj
     */
    default void ignoredSuperInterface() {
        throw new CustomUncheckedException("hehe");
    }
}
