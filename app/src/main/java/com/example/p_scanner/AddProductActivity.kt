package com.example.p_scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import com.example.p_scanner.Pojo.Item
import com.example.p_scanner.Pojo.ItemType
import com.example.p_scanner.ViewModels.ProductViewModel
import com.example.p_scanner.databinding.ActivityAddItemBinding

class AddProductActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddItemBinding
    lateinit var productViewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        productViewModel = ProductViewModel(this ,this)

        val productID = intent.extras?.getString("ProductID")?:""

        if (productID != "")
        {
            binding.tvId.setText(productID)
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
            productViewModel.setItemLiveData.value = Item(binding.tvId.text.toString() ,binding.tvName.text.toString() ,binding.tvDescription.text.toString() ,binding.tvPrice.text.toString() ,itemType)
        }
    }
}