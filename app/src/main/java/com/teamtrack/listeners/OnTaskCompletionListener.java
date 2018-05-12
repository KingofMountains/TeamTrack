package com.teamtrack.listeners;

import java.util.List;

public interface OnTaskCompletionListener<T> {
    void onTaskCompleted(List<T> list);
}