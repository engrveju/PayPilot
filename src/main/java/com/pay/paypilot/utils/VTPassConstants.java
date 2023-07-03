package com.pay.paypilot.utils;

import org.springframework.beans.factory.annotation.Value;



public class VTPassConstants {
    @Value("${biller.default.number}")
    public  static String BILLER_CODE;
    public final static String PAY_BILL_SERVICE = "https://sandbox.vtpass.com/api/service-variations?serviceID=";
    public final static String PAY_BILL = "https://sandbox.vtpass.com/api/pay";
    public final static String ALL_DATA_SERVICES = "https://sandbox.vtpass.com/api/services?identifier=data";
    public final static String ALL_ELECTRICITY_SERVICES = "https://sandbox.vtpass.com/api/services?identifier=electricity-bill";
    public final static String VERIFY_MERCHANT = "https://sandbox.vtpass.com/api/merchant-verify";
    public final static String SERVICE_TRANSACTION_STATUS = "https://sandbox.vtpass.com/api/requery";

    public final static String NETWORK_DATA_PLANS = "https://sandbox.vtpass.com/api/service-variations?serviceID=";

    public final static String All_AIRTIME_SERVICES ="https://sandbox.vtpass.com/api/services?identifier=airtime";
 
}
