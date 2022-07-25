package com.contactdemo.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contactdemo.R;
import com.contactdemo.adapter.AdapterRecordListRV;
import com.contactdemo.databaseHandler.DatabaseHandler;
import com.contactdemo.interfaces.ClickEvent;
import com.contactdemo.model.ContactModel;
import com.contactdemo.utils.GenericView;
import com.contactdemo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity implements ClickEvent {
    RelativeLayout relParent;
    LinearLayoutManager linearLayoutManager;
    TextView txtHeader, txtAdd, txtRemoveAll, txtNoRecord;
    FrameLayout frameLayout;
    RecyclerView rvMain;
    RecyclerView.LayoutManager layoutManager;
    // variable declaration
    public static final int REQUEST_EDIT = 3;
    public int position;

    List<ContactModel> recordList = new ArrayList<>();
    AdapterRecordListRV adapterRecordListRV;
    ContactModel recordModel;

    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        init();
    }

    public void init() {
        relParent = GenericView.findViewById(this, R.id.rel_parent);
        txtHeader = GenericView.findViewById(this, R.id.txt_main_header);
        txtAdd = GenericView.findViewById(this, R.id.txt_main_add);
        txtRemoveAll = GenericView.findViewById(this, R.id.txt_main_removeAll);
        frameLayout = GenericView.findViewById(this, R.id.frameLayout_main);
        rvMain = GenericView.findViewById(this,R.id.rv_main);
        // set layout in recyclerView
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMain.setLayoutManager(layoutManager);
        rvMain.setItemAnimator(new DefaultItemAnimator());

        txtNoRecord = GenericView.findViewById(this, R.id.txt_main_noRecord);

        Utils.setupOutSideTouchHideKeyboard(relParent);
        databaseHandler = new DatabaseHandler(this);
        setAdapterOrSetNoRecord();
    }

    @Override
    public void onClickEvent(View view) {
        Intent iToAddRecord;
        switch (view.getId()) {
            case R.id.txt_main_add:
                iToAddRecord = new Intent(this, AddRecordActivity.class);
                startActivity(iToAddRecord);
                break;

            case R.id.iv_rowItem_delete:
                ContactModel deleteRecord = recordList.get(Integer.parseInt(view.getTag().toString()));
                showDeleteDialog(deleteRecord);
                break;
        }
    }

    /**
     * This method use to set adapter when data exist in database otherwise
     * it shows "no record found" message.
     * */
    public void setAdapterOrSetNoRecord() {
        if (databaseHandler.isRecordFound()) {
            txtNoRecord.setVisibility(View.GONE);
            recordList = databaseHandler.getAllSortedRecord();
            adapterRecordListRV = new AdapterRecordListRV(this, recordList);
            rvMain.setAdapter(adapterRecordListRV);

        } else {
            txtNoRecord.setVisibility(View.VISIBLE);
            txtNoRecord.setText(getString(R.string.strNoRecordFound));
        }
    }

    /**
     * popUp confirmation dialog for delete operation
     *
     * @param removeModel (RecordModel) : selected item model class object
     */
    private void showDeleteDialog(final ContactModel removeModel) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setMessage(getString(R.string.strRemove) + removeModel.getFirstName() + getString(R.string.strQueMark));
        builder1.setCancelable(false);
        // Display confirmation dialog box to delete value
        builder1.setPositiveButton(getString(R.string.strYes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.i("TAG", "deleteStatus:  " + "success");
                Log.d("TAG", "deleted Item:  " + removeModel.getFirstName());

                //perform delete operation
              //  databaseHandler.deleteRecord(removeModel);
                recordList.remove(removeModel);
                adapterRecordListRV.notifyDataSetChanged();

                /* it check the database count
                 * if empList is empty than it return boolean value (true)
                 * and set default text indication "No employee found" */
                if (recordList.isEmpty()) {
                    txtNoRecord.setVisibility(View.VISIBLE);
                    txtNoRecord.setText(getString(R.string.strNoRecordFound));
                }

            }
        });
        builder1.setNegativeButton(getString(R.string.strNo), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder1.show();
    }

    public void showRemoveAllDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setMessage(getString(R.string.strRemoveAll) + getString(R.string.strQueMark));
        builder1.setCancelable(false);
        // Display confirmation dialog box to delete value
        builder1.setPositiveButton(getString(R.string.strYes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.i("TAG", "removeAllStatus:  " + "success");
                //perform delete operation
              //  databaseHandler.deleteAll();
                recordList.clear();
                adapterRecordListRV.notifyDataSetChanged();

                /* it check the realmList count
                 * if empList is empty than it return boolean value (true)
                 * and set default text indication "No employee found" */
//                if (!databaseHandler.isRecordFound()) {
//                    txtNoRecord.setVisibility(View.VISIBLE);
//                    txtNoRecord.setText(getString(R.string.strNoRecordFound));
//                }

            }
        });
        builder1.setNegativeButton(getString(R.string.strNo), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder1.show();
    }
}