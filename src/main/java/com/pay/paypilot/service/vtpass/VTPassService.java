package com.pay.paypilot.service.vtpass;

import com.pay.paypilot.service.vtpass.pojos.request.BuyAirtimeRequest;
import com.pay.paypilot.service.vtpass.pojos.request.BuyDataPlanRequest;
import com.pay.paypilot.service.vtpass.pojos.response.data.*;

public interface VTPassService {
    DataServicesResponse getDataServices();
    DataPlansResponse getDataPlans(String dataType);
    BuyDataPlanResponse payDataPlan(BuyDataPlanRequest request);

    BuyAirtimeResponse buyAirtime(BuyAirtimeRequest buyAirtimeRequest);

    AirtimeServiceResponse getAirtimeServices();

}
