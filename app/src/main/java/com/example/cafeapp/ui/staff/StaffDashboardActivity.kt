package com.example.cafeapp.ui.staff

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.R

class StaffDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_dashboard)

        findViewById<Button>(R.id.btnNewOrder).setOnClickListener {
            showTableInputDialog()
        }

        findViewById<Button>(R.id.btnActiveOrders).setOnClickListener {
            startActivity(Intent(this, ActiveOrdersActivity::class.java))
        }

        findViewById<Button>(R.id.btnTableStatus).setOnClickListener {
            startActivity(Intent(this, StaffTableStatusActivity::class.java))
        }
    }

    private fun showTableInputDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.hint = "Table Number"

        AlertDialog.Builder(this)
            .setTitle("Start New Order")
            .setMessage("Enter the customer's table number:")
            .setView(input)
            .setPositiveButton("Start Order") { _, _ ->
                val tableNum = input.text.toString()
                if (tableNum.isNotBlank()) {
                    // Pass the table number to the Menu screen!
                    val intent = Intent(this, StaffActivity::class.java)
                    intent.putExtra("EXTRA_TABLE_NUMBER", tableNum)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Table number is required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}