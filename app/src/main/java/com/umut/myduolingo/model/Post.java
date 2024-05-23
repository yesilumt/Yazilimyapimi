package com.umut.myduolingo.model;

public class Post {
    public String email;
    public String mean;
    public String trWord;
    public String engWord;
    public String DownloadUrl;

    public Post(String email, String mean, String trWord, String engWord, String downloadUrl) {
        this.email = email;
        this.mean = mean;
        this.trWord = trWord;
        this.engWord = engWord;
        DownloadUrl = downloadUrl;
    }
}
