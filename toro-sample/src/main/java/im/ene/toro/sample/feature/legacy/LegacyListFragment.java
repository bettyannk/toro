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

package im.ene.toro.sample.feature.legacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import im.ene.toro.Toro;
import im.ene.toro.sample.BaseToroFragment;
import im.ene.toro.sample.R;

/**
 * Created by eneim on 6/30/16.
 */
public class LegacyListFragment extends BaseToroFragment {

  protected RecyclerView mRecyclerView;
  protected RecyclerView.Adapter mAdapter;

  public static LegacyListFragment newInstance() {
    return new LegacyListFragment();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.generic_recycler_view, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    RecyclerView.LayoutManager layoutManager = getLayoutManager();
    mRecyclerView.setLayoutManager(layoutManager);
    if (layoutManager instanceof LinearLayoutManager) {
      mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
          ((LinearLayoutManager) layoutManager).getOrientation()));
    }

    mAdapter = getAdapter();
    mRecyclerView.setHasFixedSize(false);
    mRecyclerView.setAdapter(mAdapter);

    Toro.register(mRecyclerView);
  }

  @Override protected void dispatchFragmentActivated() {
    if (!Toro.isActive()) {
      Toro.resume();
    }
  }

  @Override protected void dispatchFragmentDeActivated() {
    Toro.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    Toro.unregister(mRecyclerView);
  }

  RecyclerView.LayoutManager getLayoutManager() {
    return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
  }

  RecyclerView.Adapter getAdapter() {
    return new LegacyAdapter();
  }
}
