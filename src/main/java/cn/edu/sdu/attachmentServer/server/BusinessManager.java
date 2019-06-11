package cn.edu.sdu.attachmentServer.server;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BusinessManager {

    // 消息流水号 word(16) 按发送顺序从 0 开始循环累加
    private volatile int currentFlowId = 0;

    private Map<String, Channel> terminalPhoneMap;
    private Map<String, String> fileNameMap;

    public static BusinessManager getInstance() {
        return Singleton.INSTANCE.getSingleton();
    }

    private BusinessManager() {
        this.terminalPhoneMap = new ConcurrentHashMap<>();
        this.fileNameMap = new ConcurrentHashMap<>();
    }

    private enum Singleton {
        INSTANCE;
        private BusinessManager singleton;
        Singleton() {
            singleton = new BusinessManager();
        }
        public BusinessManager getSingleton() {
            return singleton;
        }
    }

    public synchronized Channel findChannelByTerminalPhone(String terminalPhone) {
        return terminalPhoneMap.get(terminalPhone);
    }

    public synchronized void putByTerminalPhone(String terminalPhone, Channel channel) {
        terminalPhoneMap.put(terminalPhone, channel);
    }

    public synchronized void removeByTerminalPhone(String terminalPhone) {
        terminalPhoneMap.remove(terminalPhone);
    }


    // fileName
    public synchronized String findFileName(String key) {
        return fileNameMap.get(key);
    }

    public synchronized void putFileName(String key, String fileName) {
        fileNameMap.put(key, fileName);
    }

    public synchronized void removeFileName(String key) {
        fileNameMap.remove(key);
    }


    public synchronized int currentFlowId() {
        if (currentFlowId >= 0xffff)
            currentFlowId = 0;
        return currentFlowId++;
    }
}
