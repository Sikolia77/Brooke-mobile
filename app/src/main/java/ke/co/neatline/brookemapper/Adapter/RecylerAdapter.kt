package ke.co.neatline.brookemapper.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ke.co.neatline.brookemapper.Params
import ke.co.neatline.brookemapper.PointHome
import ke.co.neatline.brookemapper.R

class RecylerAdapter (private val paramList:ArrayList<Params>, val context: Context) :
    RecyclerView.Adapter<RecylerAdapter.ParamViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_home, parent, false)
        return ParamViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: ParamViewHolder, position: Int) {
        val param = paramList[position]
        holder.imageView.setImageResource(param.image)
        holder.textView.text = param.name
    }

    override fun getItemCount(): Int {
        return paramList.size
    }

    class ParamViewHolder(itemView:View,val context: Context) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val options: LinearLayout = itemView.findViewById(R.id.options)
        val addnew: LinearLayout = itemView.findViewById(R.id.addnew)
        val update: LinearLayout = itemView.findViewById(R.id.update)
        val item: CardView = itemView.findViewById(R.id.item)

        init {
            item.setOnClickListener {
                if(options.visibility == View.GONE){
                    options.visibility = View.VISIBLE
                }else {
                    options.visibility = View.GONE
                }
            }

            addnew.setOnClickListener{
                options.visibility = View.GONE
                when(textView.text){
                    "Agrovets" -> mapAgrovets()
                    "Farriers" -> mapFarriers()
                    "Careclubs" -> mapCareclubs()
                    "Equineowners" -> mapEquineowners()
                    "CommunityGroups" -> mapCommunityGroups()
                    "Practitioners" -> mapPractitioners()
                    "Abattoirs" -> mapAbattoirs()
                    "Schools" -> mapSchools()
                    else -> {}
                }
            }

            update.setOnClickListener{
                options.visibility = View.GONE
                when(textView.text){
//                    "Customer Meters" -> updateCustomerMeters()
//                    "Water Pipes" -> updateWaterPipes()
//                    "Master Meters" -> updateMasterMeters()
//                    "PRV" -> updatePRV()
//                    "Water Tanks" -> updateTanks()
//                    "Valves" -> updateValves()
//                    "Sewer Lines" -> updateSewerlines()
//                    "Manholes" -> updateManHoles()
//                    "Point Project" -> mapPointProjects()
//                    "Line Project" -> mapLineProjects()
                    else -> {}
                }
            }
        }

        private fun mapAgrovets() {
            val intent = Intent(context, PointHome::class.java)
            intent.putExtra("isUpdating", false)
            intent.putExtra("MappedItem", "Agrovets")
            context.startActivity(intent)
        }

        private fun mapFarriers() {
            val intent = Intent(context, PointHome::class.java)
            intent.putExtra("MappedItem", "Farriers")
            intent.putExtra("isUpdating", false)
            context.startActivity(intent)
        }

        private fun mapCareclubs() {
            val intent = Intent(context, PointHome::class.java)
            intent.putExtra("MappedItem", "Careclubs")
            intent.putExtra("isUpdating", false)
            context.startActivity(intent)
        }

        private fun mapEquineowners() {
            val intent = Intent(context, PointHome::class.java)
            intent.putExtra("MappedItem", "Equine Owners")
            intent.putExtra("isUpdating", false)
            context.startActivity(intent)
        }

        private fun mapCommunityGroups() {
            val intent = Intent(context, PointHome::class.java)
            intent.putExtra("MappedItem", "Community Groups")
            intent.putExtra("isUpdating", false)
            context.startActivity(intent)
        }

        private fun mapPractitioners() {
            val intent = Intent(context, PointHome::class.java)
            intent.putExtra("MappedItem", "Practitioners")
            intent.putExtra("isUpdating", false)
            context.startActivity(intent)
        }

        private fun mapAbattoirs() {
            val intent = Intent(context, PointHome::class.java)
            intent.putExtra("MappedItem", "Abattoirs")
            intent.putExtra("isUpdating", false)
            context.startActivity(intent)
        }

        private fun mapSchools() {
            val intent = Intent(context, PointHome::class.java)
            intent.putExtra("MappedItem", "Schools")
            intent.putExtra("isUpdating", false)
            context.startActivity(intent)
        }
    }
}