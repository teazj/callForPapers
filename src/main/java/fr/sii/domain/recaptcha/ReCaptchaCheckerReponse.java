package fr.sii.domain.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by tmaugin on 22/05/2015.
 * SII
 */
public class ReCaptchaCheckerReponse {
    @JsonProperty
    private Boolean success;

    @JsonProperty("error-codes")
    private List<String> errorCodes;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}