package com.agusibrahim.appkasir;
import android.app.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v4.widget.*;
import android.support.v7.widget.*;
import android.support.design.widget.*;
import android.view.*;
import android.support.v4.view.*;
import android.widget.Toast;
import android.content.res.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.agusibrahim.appkasir.Fragment.*;
import com.agusibrahim.appkasir.Model.Produk;
import java.util.*;
import com.agusibrahim.appkasir.Adapter.*;
import kr.co.namee.permissiongen.*;
import android.*;
import android.support.v7.app.AlertDialog;
import android.content.*;
import com.firebase.client.*;
import android.support.v7.app.AlertDialog;

public class MainActivity extends AppCompatActivity
{
	DrawerLayout mDrawer;
	NavigationView nvDrawer;
	ActionBarDrawerToggle drawerToggle;
	Toolbar toolbar;
	
	public static Firebase mFirebase;
	public static List<String> keyIndex = new ArrayList<String>();
	
	public static ProdukDataAdapter dataproduk;
	public static BelanjaanDataAdapter dataBalanjaan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{Firebase.getDefaultConfig().setPersistenceEnabled(true);
		}catch(Exception e){
			Toast.makeText(this, "Gagal setting direbase", Toast.LENGTH_SHORT).show();
		}
		Firebase.setAndroidContext(this);
		mFirebase = new Firebase("https://restjal.firebaseio.com").child("dataproduk");
		
		setContentView(R.layout.activity_main);
		toolbar=(Toolbar) findViewById(R.id.mytoolbar);
		setSupportActionBar(toolbar);
		mDrawer=(DrawerLayout) findViewById(R.id.drawer_layout);
		nvDrawer=(NavigationView) findViewById(R.id.naView);
		setupDrawer(nvDrawer);
		drawerToggle= setupDrawerToggle();
		mDrawer.addDrawerListener(drawerToggle);
		getSupportFragmentManager().beginTransaction().replace(R.id.konten, new belanjaFragment()).commit();
		nvDrawer.getMenu().getItem(0).setChecked(true);
		setTitle("Belanja");
		reqPerms();
		dataproduk=new ProdukDataAdapter(this, new ArrayList<Produk>());
		dataBalanjaan=new BelanjaanDataAdapter(this);
		mFirebase.addChildEventListener(new ChildEventListener() {
				@Override
				public void onChildAdded(DataSnapshot p1, String p2) {
					Produk p = p1.getValue(Produk.class);
					if(keyIndex.indexOf(p1.getKey())>=0) return;
					keyIndex.add(p1.getKey());
					MainActivity.dataproduk.getData().add(p);
					MainActivity.dataproduk.notifyDataSetChanged();
				}

				@Override
				public void onChildChanged(DataSnapshot p1, String p2) {
					Produk p = p1.getValue(Produk.class);
					MainActivity.dataproduk.getData().set(getPosByProduk(p), p);
					MainActivity.dataproduk.notifyDataSetChanged();
				}

				@Override
				public void onChildRemoved(DataSnapshot p1) {
					Produk p = p1.getValue(Produk.class);
					keyIndex.remove(p1.getKey());
					MainActivity.dataproduk.getData().remove(getPosByProduk(p));
					MainActivity.dataproduk.notifyDataSetChanged();
				}

				@Override
				public void onChildMoved(DataSnapshot p1, String p2) {
					// TODO: Implement this method
				}

				@Override
				public void onCancelled(FirebaseError p1) {
					// TODO: Implement this method
				}
			});
    }
	
	private void reqPerms(){
		PermissionGen.with(MainActivity.this)
			.addRequestCode(100)
			.permissions(
			Manifest.permission.CAMERA,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE)
			.request();
	}
	private void setupDrawer(NavigationView nview){
		nview.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener(){
				public boolean onNavigationItemSelected(MenuItem menu){
					Terpilih(menu);
					return true;
				}
			});
	}
	private ActionBarDrawerToggle setupDrawerToggle(){
		return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_buka, R.string.drawer_tutup);
	}
	private void Terpilih(MenuItem menu){
		Class fragclass;
		Fragment frag = null;
		switch(menu.getItemId()){
			case R.id.frag1:
				fragclass = belanjaFragment.class;
				break;
			case R.id.frag2:
				fragclass=productFragment.class;
				break;
			default:
				fragclass=belanjaFragment.class;
		}
		try{
			frag=(Fragment) fragclass.newInstance();
		}catch(Exception e){}
		FragmentManager fm=getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.konten, frag).commit();
		menu.setChecked(true);
		setTitle(menu.getTitle());
		mDrawer.closeDrawers();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId()){
			case android.R.id.home:
				mDrawer.openDrawer(GravityCompat.START);
				return true;
			
				
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}
	@PermissionSuccess(requestCode = 100)
	public void doSomething(){
		// Lakukan sesuatu disini
	}
	@PermissionFail(requestCode = 100)
	public void doFailSomething(){
		AlertDialog.Builder dlg=new AlertDialog.Builder(this);
		dlg.setTitle("Perijinan ditolak");
		dlg.setCancelable(false);
		dlg.setMessage("Untuk menggunakan Aplikasi ini kamu perlu membolehkan beberapa perijinan yang diajukan. Atau Aplikasi ini tidak bisa digunakan");
		dlg.setNegativeButton("Keluar", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					MainActivity.this.finish();
				}
			});
		dlg.show();
	}
	@Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
													 int[] grantResults) {
		PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
	}
	@Override
	protected void onDestroy() {
		dataBalanjaan.total=0;
		mFirebase=null;
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setTitle("Exit Confirmation");
		dlg.setMessage("Yakin mau keluar?");
		dlg.setPositiveButton("Ya", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					MainActivity.this.finish();
					// Ntahlah, gw belum nemu cara meng close firebase saat exit
					// cara yg gw tau hanya mengkill semua proses saat app exit
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
		dlg.setNegativeButton("Tidak", null);
		dlg.show();
	}
	
	private int getPosByProduk(Produk p){
		List<Produk> data = dataproduk.getData();
		int pos = -1;
		for(Produk pp: data){
			if(pp.getSn().equals(p.getSn())){
				pos=data.indexOf(pp);
				break;
			}
		}
		return pos;
	}

}
