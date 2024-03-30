package com.meetmountain.meetmountainapp.dto;

//use this class to send back an object as JSON

import lombok.Data;
import lombok.NonNull;

@Data
public class PurchaseResponse {

    private final String orderTrackingNumber;

}
