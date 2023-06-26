package com.pay.paypilot.service.vtpass.pojos.response.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransactionResponseViewModel {
    private int count;
    private int totalPage;
    private List<TransactionResponse> list;
}
