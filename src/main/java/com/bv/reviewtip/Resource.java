package com.bv.reviewtip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping ("/data")
public class Resource {

    @Autowired
    ResourceService resourceService;

    @ResponseBody
    @RequestMapping("/reviewtip")
    public ReviewTipResponse getReviewTips(@RequestParam ("passkey") String passkey, @RequestParam("productid") String productId) throws IOException {

        if (passkey.isEmpty()) {
            return new ReviewTipResponse("No passKey specified.", "", 403);
        }

        if (productId.isEmpty()) {
            return new ReviewTipResponse("Illegal empty query param: productid", "", 403);
        }

        return resourceService.getReviewTipsBased(passkey, productId);

    }

}
