package com.springextended.core.event;

/**
 * <p>
 * 领域事件类型
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 05 11:58
 */
public enum EventType{
   ContractPaySuccess(1,"合同支付成功"),
    ContractSignFinish(2,"合同签约成功"),
    ContractTerminated(3,"合同退租成功"),
    BillPaySuccess(4,"账单支付成功");

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    private int value;
    private String desc;

    EventType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static EventType valueOf(int value){
        if(value==1) {
            return EventType.ContractPaySuccess;
        }
        if(value==2) {
            return EventType.ContractSignFinish;
        }
        if(value==3) {
            return EventType.ContractTerminated;
        }
        if(value==4) {
            return EventType.BillPaySuccess;
        }

        return null;
    }
}
