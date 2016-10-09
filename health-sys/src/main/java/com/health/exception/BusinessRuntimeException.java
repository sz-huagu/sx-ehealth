package com.health.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class BusinessRuntimeException extends RuntimeException {
	private static final long	serialVersionUID	= 1L;

	private String				errInfo;

	private ErrEnum				err;

	public BusinessRuntimeException(ErrEnum err) {
		super(err.getName());
		this.err = err;
		log.info("异常码:" + err.getCode() + " 异常信息:" + err.getName());
	}
}
