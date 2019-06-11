package cn.edu.sdu.attachmentServer.protocol;

public class Header {

    // 消息ID byte[0-1]
    private Integer type;
    // byte[2-3]
    private Integer msgBodyPropsField;
    // 终端手机号 byte[4-9]
    private String terminalPhone;
    // 流水号 byte[10-11]
    private Integer flowId;

    /**
     * 消息体属性
     */
    // 消息体长度 bit[0-9]
    private Integer msgBodyLength = 0;
    // 数据加密方式 bit[10-12]
    private Integer encryptionType = 0b000;
    // 是否分包,true==>有消息包封装项 bit[13]
    private Boolean hasSubPackage = false;
    // 保留位 bit[14-15]
    private Integer reservedBit = 0;

    /**
     * 消息包封装项
     */
    // 消息包总数(word(16bit))
    private Integer totalSubPackage;
    // 包序号(word(16bit))这次发送的这个消息包是分包中的第几个消息包, 从 1 开始
    private Integer subPackageSeq;

    public Header() {
    }

    public Header(Integer type, Integer flowId, String terminalPhone) {
        this.type = type;
        this.flowId = flowId;
        this.terminalPhone = terminalPhone;
    }

    /**
     * getter and setter
     */
    public Integer getType() {
        return type;
    }

    public void setType(Integer msgId) {
        this.type = type;
    }

    public String getTerminalPhone() {
        return terminalPhone;
    }

    public void setTerminalPhone(String terminalPhone) {
        this.terminalPhone = terminalPhone;
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public Integer getMsgBodyPropsField() {
        if (msgBodyLength >= 1024)
            System.out.println("The max value of msgLen is 1023, but {} ." + msgBodyLength);
        int subPkg = hasSubPackage ? 1 : 0;
        int ret = (msgBodyLength & 0x3FF) |
                ((encryptionType << 10) & 0x1C00) |
                ((subPkg << 13) & 0x2000) |
                ((reservedBit << 14) & 0xC000);

        return ret & 0xffff;
    }

    public void setMsgBodyPropsField(Integer msgBodyPropsField) {
        this.msgBodyPropsField = msgBodyPropsField;

        this.msgBodyLength = msgBodyPropsField & 0x3ff;
        this.encryptionType = (msgBodyPropsField & 0x1c00) >> 10;
        this.hasSubPackage = ((msgBodyPropsField & 0x2000) >> 13) == 1;
        this.reservedBit = ((msgBodyPropsField & 0xc000) >> 14);
    }

    public Integer getMsgBodyLength() {
        return msgBodyLength;
    }

    public void setMsgBodyLength(Integer msgBodyLength) {
        this.msgBodyLength = msgBodyLength;
    }

    public Integer getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(Integer encryptionType) {
        this.encryptionType = encryptionType;
    }

    public Boolean getHasSubPackage() {
        return hasSubPackage;
    }

    public void setHasSubPackage(Boolean hasSubPackage) {
        this.hasSubPackage = hasSubPackage;
    }

    public Integer getReservedBit() {
        return reservedBit;
    }

    public void setReservedBit(Integer reservedBit) {
        this.reservedBit = reservedBit;
    }

    public Integer getTotalSubPackage() {
        return totalSubPackage;
    }

    public void setTotalSubPackage(Integer totalSubPackage) {
        this.totalSubPackage = totalSubPackage;
    }

    public Integer getSubPackageSeq() {
        return subPackageSeq;
    }

    public void setSubPackageSeq(Integer subPackageSeq) {
        this.subPackageSeq = subPackageSeq;
    }
}
