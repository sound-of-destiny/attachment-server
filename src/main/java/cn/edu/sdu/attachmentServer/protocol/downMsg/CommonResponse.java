package cn.edu.sdu.attachmentServer.protocol.downMsg;

public class CommonResponse {

    public static final byte success = 0;
    public static final byte failure = 1;
    public static final byte msg_error = 2;
    public static final byte unsupported = 3;
    public static final byte warnning_msg_ack = 4;

    // 应答流水号 byte[0-1] 对应的终端消息的流水号
    private Integer replyFlowId;

    // 应答ID byte[2-3] 对应的终端消息的ID
    private Integer replyId;

    /**
     * 0：成功∕确认<br>
     * 1：失败<br>
     * 2：消息有误<br>
     * 3：不支持<br>
     * 4：报警处理确认<br>
     */
    private Integer replyCode;

    public CommonResponse() {
    }

    public CommonResponse(Integer replyFlowId, Integer replyId, Integer replyCode) {
        this.replyFlowId = replyFlowId;
        this.replyId = replyId;
        this.replyCode = replyCode;
    }

    public Integer getReplyFlowId() {
        return replyFlowId;
    }

    public void setReplyFlowId(Integer flowId) {
        this.replyFlowId = flowId;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer msgId) {
        this.replyId = msgId;
    }

    public Integer getReplyCode() {
        return replyCode;
    }

    public void setReplyCode(Integer code) {
        this.replyCode = code;
    }

}
