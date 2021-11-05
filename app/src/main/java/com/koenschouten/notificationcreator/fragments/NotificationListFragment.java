package com.koenschouten.notificationcreator.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.koenschouten.notificationcreator.R;
import com.koenschouten.notificationcreator.database.tables.NotificationDB;
import com.koenschouten.notificationcreator.listadapters.NotificationAdapter;
import com.koenschouten.notificationcreator.utils.RecyclerItemClickListener;
import com.koenschouten.notificationcreator.viewModels.NotificationsViewModel;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class NotificationListFragment extends Fragment {
    View v;
    private NotificationsViewModel notificationsViewModel;
    private RecyclerView notificationsRecyclerView;


    public NotificationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets up the button to navigate to the createNavigation page.
     */
    private void setupGotoCreateNotificationButton(){
        FloatingActionButton gotoCreateNotificationButton =  v.findViewById(R.id.goto_create_notification_button);
        gotoCreateNotificationButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_notificationListFragment_to_createNotificationFragment));
    }

    /**
     * Makes the items in the plannedNotificationsRecyclerView swipeable to delete
     * A swipe to the right sets the deleted flag of the notification to true
     * After deletion a Snackmessage pops up that allows the user to Undo the deletion. Which sets
     * the deleted flag to false.
     */
    private void makeRecyclerViewItemsSwipeableToDelete(){
        //Create a ItemTouchHelper to make the items draggable to the Right.

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            private final int SWIPE_ICON_PADDING = convertDpToPx(24);

            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                //Draw recycle bin when swiping to the right.
                if (dX > 0) {
                    View itemView = viewHolder.itemView;
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_delete_24);

                    //get primary_color from theme
                    int color = MaterialColors.getColor(getActivity(), R.attr.colorPrimary, Color.BLACK);


                    drawable.setColorFilter(color , PorterDuff.Mode.MULTIPLY);
                    drawable.setBounds(itemView.getLeft()+ SWIPE_ICON_PADDING,
                            itemView.getTop() + SWIPE_ICON_PADDING,
                            itemView.getLeft()  + (itemView.getBottom()  - itemView.getTop()) -  SWIPE_ICON_PADDING,
                            itemView.getBottom() - SWIPE_ICON_PADDING);
                    drawable.draw(canvas);
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            private int convertDpToPx(int dp){
                return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                //Get the notification to delete and set flagtodelete to True
                int position = viewHolder.getAdapterPosition();
                NotificationDB notificationToDelete = notificationsViewModel.getNotificationByIndex(position);
                notificationsViewModel.setFlagToDelete(notificationToDelete);

                //Create a Snackbar message to allow a user to undo an accidental delete
                Snackbar.make(notificationsRecyclerView,
                        getText(R.string.delete_snackbar_text) + " \"" + notificationToDelete.contentTitle + "\"",
                        Snackbar.LENGTH_LONG).setAction(getText(R.string.delete_snackbar_action_text), v -> notificationsViewModel.removeFlagToDelete(notificationToDelete)).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(notificationsRecyclerView);
    }

    /**
     * Sets up the recyclerView for the Planed notifications. Assigns
     */
    private void setupRecyclerView(){
        notificationsRecyclerView = v.findViewById(R.id.recyclerview_notification_channels);

        final NotificationAdapter notificationAdapter = new NotificationAdapter(new NotificationAdapter.NotificationDiff());

        notificationsRecyclerView.setAdapter(notificationAdapter);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        makeRecyclerViewItemsSwipeableToDelete();

        //Makes the recyclerView observe changes from the PlannedNotificationViewModel
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        notificationsViewModel.getAllNotifications().observe(getViewLifecycleOwner(), notificationAdapter::submitList);

        addItemTouchListenerToNotificationsRecyclerView();
    }



    private void addItemTouchListenerToNotificationsRecyclerView(){
        notificationsRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), notificationsRecyclerView,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        NotificationDB clickedNotificationDB = notificationsViewModel.getNotificationByIndex(position);

                        long notificationID = clickedNotificationDB.notificationID;
                        Bundle bundle = new Bundle();

                        bundle.putLong(EditNotificationFragment.BUNDLE_NOTIFICATION_ID, notificationID);

                        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.action_notificationListFragment_to_editNotificationFragment, bundle);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_notification_list, container, false);
        setupRecyclerView();
        setupGotoCreateNotificationButton();
        return v;
    }
}