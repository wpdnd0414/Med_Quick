import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.quick_med.Medicine
import com.example.quick_med.R
import com.squareup.picasso.Picasso

class MedicineAdapter(private val context: Context, private val medicines: List<Medicine>) : BaseAdapter() {

    override fun getCount(): Int {
        return medicines.size
    }

    override fun getItem(position: Int): Any {
        return medicines[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_medicine, parent, false)
        val medicine = medicines[position]

        val nameTextView = view.findViewById<TextView>(R.id.medicine_name)
        val descriptionTextView = view.findViewById<TextView>(R.id.medicine_description)
        val imageView = view.findViewById<ImageView>(R.id.medicine_image)

        nameTextView.text = medicine.name
        descriptionTextView.text = medicine.description

        if (medicine.imageUrl != null) {
            Picasso.get().load(medicine.imageUrl).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.placeholder_image)  // 대체 이미지 리소스 ID 사용
        }

        return view
    }
}
