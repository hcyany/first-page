//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lizhou.exception;

public class FileNotExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public FileNotExistsException() {
    }

    public void printStackTrace() {
        System.out.println("Exception: 下载目录不存在");
    }
}
