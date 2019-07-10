package com.jesse.pushdemo.vo;

/**
 * @author jessehsj
 */

@SuppressWarnings("ALL")
public enum ManufacturerEnum {
    HUAWEI(1, "HuaWei", "华为"),
    XIAOMI(2, "XiaoMi", "小米"),
    MEIZU(3, "Meizu", "魅族"),
    OPPO(4, "Oppo", "oppo"),
    VIVO(5, "Vivo", "vivo");

    private Integer num;
    private String code;
    private String description;

    ManufacturerEnum(Integer num, String code, String description) {
        this.num = num;
        this.code = code;
        this.description = description;
    }


    public Integer num() {
        return num;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }
}
