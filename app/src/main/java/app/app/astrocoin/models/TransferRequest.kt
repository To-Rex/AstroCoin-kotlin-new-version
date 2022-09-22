package app.app.astrocoin.models

class TransferRequest(
    var id: String,
    var wallet_from: String,
    var wallet_to: String,
    var fio: String,
    var amount: String,
    var title: String,
    var type: String,
    var comment: String,
    var status: String,
    var date: String,
    var timestamp: String,
    var datatransfer: String
)