package cn.edu.sdu.attachmentServer.protocol.upMsg;

import cn.edu.sdu.attachmentServer.protocol.Header;
import cn.edu.sdu.attachmentServer.protocol.PackageData;

import java.util.List;

public class AlarmFileInfo extends PackageData<Header> {
    // 终端 ID
    private byte[] terminalId;
    // 报警标识号
    private byte[] alarmFlag;
    // 报警编号
    private byte[] alarmCode;
    // 信息类型
    private Integer infoType;
    // 附件数量
    private Integer fileNum;
    // 附件信息列表
    private List<FileInfo> fileInfoList;

    public static class FileInfo {
        // 文件名称长度
        private Integer fileNameLength;
        // 文件名称
        private String fileName;
        // 文件大小
        private Integer fileSize;

        public Integer getFileNameLength() {
            return fileNameLength;
        }

        public void setFileNameLength(Integer fileNameLength) {
            this.fileNameLength = fileNameLength;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Integer getFileSize() {
            return fileSize;
        }

        public void setFileSize(Integer fileSize) {
            this.fileSize = fileSize;
        }
    }

    public byte[] getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(byte[] terminalId) {
        this.terminalId = terminalId;
    }

    public byte[] getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(byte[] alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    public byte[] getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(byte[] alarmCode) {
        this.alarmCode = alarmCode;
    }

    public Integer getInfoType() {
        return infoType;
    }

    public void setInfoType(Integer infoType) {
        this.infoType = infoType;
    }

    public Integer getFileNum() {
        return fileNum;
    }

    public void setFileNum(Integer fileNum) {
        this.fileNum = fileNum;
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }
}
