package com.androiddevproject.foodorderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private Cursor cursor;
    SQLiteDatabase sqLiteDatabase;

    public CartAdapter(Context context2, Cursor cursor2) {
        this.context = context2;
        this.cursor = cursor2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.cart_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        if (this.cursor.moveToPosition(i)) {
            this.sqLiteDatabase = new GroceryDBHelper(this.context).getWritableDatabase();
            TextView textView = viewHolder.productName_tvCRT;
            Cursor cursor2 = this.cursor;
            textView.setText(cursor2.getString(cursor2.getColumnIndex("productName")));
            Picasso with = Picasso.with(this.context);
            Cursor cursor3 = this.cursor;
            with.load(cursor3.getString(cursor3.getColumnIndex("productImage"))).placeholder((int) R.drawable.loading_icon).into(viewHolder.productImage_ivCRT);
            Cursor cursor4 = this.cursor;
            int i2 = cursor4.getInt(cursor4.getColumnIndex("productQty"));
            Cursor cursor5 = this.cursor;
            float i3 = cursor5.getFloat(cursor5.getColumnIndex("productPrice"));
            TextView textView2 = viewHolder.productQty_tvCRT;
            textView2.setText(i2 + "");
            TextView textView3 = viewHolder.pricePerItemCRT_tv;
            textView3.setText(i3 + "");
            TextView textView4 = viewHolder.productId_tvCRT;
            Cursor cursor6 = this.cursor;
            textView4.setText(cursor6.getString(cursor6.getColumnIndex("productId")));
            float i4 = i2 * i3;
            TextView textView5 = viewHolder.productPrice_tvCRT;
            textView5.setText(i4 + "");
            ContentValues contentValues = new ContentValues();
            contentValues.put("priceTotal", i4);
            SQLiteDatabase sQLiteDatabase = this.sqLiteDatabase;
            Cursor cursor7 = this.cursor;
            sQLiteDatabase.update("CART", contentValues, "productId = ?", new String[]{cursor7.getString(cursor7.getColumnIndex("productId"))});
            final long queryNumEntries = DatabaseUtils.queryNumEntries(this.sqLiteDatabase, "CART");
            TextView textView6 = CartActivity.grossTotal_tv;
            textView6.setText("$" + getSumTotalPrice(this.sqLiteDatabase));
            TextView textView7 = CartActivity.totalItems_tv;
            textView7.setText(queryNumEntries + "");
            TextView textView8 = MainActivity.cartQty_tv;
            textView8.setText(queryNumEntries + "");
            viewHolder.removeCart_btnCRT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    CartAdapter.this.deleteProduct(viewHolder.productId_tvCRT.getText().toString(), "CART", CartAdapter.this.sqLiteDatabase);
                    long queryNumEntries = DatabaseUtils.queryNumEntries(CartAdapter.this.sqLiteDatabase, "CART");
                    TextView textView = CartActivity.grossTotal_tv;
                    StringBuilder sb = new StringBuilder();
                    CartAdapter cartAdapter = CartAdapter.this;
                    sb.append(cartAdapter.getSumTotalPrice(cartAdapter.sqLiteDatabase));
                    sb.append("");
                    textView.setText(sb.toString());
                    TextView textView2 = CartActivity.totalItems_tv;
                    textView2.setText(queryNumEntries + "");
                    CartActivity.loadCart_Adapter(CartAdapter.this.sqLiteDatabase, CartAdapter.this.context);
                    if (queryNumEntries < 1) {
                        CartActivity.empty_cart_layout.setVisibility(View.VISIBLE);
                        CartActivity.rv_cart.setVisibility(View.GONE);
                    }
                }
            });
            viewHolder.addQty_btnCRT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MainActivity.qtyyy = Integer.parseInt(viewHolder.productQty_tvCRT.getText().toString().trim());
                    MainActivity.qtyyy++;
                    viewHolder.productQty_tvCRT.setText(MainActivity.qtyyy + "");
                    viewHolder.productPrice_tvCRT.setText((Integer.parseInt(viewHolder.productQty_tvCRT.getText().toString().trim()) * Float.parseFloat(viewHolder.pricePerItemCRT_tv.getText().toString())) + "");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("productQty", viewHolder.productQty_tvCRT.getText().toString());
                    contentValues.put("priceTotal", Float.parseFloat(viewHolder.productPrice_tvCRT.getText().toString()));
                    CartAdapter.this.sqLiteDatabase.update("CART", contentValues, "productId = ?", new String[]{viewHolder.productId_tvCRT.getText().toString()});
                    TextView textView = CartActivity.grossTotal_tv;
                    StringBuilder sb = new StringBuilder();
                    sb.append("$");
                    CartAdapter cartAdapter = CartAdapter.this;
                    sb.append(cartAdapter.getSumTotalPrice(cartAdapter.sqLiteDatabase));
                    textView.setText(sb.toString());
                    CartActivity.totalItems_tv.setText(queryNumEntries + "");
                }
            });
            viewHolder.minusQty_btnCRT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MainActivity.qtyyy = Integer.parseInt(viewHolder.productQty_tvCRT.getText().toString().trim());
                    if (MainActivity.qtyyy != 1) {
                        MainActivity.qtyyy--;
                        viewHolder.productQty_tvCRT.setText(MainActivity.qtyyy + "");
                        viewHolder.productPrice_tvCRT.setText((Integer.parseInt(viewHolder.productQty_tvCRT.getText().toString().trim()) * Float.parseFloat(viewHolder.pricePerItemCRT_tv.getText().toString())) + "");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("productQty", viewHolder.productQty_tvCRT.getText().toString());
                        contentValues.put("priceTotal", Float.parseFloat(viewHolder.productPrice_tvCRT.getText().toString()));
                        CartAdapter.this.sqLiteDatabase.update("CART", contentValues, "productId = ?", new String[]{viewHolder.productId_tvCRT.getText().toString()});
                        CartActivity.grossTotal_tv.setText(String.valueOf(getSumTotalPrice(sqLiteDatabase)));
                        CartActivity.totalItems_tv.setText(queryNumEntries + "");
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView addQty_btnCRT;
        ImageView minusQty_btnCRT;
        TextView pricePerItemCRT_tv;
        TextView productId_tvCRT;
        ImageView productImage_ivCRT;
        TextView productName_tvCRT;
        TextView productPrice_tvCRT;
        TextView productQty_tvCRT;
        FrameLayout removeCart_btnCRT;

        public ViewHolder(View view) {
            super(view);
            this.productQty_tvCRT = (TextView) view.findViewById(R.id.productqty_tvCRT);
            this.productPrice_tvCRT = (TextView) view.findViewById(R.id.tv_productPriceCRT);
            this.productName_tvCRT = (TextView) view.findViewById(R.id.tv_productNameCRT);
            this.minusQty_btnCRT = (ImageView) view.findViewById(R.id.minusqty_btnCRT);
            this.addQty_btnCRT = (ImageView) view.findViewById(R.id.addqty_btnCRT);
            this.productImage_ivCRT = (ImageView) view.findViewById(R.id.iv_productImageCRT);
            this.removeCart_btnCRT = (FrameLayout) view.findViewById(R.id.removecart_btnCRT);
            this.pricePerItemCRT_tv = (TextView) view.findViewById(R.id.pricePerItem_tv);
            this.productId_tvCRT = (TextView) view.findViewById(R.id.productId_tvCRT);
        }
    }

    public void cursurSwap(Cursor cursor2) {
        Cursor cursor3 = this.cursor;
        if (cursor3 != null) {
            cursor3.close();
        }
        this.cursor = cursor2;
        if (cursor2 != null) {
            notifyDataSetChanged();
        }
    }

    public void deleteProduct(String str, String str2, SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.delete(str2, "productId = ?", new String[]{str});
    }

    public float getSumTotalPrice(SQLiteDatabase sQLiteDatabase) {
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT SUM(priceTotal) as Total FROM CART", (String[]) null);
        if (rawQuery.moveToFirst()) {
            return rawQuery.getFloat(rawQuery.getColumnIndex("Total"));
        }
        return 0;
    }
}
