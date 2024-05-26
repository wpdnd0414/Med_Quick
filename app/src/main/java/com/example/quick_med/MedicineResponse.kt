data class MedicineResponse(
    val items: List<MedicineItem>
)

data class MedicineItem(
    val itemName: String,
    val itemImage: String,
    val efcyQesitm: String
)