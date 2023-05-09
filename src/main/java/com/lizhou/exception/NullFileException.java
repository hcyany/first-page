//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lizhou.exception;

public class NullFileException extends Exception {
    private static final long serialVersionUID = 1L;

    public NullFileException() {
    }

    public void printStackTrace() {
        System.out.println("Exception: 上传的文件为空");
    }
}
