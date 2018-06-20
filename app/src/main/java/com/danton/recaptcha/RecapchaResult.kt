package com.danton.recaptcha

import com.google.gson.annotations.SerializedName

class RecapchaResult {

    data class RecapchaResult(
            val success: String,
            @SerializedName("error-codes")
            val list_errors: List<String>
    )


}