package com.github.jvsena42.floresta.domain.bitcoin

import android.content.SharedPreferences
import android.util.Log
import com.github.jvsena42.floresta.domain.model.Constants.PERSISTENCE_VERSION
import com.github.jvsena42.floresta.domain.model.PreferenceKeys
import com.github.jvsena42.floresta.domain.model.RequiredInitialWalletData

class WalletRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : WalletRepository {
    override fun doesWalletExist(): Boolean {
        val isWalletInitialized = sharedPreferences.getBoolean(
            "${PreferenceKeys.IS_WALLET_INITIALIZED}$PERSISTENCE_VERSION",
            false
        )
        return isWalletInitialized
    }

    override fun getInitialWalletData(): Result<RequiredInitialWalletData> {
        val descriptor: String = sharedPreferences.getString(
            PreferenceKeys.DESCRIPTOR.name,
            null
        ) ?: return Result.failure(Exception("null descriptor"))

        val changeDescriptor: String =
            sharedPreferences.getString(
                PreferenceKeys.CHANGE_DESCRIPTOR.name,
                null
            ) ?: return Result.failure(Exception("null changeDescriptor"))

        return Result.success(
            RequiredInitialWalletData(
                descriptor = descriptor,
                changeDescriptor = changeDescriptor
            )
        )
    }

    override fun saveWallet(
        path: String,
        descriptor: String,
        changeDescriptor: String
    ) {
        Log.d(
            TAG,
            "Saved wallet: path -> $path, descriptor -> $descriptor, change descriptor -> $changeDescriptor"
        )

        sharedPreferences.edit().apply {
            putBoolean("${PreferenceKeys.IS_WALLET_INITIALIZED}$PERSISTENCE_VERSION", true)
            putString(PreferenceKeys.PATH.name, path)
            putString(PreferenceKeys.DESCRIPTOR.name, descriptor)
            putString(PreferenceKeys.CHANGE_DESCRIPTOR.name, changeDescriptor)
        }.apply()
    }

    override fun saveMnemonic(mnemonic: String) {
        Log.d(TAG, "saveMnemonic: $mnemonic")

        sharedPreferences.edit().run {
            putString(PreferenceKeys.MNEMONIC.name, mnemonic)
            apply()
        }
    }

    override fun getMnemonic(): Result<String> {
        val seedPhrase =  sharedPreferences.getString(PreferenceKeys.MNEMONIC.name, null) ?: return Result.failure(
            Exception("No seed phrase saved"))

        return Result.success(seedPhrase)
    }

    private companion object {
        const val TAG = "WalletRepositoryImpl"
    }
}