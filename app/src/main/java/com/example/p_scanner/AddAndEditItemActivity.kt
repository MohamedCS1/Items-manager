package com.example.p_scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.p_scanner.Database.ItemsDatabase
import com.example.p_scanner.Pojo.Item
import com.example.p_scanner.Pojo.ItemInteractions
import com.example.p_scanner.Pojo.ItemType
import com.example.p_scanner.ViewModels.ProductViewModel
import com.example.p_scanner.databinding.ActivityAddAndEditItemBinding

class AddAndEditItemActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddAndEditItemBinding
    lateinit var productViewModel: ProductViewModel
    lateinit var interactions: ItemInteractions
    var itemBarCode: String? = null
    var item:Item? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAndEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ProductViewModel(this ,this)

        itemBarCode = intent.extras?.getString("ItemBarCode")?:""

        interactions = (intent.extras?.get("Interaction")?:ItemInteractions.ADD) as ItemInteractions

        try {
            item = intent.extras?.get("Item") as Item
        }catch (ex:Exception){}

        if (item != null && interactions == ItemInteractions.EDIT)
        {
            Toast.makeText(this ,"In Item" ,Toast.LENGTH_SHORT).show()
            binding.etId.setText(itemBarCode)
            binding.etName.setText(item?.name?:"No Name")
            binding.etDescription.setText(item?.description?:"No Description")
            binding.etPrice.setText(item?.price?:"No Price")

        }else if (itemBarCode != "" && interactions == ItemInteractions.ADD)
        {
            binding.etId.setText(itemBarCode)
        }

        binding.buBack.setOnClickListener {
            onBackPressed()
        }

        binding.buCancel.setOnClickListener {
            finish()
        }

        var itemType = ItemType.PRODUCT
        binding.radioGroupeProductType.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId)
                {
                    binding.buProduct.id -> itemType = ItemType.PRODUCT

                    binding.buService.id -> itemType = ItemType.SERVICE
                }
            }
        })

        binding.buAddItem.setOnClickListener {
            productViewModel.setItemLiveData.value = Item(binding.etId.text.toString() ,binding.etName.text.toString() ,binding.etDescription.text.toString() ,binding.etPrice.text.toString() ,itemType)
        }
    }
}