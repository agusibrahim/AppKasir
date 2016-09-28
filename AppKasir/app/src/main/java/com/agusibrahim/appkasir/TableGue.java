package com.agusibrahim.appkasir;
import de.codecrafters.tableview.*;
import com.agusibrahim.appkasir.Model.*;
import android.content.*;
import android.util.*;
import de.codecrafters.tableview.toolkit.*;
import android.support.v4.content.*;
import java.util.*;

public class TableGue extends SortableTableView<Produk>
{
	public TableGue(Context ctx){
		super(ctx, null);
	}
	public TableGue(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public TableGue(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);
		setColumnCount(3);
		SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "Produk", "Harga","Stok");
		simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.table_header_text));
		final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());
		setHeaderAdapter(simpleTableHeaderAdapter);
		setColumnComparator(1, new HargaProdukComparator());
		setColumnComparator(0, new NamaProdukComparator());
		setColumnComparator(2, new StokProdukComparator());
	}
	private static class HargaProdukComparator implements Comparator<Produk> {
        @Override
        public int compare(Produk prod1, Produk prod2) {
            if (prod1.getHarga() < prod2.getHarga()) return -1;
            if (prod1.getHarga() > prod2.getHarga()) return 1;
            return 0;
        }
	}
	private static class StokProdukComparator implements Comparator<Produk> {
        @Override
        public int compare(Produk prod1, Produk prod2) {
            if (prod1.getStok() < prod2.getStok()) return -1;
            if (prod1.getStok()> prod2.getStok()) return 1;
            return 0;
        }
	}
	private static class NamaProdukComparator implements Comparator<Produk> {

        @Override
        public int compare(final Produk prod1, final Produk prod2) {
            return prod1.getNama().compareTo(prod2.getNama());
        }
    }
}
