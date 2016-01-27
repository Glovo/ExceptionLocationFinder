package settings.blacklist;

import base.CustomUncheckedException;

public class BlackSuper{
    public void blackThrow(){
        throw new CustomUncheckedException("Black");
    }
}