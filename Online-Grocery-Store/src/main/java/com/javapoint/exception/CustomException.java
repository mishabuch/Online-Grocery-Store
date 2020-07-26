package com.javapoint.exception;

import com.javapoint.enums.ErrorCodeEnums;

public class CustomException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private final int  code;

	    public CustomException(ErrorCodeEnums enums) {
	        super(enums.getMessage());
	        this.code = enums.getCode();
	    }

	    public CustomException(Integer code, String message) {
	        super(message);
	        this.code = code;
	    }

}
