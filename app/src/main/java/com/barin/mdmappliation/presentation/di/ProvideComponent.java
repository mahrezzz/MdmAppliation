package com.barin.mdmappliation.presentation.di;

/**
 * Created by huseyinbarin on 2/21/17.
 */

public interface ProvideComponent<C, T> {
  C provideComponent(T type);
}
