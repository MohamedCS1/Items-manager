package com.example.p_scanner

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemInteractions
import com.example.p_scanner.pojo.ItemType
import com.example.p_scanner.viewmodels.ProductViewModel
import com.example.p_scanner.databinding.ActivityAddAndEditItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

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

        productViewModel = ProductViewModel(this)

        itemBarCode = intent.extras?.getString("ItemBarCode")?:""

        interactions = (intent.extras?.get("Interaction")?:ItemInteractions.ADD) as ItemInteractions

        productViewModel.itemAddedLiveData.observeForever(object: Observer<String> {
            override fun onChanged(t: String?) {
                finish()
            }
        })


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
            productViewModel.itemLiveData.value = Item(binding.etId.text.toString() ,binding.etName.text.toString() ,binding.etDescription.text.toString() ,binding.etPrice.text.toString() ,itemType)
        }
    }
}