package mrcs.mywallet.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import mrcs.mywallet.R
import mrcs.mywallet.databinding.RecordItemBinding
import mrcs.mywallet.domain.Record
import kotlin.Int

class RecordsAdapter(records: List<Record>) :
    RecyclerView.Adapter<RecordsAdapter.ViewHolder>() {
    private val records: List<Record>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RecordItemBinding = RecordItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record: Record = records[position]

        holder.binding.tvDate.text = record.date
        holder.binding.tvDescription.text = record.description

        if(record.type == "in"){
            holder.binding.tvValue.setTextColor(Color.parseColor("#009900"))
        }
        val parsedRecordValue = String.format("%.2f", record.value.toFloat()/100)
        holder.binding.tvValue.text = parsedRecordValue

    }

    override fun getItemCount(): Int {
        return records.size
    }

    class ViewHolder(binding: RecordItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding: RecordItemBinding

        init {
            this.binding = binding
        }
    }

    init {
        this.records = records
    }
}