package base;

public interface SuperInterface {
    /**
     * @throws CustomUncheckedException ;askljfdsljkfdsjkldfsljkdsljksdljk;ads;kljas;jldfkjasf
     *                                  asfdl;ajsfl;ksj
     *                                  asdlkfjasdlfj
     *                                  asdfjaslkdf
     */
    public void throwSuperInterface();

    default void throwDefaultInterface() {
        throw new CustomUncheckedException("Default interface unchecked");
    }
}
