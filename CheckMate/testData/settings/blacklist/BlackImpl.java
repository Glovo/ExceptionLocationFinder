package settings.blacklist;

import base.other_package.OtherCustomUncheckedException;

public class BlackImpl extends BlackSuper {
    @Override
    public void blackThrow() {
        throw new OtherCustomUncheckedException("Other");
    }

    public void blackException() {
        throw new BlackException("Black");
    }
}
