package com.cesfelipesegundo.itis.web;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadBean {

	/**
     * The imported file.
     */
    private MultipartFile file;

    /**
     * @return Returns the file.
     */
    public final MultipartFile getFile() {
        return file;
    }

    /**
     * @param file The file to set.
     */
    public final void setFile(final MultipartFile file) {
        this.file = file;
    }
}
