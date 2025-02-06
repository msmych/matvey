package uk.matvey.app.crypto

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class CryptoService {

    val secureRandom = SecureRandom()
    val pbkdf2Factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val base64Encoder = Base64.getEncoder()
    val base64Decoder = Base64.getDecoder()

    fun hashPassword(password: String): String {
        val salt = generateSalt()
        val spec = spec(password, salt)
        val hash = pbkdf2Factory.generateSecret(spec).encoded
        return "${base64Encoder.encodeToString(salt)}:${base64Encoder.encodeToString(hash)}"
    }

    fun verifyPassword(password: String, expected: String): Boolean {
        val (salt, expectedHash) = expected.split(":")
        val spec = spec(password, base64Decoder.decode(salt))
        val actualHash = pbkdf2Factory.generateSecret(spec).encoded
        return base64Encoder.encodeToString(actualHash) == expectedHash
    }

    private fun spec(password: String, salt: ByteArray) =
        PBEKeySpec(password.toCharArray(), salt, 65536, 128)

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        secureRandom.nextBytes(salt)
        return salt
    }
}