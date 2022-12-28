package com.example.p_scanner

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemInteractions
import com.example.p_scanner.pojo.ItemType
import com.example.p_scanner.viewmodels.ProductViewModel
import com.example.p_scanner.databinding.ActivityAddAndEditItemBinding
import com.example.p_scanner.repository.Repository

class AddAndEditItemActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddAndEditItemBinding
    lateinit var productViewModel: ProductViewModel
    lateinit var interactions: ItemInteractions
    lateinit var repository: Repository
    var itemBarCode: String? = null
    var item:Item? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAndEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ProductViewModel(this)

        repository = Repository(ItemsDatabase.getDatabase(this).itemDAO())

        itemBarCode = intent.extras?.getString("ItemBarCode")?:""

        interactions = (intent.extras?.get("Interaction")?:ItemInteractions.ADD) as ItemInteractions


        try {
            item = intent.extras?.get("Item") as Item
        }catch (ex:Exception){}

        if (item != null && interactions == ItemInteractions.EDIT)
        {
            binding.etId.setText(item!!.id)
            binding.etTitle.setText(item?.title?:"No Name")
            binding.etDescription.setText(item?.description?:"No Description")
            binding.etPrice.setText(item?.price?:"No Price")
            binding.buAddOrEditItem.text = "Edit"
            binding.buAddOrEditItem.background.setTint(Color.parseColor("#F0CC5F"))
//            binding.radioGroupeProductType
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

        binding.buAddOrEditItem.setOnClickListener {
            if (interactions == ItemInteractions.ADD)
            {
                productViewModel.itemLiveData.value = Item(binding.etId.text.toString() ,binding.etTitle.text.toString() ,binding.etDescription.text.toString() ,binding.etPrice.text.toString() ,itemType)
                finish()
            }else if (interactions == ItemInteractions.EDIT)
            {
                repository.updateItemById(binding.etId.text.toString() ,binding.etTitle.text.toString() ,binding.etDescription.text.toString() ,binding.etPrice.text.toString())
                finish()
            }
        }
    }
}