package cn.th.utils;

public class BDUSSException extends Exception{
    public BDUSSException(){
        super();
    }
    public BDUSSException(String message) {
        super(message);
    }

    public BDUSSException(String message, Throwable cause) {
        super(message, cause);
    }

    public BDUSSException(Throwable cause) {
        super(cause);
    }

    public BDUSSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
