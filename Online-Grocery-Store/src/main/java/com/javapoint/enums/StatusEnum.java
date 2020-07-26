package com.javapoint.enums;

public enum StatusEnum {
	// Order status ennum
	    CREATED(0, "CREATED"),
	    PLACED(1, "PLACED"),
	    CANCELED(2, "CANCELLED");

	    private  int code;
	    private String msg;

	    public String getMsg() {
			return msg;
		}

		StatusEnum(int code, String msg) {
	        this.code = code;
	        this.msg = msg;
	    }

	    public Integer getCode() {
	        return code;
	    }
	

}
