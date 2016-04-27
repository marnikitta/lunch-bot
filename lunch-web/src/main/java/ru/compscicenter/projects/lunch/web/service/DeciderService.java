package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.Calendar;
import java.util.List;

public interface DeciderService {

    public double sumForPeriod(long userId, Calendar start, Calendar end);

    public List<MenuItem> getForDate(long userId, Calendar date);
}
