package cn.edu.sdu.attachmentServer.exception;

public class CheckSumErrorException extends RuntimeException {
    public CheckSumErrorException(String s) {
        super(s);
    }
}
