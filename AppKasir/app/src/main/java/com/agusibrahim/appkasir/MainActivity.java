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
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import java.io.InputStream;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import android.util.Log;

public class MainActivity extends AppCompatActivity
{
	DrawerLayout mDrawer;
	NavigationView nvDrawer;
	ActionBarDrawerToggle drawerToggle;
	Toolbar toolbar;
	public static ProdukDataAdapter dataproduk;
	public static BelanjaanDataAdapter dataBalanjaan;
	public static AsyncHttpServer jserver;
	public static List<WebSocket> _sockets = new ArrayList<WebSocket>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		jserver = new AsyncHttpServer();
		reqPerms();
		dataproduk=new ProdukDataAdapter(this, Produk.getInit(this));
		dataBalanjaan=new BelanjaanDataAdapter(this);
		jserver.websocket("/live", new AsyncHttpServer.WebSocketRequestCallback() {
				@Override
				public void onConnected(final WebSocket ws, AsyncHttpServerRequest p2) {
					_sockets.add(ws);
					//ws.send("Hello broo");
				}
			});
		jserver.get("/web/.*", new HttpServerRequestCallback() {
				@Override
				public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
					//response.send("Hello!!!");
					//setTitle("web"+request.getPath());
					Log.e("WebSocket", "req: "+request.getPath());
					try {
						InputStream is=getAssets().open(request.getPath().substring(1));
						response.sendStream(is, is.available());
					} catch (Exception e) {
						response.send("Error: "+e.getMessage());
					}
				}
			});
		jserver.listen(8333);
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
				fragclass=laporanFragment.class;
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
				}
			});
		dlg.setNegativeButton("Tidak", null);
		dlg.show();
		//super.onBackPressed();
	}
}
