package com.example.p_scanner.ui.listItems

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.R
import com.example.p_scanner.Utils.RequestCodes
import com.example.p_scanner.adapters.ItemsAdapter
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.interfaces.ItemClickListener
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemInteractions
import com.example.p_scanner.pojo.ItemType
import com.example.p_scanner.repository.Repository
import com.example.p_scanner.ui.addOrEditItems.AddAndEditItemActivity
import com.example.p_scanner.viewmodels.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter


class ListItemsFragment : Fragment()  {
    lateinit var adapter: ItemsAdapter

    lateinit var repository: Repository
    lateinit var productViewModel:ProductViewModel
    lateinit var searchView:SearchView
    var arrayOfItems:ArrayList<Item>? = null
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
                if(arrayOfItems != null)
                {
                    adapter.filterBy(newText!! , arrayOfItems!!)
                }
                return true
            }
        })

        searchView.queryHint = "Search by title or price"
        val popUpMenu = PopupMenu(requireContext() ,buttonMenu)
        popUpMenu.menu.add(Menu.NONE, 0, 0, "Export database as CSV file")
        popUpMenu.menu.add(Menu.NONE, 1, 1, "Import database as CSV file")

        buttonMenu.setOnClickListener {
            popUpMenu.show()
        }

        popUpMenu.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if (item!!.itemId == 0)
                {
                    if (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        exportDatabaseAsCSVFile()
                    }
                    else
                    {
                        checkStoragePermission()
                    }
                    Toast.makeText(requireContext() ,"Export" ,Toast.LENGTH_SHORT).show()
                }
                else
                {
                    importDatabaseAsCSVFile()
                    Toast.makeText(requireContext() ,"Import" ,Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })
        return view
    }

    fun exportDatabaseAsCSVFile()
    {
        try {
            if (arrayOfItems?.size != 0)
            {
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val fileName = "i_manager_database.csv"
                val file = File("$path/$fileName")
                if (!file.exists()) {
                    file.createNewFile()
                }

                val outPutFile = FileWriter(file)
                val writer = CSVWriter(outPutFile)

                val header = arrayOf("id" ,"title" ,"description" ,"price" ,"type")
                writer.writeNext(header)

                for (item in arrayOfItems!!)
                {
                    val currentArray = arrayOf(item.id ,item.title ,item.description ,item.price ,item.type.name)
                    writer.writeNext(currentArray)
                }
                writer.close()
                NotificationsUtils(requireContext()).displayNotification()
            }
            else
            {
                Toast.makeText(requireContext() ,"No Item found to export" ,Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun intentToPickFile()
    {
        val intent =  Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.type = "*/*";
        startActivityForResult(Intent.createChooser(intent, "Open CSV"),RequestCodes.geCsvFile)
    }

    fun dialogIntegrateOrDeleteData(uri: Uri)
    {
        AlertDialog.Builder(requireContext(), R.style.MyDialogTheme).setMessage("Want to delete old data or integrate ?").setPositiveButton("Integrate" ,object:DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                readCSVFileAndAddItemsToDatabase(uri ,"Integrate")
            }
        }).setNegativeButton("Delete",object:DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                readCSVFileAndAddItemsToDatabase(uri ,"Delete")
            }
        }).show()
    }
    fun readCSVFileAndAddItemsToDatabase(uri: Uri ,integrateOrDelete:String)
    {

        val filepath = uri?.path!!.substring(uri.path!!.indexOf(":")+1)

        val file = File(filepath)
        if (file.extension == "csv")
        {
            if (integrateOrDelete == "Delete")
            {
                repository.clearDatabase()
            }
            val outPutFile = FileReader(file)
            val csvReader = CSVReader(outPutFile)
            val arrayOfItems = csvReader.readAll()
            arrayOfItems.forEach{
                if (it.size == 4)
                {
                    repository.insertItem(Item(it[0] ,it[1] ,it[2] ,it[3] ,ItemType.SERVICE))
                    Log.d("csvRead" ,it[0].toString() +"/"+ it[1] +"/"+ it[2] +"/"+ it[3])
                }
                else
                {
                    Toast.makeText(requireContext() ,"This is not a database exported from this application",Toast.LENGTH_LONG).show()
                }
            }

        }
        else
        {
            Toast.makeText(requireContext() ,"This file is not csv ,please try again",Toast.LENGTH_LONG).show()
        }
    }

    fun importDatabaseAsCSVFile()
    {
        intentToPickFile()
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).toString()) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), RequestCodes.storage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCodes.geCsvFile) {
            val uri: Uri? = data?.data
            if (uri != null) {
                dialogIntegrateOrDeleteData(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}