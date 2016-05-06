package com.app.linj.com;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public enum ActivityTypeEnum {
    CHINESE(1,"一日游"),JAPANESE(2,"周末游"),WESTERN(3,"毕业游"),DESSERT(4,"出境游");

    private Integer num;private String value;
    ActivityTypeEnum(Integer num,String value){
        this.num = num;
        this.value = value;
    }
    public Integer getNum(){
        return num;
    }
    public String getValue(){
        return value;
    }

    public static String getByNum(Integer num){
        String value = "";
        switch (num) {
            case 1:
                value = "一日游";
                break;
            case 2:
                value = "周末游";
                break;
            case 3:
                value = "毕业游";
                break;
            case 4:
                value = "出境游";
                break;
            default:System.out.println("not fund");
        }
        return value;
    }
    public static void main(String[] args){
        for (ActivityTypeEnum e : ActivityTypeEnum.values()) {
            System.out.println(e + "," + e.getValue());
        }
    }
}
