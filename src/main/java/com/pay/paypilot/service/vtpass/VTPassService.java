package com.pay.paypilot.service.vtpass;

import com.pay.paypilot.service.vtpass.pojos.request.BuyAirtimeRequest;
import com.pay.paypilot.service.vtpass.pojos.request.BuyDataPlanRequest;
import com.pay.paypilot.service.vtpass.pojos.request.BuyElectricityRequest;
import com.pay.paypilot.service.vtpass.pojos.request.VerifyMerchantRequest;
import com.pay.paypilot.service.vtpass.pojos.response.data.*;
import com.pay.paypilot.service.vtpass.pojos.response.electricity.BuyElectricityResponse;
import com.pay.paypilot.service.vtpass.pojos.response.electricity.VerifyMerchantResponse;

public interface VTPassService {
    DataServicesResponse getDataServices();
    DataPlansResponse getDataPlans(String dataType);
    BuyDataPlanResponse payDataPlan(BuyDataPlanRequest request);

    BuyAirtimeResponse buyAirtime(BuyAirtimeRequest buyAirtimeRequest);

    AirtimeServiceResponse getAirtimeServices();

    DataServicesResponse getAllElectricityService();

    VerifyMerchantResponse verifyElectricityMeter(VerifyMerchantRequest merchantRequest);

    BuyElectricityResponse buyElectricity(BuyElectricityRequest electricityRequest);


}
