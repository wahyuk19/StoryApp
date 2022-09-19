package com.dicoding.storyapp.ui.utils

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class TripleDES(strKey: String) {

    private var secretKey: SecretKey? = null
    private val key = strKey.decodeHex()

    private fun generateKey(): SecretKey {
        return SecretKeySpec(key, "DESede")
    }

    @Throws(Exception::class)
    fun encryptPKCS5(plaintext: String, iv: String? = "0000000000000000"): ByteArray {
        Security.addProvider(BouncyCastleProvider())
        secretKey = generateKey()
        val cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding")
        val ivStr = iv?.decodeHex()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(ivStr))
        return cipher.doFinal(plaintext.decodeHex())
    }

    @Throws(Exception::class)
    fun String.decodeHex(): ByteArray {

        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }


}
