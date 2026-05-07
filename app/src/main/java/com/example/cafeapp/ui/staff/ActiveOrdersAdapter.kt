package com.example.cafeapp.ui.staff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.data.remote.dto.ProcessOrderResponse

class ActiveOrdersAdapter(private val onPayClicked: (ProcessOrderResponse) -> Unit) :
    ListAdapter<ProcessOrderResponse, ActiveOrdersAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_active_order, parent, false)
        return OrderViewHolder(view, onPayClicked)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderViewHolder(itemView: View, private val onPayClicked: (ProcessOrderResponse) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvTableNumber: TextView = itemView.findViewById(R.id.tvTableNumber)
        private val tvOrderType: TextView = itemView.findViewById(R.id.tvOrderType)
        private val tvTotalPrice: TextView = itemView.findViewById(R.id.tvTotalPrice)
        private val btnProcessPayment: Button = itemView.findViewById(R.id.btnProcessPayment)

        fun bind(order: ProcessOrderResponse) {
            tvTableNumber.text = "Table ${order.tableInformation?.table_number ?: "?"}"
            tvOrderType.text = order.order_type
            tvTotalPrice.text = "Rp ${order.total_price}"

            // If it's already paid, hide the button
            if (order.payment?.status == "Paid") {
                btnProcessPayment.visibility = View.GONE
            } else {
                btnProcessPayment.visibility = View.VISIBLE
                btnProcessPayment.setOnClickListener { onPayClicked(order) }
            }
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<ProcessOrderResponse>() {
        override fun areItemsTheSame(oldItem: ProcessOrderResponse, newItem: ProcessOrderResponse) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ProcessOrderResponse, newItem: ProcessOrderResponse) = oldItem == newItem
    }
}