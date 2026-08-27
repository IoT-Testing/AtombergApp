package com.appTest.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppiumTestResult {
    private long id;
    private String name;
    private String status;
    private String platform;
    private String device;
    private long duration;
    private String timestamp;
    private String className;
    private String errorMessage;
    private String stackTrace;

    public AppiumTestResult(){}

    public static class Builder{
        private AppiumTestResult result = new AppiumTestResult();

        public Builder id(long id) { result.id = id; return this; }
        public Builder name(String name) { result.name = name; return this; }
        public Builder status(String status) { result.status = status; return this; }
        public Builder platform(String platform) { result.platform = platform; return this; }
        public Builder device(String device) { result.device = device; return this; }
        public Builder duration(long duration) { result.duration = duration; return this; }
        public Builder timestamp(String timestamp) { result.timestamp = timestamp; return this; }
        public Builder className(String className) { result.className = className; return this; }
        public Builder errorMessage(String errorMessage) { result.errorMessage = errorMessage; return this; }
        public Builder stackTrace(String stackTrace) { result.stackTrace = stackTrace; return this; }

        public AppiumTestResult build() { return result; }
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getStackTrace() { return stackTrace; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
}