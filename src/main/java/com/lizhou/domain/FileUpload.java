package com.lizhou.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;

/**
 * 上传文件
 * @author 赵燕军
 *
 */
public class FileUpload {
    /**
     * 上传文件用的临时目录
     */
    private String tempPath = null;
    /**
     * 上传文件用的文件目录
     */
    private String filePath = null;
    /**
     * 内存中缓存区的大小,默认100k
     */
    private int bufferSize = 100;
    /**
     * 上传文件的最大大小,默认1M
     */
    private int fileSize = 1000;
    /**
     * 上传文件使用的编码方式,默认UTF-8
     */
    private String encoding = "UTF-8";
    /**
     * 上传文件的格式,默认无格式限制
     */
    private List<String> fileFormat = new ArrayList<String>();
    /**
     * HttpServletRequest
     */
    private HttpServletRequest request;
    //私有化无参构造
    public FileUpload(){}

    public FileUpload(HttpServletRequest request){
        this.request = request;
    }

    public FileUpload(String tempPath, String filePath, HttpServletRequest request){
        this.request = request;
        this.tempPath = tempPath;
        this.filePath = filePath;
        makeDirectory(tempPath);
        makeDirectory(filePath);
    }

    /**
     * 上传文件
     * @return 上传成功返回true
     * @throws ProtocolException
     * @throws FileUploadException
     * @throws NullFileException
     * @throws SizeException
     * @throws FileFormatException
     * @throws IOException
     */
    public boolean uploadFile()
            throws ProtocolException, NullFileException, SizeException, FileFormatException, IOException, FileUploadException{
        boolean b = true;
        ServletFileUpload upload = getUpload();
        //解析request中的字段，每个字段(上传字段和普通字段)都会自动包装到FileItem中
        List<FileItem> fileItems = upload.parseRequest(this.request);
        for(FileItem item : fileItems){
            //如果为普通字段
            if(item.isFormField()){
                continue;
            }
            //获取文件名
            String fileName = item.getName();
            //因为IE6得到的是文件的全路径，所以进一步处理
            fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
            //判断上传的文件是否为空
            if(item.getSize() <= 0){
                b = false;
                throw new NullFileException();
            }
            //判断文件是否超过限制的大小
            if(item.getSize() > fileSize*1000){
                b = false;
                throw new SizeException();
            }
            //判断上传文件的格式是否正确
            if( !isFormat(fileName.substring(fileName.lastIndexOf(".")+1)) ){
                b = false;
                throw new FileFormatException();
            }
            String uuidFileName = getUuidFileName();
            //获取文件的输入流
            InputStream is = item.getInputStream();
            //创建输出流
            OutputStream os = new FileOutputStream(this.filePath+"/"+uuidFileName);
            //输出
            output(is, os);
            //将上传文件时产生的临时文件删除
            item.delete();
        }
        return b;
    }

    /**
     * 获取文件上传的输入流
     * @return
     * @throws ProtocolException
     * @throws NullFileException
     * @throws SizeException
     * @throws FileFormatException
     * @throws IOException
     * @throws FileUploadException
     */
    public InputStream getUploadInputStream()
            throws ProtocolException, NullFileException, SizeException, FileFormatException, IOException, FileUploadException{
        ServletFileUpload upload = getUpload();
        //解析request中的字段，每个字段(上传字段和普通字段)都会自动包装到FileItem中
        List<FileItem> fileItems = upload.parseRequest(this.request);
        for(FileItem item : fileItems){
            //如果为普通字段
            if(item.isFormField()){
                continue;
            }
            //获取文件名
            String fileName = item.getName();
            //因为IE6得到的是文件的全路径，所以进一步处理
            fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
            //判断上传的文件是否为空
            if(item.getSize() <= 0){
                throw new NullFileException();
            }
            //判断文件是否超过限制的大小
            if(item.getSize() > fileSize*1000){
                throw new SizeException();
            }
            //判断上传文件的格式是否正确
            if( !isFormat(fileName.substring(fileName.lastIndexOf(".")+1)) ){
                throw new FileFormatException();
            }
            //获取文件的输入流
            InputStream is = item.getInputStream();

            return is;
        }
        return null;
    }

