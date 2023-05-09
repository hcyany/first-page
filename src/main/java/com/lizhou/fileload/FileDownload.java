
package com.lizhou.fileload;

import com.lizhou.domain.FileUpload;
import com.lizhou.exception.FileNotExistsException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownload {
    private String filePath = null;
    private List<String> fileFormat = new ArrayList();
    private HttpServletRequest request;

    public FileDownload(HttpServletRequest request) {
        this.request = request;
    }

    public FileDownload(String filePath, HttpServletRequest request) {
        this.request = request;
        this.filePath = filePath;
    }

    public void bindFileUploadsToScope(String var) throws FileNotExistsException {
        if (this.filePath == null) {
            this.filePath = "D:/uploadFile";
        }

        if (!this.isFileExists(this.filePath)) {
            throw new FileNotExistsException();
        } else {
            List<FileUpload> list = new ArrayList();
            this.getFileUploads(this.filePath, list);
            this.request.getSession().setAttribute(var, list);
        }
    }

    private void getFileUploads(String filePath, List<FileUpload> list) {
        File file = new File(filePath);
        if (file.isFile()) {
            String uuidFileName = file.getName();
            if (this.isFormat(uuidFileName.substring(uuidFileName.lastIndexOf(".") + 1))) {
                FileUpload df = new FileUpload();
                df.setFileFormat(uuidFileName.substring(uuidFileName.indexOf("#") + 1));
                df.setUuidFileName(uuidFileName);
                df.setFilePath(file.getPath());
                list.add(df);
            }
        } else {
            File[] childFiles = file.listFiles();
            File[] var8 = childFiles;
            int var7 = childFiles.length;

            for(int var6 = 0; var6 < var7; ++var6) {
                File cf = var8[var6];
                this.getFileUploads(cf.getPath(), list);
            }
        }

    }

    public void downloadFile(String var, String uuidFileName, HttpServletResponse response) throws IOException {
        byte[] by = uuidFileName.getBytes("ISO-8859-1");
        uuidFileName = new String(by, "UTF-8");
        List<FileUpload> files = (List)this.request.getSession().getAttribute(var);
        Iterator var7 = files.iterator();

        while(var7.hasNext()) {
            FileUpload file = (FileUpload)var7.next();
            if (file.getUuidFileName().equals(uuidFileName)) {
                InputStream is = new FileInputStream(file.getFilePath());
                OutputStream os = response.getOutputStream();
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getFileName(), "UTF-8"));
                this.output(is, os);
                break;
            }
        }

    }

    public void output(InputStream is, OutputStream os) throws IOException {
        byte[] by = new byte[1024];
        //int len = false;

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

    private boolean isFileExists(String filePath) {
        boolean b = true;
        File file = new File(filePath);
        if (!file.exists()) {
            b = false;
        }

        return b;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
