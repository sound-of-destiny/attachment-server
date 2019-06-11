package cn.edu.sdu.attachmentServer.protocol.upMsg;

import cn.edu.sdu.attachmentServer.protocol.Header;
import cn.edu.sdu.attachmentServer.protocol.PackageData;

public class FIleInfoUpload extends PackageData<Header> {
    // 文件名称长度
    private Integer fileNameLength;
    // 文件名称
    private String fileName;
    // 文件类型
    private Integer fileType;
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

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
}
