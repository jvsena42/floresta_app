package com.github.jvsena42.floresta.domain.bitcoin

import com.github.jvsena42.floresta.domain.model.RequiredInitialWalletData

interface WalletRepository {
    fun doesWalletExist(): Boolean
    fun getInitialWalletData() : Result<RequiredInitialWalletData>
    fun saveWallet(path: String, descriptor: String, changeDescriptor: String)
    fun saveMnemonic(mnemonic: String)
    fun getMnemonic(): Result<String>
}