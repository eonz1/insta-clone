package com.cgram.prom;

import com.cgram.prom.tempmail.ncloud.model.FileUploadResponse;
import com.cgram.prom.tempmail.ncloud.model.MailRequest;
import com.cgram.prom.tempmail.ncloud.service.MailBiz;
import com.cgram.prom.tempmail.ncloud.service.MailBizImpl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

public class TestEmailSendRunner {

    public static void main(String[] args) {

        MailBiz mailBiz = new MailBizImpl(new RestTemplate());

        List<Pair<File, String>> fileList = List.of(
                Pair.of(new File("src/test/resources/test.png"), "고양이.png")
        );

        FileUploadResponse response = mailBiz.fileUpload(fileList);

        MailRequest request = new MailRequest();
        request.setSenderAddress("shoon@promisope.com");
        request.setSenderName("메일전송 테스트 발송인");
        request.setTitle("테스트 메일 전송 타이틀");
        request.setBody("메일 내용<br><br>html 태그 확인<hr>");
        request.addRecipient("orcpunch@gmail.com");
        request.addRecipient("orcpunch@naver.com");
        request.setAttachFileIds(response.getFileIds());

        mailBiz.send(request);
    }

}
