package com.oreilly.servlet.exception;

import java.io.IOException;

/**
 * 文件大小限制的异常
 * @author L.cm
 */
public class LimitedException extends IOException {
    private static final long serialVersionUID = -7857857001230036034L;

    public LimitedException(String message) {
        super(message);
    }
}
