package com.example.p_scanner.ui.listItems

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p_scanner.R
import com.example.p_scanner.Utils.RealPathUtil
import com.example.p_scanner.Utils.RequestCodes
import com.example.p_scanner.adapters.ItemsAdapter
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.interfaces.ItemClickListener
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemInteractions
import com.example.p_scanner.repository.Repository
import com.example.p_scanner.ui.addOrEditItems.AddAndEditItemActivity
import com.example.p_scanner.viewmodels.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hbisoft.pickit.PickiT
import com.hbisoft.pickit.PickiTCallbacks
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Path


class ListItemsFragment : Fragment(),PickiTCallbacks  {
    lateinit var adapter: ItemsAdapter

    lateinit var repository: Repository
    lateinit var productViewModel:ProductViewModel
    lateinit var searchView:SearchView
    lateinit var arrayOfItems:ArrayList<Item>
    lateinit var textViewNoItemFound:TextView
    lateinit var buttonMenu:ImageView
    lateinit var pickiT: PickiT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ProductViewModel(requireContext())

        adapter = ItemsAdapter()
        repository = Repository(ItemsDatabase.getDatabase(requireContext()).itemDAO())
        pickiT = PickiT(requireContext() ,this,requireActivity())

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
                    exportDatabaseAsCSVFile()
                    Toast.makeText(requireContext() ,"Export" ,Toast.LENGTH_SHORT).show()
                }
                else
                {
                    importDatabaseAsCSV()
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
            if (arrayOfItems.size != 0)
            {
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val fileName = "test.csv"
                val file = File("$path/$fileName")
                if (!file.exists()) {
                    file.createNewFile()
                }

                val outPutFile = FileWriter(file)
                val writer = CSVWriter(outPutFile)

                val header = arrayOf("id" ,"title" ,"description" ,"price")
                writer.writeNext(header)

                for (item in arrayOfItems)
                {
                    val currentArray = arrayOf(item.id ,item.title ,item.description ,item.price)
                    writer.writeNext(currentArray)
                }
                writer.close()
            }
            else
            {
                Toast.makeText(requireContext() ,"No Item found to export" ,Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun importDatabaseAsCSV()
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        val uri: Uri = Uri.parse(Environment.getExternalStorageDirectory().path)
        intent.setDataAndType(uri, "text/csv")
        startActivityForResult(Intent.createChooser(intent, "Open CSV"),RequestCodes.geCsvFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCodes.geCsvFile) {
            val uri: Uri? = data?.data


            val filePath = pickiT.getPath(uri, Build.VERSION.SDK_INT)
            Toast.makeText(requireContext() ,filePath.toString() ,Toast.LENGTH_LONG).show()
//            val file = File(filePath)
//            if (file.extension == "csv")
//            {
//                val outPutFile = FileReader(file)
//                val csvReader = CSVReader(outPutFile)
//                Log.d("csvRead" ,csvReader.readAll().toString())
//            }
//            else
//            {
//                Toast.makeText(requireContext() ,"This file is not csv ,please try again",Toast.LENGTH_SHORT).show()
//            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun PickiTonUriReturned() {
        return
    }

    override fun PickiTonStartListener() {
        return
    }

    override fun PickiTonProgressUpdate(progress: Int) {
        return
    }

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
        return
    }

    override fun PickiTonMultipleCompleteListener(
        paths: java.util.ArrayList<String>?,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
        return
    }
}