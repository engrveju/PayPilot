package com.pay.paypilot.service.vtpass.pojos.response.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirtimeServices {
    public  String ServiceID;
    public String name;
    public BigDecimal amount;
    public String image;

}
