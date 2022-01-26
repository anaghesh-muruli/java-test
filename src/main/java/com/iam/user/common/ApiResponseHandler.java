package com.iam.user.common;

import com.iam.user.dto.ApiResponse;

public class ApiResponseHandler {
    public static ApiResponse generateSuccessApiResponse(Object data, int statusCode) {
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("Success");
        apiResponse.setData(data);
        apiResponse.setStatus(statusCode);
        return apiResponse;
    }

    public static ApiResponse generateFailureApiResponse(String message, int statusCode) {
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage(message);
        apiResponse.setStatus(statusCode);
        return apiResponse;
    }
}
