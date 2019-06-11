package cn.edu.sdu.attachmentServer.protocol;

public class PackageData<T> {

    //消息头 byte[0-15]
    private T header;

    //消息体
    private byte[] msgBody;

    //校验码 1byte
    private Integer checkSum;

    public PackageData() {
    }

    public PackageData(T header) {
        this.header = header;
    }

    public T getHeader() {
        return header;
    }

    public void setHeader(T header) {
        this.header = header;
    }

    public byte[] getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(byte[] msgBody) {
        this.msgBody = msgBody;
    }

    public Integer getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Integer checkSum) {
        this.checkSum = checkSum;
    }
}
