package com.vds.sonhlt.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.Objects;


@Controller
@Slf4j

public class HomeController {
    private static final String CONFIRM_PAYMENT_URL = "http://4b57-27-72-60-129.ngrok.io/transactions";
    private static final String SUBMIT_OTP_URL = "http://4b57-27-72-60-129.ngrok.io/transactions/verify";

    private RestTemplate createRestTemplate() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.60.135.37", 8800));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }

    @GetMapping(value = {"/", "/index"})
    public String welcome(Model model) {
        model.addAttribute("billingInfo", new BillingInfo());
        return "index";
    }

    @PostMapping("/show-payment")
    public String showPaymentPage(Model model, @ModelAttribute("billingInfo") BillingInfo billingInfo) {
        log.info("Username: {}", billingInfo.getUsername());
        model.addAttribute("billingInfo", billingInfo);
        return "form";
    }

    @PostMapping("/confirm-payment")
    public String confirmPayment(Model model, @ModelAttribute("billingInfo") BillingInfo billingInfo) {
        //billingInfo.setUsername("84963743487");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.60.135.37", 8800));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<BillingInfo> requestBody = new HttpEntity<>(billingInfo);

        log.info("Confirm info: {}", billingInfo);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(CONFIRM_PAYMENT_URL, requestBody, String.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            String transactionCode = responseEntity.getBody();
            log.info("result: {}", transactionCode);
            model.addAttribute("transactionCode", transactionCode);
            billingInfo.setTransactionCode(transactionCode);
        } else {
            log.error("Fail to receive transaction code.");
            return "error";
        }

        //model.addAttribute("billingInfo", billingInfo);
        model.addAttribute("imgSrc", "http://localhost:8080/qr-code-gen/gen?barcodeText=" + billingInfo.getTransactionCode());

        return "qr";
    }

    @PostMapping("/submit-otp")
    public String submitOtp(Model model, @ModelAttribute("billingInfo") BillingInfo billingInfo) {
        //billingInfo.setUsername("84963743487");
        RestTemplate restTemplate = this.createRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<BillingInfo> requestBody = new HttpEntity<>(billingInfo);

        log.info("OTP verify request info: {}", billingInfo);

        ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(SUBMIT_OTP_URL, requestBody, Boolean.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode()) && !Objects.isNull(responseEntity.getBody())) {
            log.info("OTP verify Response body: {}", responseEntity.getBody());
            String result = "Sai OTP";
            String message = "Sai mã OTP! Vui lòng kiểm tra lại.";
            boolean isSuccess = false;
            if (Boolean.TRUE.equals(responseEntity.getBody())) {
                result = "Thành công";
                message = "Xác nhận OTP thành công";
                isSuccess = true;
            }
            model.addAttribute("result", result);
            model.addAttribute("message", message);
            model.addAttribute("isSuccess", isSuccess);
            return "otp-result";
        } else {
            log.error("Failed to verify OTP.");
            return "error";
        }
    }

}
