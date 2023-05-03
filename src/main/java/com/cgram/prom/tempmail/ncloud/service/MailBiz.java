package com.cgram.prom.tempmail.ncloud.service;

//import com.promisope.cmm.ncloud.model.FileUploadResponse;
//import com.promisope.cmm.ncloud.model.MailRequest;
import org.apache.commons.lang3.tuple.Pair;

import com.cgram.prom.tempmail.ncloud.model.FileUploadResponse;
import com.cgram.prom.tempmail.ncloud.model.MailRequest;

import java.io.File;
import java.util.List;

public interface MailBiz {
    void send(MailRequest mailRequest);

    FileUploadResponse fileUpload(List<Pair<File, String>> fileList);
}
