//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lizhou.exception;

public class SizeException extends Exception {
    private static final long serialVersionUID = 1L;

    public SizeException() {
    }

    public void printStackTrace() {
        System.out.println("Exception: 上传文件超过限制大小");
    }
}
