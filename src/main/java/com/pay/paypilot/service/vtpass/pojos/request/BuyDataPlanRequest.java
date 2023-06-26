package com.pay.paypilot.service.vtpass.pojos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyDataPlanRequest {

    //Mandatory field it would be set at runtime from backend
    private String request_id; //https://www.vtpass.com/documentation/how-to-generate-request-id/

    @NotBlank(message = "serviceID is mandatory")
    @Schema(example = "mtn-data, airtel-data, etc as specified by VTPASS")
    private String serviceID;

    //Mandatory field it would be set at runtime from backend
    @Schema(example = "08012345678 (you the programmer/biz owner) i.e the phone number you wish to make the Subscription payment on")
    private String billersCode;

    @NotBlank(message = "variation_code is mandatory")
    @Schema(example = "mtn-10mb-100 i.e. the code of the variation (as specified in the GET VARIATIONS method as variation_code).")
    private String variation_code;

    @NotBlank(message = "amount is mandatory")
    @Schema(example = "100.00 i.e. the amount of the variation (as specified in the GET VARIATIONS endpoint as variation_amount) optional as variation_code is mandatory")
    private BigDecimal amount;

    //for test use this 08011111111 all other phone number would fail for sandbox mode
    @NotBlank(message = "phone is mandatory")
    @Schema(example = "for test use this 08011111111 - i.e the phone number of the customer or recipient of this service")
    private String phone;



}
