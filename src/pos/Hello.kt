fun main() {
    val receipt = getReceipt()
    println(receipt)
    val result = when (receipt == expectedReceipt) {
        true -> "正确 ✅"
        false -> "错误 ❌"
    }
    println("\n结果：${result}")
}

interface Promotion {
    var barcodes: List<String>
}

data class BuyTwoGetOneFreePromotion(override var barcodes: List<String>) : Promotion {

}

fun loadPromotions(): List<Promotion> =
    listOf(BuyTwoGetOneFreePromotion(listOf("ITEM000000", "ITEM000001", "ITEM000005")))

data class Item(val barcode: String, val name: String, val unit: String, val price: Double) {

}

fun loadAllItems(): List<Item> {
    return listOf(
        Item("ITEM000000", "可口可乐", "瓶", 3.00),
        Item("ITEM000001", "雪碧", "瓶", 3.00),
        Item("ITEM000002", "苹果", "斤", 5.50),
        Item("ITEM000003", "荔枝", "斤", 15.00),
        Item("ITEM000004", "电池", "个", 2.00),
        Item("ITEM000005", "方便面", "袋", 4.50)
    )
}

val purchasedBarcodes = listOf(
    "ITEM000001",
    "ITEM000001",
    "ITEM000001",
    "ITEM000001",
    "ITEM000001",
    "ITEM000003-2",
    "ITEM000005",
    "ITEM000005",
    "ITEM000005"
)

const val expectedReceipt = """
***<没钱赚商店>收据***
名称：雪碧，数量：5瓶，单价：3.00(元)，小计：12.0(元)
名称：荔枝，数量：2斤，单价：15.00(元)，小计：30.0(元)
名称：方便面，数量：3袋，单价：4.50(元)，小计：9.0(元)
----------------------
总计：51.00(元)
节省：7.50(元)
**********************
"""

fun getReceipt(): String {
    val sb = StringBuilder("\n***<没钱赚商店>收据***\n")
    val purchasedBarcodesMap = processPurchasedBarcodes(purchasedBarcodes)
    val allItemsMap = loadAllItems().map { it.barcode to it }.toMap()
    var orginPriceSum = 0.00
    var promotedPriceSum = 0.00

    purchasedBarcodesMap.forEach {
        val barcode = it.key
        var number = it.value
        val item = allItemsMap.get(barcode)
        val itemPrice = item!!.price
        val originPrice = itemPrice * number
        var promotedPrice = originPrice

        for (promotion in loadPromotions()) {
            if (promotion.barcodes.contains(barcode)) {
                promotedPrice = (number - number / 3) * itemPrice
            }
        }

        orginPriceSum += originPrice
        promotedPriceSum += promotedPrice

        sb.append(generatorItemReceipt(item, promotedPrice, number))
    }
    sb.append("----------------------\n")
    sb.append(String.format("总计：%.2f(元)\n", promotedPriceSum))
    sb.append(String.format("节省：%.2f(元)\n", orginPriceSum - promotedPriceSum))
    sb.append("**********************\n")
    return sb.toString()
}

fun generatorItemReceipt(item: Item, promotedPrice: Double, number: Number): String {
    val itemReceiptFormat = "名称：%s，数量：%d%s，单价：%.2f(元)，小计：%.1f(元)\n"
    return String.format(itemReceiptFormat, item.name, number, item.unit, item.price, promotedPrice)
}

fun processPurchasedBarcodes(purchasedBarcodes: List<String>): MutableMap<String, Int> {
    val purchasedBarcodesMap = mutableMapOf<String, Int>()
    purchasedBarcodes.forEach {
        val barcode = if (it.contains("-")) it.substring(0, it.indexOf("-")) else it
        val number = if (it.contains("-")) it.substring(it.indexOf("-") + 1).toInt() else 1

        if (purchasedBarcodesMap.contains(barcode)) {
            purchasedBarcodesMap.put(barcode, purchasedBarcodesMap.get(barcode)!!.plus(number))
        } else {
            purchasedBarcodesMap.put(barcode, number)
        }
    }
    return purchasedBarcodesMap
}
