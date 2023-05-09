//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lizhou.exception;

public class ProtocolException extends Exception {
    private static final long serialVersionUID = 1L;

    public ProtocolException() {
    }

    public void printStackTrace() {
        System.out.println("Exception: 客户端未使用MIME协议上传文件");
    }
}
