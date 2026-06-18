package com.quocanhit.moneymanagement.constant;

public class EndpointConst {
    public static final String BASE_URL_ACTIVATION = "http://localhost:8080/api/v1/active?token=";

    public static final String BASE_URL_ENDPOINT = "/api/v1";

    // Endpoint cho Authentication
    public static final String REGISTER = BASE_URL_ENDPOINT + "/register";
    public static final String ACTIVATE_TOKEN = BASE_URL_ENDPOINT + "/active";
    public static final String LOGIN = BASE_URL_ENDPOINT + "/login";

    // Endpoint cho Profile
    public static final String PROFILE = BASE_URL_ENDPOINT + "/profile";
    public static final String PROFILE_BY_ID = BASE_URL_ENDPOINT + "/profile/{id}";

    // Endpoint cho Category
    public static final String CATEGORY = BASE_URL_ENDPOINT + "/category";
    public static final String CATEGORIES = BASE_URL_ENDPOINT + "/categories";
}
