package com.shoppi9driver.app.utility;

public class Config {

    public static final String BASE_URL = "http://shoppi9.onlinecrm.co.in/api/";

    public static final String LOGIN_URL = BASE_URL + "index.php?p=login";
    public static final String REGISTER_URL = BASE_URL + "index.php?p=signup";

    public static final String DASHBOARD_URL = BASE_URL + "rider/dashboard";

    public static final String ACCEPT_URL = BASE_URL + "accept";

    public static final String REJECT_URL = BASE_URL + "decline";

    public static final String SETTING_URL = BASE_URL + "rider/settings";

    public static final String TODAYSORDER_URL = BASE_URL + "todaysorder";

    public static final String STATUS_URL = BASE_URL + "status";

    public static final String ALLORDER_URL = BASE_URL + "allorders";

    public static final String CHANGEpASS_URL = BASE_URL + "rider/password/submit";

    public static final String ORDERDETAIL_URL = BASE_URL + "orderview?order_id=";

    public static final String ACCEPTEDORDER_URL = BASE_URL + "orders/accepted";

    public static final String CompleteORDER_URL = BASE_URL + "complete";
}
