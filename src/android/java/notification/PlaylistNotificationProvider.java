package com.rolamix.plugins.audioplayer.notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.devbrackets.android.playlistcore.components.notification.DefaultPlaylistNotificationProvider;
import com.devbrackets.android.playlistcore.data.MediaInfo;
import com.devbrackets.android.playlistcore.data.RemoteActions;
import __PACKAGE_NAME__.R;

import org.jetbrains.annotations.Nullable;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class PlaylistNotificationProvider extends DefaultPlaylistNotificationProvider {
    public PlaylistNotificationProvider(Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected PendingIntent getClickPendingIntent() {
        Context context = this.getContext();
        String pkgName  = context.getPackageName();

        Intent intent = context
                .getPackageManager()
                .getLaunchIntentForPackage(pkgName);

        intent.addFlags(
                FLAG_ACTIVITY_REORDER_TO_FRONT | FLAG_ACTIVITY_SINGLE_TOP);

        return PendingIntent.getActivity(this.getContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void setActions(NotificationCompat.Builder builder, MediaInfo info, Class<? extends Service> serviceClass) {
      Context context = this.getContext();
      // Previous
      int actionIcon = info.getMediaState().isPreviousEnabled() ? R.drawable.player_notification_previous : R.drawable.player_notification_previous;
      String title = context.getResources().getString(R.string.playlistcore_default_notification_previous);
      builder.addAction(actionIcon, title, createPendingIntent(serviceClass, RemoteActions.INSTANCE.getACTION_PREVIOUS()));

      
      // Play/Pause
      if (info.getMediaState().isPlaying()) {
        title = context.getResources().getString(R.string.playlistcore_default_notification_pause);
        actionIcon = (info.getMediaState().isLoading()) ? R.drawable.playlistcore_notification_pause_disabled : R.drawable.playlistcore_notification_pause;
      } else {
        title = context.getResources().getString(R.string.playlistcore_default_notification_play);
        actionIcon = (info.getMediaState().isLoading()) ? R.drawable.playlistcore_notification_play_disabled : R.drawable.playlistcore_notification_play;
      }

      builder.addAction(actionIcon, title, createPendingIntent(serviceClass, RemoteActions.INSTANCE.getACTION_PLAY_PAUSE()));

      // Next
      actionIcon = (info.getMediaState().isNextEnabled()) ? R.drawable.player_notification_next : R.drawable.player_notification_next;
      title = context.getResources().getString(R.string.playlistcore_default_notification_next);
      builder.addAction(actionIcon, title, createPendingIntent(serviceClass, RemoteActions.INSTANCE.getACTION_NEXT()));
        
    }
}
