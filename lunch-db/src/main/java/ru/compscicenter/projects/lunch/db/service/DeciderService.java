package ru.compscicenter.projects.lunch.db.service;

import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.Calendar;
import java.util.List;

public interface DeciderService {
    public List<MenuItem> getForDate(long userId, Calendar date);
}
