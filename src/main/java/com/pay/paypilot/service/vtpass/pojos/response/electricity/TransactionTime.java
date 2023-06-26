package com.pay.paypilot.service.vtpass.pojos.response.electricity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionTime {
    private  String date;
    private String timezone_type;
    private String timezone;
}
