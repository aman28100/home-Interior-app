import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val name: String,
    val price: Int,
    val imageUrl: Any // Can be a String (URL) or Int (Resource ID)
) : Parcelable {

    // Constructor for Parcelable
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "", // Read name
        parcel.readInt(), // Read price
        // Handle imageUrl properly based on type (String or Int)
        parcel.readValue(ClassLoader.getSystemClassLoader())?.let {
            if (it is String) it else it as? Int ?: 0 // Default to 0 if not a valid resource ID
        } ?: "" // Default to an empty string if imageUrl is null
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name) // Write name
        parcel.writeInt(price) // Write price
        parcel.writeValue(imageUrl) // Write imageUrl (either String or Int)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<CartItem> {
            override fun createFromParcel(parcel: Parcel): CartItem {
                return CartItem(parcel)
            }

            override fun newArray(size: Int): Array<CartItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}
