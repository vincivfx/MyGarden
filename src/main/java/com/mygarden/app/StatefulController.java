package com.mygarden.app;

public interface StatefulController<T> {
    /** Called before reload — return the DTO representing current state. */
    T captureState();

    /** Called after reload — restore the state into the new controller. */
    void restoreState(T state);
}
