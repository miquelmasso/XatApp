package com.example.damxat.Model;

public class ResponseModel {
    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public ResponseModel(int success, int failure) {
        this.success = success;
        this.failure = failure;
    }

    public int success;

    public ResponseModel(int success) {
        this.success = success;
    }

    public int failure;

}
