package com.gmoi.directmessage.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TwoFactorResponse {
    private String secretKey;
    private String qrCodeUrl;
}
