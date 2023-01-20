package com.example.p_scanner.ui.listItems

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.R
import com.example.p_scanner.adapters.ItemsAdapter
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.interfaces.ItemClickListener
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemInteractions
import com.example.p_scanner.repository.Repository
import com.example.p_scanner.ui.addOrEditItems.AddAndEditItemActivity
import com.example.p_scanner.viewmodels.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ListItemsFragment : Fragment() {
    lateinit var adapter: ItemsAdapter
    lateinit var repository: Repository
    lateinit var productViewModel:ProductViewModel
    lateinit var searchView:SearchView
    lateinit var arrayOfItems:ArrayList<Item>
    lateinit var textViewNoItemFound:TextView
    lateinit var buttonMenu:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ProductViewModel(requireContext())

        adapter = ItemsAdapter()
        repository = Repository(ItemsDatabase.getDatabase(requireContext()).itemDAO())



        productViewModel.listItemsLiveData.observe(this,object :Observer<List<Item>>{
            override fun onChanged(listItems: List<Item>?) {
                if ((listItems?.size ?: 0) > 0)
                {
                    textViewNoItemFound.visibility = View.INVISIBLE
                }
                adapter.setList(listItems!! as ArrayList<Item>)
                arrayOfItems = listItems as ArrayList<Item>
            }
        })

        adapter.onItemClick(object :ItemClickListener{
            override fun onClick(item: Item) {
                val bottomSheetDialog = BottomSheetDialog(requireContext() ,
                    R.style.TransparentBackgroundDialog
                )
                bottomSheetDialog.setContentView(R.layout.bottom_dialog)
                bottomSheetDialog.show()
                val buEdit = bottomSheetDialog.findViewById<LinearLayout>(R.id.bu_edit)
                val buDelete = bottomSheetDialog.findViewById<LinearLayout>(R.id.bu_remove)
                buEdit?.setOnClickListener {
                    val intent = Intent(requireContext() , AddAndEditItemActivity::class.java)
                    intent.putExtra("Item" ,item)
                    intent.putExtra("Interaction" ,ItemInteractions.EDIT)
                    startActivity(intent)
                    bottomSheetDialog.hide()
                }
                buDelete?.setOnClickListener {
                    AlertDialog.Builder(requireContext(), R.style.MyDialogTheme).setMessage("Are you sure to delete this item ?").setPositiveButton("Yes" ,object:DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            repository.deleteItemById(item.id)
                            bottomSheetDialog.hide()
                        }
                    }).setNegativeButton("No",object:DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {

                        }
                    }).show()
                }
            }
        })



    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_items, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_items)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        textViewNoItemFound = view.findViewById(R.id.TextViewNoItemFound)
        searchView = view.findViewById(R.id.searchView)
        buttonMenu = view.findViewById(R.id.buMenu)
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterBy(newText!! ,arrayOfItems)
                return true
            }
        })
        searchView.queryHint = "Search by title or price"
        val showPopUp = PopupMenu(requireContext() ,buttonMenu)
        showPopUp.menu.add(Menu.NONE, 0, 0, "Export database as CSV file")
        showPopUp.menu.add(Menu.NONE, 1, 1, "Import database as CSV file")
        buttonMenu.setOnClickListener {
            showPopUp.show()
        }
        return view
    }

}