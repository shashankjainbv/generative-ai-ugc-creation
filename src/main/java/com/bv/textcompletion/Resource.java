package com.bv.textcompletion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/data")
public class Resource {

    @Autowired
    ResourceService resourceService;

    @ResponseBody
    @RequestMapping("/reviewtip")
    public GPTResponse getReviewTips(@RequestParam("passkey") String passkey, @RequestParam("productid") String productId) throws IOException {

        if (passkey.isEmpty()) {
            return new GPTResponse("No passKey specified.", "", 403);
        }

        if (productId.isEmpty()) {
            return new GPTResponse("Illegal empty query param: productid", "", 403);
        }

        return resourceService.getReviewTipsBased(passkey, productId);

    }

    @ResponseBody
    @RequestMapping("/questiontip")
    public GPTResponse getQuestionTips(@RequestParam("passkey") String passkey, @RequestParam("productid") String productId) throws IOException {

        if (passkey.isEmpty()) {
            return new GPTResponse("No passKey specified.", "", 403);
        }

        if (productId.isEmpty()) {
            return new GPTResponse("Illegal empty query param: productid", "", 403);
        }

        return resourceService.getQuestionTipsBased(passkey, productId);

    }

}
