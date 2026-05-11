package com.example.authentication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EmailSender {

    suspend fun sendVerificationEmail(
        toEmail: String,
        otpCode: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        Result.failure(
            UnsupportedOperationException(
                "EmailSender không hỗ trợ trên commonMain. Hãy triển khai theo từng platform."
            )
        )
    }
}
