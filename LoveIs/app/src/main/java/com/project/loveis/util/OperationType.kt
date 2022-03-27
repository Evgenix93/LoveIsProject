package com.project.loveis.util

import com.squareup.moshi.Json

enum class OperationType {
    @Json(name = "wallet_add")
    WalletAdd,
    @Json(name = "wallet_sub")
    WalletSub,
    @Json(name = "loveis_cashback")
    LoveIsCashback,
    @Json(name = "loveis_create")
    LoveIsCreate,
    @Json(name = "eventis_payment")
    EventIsPayment,
    @Json(name = "eventis_income")
    EventIsIncome,
    @Json(name = "subscription")
    Subscription
}