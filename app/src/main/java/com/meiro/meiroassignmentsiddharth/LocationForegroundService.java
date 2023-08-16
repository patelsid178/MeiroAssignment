package com.meiro.meiroassignmentsiddharth;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;


public class LocationForegroundService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check if permissions are granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            createNotificationChannel();

            // Set up location listener
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateNotification(location.getLatitude(), location.getLongitude());


                }

                // ... Implement other LocationListener methods
            };

            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);

            // Create foreground notification

            Notification notification = buildNotification("Loading...");
            startForeground(NOTIFICATION_ID, notification);

        } else {
            // Handle the case where permissions are not granted

        }

        return START_STICKY;
    }


    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "location_channel";
            String channelName = "Location Updates";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification(String content) {
        String channelId = "location_channel";

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // Create a NotificationCompat.Builder instance with the specified CHANNEL_ID
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Live Location Updates")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);

        return notificationBuilder.build();
    }

    private void updateNotification(double latitude, double longitude) {
        String content = "Latitude: " + latitude + ", Longitude: " + longitude;

        // Check if the app has the necessary location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Build a new notification with the updated content
            Notification notification = buildNotification(content);

            // Update the existing notification with the new content
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            // Handle the case where permissions are not granted
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up: Stop location updates and remove notification
        locationManager.removeUpdates(locationListener);
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}