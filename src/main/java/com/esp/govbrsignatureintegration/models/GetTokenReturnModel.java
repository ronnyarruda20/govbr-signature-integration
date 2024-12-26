package com.esp.govbrsignatureintegration.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model de retorno da resquest de {@link com.esp.govbrsignatureintegration.services.GetTokenService}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTokenReturnModel {
    private String access_token;
    private String token_type;
    private int expires_in;
}
