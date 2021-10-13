package com.vds.sonhlt.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@Slf4j

public class HomeController {
    private static final String CONFIRM_PAYMENT_URL = "";

    @GetMapping("")
    public String welcome() {
        return "index";
    }

    @GetMapping("/show-payment")
    public String showPaymentPage(Model model) {
        model.addAttribute("billingInfo", new BillingInfo());
        return "form";
    }

    @PostMapping("/confirm-payment")
    public String confirmPayment(Model model, @ModelAttribute("billingInfo") BillingInfo billingInfo) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<BillingInfo> requestBody = new HttpEntity<>(billingInfo);
//
//        ResponseEntity<BillingInfo> responseEntity = restTemplate.postForEntity(CONFIRM_PAYMENT_URL, requestBody, BillingInfo.class);
//        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
//            String transactionCode = Objects.requireNonNull(responseEntity.getBody()).getTransactionCode();
//            log.info("result: {}", transactionCode);
//            model.addAttribute("transactionCode", transactionCode);
//        }
        String transactionCode = "123";
        model.addAttribute("transactionCode", transactionCode);
        billingInfo.setTransactionCode(transactionCode);
        log.info("billing info: {}", billingInfo);
        model.addAttribute("billingInfo", billingInfo);
        model.addAttribute("imgSrc", "http://localhost:8080/qr-code-gen/gen?barcodeText=" + billingInfo.getTransactionCode());
        //log.error("No transaction code received.");
        return "qr";
    }



}
