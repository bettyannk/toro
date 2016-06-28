/*
 * Copyright 2016 eneim@Eneim Labs, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.ene.lab.toro.sample.custom;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import im.ene.lab.toro.ToroPlayerViewHelper;
import im.ene.lab.toro.ToroPlayer;
import im.ene.lab.toro.ToroUtil;
import im.ene.lab.toro.PlayerViewHelper;
import im.ene.lab.toro.ext.ToroAdapter;
import im.ene.lab.toro.media.Cineer;
import im.ene.lab.toro.media.OnInfoListener;
import im.ene.lab.toro.media.PlaybackException;
import im.ene.lab.toro.media.PlaybackInfo;
import im.ene.lab.toro.media.State;

/**
 * Created by eneim on 6/23/16.
 *
 * A simple direct implementation of {@link RecyclerView.ViewHolder} with support from Toro.
 *
 * Note that by default, some extra methods from {@link ToroAdapter.ViewHolder} are not adapted
 * here
 * (for example {@link ToroAdapter.ViewHolder#onAttachedToParent()}. To use them, you can implement
 * them to your own ViewHolder extension or directly extend {@link ToroAdapter.ViewHolder}.
 */
public abstract class AbsVideoViewHolder extends RecyclerView.ViewHolder implements ToroPlayer {

  private static final String TAG = "AbsVideoViewHolder";

  private final PlayerViewHelper itemHelper;
  protected final View videoView;
  // Internal player.
  protected final Cineer.Player player;

  private boolean playable = true; // normally true

  public AbsVideoViewHolder(View itemView) {
    super(itemView);
    videoView = findVideoView();
    if (getVideoView() instanceof Cineer.Player) {
      player = (Cineer.Player) getVideoView();
    } else {
      throw new IllegalArgumentException("Un-supported Player View.");
    }
    player.setOnPlayerStateChangeListener(this);
    itemHelper = ToroPlayerViewHelper.getInstance();
  }

  // Need to called before initialize player.
  protected abstract View findVideoView();

  public abstract void bind(RecyclerView.Adapter parent, Object item);

  /* BEGIN Implement methods from Cineer.Player */

  @Override public void start() {
    player.start();
  }

  @Override public void pause() {
    player.pause();
  }

  @Override public void stop() {
    player.stop();
  }

  @Override public long getDuration() {
    return player.getDuration();
  }

  @Override public long getCurrentPosition() {
    return player.getCurrentPosition();
  }

  @Override public void seekTo(long pos) {
    player.seekTo(pos);
  }

  @Override public boolean isPlaying() {
    return player.isPlaying();
  }

  @Override public int getBufferPercentage() {
    return player.getBufferPercentage();
  }

  @Override public void setVolume(@FloatRange(from = 0.f, to = 1.f) float volume) {
    player.setVolume(volume);
  }

  @NonNull @Override public View getVideoView() {
    return videoView;
  }

  /* END Implement methods from Cineer.Player */

  /* BEGIN Implement methods from ToroPlayer */

  @Override public boolean wantsToPlay() {
    // Default implementation
    return visibleAreaOffset() >= 0.75 && playable;
  }

  @Override public boolean isLoopAble() {
    return false;
  }

  @Override public float visibleAreaOffset() {
    return ToroUtil.visibleAreaOffset(this, itemView.getParent());
  }

  @Override public int getPlayOrder() {
    return getAdapterPosition();
  }

  @Override public void onActivityActive() {

  }

  @Override public void onActivityInactive() {

  }

  @Override public void onVideoPrepared(Cineer mp) {
    playable = true;
  }

  @Override public boolean onPlaybackError(Cineer mp, PlaybackException error) {
    playable = false;
    return false;
  }

  @Override public void onPlaybackInfo(Cineer mp, PlaybackInfo info) {

  }

  @Override public void onPlaybackStarted() {

  }

  @Override public void onPlaybackPaused() {

  }

  @Override public void onPlaybackStopped() {

  }

  @Override public void onPlaybackProgress(long position, long duration) {

  }

  /* END Implement methods from ToroPlayer */

  /* BEGIN Implement methods from other Player callbacks */

  @Override
  public void onPlayerStateChanged(Cineer player, boolean playWhenReady, @State int playbackState) {
    switch (playbackState) {
      case Cineer.PLAYER_PREPARED:
        itemHelper.onPrepared(this, itemView, itemView.getParent(), player);
        break;
      case Cineer.PLAYER_ENDED:
        itemHelper.onCompletion(this, player);
        break;
      // TODO Support buffering update.
      case Cineer.PLAYER_BUFFERING:
        Log.i(TAG, "onPlayerStateChanged: " + player.getBufferedPercentage());
        break;
      case Cineer.PLAYER_IDLE:
        break;
      case Cineer.PLAYER_PREPARING:
        break;
      case Cineer.PLAYER_READY:
        break;
      default:
        break;
    }
  }

  @Override public boolean onPlayerError(Cineer player, PlaybackException error) {
    return itemHelper.onError(this, player, error);
  }

  /**
   * Implement from {@link OnInfoListener}
   */
  @Override public final boolean onInfo(Cineer mp, PlaybackInfo info) {
    return itemHelper.onInfo(this, mp, info);
  }

  /* END Implement methods from other Player callbacks */
}
