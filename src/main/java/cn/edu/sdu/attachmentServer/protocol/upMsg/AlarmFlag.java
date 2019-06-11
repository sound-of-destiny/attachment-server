package cn.edu.sdu.attachmentServer.protocol.upMsg;

import java.util.Arrays;

public class AlarmFlag {
    // 终端 ID
    private byte[] terminalId;
    // 时间
    private String time;
    // 序号
    private Integer sequenceId;
    // 附件数量
    private Integer fileNum;

    public byte[] getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(byte[] terminalId) {
        this.terminalId = terminalId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
    }

    public Integer getFileNum() {
        return fileNum;
    }

    public void setFileNum(Integer fileNum) {
        this.fileNum = fileNum;
    }

    @Override
    public String toString() {
        return "{" +
                " \"terminalId\" : " + Arrays.toString(terminalId) +
                ", \"time\" : " + time +
                ", \"sequenceId\" : " + sequenceId +
                ", \"fileNum\" : " + fileNum +
                " }";
    }
}

