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
 * �ϴ��ļ�
 * @author �����
 *
 */
public class FileUpload {
    /**
     * �ϴ��ļ��õ���ʱĿ¼
     */
    private String tempPath = null;
    /**
     * �ϴ��ļ��õ��ļ�Ŀ¼
     */
    private String filePath = null;
    /**
     * �ڴ��л������Ĵ�С,Ĭ��100k
     */
    private int bufferSize = 100;
    /**
     * �ϴ��ļ�������С,Ĭ��1M
     */
    private int fileSize = 1000;
    /**
     * �ϴ��ļ�ʹ�õı��뷽ʽ,Ĭ��UTF-8
     */
    private String encoding = "UTF-8";
    /**
     * �ϴ��ļ��ĸ�ʽ,Ĭ���޸�ʽ����
     */
    private List<String> fileFormat = new ArrayList<String>();
    /**
     * HttpServletRequest
     */
    private HttpServletRequest request;
    //˽�л��޲ι���
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
     * �ϴ��ļ�
     * @return �ϴ��ɹ�����true
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
        //����request�е��ֶΣ�ÿ���ֶ�(�ϴ��ֶκ���ͨ�ֶ�)�����Զ���װ��FileItem��
        List<FileItem> fileItems = upload.parseRequest(this.request);
        for(FileItem item : fileItems){
            //���Ϊ��ͨ�ֶ�
            if(item.isFormField()){
                continue;
            }
            //��ȡ�ļ���
            String fileName = item.getName();
            //��ΪIE6�õ������ļ���ȫ·�������Խ�һ������
            fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
            //�ж��ϴ����ļ��Ƿ�Ϊ��
            if(item.getSize() <= 0){
                b = false;
                throw new NullFileException();
            }
            //�ж��ļ��Ƿ񳬹����ƵĴ�С
            if(item.getSize() > fileSize*1000){
                b = false;
                throw new SizeException();
            }
            //�ж��ϴ��ļ��ĸ�ʽ�Ƿ���ȷ
            if( !isFormat(fileName.substring(fileName.lastIndexOf(".")+1)) ){
                b = false;
                throw new FileFormatException();
            }
            String uuidFileName = getUuidFileName();
            //��ȡ�ļ���������
            InputStream is = item.getInputStream();
            //���������
            OutputStream os = new FileOutputStream(this.filePath+"/"+uuidFileName);
            //���
            output(is, os);
            //���ϴ��ļ�ʱ��������ʱ�ļ�ɾ��
            item.delete();
        }
        return b;
    }

    /**
     * ��ȡ�ļ��ϴ���������
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
        //����request�е��ֶΣ�ÿ���ֶ�(�ϴ��ֶκ���ͨ�ֶ�)�����Զ���װ��FileItem��
        List<FileItem> fileItems = upload.parseRequest(this.request);
        for(FileItem item : fileItems){
            //���Ϊ��ͨ�ֶ�
            if(item.isFormField()){
                continue;
            }
            //��ȡ�ļ���
            String fileName = item.getName();
            //��ΪIE6�õ������ļ���ȫ·�������Խ�һ������
            fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
            //�ж��ϴ����ļ��Ƿ�Ϊ��
            if(item.getSize() <= 0){
                throw new NullFileException();
            }
            //�ж��ļ��Ƿ񳬹����ƵĴ�С
            if(item.getSize() > fileSize*1000){
                throw new SizeException();
            }
            //�ж��ϴ��ļ��ĸ�ʽ�Ƿ���ȷ
            if( !isFormat(fileName.substring(fileName.lastIndexOf(".")+1)) ){
                throw new FileFormatException();
            }
            //��ȡ�ļ���������
            InputStream is = item.getInputStream();

            return is;
        }
        return null;
    }

    /**
     * ��ȡ�ϴ��ļ��ĺ���
     * @return ServletFileUpload
     * @throws ProtocolException
     */
    public ServletFileUpload getUpload() throws ProtocolException{
        //�����ϴ��ļ�����
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //�����ڴ��л������Ĵ�С
        factory.setSizeThreshold(bufferSize);
        //����û�δ�����ϴ��ļ�Ŀ¼��������Ĭ��Ŀ¼
        if(filePath == null){
            setDefaultFilePath();
        }
        //����û�δ������ʱ���Ŀ¼��������Ĭ��Ŀ¼
        if(tempPath == null){
            setDefaultTempPath();
        }
        //�����ϴ��ļ��ĵ���ʱ���Ŀ¼
        factory.setRepository(new File(this.tempPath));
        //�����ϴ��ļ�����[����]
        ServletFileUpload upload = new ServletFileUpload(factory);
        //�����ϴ��ļ��ı��뷽ʽ
        upload.setHeaderEncoding(this.encoding);
        /*
         * �жϿͻ����ϴ��ļ��Ƿ�ʹ��MIMEЭ��
         * ֻ�е���MIMEЭ���ϴ��ļ�ʱ��upload���ܽ���request�е��ֶ�
         */
        if(!upload.isMultipartContent(this.request)){
            throw new ProtocolException();
        }
        return upload;
    }

    /**
     * ���
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
     * �ж��ϴ��ļ��ĸ�ʽ�Ƿ���ȷ
     * @param format �ļ���ʽ
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
     * �����ļ���UUID��,��ֹ�ļ����ظ�

     * @return uuid��
     */
    public String getUuidFileName(){
        return UUID.randomUUID().toString();
    }

    /**
     * ����Ĭ����ʱĿ¼
     */
    private void setDefaultTempPath(){
        tempPath = filePath+"/temp";

        makeDirectory(tempPath);
    }

    /**
     * ����Ĭ���ļ�Ŀ¼
     * Ĭ����D��
     */
    private void setDefaultFilePath(){
        filePath = "D:/uploadFile";

        makeDirectory(filePath);
    }

    public void setFileFormat(List<String> fileFormat) {
        this.fileFormat = fileFormat;
    }

    /**
     * ���ݸ������ļ�Ŀ¼����Ŀ¼
     * @param filePath
     */
    private void makeDirectory(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdir();
        }
    }



    /**
     * ��ȡ��ʱĿ¼
     * @return
     */
    public String getTempPath() {
        return tempPath;
    }
    /**
     * ������ʱĿ¼
     * @param tempPath
     */
    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
        makeDirectory(tempPath);
    }
    /**
     * ��ȡ�ļ�·��
     * @return
     */
    public String getFilePath() {
        return filePath;
    }
    /**
     * �����ļ�·��
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        makeDirectory(filePath);
    }
    /**
     * ��ȡ�ڴ��л�������С
     * @return
     */
    public int getBufferSize() {
        return bufferSize;
    }
    /**
     * �����ڴ��л�������С
     * Ĭ��100k
     * @param bufferSize
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    /**
     * ��ȡ�ϴ��ļ�������С
     * @return
     */
    public int getFileSize() {
        return fileSize;
    }
    /**
     * �����ϴ��ļ�������С,��λΪk
     * Ĭ��1000k
     * @param fileSize
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    /**
     * �����ϴ��ļ��ı��뷽ʽ
     * @return
     */
    public String getEncoding() {
        return encoding;
    }
    /**
     * �����ϴ��ļ��ı��뷽ʽ
     * Ĭ��UTF-8��ʽ
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**
     * ��ȡ�����ϴ��ļ��ĸ�ʽ
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
     * �����ϴ��ļ��ĸ�ʽ,����ļ���ʽ���ε��ø÷�����������
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
