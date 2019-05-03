package com.capillary.ops.deployer.bo;

import java.io.InputStream;

public class S3DumpFile {

    public S3DumpFile() {}

    public S3DumpFile(InputStream inputStream, Long contentLength) {
        this.inputStream = inputStream;
        this.contentLength = contentLength;
    }

    private InputStream inputStream;

    private Long contentLength;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }
}
