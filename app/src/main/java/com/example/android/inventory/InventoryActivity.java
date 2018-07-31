package com.example.android.inventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.android.inventory.data.InventoryContract.InventoryEntry;
import com.example.android.inventory.data.InventoryDbHelper;

public class InventoryActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        mDbHelper = new InventoryDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        insertInventory();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE_NUMBER};

        Cursor cursor = db.query(
                InventoryEntry.TABLE_NAME,
                projection,
                InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,
                null,
                null,
                null,
                null);

        Log.d("LOG_TAG", getString(R.string.total_rows) + cursor.getCount());

        TextView displayView = findViewById(R.id.text_view_inventory);

        try {
            displayView.setText(getString(R.string.inventory_count) + cursor.getCount() + " products.\n\n");
            displayView.append(InventoryEntry._ID + " - " +
                    InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME + " - " +
                    InventoryEntry.COLUMN_INVENTORY_PRICE + " - " +
                    InventoryEntry.COLUMN_INVENTORY_QUANTITY + " _ " +
                    InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME + " - " +
                    InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE_NUMBER + "\n");

            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierNameColumnIndex);
                int currentSupplierNumber = cursor.getInt(supplierPhoneNumberColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplier + " - " +
                        currentSupplierNumber));
            }
        } finally {
            cursor.close();

        }

    }

    private void insertInventory() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME, getString(R.string.product_a));
        values.put(InventoryEntry.COLUMN_INVENTORY_PRICE, 10);
        values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, 1);
        values.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME, getString(R.string.supplier_a));
        values.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE_NUMBER, 1234567890);

        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Log.d("LOG_TAG", getString(R.string.insert_successful) + newRowId );
        } else {
            Log.d("LOG_TAG", getString(R.string.insert_unsuccessful));
        }

    }
}