package mrcs.mywallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        holder.binding.tvValue.text = record.value.toString()

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