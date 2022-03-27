package com.project.loveis.repositories

import android.util.Log
import com.project.loveis.models.OperationsWrapper
import com.project.loveis.models.Wallet
import com.project.loveis.singletones.Network
import retrofit2.Response

class WalletRepository {
  suspend  fun addMoney(amount: Int): Response<Any>? {
     return try {
         Network.walletApi.addMoney(Wallet(null, amount, null))
     }catch (e: Throwable){
         Log.e("MyDebug", "addMoney error = ${e.message}")
         null
     }
    }

    suspend fun getMoney(amount: Int, card: String): Response<Any>?{
      return try{
          Network.walletApi.getMoney(Wallet(null, amount, card))
      }catch (e: Throwable){
          Log.e("MyDebug", "addMoney error = ${e.message}")
          null
      }
    }

    suspend fun getOperations(): Response<OperationsWrapper>? {
        return try {
            Network.walletApi.getOperations(1, 20)
        }catch (e: Throwable){
            Log.e("MyDebug", "addMoney error = ${e.message}")
            null
        }
    }
}