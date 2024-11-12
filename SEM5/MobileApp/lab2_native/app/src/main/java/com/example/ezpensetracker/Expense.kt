import android.os.Parcel
import android.os.Parcelable

data class Expense(
    val id: Int,
    var amount: Double,
    var category: String,
    var date: String,
    var type: String,
    var notes: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString() ?: "", // if null then empty string
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeDouble(amount)
        parcel.writeString(category)
        parcel.writeString(date)
        parcel.writeString(type)
        parcel.writeString(notes)
    }

    override fun describeContents(): Int { // nr of special objects
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Expense> {
        override fun createFromParcel(parcel: Parcel): Expense { // create an instance of expense form parcel
            return Expense(parcel)
        }

        override fun newArray(size: Int): Array<Expense?> { // array of expense objects of specified size
            return arrayOfNulls(size)
        }
    }
}
