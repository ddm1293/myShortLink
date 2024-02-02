package org.myShortLink.project.common.enums;

public enum ValidDateTypeEnum {

    PERMANENT(Boolean.FALSE),

    CUSTOM(Boolean.TRUE);

    private final Boolean type;

    ValidDateTypeEnum(Boolean type) {
        this.type = type;
    }

    public Boolean getType() {
        return type;
    }
}
