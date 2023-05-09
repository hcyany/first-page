//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lizhou.fileload;

import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUpload {
    private String tempPath = null;
    private String filePath = null;
    private int bufferSize = 100;
    private int fileSize = 1000;
    private String encoding = "UTF-8";
    private List<String> fileFormat = new ArrayList();
    private HttpServletRequest request;

    private FileUpload() {
    }

    public FileUpload(HttpServletRequest request) {
        this.request = request;
    }

    public FileUpload(String tempPath, String filePath, HttpServletRequest request) {
        this.request = request;
        this.tempPath = tempPath;
        this.filePath = filePath;
        this.makeDirectory(tempPath);
        this.makeDirectory(filePath);
    }

    public boolean uploadFile() throws ProtocolException, NullFileException, SizeException, FileFormatException, IOException, FileUploadException {
        boolean b = true;
        ServletFileUpload upload = this.getUpload();
        List<FileItem> fileItems = upload.parseRequest(this.request);
        Iterator var5 = fileItems.iterator();

        while(var5.hasNext()) {
            FileItem item = (FileItem)var5.next();
            if (!item.isFormField()) {
                if (item.getSize() <= 0L) {
                    b = false;
                    throw new NullFileException();
                }

                if (item.getSize() > (long)(this.fileSize * 1000)) {
                    b = false;
                    throw new SizeException();
                }

                String fileName = item.getName();
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                if (!this.isFormat(fileName.substring(fileName.lastIndexOf(".") + 1))) {
                    b = false;
                    throw new FileFormatException();
                }

                String uuidFileName = this.getUuidFileName(fileName);
                InputStream is = item.getInputStream();
                OutputStream os = new FileOutputStream(this.filePath + "/" + uuidFileName);
                this.output(is, os);
                item.delete();
            }
        }

        return b;
    }

    public InputStream getUploadInputStream() throws ProtocolException, NullFileException, SizeException, FileFormatException, IOException, FileUploadException {
        ServletFileUpload upload = this.getUpload(true);
        List<FileItem> fileItems = upload.parseRequest(this.request);
        Iterator var4 = fileItems.iterator();

        while(var4.hasNext()) {
            FileItem item = (FileItem)var4.next();
            if (!item.isFormField()) {
                if (item.getSize() <= 0L) {
                    throw new NullFileException();
                }

                if (item.getSize() > (long)(this.fileSize * 1000)) {
                    throw new SizeException();
                }

                String fileName = item.getName();
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                if (!this.isFormat(fileName.substring(fileName.lastIndexOf(".") + 1))) {
                    throw new FileFormatException();
                }

                InputStream is = item.getInputStream();
                return is;
            }
        }

        return null;
    }

    private ServletFileUpload getUpload() throws ProtocolException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(this.bufferSize);
        if (this.filePath == null) {
            this.setDefaultFilePath();
        }

        if (this.tempPath == null) {
            this.setDefaultTempPath();
        }

        factory.setRepository(new File(this.tempPath));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(this.encoding);
        if (!ServletFileUpload.isMultipartContent(this.request)) {
            throw new ProtocolException();
        } else {
            return upload;
        }
    }

    private ServletFileUpload getUpload(boolean stream) throws ProtocolException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(this.bufferSize);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(this.encoding);
        if (!ServletFileUpload.isMultipartContent(this.request)) {
            throw new ProtocolException();
        } else {
            return upload;
        }
    }

    private void output(InputStream is, OutputStream os) throws IOException {
        byte[] by = new byte[1024];
        //boolean len = false;

        int len;
        while((len = is.read(by)) > 0) {
            os.write(by, 0, len);
        }

        is.close();
        os.close();
    }

    private boolean isFormat(String format) {
        if (this.fileFormat.size() == 0) {
            return true;
        } else {
            Iterator var3 = this.fileFormat.iterator();

            while(var3.hasNext()) {
                String f = (String)var3.next();
                if (f.equalsIgnoreCase(format)) {
                    return true;
                }
            }

            return false;
        }
    }

    public String getUuidFileName(String fileName) {
        return UUID.randomUUID().toString() + "#" + fileName;
    }

    private void setDefaultTempPath() {
        this.tempPath = this.filePath + "/temp";
        this.makeDirectory(this.tempPath);
    }

    private void setDefaultFilePath() {
        this.filePath = "D:/uploadFile";
        this.makeDirectory(this.filePath);
    }

    private void makeDirectory(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }

    }

    public String getTempPath() {
        return this.tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
        this.makeDirectory(tempPath);
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        this.makeDirectory(filePath);
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getFileFormat() {
        if (this.fileFormat.size() == 0) {
            return "*";
        } else {
            String format = "";

            String s;
            for(Iterator var3 = this.fileFormat.iterator(); var3.hasNext(); format = format + "," + s) {
                s = (String)var3.next();
            }

            format = format.substring(format.indexOf(",") + 1);
            return format;
        }
    }

    public void setFileFormat(String format) {
        this.fileFormat.add(format);
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
