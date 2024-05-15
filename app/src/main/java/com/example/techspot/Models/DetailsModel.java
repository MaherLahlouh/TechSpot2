package com.example.techspot.Models;

import java.util.ArrayList;
import java.util.List;

public class DetailsModel {
    private String osName;
    private String cpuDescription;
    private String ramSize;
    private String backCameraInfo;
    private String frontCameraInfo;
    private String displayInfo;
    private String batteryInfo;
    private String memorySize;
    private String gpuInfo;
    private String price;
    private List<String> images = new ArrayList<>();

    public DetailsModel(String osName, String cpuDescription, String ramSize, String backCameraInfo, String frontCameraInfo, String displayInfo, String batteryInfo, String memorySize, String gpuInfo, String price, List<String> images) {
        this.osName = osName;
        this.cpuDescription = cpuDescription;
        this.ramSize = ramSize;
        this.backCameraInfo = backCameraInfo;
        this.frontCameraInfo = frontCameraInfo;
        this.displayInfo = displayInfo;
        this.batteryInfo = batteryInfo;
        this.memorySize = memorySize;
        this.gpuInfo = gpuInfo;
        this.price = price;
        this.images = images;
    }

    public String getOsName() {
        return osName;
    }

    public String getCpuDescription() {
        return cpuDescription;
    }

    public String getRamSize() {
        return ramSize;
    }

    public String getBackCameraInfo() {
        return backCameraInfo;
    }

    public String getFrontCameraInfo() {
        return frontCameraInfo;
    }

    public String getDisplayInfo() {
        return displayInfo;
    }

    public String getBatteryInfo() {
        return batteryInfo;
    }

    public String getMemorySize() {
        return memorySize;
    }

    public String getGpuInfo() {
        return gpuInfo;
    }

    public String getPrice() {
        return price;
    }

    public List<String> getImages() {
        return images;
    }
}
