package com.wp.ha.zk.common;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 *
 */
public class ClientException extends NestableRuntimeException {

    private static final long serialVersionUID = -7545341502620139031L;

    public ClientException(String errorCode){
        super(errorCode);
    }

    public ClientException(String errorCode, Throwable cause){
        super(errorCode, cause);
    }

    public ClientException(String errorCode, String errorDesc){
        super(errorCode + ":" + errorDesc);
    }

    public ClientException(String errorCode, String errorDesc, Throwable cause){
        super(errorCode + ":" + errorDesc, cause);
    }

    public ClientException(Throwable cause){
        super(cause);
    }
}
