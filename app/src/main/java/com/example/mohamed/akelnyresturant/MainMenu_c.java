package com.example.mohamed.akelnyresturant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.mohamed.akelnyresturant.Database.DataBase;
import com.example.mohamed.akelnyresturant.Service.ListenOrder;
import com.example.mohamed.akelnyresturant.ViewHolders.MenuMainViewHolder;
import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.model.Banner;
import com.example.mohamed.akelnyresturant.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainMenu_c extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<Category, MenuMainViewHolder> adapter;
    HashMap<String,String>image_list;

    RecyclerView recycler_Menu;
    SwipeRefreshLayout swipeRefreshLayout;
    CounterFab fab;
    SliderLayout mslider;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_c);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu");


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Category");




        //we move this from function loadmenu for animation affect on it and it must below databaserefrence
        adapter = new FirebaseRecyclerAdapter<Category, MenuMainViewHolder>(Category.class, R.layout.item_main_c, MenuMainViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(MenuMainViewHolder viewHolder, Category category, int position) {

                viewHolder.menuname.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage()).into(viewHolder.menuimag);

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(MainMenu_c.this, FoodList_c.class);
                        intent.putExtra("categoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };




        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_layout);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMenu();

            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadMenu();

            }
        });

        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu_c.this, Cart_c.class);
                startActivity(intent);
            }
        });

        fab.setCount(new DataBase(this).getCountCart());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recycler_Menu = (RecyclerView) findViewById(R.id.recycler);
        recycler_Menu.setLayoutManager(new LinearLayoutManager(this));
        LayoutAnimationController controller= AnimationUtils.loadLayoutAnimation(recycler_Menu.getContext(),R.anim.layout_fall_down);
        recycler_Menu.setLayoutAnimation(controller);


        //Listen class in package Service for otification
        Intent intent = new Intent(MainMenu_c.this, ListenOrder.class);
        startService(intent);



        //for banner
        setupSlider();


    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setupSlider() {

        mslider=(SliderLayout)findViewById(R.id.slider);
        image_list=new HashMap<>();

        final DatabaseReference databaseReference_banners=database.getReference("Banner");

        databaseReference_banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    Banner banner=postSnapShot.getValue(Banner.class);

                    //concat name and id
                    image_list.put(banner.getName()+"@@@@"+banner.getId(),banner.getImage());
                }
                for (String key:image_list.keySet()){
                    String[]keySplit=key.split("@@@@");
                    String nameOfFood=keySplit[0];
                    String idOfFood=keySplit[1];

                    //create slider
                    final TextSliderView textSliderView=new TextSliderView(getBaseContext());
                    textSliderView
                            .description(nameOfFood)
                            .image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent=new Intent(MainMenu_c.this,FoodDetails_c.class);
                                    intent.putExtras(textSliderView.getBundle());
                                    startActivity(intent);
                                }
                            });

                    //Add extra bundel
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodId",idOfFood);

                    mslider.addSlider(textSliderView);

                    //remove event after finish
                    databaseReference_banners.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mslider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mslider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mslider.setCustomAnimation(new DescriptionAnimation());
        mslider.setDuration(3000);

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new DataBase(this).getCountCart());

    }

    @Override
    protected void onStop() {
        super.onStop();
        mslider.stopAutoCycle();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadMenu() {

        recycler_Menu.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);

        //Animation
        recycler_Menu.getAdapter().notifyDataSetChanged();
        recycler_Menu.scheduleLayoutAnimation();

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_search) {
            startActivity(new Intent(MainMenu_c.this,Search_c.class));
        }

        return super.onOptionsItemSelected(item);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(MainMenu_c.this,OrderStatus_c.class));

        } else if (id == R.id.nav_bag) {

            Intent intent = new Intent(MainMenu_c.this, Cart_c.class);
            startActivity(intent);

        } else if (id == R.id.manage) {

            Intent intent = new Intent(MainMenu_c.this, Manage_c.class);
            startActivity(intent);

        } else if (id == R.id.nav_listfav) {
            Intent intent = new Intent(MainMenu_c.this, FavoritesList.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