    /**
     * 获取上传文件的核心
     * @return ServletFileUpload
     * @throws ProtocolException
     */
    public ServletFileUpload getUpload() throws ProtocolException{
        //创建上传文件工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置内存中缓存区的大小
        factory.setSizeThreshold(bufferSize);
        //如果用户未设置上传文件目录，则设置默认目录
        if(filePath == null){
            setDefaultFilePath();
        }
        //如果用户未设置临时存放目录，则设置默认目录
        if(tempPath == null){
            setDefaultTempPath();
        }
        //设置上传文件的的临时存放目录
        factory.setRepository(new File(this.tempPath));
        //创建上传文件对象[核心]
        ServletFileUpload upload = new ServletFileUpload(factory);
        //设置上传文件的编码方式
        upload.setHeaderEncoding(this.encoding);
        /*
         * 判断客户端上传文件是否使用MIME协议
         * 只有当以MIME协议上传文件时，upload才能解析request中的字段
         */
        if(!upload.isMultipartContent(this.request)){
            throw new ProtocolException();
        }
        return upload;
    }

    /**
     * 输出
     * @param is
     * @param os
     * @throws IOException
     */
    public void output(InputStream is, OutputStream os) throws IOException{
        byte[] by = new byte[1024];
        int len = 0;
        while( (len = is.read(by)) > 0 ){
            os.write(by, 0, len);
        }
        is.close();
        os.close();
    }

    /**
     * 判断上传文件的格式是否正确
     * @param format 文件格式
     * @return boolean
     */
    private boolean isFormat(String format){
        if(fileFormat.size() == 0){
            return true;
        }
        for(String f : fileFormat){
            if(f.equalsIgnoreCase(format)){
                return true;
            }
        }
        return false;
    }

    /**
     * 返回文件的UUID名,防止文件名重复

     * @return uuid名
     */
    public String getUuidFileName(){
        return UUID.randomUUID().toString();
    }

    /**
     * 设置默认临时目录
     */
    private void setDefaultTempPath(){
        tempPath = filePath+"/temp";

        makeDirectory(tempPath);
    }

    /**
     * 设置默认文件目录
     * 默认在D盘
     */
    private void setDefaultFilePath(){
        filePath = "D:/uploadFile";

        makeDirectory(filePath);
    }

    public void setFileFormat(List<String> fileFormat) {
        this.fileFormat = fileFormat;
    }

    /**
     * 根据给定的文件目录创建目录
     * @param filePath
     */
    private void makeDirectory(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdir();
        }
    }



    /**
     * 获取临时目录
     * @return
     */
    public String getTempPath() {
        return tempPath;
    }
    /**
     * 设置临时目录
     * @param tempPath
     */
    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
        makeDirectory(tempPath);
    }
    /**
     * 获取文件路径
     * @return
     */
    public String getFilePath() {
        return filePath;
    }
    /**
     * 返回文件路径
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        makeDirectory(filePath);
    }
    /**
     * 获取内存中缓存区大小
     * @return
     */
    public int getBufferSize() {
        return bufferSize;
    }
    /**
     * 设置内存中缓存区大小
     * 默认100k
     * @param bufferSize
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    /**
     * 获取上传文件的最大大小
     * @return
     */
    public int getFileSize() {
        return fileSize;
    }
    /**
     * 限制上传文件的最大大小,单位为k
     * 默认1000k
     * @param fileSize
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    /**
     * 返回上传文件的编码方式
     * @return
     */
    public String getEncoding() {
        return encoding;
    }
    /**
     * 设置上传文件的编码方式
     * 默认UTF-8格式
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**
     * 获取允许上传文件的格式
     * @return
     */
    public String getFileFormat() {
        if(fileFormat.size() == 0){
            return "*";
        }
        String format = "";
        for(String s:fileFormat){
            format += ","+s;
        }
        format = format.substring(format.indexOf(",")+1);
        return format;
    }
    /**
     * 设置上传文件的格式,多个文件格式则多次调用该方法进行设置
     * @param 
     */
    public void setFileFormat(String format) {
        this.fileFormat.add(format);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }


    public void setUuidFileName(String uuidFileName) {
        this.filePath = uuidFileName;
    }

    public String getFileName() {
        return filePath;
    }
}
