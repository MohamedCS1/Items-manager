package com.example.p_scanner.ui.addOrEditItems

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioGroup
import android.widget.Toast
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
    @SuppressLint("SetTextI18n")
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
            binding.tvId.text = item!!.id
            binding.etTitle.setText(item?.title?:"No Name")
            binding.etDescription.setText(item?.description?:"No Description")
            binding.etPrice.setText(item?.price?:"No Price")
            binding.buAddOrEditItem.text = "Edit"
            binding.buAddOrEditItem.background.setTint(Color.parseColor("#F0CC5F"))
            if (item!!.type == ItemType.PRODUCT){
                binding.radioButtonProduct.isChecked = true
            }else{
                binding.radioButtonService.isChecked = true
            }
        }else if (itemBarCode != "" && interactions == ItemInteractions.ADD)
        {
            binding.tvId.text = itemBarCode
        }

        binding.buBack.setOnClickListener {
            onBackPressed()
        }

        binding.buCancel.setOnClickListener {
            finish()
        }

        var itemType = ItemType.PRODUCT
        binding.radioGroupeItemType.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId)
                {
                    binding.radioButtonProduct.id -> itemType = ItemType.PRODUCT

                    binding.radioButtonService.id -> itemType = ItemType.SERVICE
                }
            }
        })

        binding.buAddOrEditItem.setOnClickListener {
            if (interactions == ItemInteractions.ADD)
            {
                productViewModel.itemLiveData.value = Item(binding.tvId.text.toString() ,binding.etTitle.text.toString() ,binding.etDescription.text.toString() ,binding.etPrice.text.toString() ,itemType)
                productViewModel.itemAddedLiveData.observeForever(object:Observer<Boolean>{
                    override fun onChanged(isAdded: Boolean?) {
                        if (isAdded == false)
                        {
                            Handler(Looper.getMainLooper()).post { Toast.makeText(baseContext ,"Enter All Information please" ,
                                Toast.LENGTH_SHORT).show()}
                        }
                        else
                        {
                            finish()
                        }
                    }
                })
            }else if (interactions == ItemInteractions.EDIT)
            {
                if (binding.tvId.text.toString().isNotBlank() && binding.etTitle.text.toString().isNotBlank() && binding.etDescription.text.toString().isNotBlank() && binding.etPrice.text.toString().isNotBlank())
                {
                    repository.updateItemById(binding.tvId.text.toString() ,binding.etTitle.text.toString() ,binding.etDescription.text.toString() ,binding.etPrice.text.toString() ,itemType)
                    finish()
                }
                else{
                    Handler(Looper.getMainLooper()).post { Toast.makeText(baseContext ,"Enter All Information please" ,
                        Toast.LENGTH_SHORT).show()}
                }

            }
        }
    }
}