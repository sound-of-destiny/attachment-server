package cn.edu.sdu.attachmentServer.protocol.downMsg;

import cn.edu.sdu.attachmentServer.protocol.Header;
import cn.edu.sdu.attachmentServer.protocol.PackageData;

import java.util.List;

public class FileUploadFinishResponse extends PackageData<Header> {
    // 文件名称长度
    private Integer fileNameLength;
    // 文件名称
    private String fileName;
    // 文件类型
    private Integer fileType;
    // 上传结果
    private Integer uploadResult;
    // 补传数据包数量
    private Integer retransmittedPacketNum;
    // 补传数据包列表
    private List<RetransmittedPacket> retransmittedPackets;

    public static class RetransmittedPacket {
        // 数据偏移量
        private Integer dataOffset;
        // 数据偏移量
        private Integer dataLength;

        public Integer getDataOffset() {
            return dataOffset;
        }

        public void setDataOffset(Integer dataOffset) {
            this.dataOffset = dataOffset;
        }

        public Integer getDataLength() {
            return dataLength;
        }

        public void setDataLength(Integer dataLength) {
            this.dataLength = dataLength;
        }
    }

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

    public Integer getUploadResult() {
        return uploadResult;
    }

    public void setUploadResult(Integer uploadResult) {
        this.uploadResult = uploadResult;
    }

    public Integer getRetransmittedPacketNum() {
        return retransmittedPacketNum;
    }

    public void setRetransmittedPacketNum(Integer retransmittedPacketNum) {
        this.retransmittedPacketNum = retransmittedPacketNum;
    }

    public List<RetransmittedPacket> getRetransmittedPackets() {
        return retransmittedPackets;
    }

    public void setRetransmittedPackets(List<RetransmittedPacket> retransmittedPackets) {
        this.retransmittedPackets = retransmittedPackets;
    }
}
