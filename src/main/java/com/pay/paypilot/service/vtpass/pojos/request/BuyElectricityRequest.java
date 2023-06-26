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
public class BuyElectricityRequest {

    @NotBlank
    @Schema
    private String request_id;
    @NotBlank()
    @Schema()
    private String serviceID;
    @NotBlank
    @Schema
    private String billersCode;
    @NotBlank
    @Schema
    private String variation_code;
    @NotBlank
    @Schema
    private BigDecimal amount;
    @NotBlank
    @Schema
    private String phone;
}
